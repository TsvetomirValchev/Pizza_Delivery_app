package View;



import View.abstraction.View;
import db.CustomerController;
import logging.PizzaDeliveryLogger;

import java.util.logging.Logger;

public class CustomerView implements View {

    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(CustomerView.class.getName());
    private final CustomerController customerController;

    public CustomerView(CustomerController customerController){
        this.customerController = customerController;
    }


    @Override
    public void printMenu() {
        System.out.println("\nDominos 2 where your choice matters!\n");
        System.out.println("1. View all pizzas");
        System.out.println("2. View all drinks");
        System.out.println("3. View all desserts");
        System.out.println("4. Make an order");
        System.out.println("5. Check current order details");
        System.out.println("0. Exit");

    }

    @Override
    public void getChoice() {

    }




    //TODO: make private once you don't need tests
    public void printCurrentOrderDetails(){
        System.out.println("All products in your order:");
        customerController.getCurrentOrderDetails().forEach(System.out::println);
    }

    //TODO: make private once you don't need tests
    public void printAllPizzas(){
        System.out.println("All pizzas in our restaurant:");
        customerController.getPizzas().forEach(System.out::println);
    }
    //TODO: make private once you don't need tests
    public void printAllDesserts(){
        System.out.println("All desserts in our restaurant:");
        customerController.getDesserts().forEach(System.out::println);
    }
    //TODO: make private once you don't need tests
    public void printAllDrinks(){
        System.out.println("All drinks in our restaurant:");
        customerController.getDrinks().forEach(System.out::println);
    }
}
