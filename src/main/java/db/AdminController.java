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
import View.abstraction.View;
import View.AdminView;
import db.abstraction.Controller;


import java.sql.SQLDataException;
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

    private static final OrderDAO orderDAO = new OrderDAO();


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

    public void createDessertProduct(int productId,String name, double price,Boolean isVegan){
        addProduct(productId,name,price);
        addDessert(productId,name,price,isVegan);
    }

    public void createPizzaProduct(int productId,String name, double price, Size size, Cheese cheese, Meat meat, Sauce sauce, Addon addon){
        addProduct(productId,name,price);
        addPizza(productId, name, price, size, cheese, meat, sauce, addon);
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
            customerDAO.create(customer);
        }catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't register customer!");
        }

    }
    public void deleteCustomer(String customerUsername){
        try {
            Customer customer = getCustomerByUsername(customerUsername);
            if (customer != null ) {
                customerDAO.delete(customerUsername);
            } else {
                transmitException(new IllegalArgumentException(),Level.WARNING,"User with username: '"+customerUsername+"' does not exist!");
            }
        } catch (SQLException e) {
            if (e instanceof SQLDataException) {
                transmitException(e, Level.SEVERE, "Couldn't delete customer account!");
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

//    public Customer getCustomerById(int customerId){
//        Map<Integer, Customer> customers = getAllCustomers();
//        return customers.values().stream()
//                .filter(c -> c.getId().equals(customerId))
//                .findFirst()
//                .orElse(null);
//    }

    public Map<Integer, PizzaIngredient> getAllIngredients(String tableName){
        try{
            return pizzaDAO.readAllIngredients(tableName);
        } catch (SQLException e) {
            transmitException(e, Level.SEVERE, "Couldn't read ingredients!");
        }
        return  Collections.emptyMap();
    }


    //utils

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

    public void deletePizza(int productId){
        try {
            Pizza pizza = getAllPizzas()
                    .values()
                    .stream()
                    .filter((p)->p.getId()==productId).findFirst().orElse(null);

            if(pizza==null){
                transmitException(new IllegalArgumentException(),Level.WARNING,"No pizza found with  product ID of: '"+productId+"'");
                System.err.println("Product with that id does not exist!");
            }
            else if (isProductCurrentlyOrdered(pizza.getId())){
                transmitException(new IllegalArgumentException(),Level.WARNING,"This product is currently in a delivery: '"+productId+"'");
                System.err.println("Cannot delete product that is currently in an active order!");
            }
            else
            {
                pizzaDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Pizza removed successfully! ");
            }

        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                transmitException(e,Level.WARNING,e.getMessage());
            } else {
                transmitException(e,Level.SEVERE,"Couldn't remove pizza!");
            }
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

    public void deleteDrink(int productId){
        try {
            Drink drink = getAllDrinks()
                    .values()
                    .stream()
                    .filter((d)->d.getId()==productId).findFirst().orElse(null);
            if(drink==null)
            {
                transmitException(new IllegalArgumentException(),Level.WARNING,"No drink found with product ID of: '"+productId+"'");
                System.err.println("Product with that id does not exist!");
            }
            else if (isProductCurrentlyOrdered(drink.getId()))
            {
                transmitException(new IllegalArgumentException(),Level.WARNING,"This product is currently in a delivery: '"+productId+"'");
                System.err.println("Cannot delete product that is currently in an active order!");
            }
            else
            {
            drinkDAO.delete(productId);
            productDAO.delete(productId);
            System.out.println("Drink removed successfully!");
            }

        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                transmitException(e,Level.WARNING,e.getMessage());
            } else {
                transmitException(e,Level.SEVERE,"Couldn't remove drink!");
            }
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

    public void deleteDessert(int productId){
        try {
            Dessert dessert = getAllDesserts()
                    .values()
                    .stream()
                    .filter((d)->d.getId()==productId).findFirst().orElse(null);
            if(dessert==null){
                transmitException(new IllegalArgumentException(),Level.WARNING,"No dessert found with product ID of: '"+productId+"'");
                System.err.println("Product with that id does not exist!");
            }else if (isProductCurrentlyOrdered(dessert.getId())){
                transmitException(new IllegalArgumentException(),Level.WARNING,"This product is currently in a delivery: '"+productId+"'");
                System.err.println("Cannot delete product that is currently in an active order!");
            }else
            {
                dessertDAO.delete(productId);
                productDAO.delete(productId);
                System.out.println("Dessert removed successfully");
            }

        } catch (SQLException e) {
            if (e instanceof SQLDataException){
                transmitException(e,Level.WARNING,e.getMessage());
            } else {
                transmitException(e,Level.SEVERE,"Couldn't remove dessert!");
            }
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




    @Override
    protected View getView() {
        return adminView;
    }
}
