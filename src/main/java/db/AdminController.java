package db;

import Products.Pizza;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class AdminController {

    private static final PizzaDAO pizzaDAO = new PizzaDAO();

    public Map<Integer, Pizza> getAllPizzas() {
        try {
            return pizzaDAO.read();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
}
