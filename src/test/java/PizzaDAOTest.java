import db.AdminController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class PizzaDAOTest {



    @Test
    @DisplayName("Should read all pizzas from the database with the correct names using join")
    public void testReadingPizzaFromDB(){
        AdminController adminController = new AdminController();
        adminController.getAllPizzas()
            .values()
            .stream()
            .sorted()
            .forEach(System.out::println);
    }

}
