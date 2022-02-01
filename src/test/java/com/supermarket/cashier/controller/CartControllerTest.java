package com.supermarket.cashier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarket.cashier.model.Cart;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.service.CartService;
import com.supermarket.cashier.service.ProductService;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;
import javax.money.Monetary;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private Product GREEN_TEA_FAKE;

    // here we can inject the mock bean created by EarlyConfiguration (It was needed because it was breaking the initialize DB
    @Autowired
    private ProductService productService;
    @Autowired CartService cartService;

    // this inner class must be static!
    @TestConfiguration
    public static class EarlyConfiguration {
        @MockBean
        private ProductService productService;
        @MockBean
        private CartService cartService;

        private Product GREEN_TEA_FAKE = new Product("FAKE", "Fake produuct for testing",
                Money.of(5.50, Monetary.getCurrency(Locale.US)));

        @PostConstruct
        public void initMock(){
            Mockito.when(productService.findProductByCode(Mockito.any(String.class)))
                    .thenReturn(GREEN_TEA_FAKE);
            Mockito.when(cartService.findByKey(Mockito.any(String.class)))
                    .thenReturn(new Cart("testKey", Monetary.getCurrency(Locale.US)));
        }
    }


    @BeforeAll
    void setup() {
        GREEN_TEA_FAKE = new Product("FAKE", "Fake produuct for testing",
                Money.of(5.50, Monetary.getCurrency(Locale.US)));
    }


    @Test
    @DisplayName("When an existent product is added for the first time to a cart then returns ok")
    void addANewProduct() throws Exception {
        Mockito.when(productService.findProductByCode(Mockito.any(String.class)))
                .thenReturn(GREEN_TEA_FAKE);
        //then
        mockMvc
            .perform(
                post(String.format("/cart/product/add/%s", "CF1"))
                        .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("When a non existent product is added to a cart then returns bad request")
    void addANewNonExistentProduct() throws Exception {
        Mockito.when(productService.findProductByCode(Mockito.any(String.class)))
                .thenReturn(null);
        //then
        mockMvc
                .perform(
                        post(String.format("/cart/product/add/%s", "CF1"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When a non existent product is tried to be deleted from a cart then returns not found")
    void deleteANonExistentProduct() throws Exception {
        Mockito.when(productService.findProductByCode(Mockito.any(String.class)))
                .thenReturn(null);
        //then
        mockMvc
                .perform(
                        delete(String.format("/cart/product/%s", "CF1"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("When try to delete a product from a non existent cart then returns bad request")
    void deleteProductFromANonExistingCart() throws Exception {
        Mockito.when(productService.findProductByCode(Mockito.any(String.class)))
                .thenReturn(GREEN_TEA_FAKE);
        Mockito.when(cartService.findByKey(Mockito.any(String.class)))
                .thenReturn(null);
        //then
        mockMvc
                .perform(
                        delete(String.format("/cart/product/%s", "CF1"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("When a non contained product is tried to be deleted from a cart then returns bad request")
    void deleteANonContainedProduct() throws Exception {
        Mockito.when(productService.findProductByCode(Mockito.any(String.class)))
                .thenReturn(GREEN_TEA_FAKE);
        //then
        mockMvc
                .perform(
                        delete(String.format("/cart/product/%s", "CF1"))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
