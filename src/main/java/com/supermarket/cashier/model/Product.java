package com.supermarket.cashier.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Setter
@Getter
@AllArgsConstructor
public class Product {
    private String code;
    private String name;
    private Money price;
}
