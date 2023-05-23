package db;

import Order.Order;
import Products.Dessert;
import Products.Drink;
import Products.Pizza;
import Products.Product;
import Users.Customer;
import View.abstraction.View;
import View.CustomerView;
import db.abstraction.Controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class CustomerController extends Controller {

    private static final DAO<Pizza> pizzaDAO = new PizzaDAO();
    private static final DAO<Dessert> dessertDAO = new DessertDAO();
    private static final DAO<Drink> drinkDAO = new DrinkDAO();
    private static final DAO<Product> productDAO = new ProductDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
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

    public Customer getCustomerAccount() {
        try {
            for(Customer c: customerDAO.readAll().values()){
                if(c.getEmail().equals(customerEmail)){
                    return c;
                }
            }
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE,"Couldn't find account!");
        }
        return null;
    }
    public boolean placeAnOrder(int productId){
        try {
            if (getProductByID(productId) == null) {
                transmitException(new IllegalArgumentException(), Level.WARNING, "Product with ID '" + productId + "' not found!");
                return false;
            }
            if (isOrderFinalized()) {
                orderDAO.create(new Order(null,
                        getCustomerAccount().getId(),
                       Optional.empty(),
                        Optional.empty()));
            }
            if(!isOrderFinalized())
            {
                addProductToOrderCart(productId);
                return true;
            }else {
                transmitException(new IllegalArgumentException() , Level.WARNING, "You cannot add a product to an order that is already waiting for delivery!");
            }

        }catch (SQLException e){
            transmitException(e, Level.SEVERE , "Couldn't place an order!");
        }
        return false;
    }
    private void addProductToOrderCart(int productId){
        try {
            orderDAO.InsertInOrderItemTable(productId, getCurrentOrderIdByCustomerId(getCustomerAccount().getId()));
        }catch (SQLException e){
            transmitException(e, Level.SEVERE, "Couldn't add product to cart!");
        }
    }

    public String calculateCurrentOrderTotal(){
        double orderTotal = 0;
        for (Product product: GetAllProductsInCurrentOrder()){
            orderTotal += product.getPrice();
        }
        return orderTotal+" BGN";
    }

    public List<Product> GetAllProductsInCurrentOrder(){
            try{
                return new ArrayList<>(
                        orderDAO.getAllProductsInOrder
                                (getCurrentOrderIdByCustomerId(getCustomerAccount().getId())
                        )
                );
            }
            catch (SQLException e)
            {
                transmitException(e, Level.SEVERE, "Couldn't display your order details!");
            }
            return Collections.emptyList();
    }

    public void markOrderAsReceived(){
        try{
            if (isUserCurrentlyWaitingDelivery()) {
                orderDAO.update(getCurrentOrderIdByCustomerId(getCustomerAccount().getId()),4,LocalDateTime.now());

            } else {
                transmitException(new IllegalStateException(),Level.WARNING,"You are not expecting delivery!");
            }
        }catch (SQLException e){
            transmitException(e,Level.SEVERE,"Couldn't update order status!");
        }

    }

    public void markOrderAsFinalized(){
        try{
            if (!isOrderFinalized()) {
                orderDAO.update(getCurrentOrderIdByCustomerId(getCustomerAccount().getId()),3,LocalDateTime.now());
            } else {
                transmitException(new IllegalStateException(),Level.WARNING,"You do not have an order to finalize!");
            }
        }catch (SQLException e){
            transmitException(e,Level.SEVERE,"Couldn't update order status!");
        }

    }

    public List<Pizza> getPizzas() {
        try {
            return new ArrayList<>(pizzaDAO.readAll().values());
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load pizza list!");
        }
        return Collections.emptyList();
    }

    public List<Dessert> getDesserts() {
        try {
            return new ArrayList<>(dessertDAO.readAll().values());
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load dessert list!");
        }
        return Collections.emptyList();
    }

    public List<Drink> getDrinks() {
        try {
            return new ArrayList<>(drinkDAO.readAll().values());
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load drinks list!");
        }
        return Collections.emptyList();
    }

    //utils
    private int getCurrentOrderIdByCustomerId(int customerId) {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == customerId && order.getDeliveredAt().isEmpty()) {
                    return order.getId();
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't find order!");
        }
        return 0;
    }

    private Product getProductByID(int productId) {
        try {
            for (Product product : productDAO.readAll().values()) {
                if (product.getId() == productId) {
                    return product;
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't find product!");
        }
        return null;
    }

    private boolean isUserCurrentlyWaitingDelivery() {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId()==(getCustomerAccount().getId()) && order.getDeliveredAt().isEmpty()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load order details!");
        }
        return false;
    }

    private boolean isOrderFinalized(){
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId()==(getCustomerAccount().getId()) && order.getOrderedAt().isEmpty()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't load order details!");
        }
        return true;
    }



}


