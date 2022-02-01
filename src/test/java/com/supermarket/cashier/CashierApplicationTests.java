package com.supermarket.cashier;

import com.supermarket.cashier.conf.CurrencyConfiguration;
import com.supermarket.cashier.model.Cart;
import com.supermarket.cashier.model.Product;
import com.supermarket.cashier.model.ProductCode;
import com.supermarket.cashier.service.CartService;
import com.supermarket.cashier.service.ProductService;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CashierApplicationTests {
	@Autowired
	CartService cartService;
	@Autowired
	ProductService productService;
	@Autowired
	CurrencyConfiguration currencyConfiguration;

	private Cart cart;
	private Product GREEN_TEA;
	private Product STRAWBERRIES;
	private Product COFFEE;

	@BeforeAll
	void setup() {
		GREEN_TEA = productService.findProductByCode(ProductCode.GR1.getCode());
		STRAWBERRIES = productService.findProductByCode(ProductCode.SR1.getCode());
		COFFEE = productService.findProductByCode(ProductCode.CF1.getCode());
	}

	@BeforeEach
	void setupCart() {
		String CART_KEY = "test";
		cart = cartService.create(CART_KEY, currencyConfiguration.getCurrencyUnit());
	}

	@AfterEach
	void clean() {
		cartService.delete(cart);
	}

	@Test
	@DisplayName("When Basket: GR1,SR1,GR1,GR1,CF1 then Total price expected: ​£22.45")
	void testData1() {

		//when
		cartService.addProduct(GREEN_TEA, cart);
		cartService.addProduct(STRAWBERRIES, cart);
		cartService.addProduct(GREEN_TEA, cart);
		cartService.addProduct(GREEN_TEA, cart);
		cartService.addProduct(COFFEE, cart);

		//then
		assertEquals(cart.getTotal(), Money.of(22.45, currencyConfiguration.getCurrencyUnit()));
	}

	@Test
	@DisplayName("When Basket: GR1,GR1 then Total price expected: £3.11")
	void testData2() {

		//when
		cartService.addProduct(GREEN_TEA, cart);
		cartService.addProduct(GREEN_TEA, cart);

		//then
		assertEquals(cart.getTotal(), Money.of(3.11, currencyConfiguration.getCurrencyUnit()));
	}

	@Test
	@DisplayName("When Basket: SR1,SR1,GR1,SR1 then Total price expected: £16.61")
	void testData3() {

		//when
		cartService.addProduct(STRAWBERRIES, cart);
		cartService.addProduct(STRAWBERRIES, cart);
		cartService.addProduct(GREEN_TEA, cart);
		cartService.addProduct(STRAWBERRIES, cart);

		//then
		assertEquals(cart.getTotal(), Money.of(16.61, currencyConfiguration.getCurrencyUnit()));
	}

	@Test
	@DisplayName("When Basket: GR1,CF1,SR1,CF1,CF1 then Total price expected: £30.57")
	void testData4() {

		//when
		cartService.addProduct(GREEN_TEA, cart);
		cartService.addProduct(COFFEE, cart);
		cartService.addProduct(STRAWBERRIES, cart);
		cartService.addProduct(COFFEE, cart);
		cartService.addProduct(COFFEE, cart);

		//then
		assertEquals(cart.getTotal(), Money.of(30.57, currencyConfiguration.getCurrencyUnit()));
	}
}
