import products.Dessert;
import products.Drink;
import products.Pizza;
import users.Customer;
import db.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class CustomerServiceTest {
    CustomerService customerService = new CustomerService(new Customer("testCustomer","testPassword",1,"test@email.com","TestAddress"));

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

    @DisplayName("Should add a product to an order that has no ordered_at value")
    @Test
    public void testAddProductToOrderCart(){
        customerService.placeAnOrder(101);
    }

    @DisplayName("Should give us all the products that are currently in the cart(an order without ordered_at value) and their total price")
    @Test
    public void testGetCartDetails(){
        System.out.println(customerService.getAllProductsInCartOrder());
        System.out.println(customerService.calculateCartOrderTotal());
    }
    @DisplayName("Should finalize current order by giving it a ordered_at value")
    @Test
    public void testFinalizeOrder(){
        customerService.markOrderAsFinalized();
    }
    @DisplayName("Should give us all the products that are currently waiting delivery(an order with ordered_at value but without delivered_at value) and their total price")
    @Test
    public void testGetCurrentOrderWaitingDeliveryDetails(){
        System.out.println(customerService.getAllProductsInCurrentlyDeliveringOrder());
        System.out.println(customerService.calculateCurrentOrderTotal());
    }
    @DisplayName("Should mark currently delivering order as delivered by giving it a delivered_at value")
    @Test
    public void testMarkOrderAsDone(){
        customerService.markOrderAsReceived();
    }
}
