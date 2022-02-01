# supermarket-cashier

This application is an API made in with Spring Boot and represents a cashier system.

##Running the application
### What do I need to install to run it locally?
- Install JDK 11.0.6: [Oracle Official Repository](https://www.oracle.com/ar/java/technologies/javase/jdk11-archive-downloads.html)
- Maven 3.6.1 [Official Site](https://maven.apache.org/docs/3.6.1/release-notes.html)
- No Database is needed

After installing all the previous libraries go to the root folder of the project and run `mvn clean install` to start installing the dependencies._ No VPN is needed._

### How to configure and run it:
####You may wanna know this before start running it:
You can choose the currency unit from the application.properties file by changing the property `currency.unit=GBP`to "USD" or "EUR" for example.
The default value used is the one of the document examples.

####Now we can run it!
Once all dependencies are downloaded and after the tests have been run, we can start our application using the next command `mvn spring-boot:run`

####API Documentation
Once its up and running we can start playing with the API. The project has [OpenAPi](https://oai.github.io/Documentation/) so we can go to our browser and 
search by for this [url](http://localhost:8080/rest-api-info.html)  (the url is customizable in applications.properties). There you will have the endpoints and a tool to run them without having to download Postman
or any other client.

##Adding new Features
* If new rules for discounts are needed, you just need to implement `DiscountCalculator` interface and 
the instantiate your new class from the `DiscountCalculatorFactory`
* Just the model classes that have enough optional attributes are created with Builder pattern. Feel free to implement in the ones you consider appropriated.



##Details about the resolution
###What I understood:
* The client needs an API to add products to a shopping cart. 
* The client has certain rules that are going to be applied depending on the product.
* The client needs to see the total amount for his cart.
* No database is needed

###My assumptions:
* As I dont need a database, I can store the data in memory.
* As I was not asked to implement authentication or authorization, CRUD of Carts or products. So I assume that the focus should be in the addition of products and applying rules. S
For example: the client can create his cart just adding his first product.
* I was not asked to create a production ready app so I did not focus on profiles to run the application with different configurations.
* If you need to add a new product that does not need a discount, you can should the `ZeroDiscountCalculator` to it. 
###Resolution:
* As I assume that the focus should be in the addition process, I just left the work of creating the new Cart by calling the API as Get Cart or even Adding your first Product.
* Thanks to that I will just use the request session id to assign the cart to its respective owner. It does not matter if it was already created before or not. 
* I dont know how many products can be in the cart, so to maintain the performance stable I wont calculate all the rules every time I modify the cart. Instead I just add or subtract for these
particular product, due to this strategy I keep the time complexity between logN and N for every insertion and calculation.
* I researched libraries to operate with currencies and I found javax.money really interesting so I added it to the app.
* As I used the **TDD** methodology from the beginning the, most of the application is covered by tests. 
* The use cases of the document were tested in the Test file "`CashierApplicationTests.java`"
