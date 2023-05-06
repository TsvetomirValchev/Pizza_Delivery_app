package db;

import Order.Order;
import Products.Pizza;
import Products.Product;
import Users.Customer;
import View.abstraction.View;
import View.CustomerView;
import db.abstraction.Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class CustomerController extends Controller {

    private static final DAO<Pizza> pizzaDAO = new PizzaDAO();
    private static final DAO<Product> productDAO = new ProductDAO();
    private static final DAO<Order> orderDAO = new OrderDAO();
    private static final DAO<Customer> customerDAO = new CustomerDAO();
    private final CustomerView customerView = new CustomerView(this);
     private final String customerEmail;

    public CustomerController(String customerEmail){
        this.customerEmail = customerEmail;
    }


    @Override
    protected View getView() {
        return customerView;
    }

    public Customer getCustomer() {
        try {
            for(Customer c: customerDAO.read().values()){
                if(c.getEmail().equals(customerEmail)){
                    return c;
                }
            }
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE,"Couldn't find account!");
        }
        return null;
    }












    public List<Pizza> getPizzas() {
        try {
            return new ArrayList<>(pizzaDAO.read().values())
                    .stream()
                    .toList();
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load pizza list!");
        }
        return Collections.emptyList();
    }



}
