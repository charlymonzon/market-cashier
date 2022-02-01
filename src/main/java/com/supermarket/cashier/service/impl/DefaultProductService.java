package com.supermarket.cashier.service.impl;

import com.supermarket.cashier.conf.CurrencyConfiguration;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.repository.ProductRepository;
import com.supermarket.cashier.service.ProductService;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

@Service
public class DefaultProductService implements ProductService {

    ProductRepository productRepository;
    CurrencyConfiguration currencyConfiguration;

    public DefaultProductService(ProductRepository productRepository, CurrencyConfiguration currencyConfiguration) {
        this.productRepository = productRepository;
        this.currencyConfiguration = currencyConfiguration;
    }

    @Override
    public Product create(String key, String name, float price) {
        Product product = new Product(key, name, Money.of(price, currencyConfiguration.getCurrencyUnit()));
        productRepository.save(product);
        return product;
    }

    @Override
    public Product findProductByCode(String code) {
        return productRepository.findProductByCode(code);
    }
}
