package db;

import order.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import products.*;
import users.Admin;
import users.User;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class AdminService extends Service {

    private static final Logger LOGGER = LogManager.getLogger(AdminService.class.getName());
    private final PizzaDAO pizzaDAO = new PizzaDAO();
    private final DAO<Drink> drinkDAO = new DrinkDAO();
    private final DAO<Dessert> dessertDAO = new DessertDAO();
    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();


    public void deleteDrink(int productId) {
        try {
            Drink drink = getAllDrinks()
                    .values()
                    .stream()
                    .filter((d) -> d.getId() == productId).findFirst().orElse(null);
            if (drink == null) {
                LOGGER.error("No drink found with product ID of: '" + productId + "'");
            } else if (isProductCurrentlyOrdered(drink.getId())) {
                LOGGER.error("This product is currently in a delivery: '" + productId + "'");
            } else {
                orderDAO.deleteProductFromCompletedOrder(drink.getId());
                drinkDAO.deleteProductSizesAndPrices(drink.getId());
                drinkDAO.delete(drink.getId());
                System.out.println("Drink removed successfully!");
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't remove drink!");
        }
    }


    public void deleteDessert(int productId) {
        try {
            Dessert dessert = getAllDesserts()
                    .values()
                    .stream()
                    .filter((d) -> d.getId() == productId).findFirst().orElse(null);
            if (dessert == null) {
                LOGGER.error("No dessert found with product ID of: '" + productId + "'");
            } else if (isProductCurrentlyOrdered(dessert.getId())) {
                LOGGER.error("This product is currently in a delivery: '" + productId + "'");
            } else {
                orderDAO.deleteProductFromCompletedOrder(dessert.getId());
                dessertDAO.deleteProductSizesAndPrices(dessert.getId());
                dessertDAO.delete(dessert.getId());
                System.out.println("Dessert removed successfully");
            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't remove dessert!");
        }
    }

    public void deletePizza(int productId) {
        try {
            Pizza pizza = getAllPizzas()
                    .values()
                    .stream()
                    .filter((p) -> p.getId() == productId).findFirst().orElse(null);

            if (pizza == null) {
                LOGGER.error("No pizza found with  product ID of: '" + productId + "'");
            } else if (isProductCurrentlyOrdered(pizza.getId())) {
                LOGGER.error("This product is currently in a delivery: '" + productId + "'");
            } else {
                orderDAO.deleteProductFromCompletedOrder(pizza.getId());
                pizzaDAO.deleteProductSizesAndPrices(pizza.getId());
                pizzaDAO.deletePizzaIngredientList(pizza.getId());
                pizzaDAO.delete(pizza.getId());
                System.out.println("Pizza removed successfully!");
            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't remove pizza!");
        }
    }

    public Map<Integer, User> getAllUsers() {
        try {
            return userDAO.readAll();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
        }
        return Collections.emptyMap();
    }

    public void deleteUser(String username) {
        try {
            User user = getUserByUsername(username);
            if (user == null) {
                throw new IllegalArgumentException("User with username: '" + username + "' does not exist!");

            }
            if (orderDAO.hasActiveOrders(user.getId())) {
                throw new IllegalArgumentException("User is currently awaiting delivery and cannot be deleted.");
            }
            orderDAO.deleteCompletedOrdersItemsList(user.getId());
            orderDAO.deleteCustomerFromCompletedOrder(user.getId());
            userDAO.delete(user.getId());
        } catch (SQLException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException) {
                LOGGER.debug(e.getMessage());
                LOGGER.error(e.getMessage());
            } else {
                LOGGER.debug(e.getMessage());
                LOGGER.error("Something went wrong with deleting user!");
            }
        }
    }

    public User getUserByUsername(String Username) {
        Map<Integer, User> users = getAllUsers();
        return users.values()
                .stream()
                .filter(u -> u.getUsername().equals(Username))
                .findFirst()
                .orElse(null);
    }

    public Map<Integer, Ingredient> getAllIngredientsAvailable() {
        try {
            return pizzaDAO.readAllIngredientsAvailable();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't read ingredients!");
        }
        return Collections.emptyMap();
    }

    public Map<Integer, Size> getAllSizesAvailable() {
        try {
            return pizzaDAO.readAllFromSizeTable();
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't read sizes!");
        }
        return Collections.emptyMap();
    }

    public void addPizza(int id, String name, Map<Size, Double> sizesAndPrices, List<Ingredient> ingredients) {
        try {

            Pizza pizza = new Pizza(id, name, new ProductType(1, "pizza"), sizesAndPrices, ingredients);
            pizzaDAO.insert(pizza);

            for (Ingredient ingredient : ingredients) {
                pizzaDAO.insertInProductIngredientTable(pizza.getId(), ingredient.getId());
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add Pizza!");
        }
    }


    public void addDrink(int productId, String name, Map<Size, Double> sizesAndPrices, boolean isDiet) {
        try {
            Drink drink = new Drink(productId, name, new ProductType(2, "drink"), sizesAndPrices, isDiet);
            drinkDAO.insert(drink);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add drink!");
        }
    }

    public void addDessert(int productId, String name, Map<Size, Double> sizesAndPrices, boolean isVegan) {
        try {
            Dessert dessert = new Dessert(productId, name, new ProductType(3, "dessert"), sizesAndPrices, isVegan);
            dessertDAO.insert(dessert);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add dessert!");
        }
    }

    private boolean isProductCurrentlyOrdered(int productId) throws SQLException {
        for (Order order : orderDAO.getOrderByProductId(productId)) {
            if (order.getDeliveredAt().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void createAdminAccount(String username, String password, String email) {
        try {
            Admin admin = new Admin(null, username, password, email);
            userDAO.insert(admin);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't create admin account");
        }
    }
}
