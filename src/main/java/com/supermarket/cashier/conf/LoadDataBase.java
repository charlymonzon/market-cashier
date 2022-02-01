package com.supermarket.cashier.conf;

import com.supermarket.cashier.CashierApplication;
import com.supermarket.cashier.model.ProductCode;
import com.supermarket.cashier.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDataBase {
    private static final Logger logger = LogManager.getLogger(LoadDataBase.class);

    @Bean
    CommandLineRunner initDB(ProductService productService) {
        return args -> {
            //As we dont use a database, we initialize the products here
            productService.create(ProductCode.GR1.toString(), "Green tea", (float)3.11);
            productService.create(ProductCode.SR1.toString(), "Strawberries", (float)5.00);
            productService.create(ProductCode.CF1.toString(), "Coffee", (float)11.23);

            logger.debug("Default products were added to the database at initial loading");
        };
    }

}
