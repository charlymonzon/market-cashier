package com.supermarket.cashier.factory;

import com.supermarket.cashier.calculator.DiscountCalculator;
import com.supermarket.cashier.calculator.impl.FixedPriceDiscountCalculator;
import com.supermarket.cashier.calculator.impl.GetForFreeDiscountCalculator;
import com.supermarket.cashier.calculator.impl.PercentagePriceDiscountCalculator;
import com.supermarket.cashier.calculator.impl.ZeroDiscountCalculator;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.model.ProductCode;
import org.javamoney.moneta.Money;

public abstract class DiscountCalculatorFactory {

    public static DiscountCalculator create(Product product) {
        if(ProductCode.GR1.getCode().equals(product.getCode())) return new GetForFreeDiscountCalculator(product, 1, 1);
        if(ProductCode.SR1.getCode().equals(product.getCode())) return new FixedPriceDiscountCalculator(product, 3, Money.of(4.50, product.getPrice().getCurrency()));
        if(ProductCode.CF1.getCode().equals(product.getCode())) return new PercentagePriceDiscountCalculator(product, 3, (float)2/3);
        return new ZeroDiscountCalculator(product);
    }
}
