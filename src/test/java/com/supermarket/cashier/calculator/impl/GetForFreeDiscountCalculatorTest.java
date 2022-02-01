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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetForFreeDiscountCalculatorTest {

    private GetForFreeDiscountCalculator calculator;
    @Autowired
    ProductService productService;
    @Autowired
    private CurrencyConfiguration currencyConfiguration;

    private Product GREEN_TEA;

    @BeforeAll
    void setup() {
        GREEN_TEA = productService.findProductByCode(ProductCode.GR1.toString());
    }

    @Test
    @DisplayName("When the quantity is less than goal + free then 0 discount is returned")
    void calculateBelowGoalQuantity() {
        //given
        final int pay = 2;
        final int free = 1;
        calculator = new GetForFreeDiscountCalculator(GREEN_TEA, pay, free);

        //when
        Money discount = calculator.calculate(pay);

        //then
        assertEquals(discount, Money.zero(currencyConfiguration.getCurrencyUnit()));
    }

    @Test
    @DisplayName("When the quantity is equals than pay+free then the discount is equals to price returned")
    void calculateWithGoalQuantityAchieved() {
        //given
        final int pay = 2;
        final int free = 1;
        calculator = new GetForFreeDiscountCalculator(GREEN_TEA, pay, free);

        //when
        Money discount = calculator.calculate(pay+free);

        //then
        assertEquals(discount, GREEN_TEA.getPrice());
    }

    @Test
    @DisplayName("When the quantity is greater than pay+free then the discount is equals to free multiplied by goal+free times in quantity")
    void calculateWithHigherThanGoalQuantityAchieved() {
        //given
        final int pay = 2;
        final int free = 1;
        calculator = new GetForFreeDiscountCalculator(GREEN_TEA, pay, free);

        //when
        Money discount = calculator.calculate(8);

        //then
        assertEquals(discount, GREEN_TEA.getPrice().multiply(2));
    }

    @DisplayName("When the goal is not a positive integer then throws an InvalidGoalException")
    void calculateWithInvalidGoal() {
        //given
        final int zeroPay = 0;
        final int negativePay = -1;
        final int free = 1;

        //then
        assertThrows(InvalidDiscountGoalException.class, () -> {
            //when
            calculator = new GetForFreeDiscountCalculator(GREEN_TEA, zeroPay, free);
        });
        //then
        assertThrows(InvalidDiscountGoalException.class, () -> {
            //when
            calculator = new GetForFreeDiscountCalculator(GREEN_TEA, negativePay, free);
        });
    }

    @DisplayName("When the free is not a positive integer then throws an InvalidGoalException")
    void calculateWithInvalidFixedPrice() {
        //given
        final float lowerThanOriginalPrice = GREEN_TEA.getPrice().getNumberStripped().floatValue() / (2);
        final int pay = 2;

        //then
        assertThrows(InvalidDiscountValueException.class, () -> {
            //when
            calculator = new GetForFreeDiscountCalculator(GREEN_TEA, pay, Integer.MIN_VALUE);
        });
    }
}
