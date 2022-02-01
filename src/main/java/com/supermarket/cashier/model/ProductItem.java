package com.supermarket.cashier.model;

import lombok.Getter;
import org.javamoney.moneta.Money;


@Getter
public class ProductItem {
    private Product product;
    private Integer quantity;
    private Money discount;

    public ProductItem(ProductItemBuilder productItemBuilder) {
        this.product = productItemBuilder.product;
        this.quantity = productItemBuilder.quantity;
        this.discount = productItemBuilder.discount;
    }

    public Money calculateTotal() {
        return product.getPrice().multiply(quantity).subtract(discount);
    }

    //Builder Class
    public static class ProductItemBuilder{

        //required parameters
        private Product product;
        //Optional parameters
        private Integer quantity;
        private Money discount;

        public ProductItemBuilder(Product product){
            this.product = product;
            this.quantity = 0;
            this.discount = Money.of(0, product.getPrice().getCurrency());
        }

        public ProductItemBuilder setQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductItemBuilder setDiscount(Money discount) {
            this.discount = discount;
            return this;
        }

        public ProductItem build(){
            return new ProductItem(this);
        }

    }
}
