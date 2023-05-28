import Products.Dessert;
import Products.Drink;
import Products.Pizza;
import Users.Customer;
import db.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class CustomerServiceTest {


    CustomerService customerService = new CustomerService(new Customer("testCustomer","testPassword",1001,"test@email.com","TestAddress"));


    @DisplayName("Should read all pizzas from the database")
    @Test
    public void readAllPizzasFromDatabase(){
        customerService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Pizza::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should read all drinks from the database")
    @Test
    public void readAllDrinksFromDatabase(){
        customerService.getAllDrinks()
                .values()
                .stream()
                .sorted(Comparator.comparing(Drink::getId))
                .forEach(System.out::println);
    }

    @DisplayName("Should read all desserts from the database")
    @Test
    public void readAllDessertsFromDatabase(){
        customerService.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Dessert::getId))
                .forEach(System.out::println);
    }
}
