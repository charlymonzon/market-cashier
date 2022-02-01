package com.supermarket.cashier.model;


import lombok.Getter;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Cart {
    private String key;
    private Map<String, ProductItem> productItems;
    private Money total;

    //As we dont have a bunch of non required attrs, then Builder pattern is not needed
    public Cart(String key, CurrencyUnit currencyUnit) {
        this.key = key;
        this.productItems = new HashMap<>();
        this.total = Money.zero(currencyUnit);
    }

    public void addToTotal(Money newAmount) {
        this.total = this.total.add(newAmount);
    }

    public void addProductItem(ProductItem productItem) {
        this.productItems.put(productItem.getProduct().getCode(), productItem);
    }

    public void removeProductItem(ProductItem productItem) {
        this.productItems.remove(productItem.getProduct().getCode());
    }

    public Integer productsCount() {
        return productItems.size();
    }

    public ProductItem getProductItemByProduct(Product product) {
        return this.productItems.get(product.getCode());
    }

    public ProductItem getProductItemByProductOrCreateNew(Product product) {
        ProductItem productItem = this.productItems.get(product.getCode());
        return productItem != null ? productItem : new ProductItem.ProductItemBuilder(product).build();
    }

    public boolean hasProduct(Product product) {
        return Objects.nonNull(productItems.get(product.getCode()));
    }

    public void subtractFromTotal(Money calculateTotal) {
        this.total = total.subtract(calculateTotal);
    }
}
