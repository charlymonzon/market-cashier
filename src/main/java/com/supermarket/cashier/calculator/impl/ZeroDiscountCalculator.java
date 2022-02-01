package com.supermarket.cashier.calculator.impl;

import com.supermarket.cashier.calculator.DiscountCalculator;
import com.supermarket.cashier.model.Product;
import org.javamoney.moneta.Money;

public class ZeroDiscountCalculator implements DiscountCalculator {
    private Product product;

    public ZeroDiscountCalculator(Product product) {
        this.product = product;
    }

    @Override
    public Money calculate(Integer quantity) {
        // No discount is applied
        return this.product.getPrice().multiply(0);
    }
}
