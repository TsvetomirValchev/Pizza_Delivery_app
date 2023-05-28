import Products.Pizza;
import Users.Admin;
import db.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class AdminServiceTest {


    AdminService adminService = new AdminService(new Admin());


    @DisplayName("Should read all pizzas from the database")
    @Test
    public void readAllPizzasFromDatabase(){
        adminService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Pizza::getId))
                .forEach(System.out::println);
    }



}
