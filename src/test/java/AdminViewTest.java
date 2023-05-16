import Users.Admin;
import View.AdminView;
import db.AdminController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AdminViewTest {
    AdminController adminController = new AdminController(new Admin());
    AdminView adminView = new AdminView(adminController);
    
    @DisplayName("Should add a specific Pizza to the database both in the product table and in the pizza table.")
    @Test
    public void addAPizzaMenuTest()
    {
    adminView.addAPizzaMenu();
    }
}
