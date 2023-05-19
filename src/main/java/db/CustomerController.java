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
            orderDAO.create(new Order(null,
                    getCustomerAccount().getId(),
                    LocalDateTime.now(),
                    Optional.empty()));

            addProductToOrderCart(productId);
            return true;
        }catch (SQLException e){
            transmitException(e,Level.SEVERE, "Couldn't place an order!");
        }
        return false;
    }

    public List<Product> getCurrentOrderDetails(){
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


    public void addProductToOrderCart(int productId){
        try {
            orderDAO.InsertInOrderItemTable(productId, getCustomerAccount().getId());
        }catch (SQLException e){
            transmitException(e, Level.SEVERE, "Couldn't add product to cart!");
        }
    }

    public void markOrderAsComplete(){
        try{
            if (isUserCurrentlyWaitingDelivery()) {
                for (Order o : orderDAO.readAll().values()) {
                    if (getCustomerAccount().getId() == o.getCustomerId() && o.getDeliveredAt().isEmpty()) {
                        orderDAO.update(o.getId(), 3, LocalDateTime.now());
                        break;
                    }
                }
            } else {
                transmitException(new IllegalStateException(),Level.WARNING,"You are not expecting delivery!");
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

    public int getCurrentOrderIdByCustomerId(int customerId) {
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

    public Product getProductByID(int productId) {
        try {
            for (Product product : productDAO.readAll().values()) {
                if (product.getId() == productId) {
                    return product;
                }
            }
        } catch (SQLException e) {
            transmitException(e,Level.SEVERE,"Couldn't find order!");
        }
        return null;
    }


    boolean isUserCurrentlyWaitingDelivery() {
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




}


