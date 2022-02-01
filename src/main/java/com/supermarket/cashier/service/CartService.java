package com.supermarket.cashier.service;

import com.supermarket.cashier.model.Cart;
import com.supermarket.cashier.model.Product;

import javax.money.CurrencyUnit;

public interface CartService {

    Cart create(String key, CurrencyUnit currencyUnit);

    Cart addProduct(Product product, Cart cart);

    Cart findByKey(String key);

    void delete(Cart cart);

    Cart removeProduct(Product product, Cart cart);

}
