package com.supermarket.cashier.repository;

import com.supermarket.cashier.model.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductRepository {
    Map<String, Product> products = new HashMap<>();

    public Product save(Product product) {
        products.put(product.getCode(), product);
        return product;
    }

    public Product findProductByCode(String code) {
        return products.get(code);
    }
}
