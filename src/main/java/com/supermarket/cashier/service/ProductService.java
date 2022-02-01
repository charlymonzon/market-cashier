package com.supermarket.cashier.service;

import com.supermarket.cashier.model.Product;

public interface ProductService {

    Product create(String code, String name, float price);

    Product findProductByCode(String code);
}
