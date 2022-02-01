package com.supermarket.cashier.calculator.impl;

import com.supermarket.cashier.CashierApplication;
import com.supermarket.cashier.calculator.DiscountCalculator;
import com.supermarket.cashier.exception.InvalidDiscountGoalException;
import com.supermarket.cashier.exception.InvalidDiscountValueException;
import com.supermarket.cashier.model.Product;
import org.javamoney.moneta.Money;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixedPriceDiscountCalculator implements DiscountCalculator {
    private Product product;
    private Integer goal;
    private Money newPrice;

    private static final Logger logger = LogManager.getLogger(FixedPriceDiscountCalculator.class);

    public FixedPriceDiscountCalculator(Product product, Integer goal, Money newPrice) {
        if (goal<=0){
            logger.error("FixedPriceDiscountCalculator goal was wrong configured");
            throw new InvalidDiscountGoalException();
        }
        if (newPrice.isLessThanOrEqualTo(Money.zero(newPrice.getCurrency())) || newPrice.isGreaterThan(product.getPrice())){
            logger.error("FixedPriceDiscountCalculator value wrong configured");
            throw new InvalidDiscountValueException("New price for FixedPriceDiscount must be positive and greather than original price");
        }
        this.product = product;
        this.goal = goal;
        this.newPrice = newPrice;
    }

    @Override
    public Money calculate(Integer quantity) {
        System.out.println("Applying FixedPrice discount");
        if (quantity<this.goal)
            return Money.of(0, this.product.getPrice().getCurrency());
        //discount = (price - newPrice) * quantity
        return this.product.getPrice().subtract(newPrice).multiply(quantity);
    }
}
