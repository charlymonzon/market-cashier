package com.supermarket.cashier.model;

import lombok.Getter;

@Getter
public enum ProductCode {
    GR1("GR1"),
    SR1("SR1"),
    CF1("CF1");

    @Getter
    private String code;

    ProductCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
