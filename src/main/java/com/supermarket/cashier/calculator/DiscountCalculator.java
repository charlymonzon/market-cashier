package com.supermarket.cashier.calculator;

import org.javamoney.moneta.Money;

public interface DiscountCalculator {
    Money calculate(Integer quantity);
}
