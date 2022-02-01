package com.supermarket.cashier.calculator.impl;

import com.supermarket.cashier.conf.CurrencyConfiguration;
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

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZeroDiscountCalculatorTest {

    @Autowired
    ProductService productService;
    @Autowired
    private CurrencyConfiguration currencyConfiguration;
    private ZeroDiscountCalculator calculator;
    private Product STRAWBERRIES;

    @BeforeAll
    void setup() {
        STRAWBERRIES = productService.findProductByCode(ProductCode.SR1.getCode());
    }

    @Test
    @DisplayName("When any amount of products are added then 0 discount is returned")
    void calculateBelowGoalQuantity() {
        //given
        calculator = new ZeroDiscountCalculator(STRAWBERRIES);

        //when
        Money discount = calculator.calculate(Integer.MAX_VALUE);

        //then
        assertEquals(discount, Money.zero(currencyConfiguration.getCurrencyUnit()));
    }
}
