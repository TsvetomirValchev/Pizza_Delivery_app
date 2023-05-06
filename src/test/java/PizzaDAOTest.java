import db.PizzaDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class PizzaDAOTest {





    @Test
    @DisplayName("Should read all meats in the database")
    public void testReadingMeatFromDB() throws SQLException {
        PizzaDAO pizzaDAO = new PizzaDAO();
        pizzaDAO.readAllIngredients("meat");
    }
}
