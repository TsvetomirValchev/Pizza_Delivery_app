package db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import products.Dessert;
import products.Drink;
import products.Pizza;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class Service {
    private static final Logger LOGGER = LogManager.getLogger(Service.class.getName());
    private final PizzaDAO pizzaDAO = new PizzaDAO();
    private final DAO<Drink> drinkDAO = new DrinkDAO();
    private final DAO<Dessert> dessertDAO = new DessertDAO();

    public Map<Integer, Pizza> getAllPizzas() {
        try {
            return pizzaDAO.readAll();
        } catch (SQLException e) {
            LOGGER.debug(e);
        }
        return Collections.emptyMap();
    }

    public Map<Integer, Drink> getAllDrinks() {
        try {
            return drinkDAO.readAll();
        } catch (SQLException e) {
            LOGGER.debug(e);
        }
        return Collections.emptyMap();
    }

    public Map<Integer, Dessert> getAllDesserts() {
        try {
            return dessertDAO.readAll();
        } catch (SQLException e) {
            LOGGER.debug(e);
        }
        return Collections.emptyMap();
    }
}
