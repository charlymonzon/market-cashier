package com.supermarket.cashier.calculator.impl;

import com.supermarket.cashier.conf.CurrencyConfiguration;
import com.supermarket.cashier.exception.InvalidDiscountGoalException;
import com.supermarket.cashier.exception.InvalidDiscountValueException;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.model.ProductCode;
import com.supermarket.cashier.service.ProductService;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PercentagePriceDiscountCalculatorTest {

    private PercentagePriceDiscountCalculator calculator;
    @Autowired
    ProductService productService;
    @Autowired
    private CurrencyConfiguration currencyConfiguration;

    private Product COFFEE;

    @BeforeAll
    void setup() {
        COFFEE = productService.findProductByCode(ProductCode.CF1.toString());
    }

    @Test
    @DisplayName("When the goal is not reached then 0 discount is returned")
    void calculateBelowGoalQuantity() {
        //given
        calculator = new PercentagePriceDiscountCalculator(COFFEE, 5, (float)2/3);

        //when
        Money discount = calculator.calculate(4);

        //then
        assertEquals(discount, Money.zero(currencyConfiguration.getCurrencyUnit()));
    }

    @Test
    @DisplayName("When the goal is reached then the discount is returned")
    void calculateWithGoalQuantityAchieved() {
        //given
        final int goal = 5;
        calculator = new PercentagePriceDiscountCalculator(COFFEE, goal, (float)2/3);

        //when
        Money discount = calculator.calculate(goal);

        //then
        assertTrue(discount.isGreaterThan(Money.zero(currencyConfiguration.getCurrencyUnit())));
    }

    @DisplayName("When the goal is not a positive integer then throws an InvalidGoalException")
    void calculateWithInvalidGoal() {
        //given
        final int zeroGoal = 0;
        final int negativeGoal = -1;

        //then
        assertThrows(InvalidDiscountGoalException.class, () -> {
            //when
            calculator = new PercentagePriceDiscountCalculator(COFFEE, zeroGoal, (float)2/3);
        });
        //then
        assertThrows(InvalidDiscountGoalException.class, () -> {
            //when
            calculator = new PercentagePriceDiscountCalculator(COFFEE, negativeGoal, (float)2/3);
        });
    }

    @DisplayName("When the percentage is not between 0 and 1 (both inclusive) then throws an InvalidDiscountValueException")
    void calculateWithInvalidFixedPrice() {
        //given
        final int goal = 5;

        //then
        assertThrows(InvalidDiscountValueException.class, () -> {
            //when
            calculator = new PercentagePriceDiscountCalculator(COFFEE, goal, (float)-0.01);
        });
        //then
        assertThrows(InvalidDiscountValueException.class, () -> {
            //when
            calculator = new PercentagePriceDiscountCalculator(COFFEE, goal, (float)1.01);
        });
    }


}
