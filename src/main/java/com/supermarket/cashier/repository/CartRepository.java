package com.supermarket.cashier.repository;

import com.supermarket.cashier.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CartRepository {

    Map<String, Cart> carts = new HashMap<>();

    public Cart save(Cart cart) {
        carts.put(cart.getKey(), cart);
        return cart;
    }

    public Cart findByKey(String key) {
        return carts.get(key);
    }

    public void delete(Cart cart) {
        carts.remove(cart.getKey());
    }
}
