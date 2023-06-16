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
                drinkDAO.delete(productId);
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
                dessertDAO.delete(productId);
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
                pizzaDAO.delete(productId);
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

    public void deleteUser(String Username) {
        try {
            User user = getUserByUsername(Username);
            if (user != null) {
                userDAO.delete(user.getId());
            } else {
                LOGGER.error("User with username: '" + Username + "' does not exist!");
            }
        } catch (SQLException e) {

            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't delete customer account!");

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

    public List<Ingredient> getAllIngredients(Integer productId) {
        try {
            return pizzaDAO.readAllIngredientsInsideProduct(productId);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't read ingredients!");
        }
        return Collections.emptyList();
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

    public void addPizza(int id, String name, double price, List<Size> sizes, List<Ingredient> ingredients) {
        try {
            Pizza pizza = new Pizza(id, name, price, new ProductType(1, "pizza"), sizes, ingredients);
            pizzaDAO.insert(pizza);
            addSizesToProduct(sizes, id, price);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add Pizza!");
        }
    }


    private void addSizesToProduct(List<Size> sizes, int productId, double price) throws SQLException {
        try {
            for (Size size : sizes) {
                pizzaDAO.insertInProductSizeTable(productId, size.getId(), price);
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with adding sizes to the pizza");
        }
    }

    public void addDrink(int productId, String name, double price, List<Size> sizes, boolean isDiet) {
        try {
            Drink drink = new Drink(productId, name, price, new ProductType(2, "drink"), sizes, isDiet);
            drinkDAO.insert(drink);
            addSizesToProduct(sizes, productId, price);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add drink!");
        }
    }

    public void addDessert(int productId, String name, double price, List<Size> sizes, boolean isVegan) {
        try {
            Dessert dessert = new Dessert(productId, name, price, new ProductType(3, "dessert"), sizes, isVegan);
            dessertDAO.insert(dessert);
            addSizesToProduct(sizes, productId, price);
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
