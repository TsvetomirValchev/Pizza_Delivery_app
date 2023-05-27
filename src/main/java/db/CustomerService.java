package db;

import Order.Order;
import Products.Dessert;
import Products.Drink;
import Products.Pizza;
import Products.Product;
import Users.Customer;
import logging.PizzaDeliveryLogger;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerService {

    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(CustomerService.class.getName());
    private static final DAO<Pizza> pizzaDAO = new PizzaDAO();
    private static final DAO<Dessert> dessertDAO = new DessertDAO();
    private static final DAO<Drink> drinkDAO = new DrinkDAO();
    private static final DAO<Product> productDAO = new ProductDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final DAO<Customer> customerDAO = new CustomerDAO();
    private final Customer customer;

    public CustomerService(Customer customer){
        this.customer = customer;
    }


    public boolean placeAnOrder(int productId){
        try {
            if (getProductByID(productId) == null) {
                LOGGER.log(Level.WARNING,"There is no product with such id");
                return false;
            }
            if (isOrderFinalized()) {
                orderDAO.create(new Order(null,
                        customer.getId(),
                       Optional.empty(),
                        Optional.empty()));
            }
            if(!isOrderFinalized())
            {
                addProductToOrderCart(productId);
                return true;
            }else {
                LOGGER.log(Level.WARNING, "You cannot add a product to an order that is already waiting for delivery!");
            }

        }catch (SQLException e){
            LOGGER.log(Level.SEVERE , "Couldn't place an order!");
        }
        return false;
    }
    private void addProductToOrderCart(int productId){
        try {
            orderDAO.InsertInOrderItemTable(productId, getCurrentOrderIdByCustomerId(customer.getId()));
        }catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Couldn't add product to cart!");
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
                                (getCurrentOrderIdByCustomerId(customer.getId())
                        )
                );
            }
            catch (SQLException e)
            {
                LOGGER.log(Level.SEVERE, "Couldn't get all product in the order cart!");
            }
            return Collections.emptyList();
    }

    public void markOrderAsReceived(){
        try{
            if (isUserCurrentlyWaitingDelivery()) {
                orderDAO.update(getCurrentOrderIdByCustomerId(customer.getId()),4,LocalDateTime.now());

            } else {
                LOGGER.log(Level.WARNING,"You are not expecting delivery!");
            }
        }catch (SQLException e){
            LOGGER.log(Level.SEVERE,"Couldn't update order status!");
        }

    }

    public void markOrderAsFinalized(){
        try{
            if (!isOrderFinalized()) {
                orderDAO.update(getCurrentOrderIdByCustomerId(customer.getId()),3,LocalDateTime.now());
            } else {
                LOGGER.log(Level.WARNING,"You do not have an order to finalize!");
            }
        }catch (SQLException e){
            LOGGER.log(Level.SEVERE,"Couldn't update order status!");
        }

    }

    public List<Pizza> getPizzas() {
        try {
            return new ArrayList<>(pizzaDAO.readAll().values());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Couldn't load pizza list!");
        }
        return Collections.emptyList();
    }

    public List<Dessert> getDesserts() {
        try {
            return new ArrayList<>(dessertDAO.readAll().values());
        } catch (SQLException e) {
           LOGGER.log(Level.SEVERE,"Couldn't load dessert list!");
        }
        return Collections.emptyList();
    }

    public List<Drink> getDrinks() {
        try {
            return new ArrayList<>(drinkDAO.readAll().values());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Couldn't load drinks list!");
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
           LOGGER.log(Level.SEVERE,"Couldn't find order!");
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
            LOGGER.log(Level.SEVERE,"No product with such id exists.");
        }
        return null;
    }

    private boolean isUserCurrentlyWaitingDelivery() {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId()==(customer.getId()) && order.getDeliveredAt().isEmpty()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Couldn't load order details!");
        }
        return false;
    }

    private boolean isOrderFinalized(){
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId()==(customer.getId()) && order.getOrderedAt().isEmpty()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Couldn't load order details!");
        }
        return true;
    }



}


