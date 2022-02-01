package com.supermarket.cashier.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.jackson.datatype.money.MoneyModule;

@Configuration
class JacksonConfig {
    @Bean //This configuration is for helping Jackson to serialize the money object
    public MoneyModule moneyModule() {
        return new MoneyModule();
    }
}
