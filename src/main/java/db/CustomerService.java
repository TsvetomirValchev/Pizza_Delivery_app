package db;

import Order.Order;
import Products.Dessert;
import Products.Drink;
import Products.Pizza;
import Products.Product;
import Users.Customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class CustomerService {

    /* making them static to ensure only 1 instance is ever created(so whenever an AdminService instance is created it will always be the same DAO objects) and final, so they are immutable
     * Should these be static? Is there any benefit from using dependency injection in my concrete scenario? Should I initialize them in the constructor instead? Is there benefit from that?
     * */
    private final DAO<Pizza> pizzaDAO = new PizzaDAO();
    private final DAO<Dessert> dessertDAO = new DessertDAO();
    private final DAO<Drink> drinkDAO = new DrinkDAO();
    private final DAO<Product> productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final Customer customer;

    /*Making it take customer as a dependency because I want to be able to perform various checks using this specific customer's fields in the database...*/
    public CustomerService(Customer customer){
        this.customer = customer;
    }


    public boolean placeAnOrder(int productId){
        try {
            if (getProductByID(productId) == null) {
                System.err.println("There is no product with such id");
                return false;
            }
            if (isOrderFinalized()) {
                orderDAO.insert(new Order(null,
                        customer.getId(),
                       Optional.empty(),
                        Optional.empty()));
            }
            if(!isOrderFinalized())
            {
                addProductToOrderCart(productId);
                return true;
            }else {
                System.err.println( "You cannot add a product to an order that is already waiting for delivery!");
            }

        }catch (SQLException e){
            System.err.println("Couldn't place an order!");
        }
        return false;
    }
    private void addProductToOrderCart(int productId){
        try {
            orderDAO.InsertInOrderItemTable(productId, getCurrentOrderIdByCustomerId(customer.getId()));
        }catch (SQLException e){
            System.err.println( "Couldn't add product to cart!");
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
                System.err.println( "Couldn't get all product in the order cart!");
            }
            return Collections.emptyList();
    }

    public void markOrderAsReceived(){
        try{
            if (isUserCurrentlyWaitingDelivery()) {
                orderDAO.update(getCurrentOrderIdByCustomerId(customer.getId()),4,LocalDateTime.now());

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
                orderDAO.update(getCurrentOrderIdByCustomerId(customer.getId()),3,LocalDateTime.now());
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
    private int getCurrentOrderIdByCustomerId(int customerId) {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == customerId && order.getDeliveredAt().isEmpty()) {
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
                if (order.getCustomerId()==(customer.getId()) && order.getDeliveredAt().isEmpty()) {
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


