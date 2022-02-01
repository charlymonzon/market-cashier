package com.supermarket.cashier.service.impl;

import com.supermarket.cashier.calculator.DiscountCalculator;
import com.supermarket.cashier.conf.CurrencyConfiguration;
import com.supermarket.cashier.factory.DiscountCalculatorFactory;
import com.supermarket.cashier.model.Cart;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.model.ProductItem;
import com.supermarket.cashier.repository.CartRepository;
import com.supermarket.cashier.service.CartService;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import javax.money.CurrencyUnit;

@Service
public class DefaultCartService implements CartService {

    CurrencyConfiguration currencyConfiguration;
    CartRepository cartRepository;

    public DefaultCartService(CurrencyConfiguration currencyConfiguration, CartRepository cartRepository) {
        this.currencyConfiguration = currencyConfiguration;
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart create(String key, CurrencyUnit currencyUnit) {
        Cart cart = new Cart(key, currencyUnit);
        cartRepository.save(cart);
        return cart;
    }

    @Override
    public Cart addProduct(Product product, Cart cart) {
        //get a productItem for that product or creates a new one and get its data
        ProductItem previousProductItem = cart.getProductItemByProductOrCreateNew(product);
        int quantity = previousProductItem.getQuantity();
        DiscountCalculator discountCalculator = DiscountCalculatorFactory.create(product);
        Money discount = discountCalculator.calculate(++quantity);

        //creates the new productItem to be saved with new quantity and discount
        ProductItem newProductItem = new ProductItem.ProductItemBuilder(previousProductItem.getProduct()).
                            setQuantity(quantity).setDiscount(discount)
                            .build();
        cart.addProductItem(newProductItem);
        //if I calculate the difference between the total of the previous item and the new one-
        //then I dont need to calculate the entire total for the cart every time that I add a product.
        //This way having it updated at constant time instead logN
        Money difference = quantity==1 ? newProductItem.calculateTotal() : newProductItem.calculateTotal().subtract(previousProductItem.calculateTotal());
        cart.addToTotal(difference);

        return cartRepository.save(cart);
    }

    @Override
    public Cart removeProduct(Product product, Cart cart) {
        ProductItem previousProductItem = cart.getProductItemByProduct(product);
        int quantity = previousProductItem.getQuantity();

        if(quantity==1) { //just remove it and its total amount
            cart.removeProductItem(previousProductItem);
            cart.subtractFromTotal(previousProductItem.calculateTotal());
            return cartRepository.save(cart);
        }

        //quantity > 1 so we need to remove one and recalculate discount (just subtract the difference)
        DiscountCalculator discountCalculator = DiscountCalculatorFactory.create(product);
        Money discount = discountCalculator.calculate(--quantity);
        ProductItem newProductItem = new ProductItem.ProductItemBuilder(previousProductItem.getProduct()).
                setQuantity(quantity).setDiscount(discount)
                .build();
        cart.addProductItem(newProductItem);
        cart.subtractFromTotal(previousProductItem.calculateTotal().subtract(newProductItem.calculateTotal()));

        return cartRepository.save(cart);
    }

    @Override
    public Cart findByKey(String key) {
        return this.cartRepository.findByKey(key);
    }


    @Override
    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }
}
