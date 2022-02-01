package com.supermarket.cashier.service.impl;

import com.supermarket.cashier.conf.CurrencyConfiguration;
import com.supermarket.cashier.model.Cart;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.model.ProductCode;
import com.supermarket.cashier.service.CartService;
import com.supermarket.cashier.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultCartServiceTest {

  @Autowired
  CartService cartService;
  @Autowired
  ProductService productService;
  @Autowired
  CurrencyConfiguration currencyConfiguration;

  private final String CART_KEY = "test";
  private Cart cart;
  private Product GREEN_TEA;
  private Product STRAWBERRIES;
  private Product COFFEE;

  @BeforeAll
  void setup() {
    GREEN_TEA = productService.findProductByCode(ProductCode.GR1.getCode());
    STRAWBERRIES = productService.findProductByCode(ProductCode.SR1.getCode());
    COFFEE = productService.findProductByCode(ProductCode.CF1.getCode());
  }

  @BeforeEach
  void setupCart() {
    cart = cartService.create(CART_KEY, currencyConfiguration.getCurrencyUnit());
  }

  @AfterEach
  void clean() {
    cartService.delete(cart);
  }

  @Test
  @DisplayName("When a cart is created then it is returned with an empty list of product items")
  void createNewCart() {

    //given
    assertTrue(Objects.nonNull(CART_KEY));

    //when
    Cart cart = cartService.create(CART_KEY, currencyConfiguration.getCurrencyUnit());

    //then
    assertEquals(cart.productsCount(), 0);
  }

  @Test
  @DisplayName("When a product is added to a cart then the filled cart is returned")
  void addNewProductItem() {

    //given
    assertEquals(cart.productsCount(), 0);

    //when
    cartService.addProduct(COFFEE, cart);

    //then
    assertEquals(cart.productsCount(), 1);
  }

  @Test
  @DisplayName("When a product is added twice to a cart then its quantity will be 2")
  void addRepeatedProductItems() {

    //given
    assertEquals(cart.productsCount(), 0);

    //when
    cartService.addProduct(COFFEE, cart);
    cartService.addProduct(GREEN_TEA, cart);
    cartService.addProduct(STRAWBERRIES, cart);
    cartService.addProduct(GREEN_TEA, cart);

    //then
    assertEquals(cart.productsCount(), 3);
    assertEquals(cart.getProductItemByProductOrCreateNew(GREEN_TEA).getQuantity(), 2);
  }
}
