import products.Dessert;
import products.Drink;
import products.Pizza;
import products.ingredient.*;
import users.Customer;
import db.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminServiceTest {

    AdminService adminService = new AdminService();


    @DisplayName("Should add a product with an id, name and price as well as a pizza that references the product with the ingredients choosen.")
    @Test
    public void testAddPizzaProduct() {
        List<Meat> meats = new ArrayList<>();
        List<Addon> addons = new ArrayList<>();
        List<Cheese> cheeses = new ArrayList<>();

        meats.add(new Meat(1, ""));
        meats.add(new Meat(2, ""));
        cheeses.add(new Cheese(1, ""));
        cheeses.add(new Cheese(2, ""));
        addons.add(new Addon(1, ""));
        addons.add(new Addon(2, ""));
        adminService.createPizzaProduct(
                1020,
                "chorizo pizza",
                19.99,
                new Size(2, ""),
                new Sauce(1, ""),
                meats,
                cheeses,
                addons
        );
    }

    @DisplayName("Should add a product with an id,name and price as well as a drink referencing the product with information about it being diet drink or not")
    @Test
    public void testAddDrinkProduct() {
        adminService.createDrinkProduct(2020, "Sprite", 2.89, false);
    }


    @DisplayName("Should add a product with an id,name and price as well as a dessert referencing the product with information about it being vegan drink or not")
    @Test
    public void testAddDessertProduct() {
        adminService.createDessertProduct(3020, "cookie flavoured ice cream", 4.55, false);
    }


    @DisplayName("Should read all pizzas from the database")
    @Test
    public void testReadAllPizzasFromDatabase() {
        adminService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Pizza::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should read all customers from the database")
    @Test
    public void testReadAllCustomersFromDatabase() {

        adminService.getAllCustomers()
                .values()
                .stream()
                .sorted(Comparator.comparing(Customer::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should read all drinks from the database")
    @Test
    public void testReadAllDrinksFromDatabase() {
        adminService.getAllDrinks()
                .values()
                .stream()
                .sorted(Comparator.comparing(Drink::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should read all desserts from the database")
    @Test
    public void testReadAllDessertsFromDatabase() {
        adminService.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Dessert::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should delete a pizza and a product with specified id")
    @Test
    public void testDeletePizzaProduct() {
        adminService.deletePizza(1020);
    }

    @DisplayName("Should delete a drink and a product with specified id")
    @Test
    public void testDeleteDrinkProduct() {
        adminService.deleteDrink(2020);
    }

    @DisplayName("Should delete a pizza and a product with specified id")
    @Test
    public void testDeleteDessertProduct() {
        adminService.deleteDessert(3020);
    }

    @DisplayName("Should add a customer account with the values provided")
    @Test
    public void testAddCustomer() {
        adminService.addCustomer(new Customer("Peter", "PeterPass123", null, "email@gmail.com", "Peter str."));
    }

    @DisplayName("Should delete a customer by providing the account's username")
    @Test
    public void testDeleteCustomer() {
        adminService.deleteCustomer("Peter");
    }
}
