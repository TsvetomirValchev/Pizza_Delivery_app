package db;

import Order.*;
import Products.Dessert;
import Products.Drink;
import Products.Pizza;
import Products.PizzaIngredient.*;
import Products.PizzaIngredient.abstraction.PizzaIngredient;
import Products.Product;
import Users.Admin;
import Users.Customer;
import logging.PizzaDeliveryLogger;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminService {

    // making them static to ensure only 1 instance is ever created and final, so they are immutable
    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(AdminService.class.getName());
    private static final PizzaDAO pizzaDAO = new PizzaDAO();
    private static final DAO<Product> productDAO = new ProductDAO();
    private static final DAO<Drink> drinkDAO = new DrinkDAO();
    private static final DAO<Dessert> dessertDAO = new DessertDAO();
    private static final DAO<Customer> customerDAO = new CustomerDAO();
    private static final OrderDAO orderDAO = new OrderDAO();



    public AdminService() {

    }

    public Map<Integer, Pizza> getAllPizzas() {
        try {
            return pizzaDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public Map<Integer, Drink> getAllDrinks() {
        try {
            return drinkDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public Map<Integer, Dessert> getAllDesserts() {
        try {
            return dessertDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
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
                LOGGER.log(Level.WARNING,"No drink found with product ID of: '"+productId+"'");

            }
            else if (isProductCurrentlyOrdered(drink.getId()))
            {
                LOGGER.log(Level.WARNING,"This product is currently in a delivery: '"+productId+"'");

            }
            else
            {
                drinkDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Drink removed successfully!");
            }
        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                LOGGER.log(Level.WARNING,e.getMessage());
            } else {
                LOGGER.log(Level.SEVERE,"Couldn't remove drink!");
            }
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
                LOGGER.log(Level.WARNING,"No dessert found with product ID of: '"+productId+"'");
            }else if (isProductCurrentlyOrdered(dessert.getId())){
                LOGGER.log(Level.WARNING,"This product is currently in a delivery: '"+productId+"'");
            }else
            {
                dessertDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Dessert removed successfully");
            }

        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                LOGGER.log(Level.WARNING,e.getMessage());
            } else {
                LOGGER.log(Level.SEVERE,"Couldn't remove dessert!");
            }
        }
    }

    public void createPizzaProduct(int productId,String name, double price, Size size, Cheese cheese, Meat meat, Sauce sauce, Addon addon){
        addProduct(productId,name,price);
        addPizza(productId, name, price, size, cheese, meat, sauce, addon);
    }

    public void deletePizza(int productId){
        try {
            Pizza pizza = getAllPizzas()
                    .values()
                    .stream()
                    .filter((p)->p.getId()==productId).findFirst().orElse(null);

            if(pizza==null){
                LOGGER.log(Level.WARNING,"No pizza found with  product ID of: '"+productId+"'");
            }
            else if (isProductCurrentlyOrdered(pizza.getId())){
                LOGGER.log(Level.WARNING,"This product is currently in a delivery: '"+productId+"'");
            }
            else
            {
                pizzaDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Pizza removed successfully! ");
            }

        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                LOGGER.log(Level.WARNING,e.getMessage());
            } else {
                LOGGER.log(Level.SEVERE,"Couldn't remove pizza!");
            }
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
            LOGGER.log(Level.SEVERE, "Couldn't register customer!");
        }

    }

    public void deleteCustomer(String customerUsername){
        try {
            Customer customer = getCustomerByUsername(customerUsername);
            if (customer != null ) {
                customerDAO.delete(customer.getId());
            } else {
                LOGGER.log(Level.WARNING,"User with username: '"+customerUsername+"' does not exist!");
            }
        } catch (SQLException e) {
            if (e instanceof SQLDataException) {
                LOGGER.log(Level.SEVERE, "Couldn't delete customer account!");
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

    public Map<Integer, PizzaIngredient> getAllIngredients(String tableName){
        try{
            return pizzaDAO.readAllIngredients(tableName);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't read ingredients!");
        }
        return  Collections.emptyMap();
    }


    //utils
    private void addProduct(int productId,String name, double price) {
        try {
            Product product = new Product(productId, name, price);
            productDAO.insert(product);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't add pizza!");
        }
    }

    private void addPizza(int productId,String name, double price, Size size, Cheese cheese, Meat meat, Sauce sauce, Addon addon) {
        try {
            Pizza pizza = new Pizza(productId, name, price, size, cheese, meat, sauce, addon);
            pizzaDAO.insert(pizza);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't add pizza!");
        }
    }
    private void addDrink(int productId, String name, double price, boolean isDiet) {
        try {
            Drink drink = new Drink(productId, name, price, isDiet);
            drinkDAO.insert(drink);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't add drink!");
        }
    }

    private void addDessert(int productId,String name, double price, boolean isVegan) {
        try {
            Dessert dessert = new Dessert(productId, name, price, isVegan);
            dessertDAO.insert(dessert);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Couldn't add dessert!");
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
