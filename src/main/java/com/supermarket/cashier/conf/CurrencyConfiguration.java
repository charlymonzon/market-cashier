package com.supermarket.cashier.conf;

import com.supermarket.cashier.CashierApplication;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Locale;

@Configuration
@ConfigurationProperties(prefix = "currency")
public class CurrencyConfiguration {

    @Value( "${currency.unit}" )
    @Setter
    private String unit;

    private static final Logger logger = LogManager.getLogger(CurrencyConfiguration.class);

    //Help us to unify the currency unit in the entire app once we set a default in application.properties
    public CurrencyUnit getCurrencyUnit() {
        logger.debug(String.format("Currency unit: %s was set as default", unit));
        return unit!=null ? Monetary.getCurrency(this.unit) : Monetary.getCurrency(Locale.US);
    }

}
