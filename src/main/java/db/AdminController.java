package db;

import Order.*;
import Products.Pizza;
import Products.PizzaIngredient.*;
import Products.PizzaIngredient.abstraction.PizzaIngredient;
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
    private static final DAO<Pizza> pizzaDAO = new PizzaDAO();
    private static final DAO<Product> productDAO = new ProductDAO();
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
            return pizzaDAO.read();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }



    public void addPizza(Integer productID, String name,double price,Size size,Cheese cheese,Meat meat,Sauce sauce,Addon addon){
        try {
            productDAO.create(new Product(null,name,price));
            pizzaDAO.create(new Pizza(null,name,price,productID,size,cheese,meat,sauce,addon));
        }catch (SQLException e){
            transmitException(e,
                    Level.SEVERE,
                    "Couldn't add pizza!");
        }

    }

    public Map<Integer, Customer> getAllCustomers() {
        try {
            return customerDAO.read();
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

    public Customer getCustomerByEmail(String customerEmail){
        Map<Integer, Customer> customers = getAllCustomers();
        return customers.values().stream()
                .filter(c -> c.getUsername().equals(customerEmail))
                .findFirst()
                .orElse(null);
    }

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
            return orderDAO.read();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public Map<Integer, Driver> getAllDrivers() {
        try {
            return driverDAO.read();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    @Override
    protected View getView() {
        return adminView;
    }
}
