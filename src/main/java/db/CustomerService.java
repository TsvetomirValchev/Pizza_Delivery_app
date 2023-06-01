package db;

import order.Order;
import products.Dessert;
import products.Drink;
import products.Pizza;
import products.Product;
import users.Customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class CustomerService {

    private final DAO<Pizza> pizzaDAO = new PizzaDAO();
    private final DAO<Dessert> dessertDAO = new DessertDAO();
    private final DAO<Drink> drinkDAO = new DrinkDAO();
    private final DAO<Product> productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final Customer customer;

    public CustomerService(Customer customer){
        this.customer = customer;
    }

    public boolean placeAnOrder(int productId){
        try {
            if (getProductByID(productId) == null) {
                System.err.println("There is no product with such id");
                return false;
            }
            if(!isOrderFinalized())
            {
                addProductToCart(productId);
            }
            else {
                orderDAO.insert(new Order(null,
                        customer.getId(),
                        Optional.empty(),
                        Optional.empty()));
                addProductToCart(productId);
                System.out.println("the product has been added to your cart");
            }

        }catch (SQLException e){
            System.err.println("Couldn't place an order!");
        }
        return true;
    }
    private void addProductToCart(int productId){
        try {
            orderDAO.InsertInOrderItemTable(productId, getCartOrderIdByCustomerId(customer.getId()));
        }catch (SQLException e){
            System.err.println( "Couldn't add product to cart!");
        }
    }

    public String calculateCurrentOrderTotal(){
        double orderTotal = 0;
        for (Product product: getAllProductsInCurrentlyDeliveringOrder()){
            orderTotal += product.getPrice();
        }
        return orderTotal+" BGN";
    }

    public String calculateCartTotal(){
        double orderTotal = 0;
        for (Product product: getAllProductsInCart()){
            orderTotal += product.getPrice();
        }
        return orderTotal+" BGN";
    }

    public List<Product> getAllProductsInCurrentlyDeliveringOrder(){
            try{
                return new ArrayList<>(
                        orderDAO.getAllProductsInOrder
                                (getCurrentlyDeliveringOrderIdByCustomerId(customer.getId())
                        )
                );
            }
            catch (SQLException e)
            {
                System.err.println( "Couldn't get all product in the order cart!");
            }
            return Collections.emptyList();
    }

    public List<Product> getAllProductsInCart(){
        try{
            return new ArrayList<>(
                    orderDAO.getAllProductsInOrder
                            (getCartOrderIdByCustomerId(customer.getId()))
            );
        }
        catch (SQLException e)
        {
            System.err.println( "Couldn't get all product in the order cart!");
        }
        return Collections.emptyList();
    }

    public void markOrderAsReceived(){
        try{
            if (isUserCurrentlyWaitingDelivery()) {
                orderDAO.update(getCurrentlyDeliveringOrderIdByCustomerId(customer.getId()),4,LocalDateTime.now());

            } else {
                System.err.println("You are not expecting delivery!");
            }
        }catch (SQLException e){
            System.err.println("Couldn't update order status!");
        }

    }

    public void markOrderAsFinalized(){
        try{
            if (!isOrderFinalized()) {
                orderDAO.update(getCartOrderIdByCustomerId(customer.getId()),3,LocalDateTime.now());
            } else {
                System.err.println("You do not have an order to finalize!");
            }
        }catch (SQLException e){
            System.err.println("Couldn't update order status!");
        }

    }

    public Map<Integer, Pizza> getAllPizzas() {
        try {
            return pizzaDAO.readAll();
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

    public Map<Integer, Drink> getAllDrinks() {
        try {
            return drinkDAO.readAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    //utils
    private int getCurrentlyDeliveringOrderIdByCustomerId(int customerId) {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == customerId && order.getOrderedAt().isPresent() && order.getDeliveredAt().isEmpty()) {
                    return order.getId();
                }
            }
        } catch (SQLException e) {
           System.err.println("Couldn't find order!");
        }
        return 0;
    }

    private int getCartOrderIdByCustomerId(int customerId) {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == customerId && order.getOrderedAt().isEmpty()) {
                    return order.getId();
                }
            }
        } catch (SQLException e) {
            System.err.println("Couldn't find order!");
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
            System.err.println("No product with such id exists.");
        }
        return null;
    }

    private boolean isUserCurrentlyWaitingDelivery() {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId()==(customer.getId()) && order.getOrderedAt().isPresent() && order.getDeliveredAt().isEmpty()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Couldn't load order details!");
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
            System.err.println("Couldn't load order details!");
        }
        return true;
    }

}


