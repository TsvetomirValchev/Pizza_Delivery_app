import Products.PizzaIngredient.*;
import Products.Product;
import Users.Admin;
import Users.Customer;
import db.AdminController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;


public class AdminControllerTest {

    AdminController adminController = new AdminController(new Admin());
    @Test
    @DisplayName("Should read all pizzas from the database with the correct names using join")
    public void testReadingPizzaFromDB(){

        adminController.getAllPizzas()
            .values()
            .stream()
            .sorted(Comparator.comparing(Product::getId))
            .forEach(System.out::println);
    }

    @Test
    @DisplayName("Should read all customers from the database with the correct usernames,e-mails and addresses ")
    public void testReadingCustomersFromDB(){

        adminController.getAllCustomers()
                .values()
                .stream()
                .sorted(Comparator.comparing(Customer::getUsername))
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("This should add a pizza with the hardcoded values")
    public void addPizzaProductTest(){

        adminController.createPizzaProduct(2,"test",20.20,
                new Size(2,""),
                new Cheese(1,""),
                new Meat(4,""),
                new Sauce(2,""),
                new Addon(1,""));

         }

}