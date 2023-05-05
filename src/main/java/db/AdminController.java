package db;

import Order.*;
import Products.Pizza;
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
    private static final CustomerDAO customerDAO = new CustomerDAO();

    private static final OrderDAO orderDAO = new OrderDAO();
    private static final DriverDAO driverDAO = new DriverDAO();
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

    public Map<Integer, Customer> getAllCustomers() {
        try {
            return customerDAO.read();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
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


    public void addCustomer(Customer customer){
        try{
            customerDAO.create(customer);
        }catch (SQLException e) {
            transmitException(e,
                    Level.SEVERE,
                    "Couldn't register customer!");
        }

    }


    public void addProduct(){}





    @Override
    protected View getView() {
        return adminView;
    }
}
