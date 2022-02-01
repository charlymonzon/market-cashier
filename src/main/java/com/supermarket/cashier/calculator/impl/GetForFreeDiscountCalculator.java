package com.supermarket.cashier.calculator.impl;

import com.supermarket.cashier.CashierApplication;
import com.supermarket.cashier.calculator.DiscountCalculator;
import com.supermarket.cashier.exception.InvalidDiscountGoalException;
import com.supermarket.cashier.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javamoney.moneta.Money;

public class GetForFreeDiscountCalculator implements DiscountCalculator {
    private Product product;
    private Integer pay;
    private Integer free;

    private static final Logger logger = LogManager.getLogger(GetForFreeDiscountCalculator.class);

    public GetForFreeDiscountCalculator(Product product, Integer pay, Integer free) {
        if (pay<=0) {
            logger.error("GetForFreeDiscountCalculator pay was wrong configured");
            throw new InvalidDiscountGoalException();
        }
        this.product = product;
        this.pay = pay;
        this.free = free;
    }

    @Override
    public Money calculate(Integer quantity) {
        if(quantity<this.pay+this.free)
            return Money.of(0, this.product.getPrice().getCurrency());
        System.out.println("Applying GetForFree discount");
        Integer discountItems = (quantity/(this.pay+this.free)) * free;
        return this.product.getPrice().multiply(discountItems);
    }
}
