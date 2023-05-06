import Users.Admin;
import db.AdminController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class AdminControllerTest {

    @Test
    @DisplayName("Should read all pizzas from the database with the correct names using join")
    public void testReadingPizzaFromDB(){

        AdminController adminController = new AdminController(new Admin());
        adminController.getAllPizzas()
            .values()
            .stream()
            .sorted()
            .forEach(System.out::println);
    }

    @Test
    @DisplayName("Should read all customers from the database with the correct usernames,e-mails and addresses ")
    public void testReadingCustomersFromDB(){

        AdminController adminController = new AdminController(new Admin());
        adminController.getAllCustomers()
                .values()
                .stream()
                .sorted()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("Should read all orders from the database with the correct username of the client, first and last name of the driver")
    public void testReadingOrdersFromDB(){

        AdminController adminController = new AdminController(new Admin());
        adminController.getAllOrders()
                .values()
                .stream()
                .sorted()
                .forEach(System.out::println);
    }

    @Test
    @DisplayName("Should read all drivers from the database with the correct first and last name of the driver and status")
    public void testReadingDriversFromDB(){

        AdminController adminController = new AdminController(new Admin());
        adminController.getAllDrivers()
                .values()
                .forEach(System.out::println);
    }



}
