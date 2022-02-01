package com.supermarket.cashier.controller;

import com.supermarket.cashier.CashierApplication;
import com.supermarket.cashier.conf.CurrencyConfiguration;
import com.supermarket.cashier.model.Cart;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.service.CartService;
import com.supermarket.cashier.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("cart")
public class CartController {
    private CartService cartService;
    private ProductService productService;
    private CurrencyConfiguration currencyConfiguration;

    private static final Logger logger = LogManager.getLogger(CartController.class);

    public CartController(CartService cartService, ProductService productService, CurrencyConfiguration currencyConfiguration) {
        this.cartService = cartService;
        this.productService = productService;
        this.currencyConfiguration = currencyConfiguration;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Cart get(HttpServletRequest request) {
        logger.trace("Requesting a cart API was called");
        String sessionId = request.getSession().getId();
        Cart cart = cartService.findByKey(sessionId);
        if(cart==null) { //Makes no sense to create an api just to assign an empty cart
            logger.debug("First time operation with the cart so a new one will be created");
            cart = cartService.create(sessionId, currencyConfiguration.getCurrencyUnit());
        }

        return cart;
    }

    @PostMapping("/product/add/{productCode}")
    @ResponseStatus(HttpStatus.OK)
    public Cart insert(@PathVariable String productCode, HttpServletRequest request) {
        logger.trace("Insert product into cart API was called");
        Product product = productService.findProductByCode(productCode);
        if(product==null) {
            logger.error("Product not found when tried to be added to a cart");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Product with code %s was not found", productCode));
        }

        String sessionId = request.getSession().getId();
        Cart cart = cartService.findByKey(sessionId);
        if(cart==null) { //Makes no sense to create an api just to assign an empty cart
            logger.debug("First time operation with the cart so a new one will be created");
            cart = cartService.create(sessionId, currencyConfiguration.getCurrencyUnit());
        }

        cartService.addProduct(product, cart);
        return cart;
    }

    @DeleteMapping("/product/{productCode}")
    @ResponseStatus(HttpStatus.OK)
    public Cart removeProduct(@PathVariable String productCode, HttpServletRequest request) {
        logger.trace("Remove product from cart API was called");
        Product product = productService.findProductByCode(productCode);
        String sessionId = request.getSession().getId();
        if(product==null) {
            logger.error("Product not found when tried to be added to a cart");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Product with code %s was not found", productCode));
        }
        Cart cart = cartService.findByKey(sessionId);
        if(cart==null) {
            logger.error("Trying to delete products in a cart that was not created");
            cartService.create(sessionId, currencyConfiguration.getCurrencyUnit());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("There is not product %s: %s into the cart", product.getCode(), product.getName()));
        }
        if(!cart.hasProduct(product)) {
            logger.error("Trying to delete a product in a cart that does not have that product");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("There is not product %s: %s into the cart", product.getCode(), product.getName()));
        }

        return cartService.removeProduct(product, cart);
    }
}
