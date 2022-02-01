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
class FixedPriceDiscountCalculatorTest {

    private FixedPriceDiscountCalculator calculator;
    @Autowired
    ProductService productService;
    @Autowired
    private CurrencyConfiguration currencyConfiguration;

    private Product STRAWBERRIES;

    @BeforeAll
    void setup() {
        STRAWBERRIES = productService.findProductByCode(ProductCode.SR1.getCode());
    }

    @Test
    @DisplayName("When the goal is not reached then 0 discount is returned")
    void calculateBelowGoalQuantity() {
        //given
        Money fixedPrice = STRAWBERRIES.getPrice().divide(2); //This way we make sure that fixed price is lower than original
        calculator = new FixedPriceDiscountCalculator(STRAWBERRIES, 5, fixedPrice);

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
        Money fixedPrice = STRAWBERRIES.getPrice().divide(2); //This way we make sure that fixed price is lower than original
        calculator = new FixedPriceDiscountCalculator(STRAWBERRIES, goal, fixedPrice);

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
        Money fixedPrice = STRAWBERRIES.getPrice().divide(2); //This way we make sure that fixed price is lower than original

        //then
        assertThrows(InvalidDiscountGoalException.class, () -> {
            //when
            calculator = new FixedPriceDiscountCalculator(STRAWBERRIES, zeroGoal, fixedPrice);
        });
        //then
        assertThrows(InvalidDiscountGoalException.class, () -> {
            //when
            calculator = new FixedPriceDiscountCalculator(STRAWBERRIES, negativeGoal, fixedPrice);
        });
    }

    @DisplayName("When the fixed price is lower than the original product price or is not a positive integer then throws an InvalidGoalException")
    void calculateWithInvalidFixedPrice() {
        //given
        final Money zeroFixedPrice = Money.zero(currencyConfiguration.getCurrencyUnit());
        final Money negativeFixedPrice = Money.of(-2.3, currencyConfiguration.getCurrencyUnit());
        final Money lowerThanOriginalPrice = STRAWBERRIES.getPrice().divide(2);
        final int goal = 5;

        //then
        assertThrows(InvalidDiscountValueException.class, () -> {
            calculator = new FixedPriceDiscountCalculator(STRAWBERRIES, goal, zeroFixedPrice);
        });
        //then
        assertThrows(InvalidDiscountValueException.class, () -> {
            //when
            calculator = new FixedPriceDiscountCalculator(STRAWBERRIES, goal, negativeFixedPrice);
        });
        //then
        assertThrows(InvalidDiscountValueException.class, () -> {
            //when
            calculator = new FixedPriceDiscountCalculator(STRAWBERRIES, goal, lowerThanOriginalPrice);
        });
    }


}
