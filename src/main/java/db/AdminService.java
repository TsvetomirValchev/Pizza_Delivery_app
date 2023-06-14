package db;

import order.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import products.Dessert;
import products.Drink;
import products.Pizza;
import products.ingredient.*;
import products.ingredient.abstraction.Ingredient;
import products.Product;
import users.Admin;
import users.Customer;
import users.User;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class AdminService extends Service {

    private static final Logger LOGGER = LogManager.getLogger(AdminService.class.getName());
    private final PizzaDAO pizzaDAO = new PizzaDAO();
    private final DAO<Product> productDAO = new ProductDAO();
    private final DAO<Drink> drinkDAO = new DrinkDAO();
    private final DAO<Dessert> dessertDAO = new DessertDAO();
    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();


    public void createDrinkProduct(int productId, String name, double price, Boolean isDiet) {
        addProduct(productId, name, price);
        addDrink(productId, name, price, isDiet);
    }

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
                productDAO.delete(productId);
                System.out.println("Drink removed successfully!");
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't remove drink!");
        }
    }


    public void createDessertProduct(int productId, String name, double price, Boolean isVegan) {
        addProduct(productId, name, price);
        addDessert(productId, name, price, isVegan);
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
                productDAO.delete(productId);
                System.out.println("Dessert removed successfully");
            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't remove dessert!");
        }
    }

    public void createPizzaProduct(int productId, String name, double price, Sauce sauce, List<Size> sizes, List<Meat> meats, List<Cheese> cheese, List<Addon> addons) {
        addProduct(productId, name, price);
        addPizza(productId, name, price, sauce, sizes, meats, cheese, addons);
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
                pizzaDAO.deletePizzaIngredientList("pizza_cheese", productId);
                pizzaDAO.deletePizzaIngredientList("pizza_meat", productId);
                pizzaDAO.deletePizzaIngredientList("pizza_addon", productId);
                pizzaDAO.delete(productId);
                productDAO.delete(productId);
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


    public Map<Integer, Customer> getAllCustomers() {
        try {
            return userDAO.readOnlyCustomers();
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

    public Map<Integer, Ingredient> getAllIngredients(String tableName) {
        try {
            return pizzaDAO.readAllIngredients(tableName);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't read ingredients!");
        }
        return Collections.emptyMap();
    }


    private void addProduct(int productId, String name, double price) {
        try {
            Product product = new Product(productId, name, price);
            productDAO.insert(product);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add product!");
        }
    }

    private void addPizza(int productId, String name, double price, Sauce sauce, List<Size> sizes, List<Meat> meats, List<Cheese> cheeses, List<Addon> addons) {
        try {
            Pizza pizza = new Pizza(productId, name, price, sauce, sizes, meats, cheeses, addons);
            pizzaDAO.insert(pizza);
            addSizesToPizza(pizza.getSizes(), pizza.getId());
            addMeatsToPizza(pizza.getMeats(), pizza.getId());
            addCheesesToPizza(pizza.getCheeses(), pizza.getId());
            addAddonsToPizza(pizza.getAddons(), pizza.getId());
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add pizza!");
        }
    }

    private void addMeatsToPizza(List<Meat> meats, int pizzaId) throws SQLException {
        try {
            for (Meat meat : meats) {
                pizzaDAO.insertInPizzaMeatTable(pizzaId, meat.getId());
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with adding Meats to the pizza");
        }
    }

    private void addCheesesToPizza(List<Cheese> cheeses, int pizzaId) throws SQLException {
        try {
            for (Cheese cheese : cheeses) {
                pizzaDAO.insertInPizzaCheeseTable(pizzaId, cheese.getId());
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with adding cheeses to the pizza");
        }
    }

    private void addAddonsToPizza(List<Addon> addons, int pizzaId) throws SQLException {
        try {
            for (Addon addon : addons) {
                pizzaDAO.insertInPizzaAddonTable(pizzaId, addon.getId());
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with adding addons to the pizza");
        }
    }

    private void addSizesToPizza(List<Size> sizes, int pizzaId) throws SQLException {
        try {
            for (Size size : sizes) {
                pizzaDAO.insertInPizzaSizeTable(pizzaId, size.getId());
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with adding sizes to the pizza");
        }
    }

    private void addDrink(int productId, String name, double price, boolean isDiet) {
        try {
            Drink drink = new Drink(productId, name, price, isDiet);
            drinkDAO.insert(drink);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add drink!");
        }
    }

    private void addDessert(int productId, String name, double price, boolean isVegan) {
        try {
            Dessert dessert = new Dessert(productId, name, price, isVegan);
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
