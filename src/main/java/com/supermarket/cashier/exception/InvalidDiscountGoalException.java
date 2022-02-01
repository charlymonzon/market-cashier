package com.supermarket.cashier.exception;

public class InvalidDiscountGoalException extends RuntimeException {
    public InvalidDiscountGoalException() {
        super("Discount goal must be a positive integer");
    }
}
