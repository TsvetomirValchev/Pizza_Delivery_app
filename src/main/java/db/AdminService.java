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
import users.Customer;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class AdminService {

    private static final Logger LOGGER = LogManager.getLogger(AdminService.class.getName());
    private final PizzaDAO pizzaDAO = new PizzaDAO();
    private final DAO<Product> productDAO = new ProductDAO();
    private final DAO<Drink> drinkDAO = new DrinkDAO();
    private final DAO<Dessert> dessertDAO = new DessertDAO();
    private final DAO<Customer> customerDAO = new CustomerDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public AdminService() {}

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

    public void createDrinkProduct(int productId,String name, double price,Boolean isDiet){
        addProduct(productId,name,price);
        addDrink(productId,name,price,isDiet);
    }

    public void deleteDrink(int productId){
        try {
            Drink drink = getAllDrinks()
                    .values()
                    .stream()
                    .filter((d)->d.getId()==productId).findFirst().orElse(null);
            if(drink==null)
            {
                LOGGER.error("No drink found with product ID of: '"+productId+"'");
            }
            else if (isProductCurrentlyOrdered(drink.getId()))
            {
                LOGGER.error("This product is currently in a delivery: '"+productId+"'");
            }
            else
            {
                drinkDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Drink removed successfully!");
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't remove drink!");
        }
    }


    public void createDessertProduct(int productId,String name, double price,Boolean isVegan){
        addProduct(productId,name,price);
        addDessert(productId,name,price,isVegan);
    }

    public void deleteDessert(int productId){
        try {
            Dessert dessert = getAllDesserts()
                    .values()
                    .stream()
                    .filter((d)->d.getId()==productId).findFirst().orElse(null);
            if(dessert==null){
                LOGGER.error("No dessert found with product ID of: '"+productId+"'");
            }else if (isProductCurrentlyOrdered(dessert.getId())){
                LOGGER.error("This product is currently in a delivery: '"+productId+"'");
            }else
            {
                dessertDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Dessert removed successfully");
            }

        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't remove dessert!");
        }
    }

    public void createPizzaProduct(int productId,String name, double price, Size size,Sauce sauce, List<Meat> meats, List<Cheese> cheese, List<Addon> addons){
        addProduct(productId,name,price);
        addPizza(productId, name, price, size,sauce, meats, cheese, addons);
    }

    public void deletePizza(int productId){
        try {
            Pizza pizza = getAllPizzas()
                    .values()
                    .stream()
                    .filter((p)->p.getId()==productId).findFirst().orElse(null);

            if(pizza==null){
                LOGGER.error("No pizza found with  product ID of: '"+productId+"'");
            }
            else if (isProductCurrentlyOrdered(pizza.getId())){
                LOGGER.error("This product is currently in a delivery: '"+productId+"'");
            }
            else
            {
                pizzaDAO.deletePizzaIngredientList("cheese", productId,"pizza_id");
                pizzaDAO.deletePizzaIngredientList("meat", productId,"pizza_id");
                pizzaDAO.deletePizzaIngredientList("addon", productId,"pizza_id");
                pizzaDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Pizza removed successfully! ");
            }

        } catch (SQLException e) {
                LOGGER.debug(e.getMessage());
                LOGGER.error("Couldn't remove pizza!");
        }
    }

    public Map<Integer, Customer> getAllCustomers() {
        try {
            return customerDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public void addCustomer(Customer customer){
        try{
            customerDAO.insert(customer);
        }catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't register customer!");
        }

    }

    public void deleteCustomer(String customerUsername){
        try {
            Customer customer = getCustomerByUsername(customerUsername);
            if (customer != null ) {
                customerDAO.delete(customer.getId());
            } else {
                LOGGER.error("User with username: '"+customerUsername+"' does not exist!");
            }
        } catch (SQLException e) {
            if (e instanceof SQLDataException) {
                LOGGER.debug(e.getMessage());
                LOGGER.error("Couldn't delete customer account!");
            }
        }
    }

    public Customer getCustomerByUsername(String customerUsername){
        Map<Integer, Customer> customers = getAllCustomers();
        return customers.values().stream()
                .filter(c -> c.getUsername().equals(customerUsername))
                .findFirst()
                .orElse(null);
    }

    public Map<Integer, Ingredient> getAllIngredients(String tableName){
        try{
            return pizzaDAO.readAllIngredients(tableName);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't read ingredients!");
        }
        return  Collections.emptyMap();
    }


    //utils
    private void addProduct(int productId,String name, double price) {
        try {
            Product product = new Product(productId, name, price);
            productDAO.insert(product);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add product!");
        }
    }

    private void addPizza(int productId, String name, double price, Size size, Sauce sauce, List<Meat> meats, List<Cheese> cheeses, List<Addon> addons) {
        try {
            Pizza pizza = new Pizza(productId, name, price, size,sauce,meats,cheeses,addons);
            pizzaDAO.insert(pizza);
            addMeatsToPizza(pizza.getMeats(),pizza.getId());
            addCheesesToPizza(pizza.getCheeses(),pizza.getId());
            addAddonsToPizza(pizza.getAddons(),pizza.getId());
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add pizza!");
        }
    }

    private void addMeatsToPizza(List<Meat> meats, int pizzaId) throws SQLException {
        try{
            for (Meat meat : meats) {
                pizzaDAO.insertInPizzaMeatTable(pizzaId, meat.getId());
            }
        }catch (SQLException e){
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with adding Meats to the pizza");
        }
    }

    private void addCheesesToPizza(List<Cheese> cheeses, int pizzaId) throws SQLException {
        try{
            for (Cheese cheese : cheeses) {
                pizzaDAO.insertInPizzaCheeseTable(pizzaId, cheese.getId());
            }
        }catch (SQLException e){
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with adding cheeses to the pizza");
        }
    }

    private void addAddonsToPizza(List<Addon> addons, int pizzaId) throws SQLException {
        try{
            for (Addon addon : addons) {
                pizzaDAO.insertInPizzaCheeseTable(pizzaId, addon.getId());
            }
        }catch (SQLException e){
            LOGGER.debug(e.getMessage()+ "1");
            LOGGER.error("Something went wrong with adding addons to the pizza");
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

    private void addDessert(int productId,String name, double price, boolean isVegan) {
        try {
            Dessert dessert = new Dessert(productId, name, price, isVegan);
            dessertDAO.insert(dessert);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add dessert!");
        }
    }

    public boolean isProductCurrentlyOrdered(int productId) throws SQLException {
        for (Order order : orderDAO.getOrderByProductId(productId)) {
            if (order.getDeliveredAt().isEmpty()) {
                return true;
            }
    }
        return false;
    }


}
