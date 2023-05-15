package db;

import Order.*;
import Products.Dessert;
import Products.Drink;
import Products.Pizza;
import Products.PizzaIngredient.*;
import Products.Product;
import Users.Admin;
import Users.Customer;
import View.abstraction.View;
import View.AdminView;
import db.abstraction.Controller;


import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

public class AdminController extends Controller {

    // making them static to ensure only 1 instance is ever created and final, so they are immutable
    private static final PizzaDAO pizzaDAO = new PizzaDAO();
    private static final DAO<Product> productDAO = new ProductDAO();
    private static final DAO<Drink> drinkDAO = new DrinkDAO();

    private static final DAO<Dessert> dessertDAO = new DessertDAO();

    private static final DAO<Customer> customerDAO = new CustomerDAO();
    private static final DAO<Order> orderDAO = new OrderDAO();
    private static final DAO<Driver> driverDAO = new DriverDAO();

    private final AdminView adminView = new AdminView(this);
    private final Admin adminModel;


    public AdminController(Admin admin) {
        this.adminModel = new Admin();
    }

    public Map<Integer, Pizza> getAllPizzas() {
        try {
            return pizzaDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }


    public void createDrinkProduct(int productId,String name, double price,Boolean isDiet){
        addProduct(productId,name,price);
        addDrink(productId,name,price,isDiet);
    }

    public void createDessertProduct(int productId,String name, double price,Boolean isVegan){
        addProduct(productId,name,price);
        addDrink(productId,name,price,isVegan);
    }

    public void createPizzaProduct(int productId,String name, double price, Size size, Cheese cheese, Meat meat, Sauce sauce, Addon addon){
        addProduct(productId,name,price);
        addPizza(productId, name, price, size, cheese, meat, sauce, addon);
    }

    public void addProduct(int productId,String name, double price) {
        try {
            Product product = new Product(productId, name, price);
            productDAO.create(product);
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't add pizza!");
        }
    }

    public void addPizza(int productId,String name, double price, Size size, Cheese cheese, Meat meat, Sauce sauce, Addon addon) {
        try {
            Pizza pizza = new Pizza(productId, name, price, size, cheese, meat, sauce, addon);
            pizzaDAO.create(pizza);
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't add pizza!");
        }
    }

    public void addDrink(int productId,String name, double price, boolean isDiet) {
        try {
            Drink drink = new Drink(productId, name, price, isDiet);
            drinkDAO.create(drink);
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't add drink!");
        }
    }

    public void addDessert(int productId,String name, double price, boolean isVegan) {
        try {
            Dessert dessert = new Dessert(productId, name, price, isVegan);
            dessertDAO.create(dessert);
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't add dessert!");
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

    public Customer getCustomerByUsername(String customerUsername){
        Map<Integer, Customer> customers = getAllCustomers();
        return customers.values().stream()
                .filter(c -> c.getUsername().equals(customerUsername))
                .findFirst()
                .orElse(null);
    }

//    public Customer getCustomerByEmail(String customerEmail){
//        Map<Integer, Customer> customers = getAllCustomers();
//        return customers.values().stream()
//                .filter(c -> c.getUsername().equals(customerEmail))
//                .findFirst()
//                .orElse(null);
//    }

    public void addCustomer(Customer customer){
        try{
            customerDAO.create(customer);
        }catch (SQLException e) {
            transmitException(e,
                    Level.SEVERE,
                    "Couldn't register customer!");
        }

    }

    public Map<Integer, Order> getAllOrders() {
        try {
            return orderDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public Map<Integer, Driver> getAllDrivers() {
        try {
            return driverDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public void getAllIngredients(String tableName){
        try{
            pizzaDAO.readAllIngredients(tableName).forEach(System.out::println);
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't read ingredients!");
        }
    }


    @Override
    protected View getView() {
        return adminView;
    }
}
