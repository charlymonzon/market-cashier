package com.supermarket.cashier.calculator.impl;

import com.supermarket.cashier.CashierApplication;
import com.supermarket.cashier.calculator.DiscountCalculator;
import com.supermarket.cashier.exception.InvalidDiscountGoalException;
import com.supermarket.cashier.exception.InvalidDiscountValueException;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.util.CurrencyUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

public class PercentagePriceDiscountCalculator implements DiscountCalculator {
    private Product product;
    private Integer goal;
    private Float remainingPercentage;

    private static final Logger logger = LogManager.getLogger(PercentagePriceDiscountCalculator.class);

    public PercentagePriceDiscountCalculator(Product product, Integer goal, Float remainingPercentage) {
        if(goal<0) {
            logger.error("PercentagePriceDiscountCalculator goal was wrong configured");
            throw new InvalidDiscountGoalException();
        }
        if(remainingPercentage<0 || remainingPercentage>1) {
            logger.error("PercentagePriceDiscountCalculator value was wrong configured");
            throw new InvalidDiscountValueException("Remaining Percentage must be between 0 and 1, both of them inclusive");
        }
        this.product = product;
        this.goal = goal;
        this.remainingPercentage = remainingPercentage;
    }

    @Override
    public Money calculate(Integer quantity) {
        System.out.println("Applying PercentagePrice discount");
        if(quantity<this.goal)
            return Money.of(0, this.product.getPrice().getCurrency());
        return CurrencyUtil.round(this.product.getPrice().multiply(quantity).multiply(1-this.remainingPercentage));
    }
}
