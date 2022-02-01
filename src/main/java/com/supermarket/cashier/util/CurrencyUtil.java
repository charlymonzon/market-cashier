package com.supermarket.cashier.util;

import org.javamoney.moneta.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyUtil {

    public static Money round(Money money) {
        //Helps to keep scale for calculations in discount rules
        BigDecimal number = money.getNumberStripped();
        number = number.setScale(2, RoundingMode.HALF_EVEN);
        return Money.of(number, money.getCurrency());
    }
}
