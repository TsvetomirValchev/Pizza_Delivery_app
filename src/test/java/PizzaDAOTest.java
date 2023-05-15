
import Products.PizzaIngredient.*;
import Products.Product;
import db.*;
import Products.Pizza;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class PizzaDAOTest {

    ProductDAO productDAO = new ProductDAO();
    PizzaDAO pizzaDAO = new PizzaDAO();



    @Test
    @DisplayName("Should read all meats in the database")
    public void testReadingMeatFromDB() throws SQLException {

        pizzaDAO.readAllIngredients("meat");
    }



    @Test
    @DisplayName("Test the create pizza method.")
    public void testAddPizza() throws SQLException {
        Product product = new Product(null,"salami",20.20);
        pizzaDAO.create(new Pizza(
                product.getId(),
                product.getName(),
                product.getPrice(),
                new Size(1,""),
                new Cheese(2,""),
                new Meat(2,""),
                new Sauce(2,""),
                new Addon(2,"")));
    }
}
