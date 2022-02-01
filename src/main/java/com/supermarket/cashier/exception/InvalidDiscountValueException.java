package com.supermarket.cashier.exception;

public class InvalidDiscountValueException extends RuntimeException {
    public InvalidDiscountValueException(String message) {
        super(message);
    }
}
