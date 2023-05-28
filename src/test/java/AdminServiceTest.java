import Products.Dessert;
import Products.Drink;
import Products.Pizza;
import Users.Admin;
import Users.Customer;
import db.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class AdminServiceTest {

    AdminService adminService = new AdminService();

    @DisplayName("Should read all pizzas from the database")
    @Test
    public void readAllPizzasFromDatabase(){
        adminService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Pizza::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should read all customers from the database")
    @Test
    public void readAllCustomersFromDatabase(){
        adminService.getAllCustomers()
                .values()
                .stream()
                .sorted(Comparator.comparing(Customer::getId))
                .forEach(System.out::println);
    }
    @DisplayName("Should read all drinks from the database")
    @Test
    public void readAllDrinksFromDatabase(){
        adminService.getAllDrinks()
                .values()
                .stream()
                .sorted(Comparator.comparing(Drink::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should read all desserts from the database")
    @Test
    public void readAllDessertsFromDatabase(){
        adminService.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Dessert::getId))
                .forEach(System.out::println);
    }


}
