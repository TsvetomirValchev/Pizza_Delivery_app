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
        System.out.println("4. Make and order");
        System.out.println("5. Check order status");
        System.out.println("0. Exit");

    }

    @Override
    public void getChoice() {

    }

    @Override
    public void printExceptionMsg(String msg) {

    }
}
