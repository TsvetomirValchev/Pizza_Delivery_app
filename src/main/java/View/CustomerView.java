package View;



import View.abstraction.View;
import db.CustomerController;
import logging.PizzaDeliveryLogger;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class CustomerView implements View {

    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(CustomerView.class.getName());
    private final CustomerController customerController;

    public CustomerView(CustomerController customerController){
        this.customerController = customerController;
    }


    @Override
    public void printMenu() {
        System.out.println("\nWelcome to the imaginary pizza restaurant!\n");
        System.out.println("1. View all pizzas");
        System.out.println("2. View all drinks");
        System.out.println("3. View all desserts");
        System.out.println("4. Place an order");
        System.out.println("5. Check current order details");
        System.out.println("6. Mark your order as received");
        System.out.println("0. Exit");

    }

    @Override
    public void getChoice() {
        Scanner scan = new Scanner(System.in);
        int choice;
        do{
            printMenu();
            choice = scan.nextInt();
            printSeparator(80);
            switch (choice){
                case 1 -> printAllPizzas();
                case 2 -> printAllDrinks();
                case 3 -> printAllDesserts();
                case 4 -> placeAnOrderMenu();
                case 5 -> printCurrentOrderDetails(); //TODO: make it print the total cost of the order somehow
                case 6 -> markOrderAsReceived();
                case 0-> System.out.println("Exiting..");
                default -> System.err.println("Enter a valid option!");
            }
            if(choice!=0){
                printSeparator(80);
            }
        }while (choice!=0);

    }

    private void markOrderAsReceived(){
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Is the order received?");
            System.out.println("1.Yes\n2.No");
            int choice = scanner.nextInt();

            switch (choice){
                case 1 -> {
                    customerController.markOrderAsComplete();
                    System.out.println("Order has been received...");
                    getChoice();
                }
                case 2 -> getChoice();
            }

        }catch (InputMismatchException e) {
            LOGGER.warning(e.getMessage());
            System.err.println("Please select one of the options");
            markOrderAsReceived();

        }

    }
    private void placeAnOrderMenu(){
        try{
            Scanner scanner = new Scanner(System.in);

            printAllProductsInTheRestaurant();
            printSeparator(100);

            System.out.println("Enter the ID of the product you wish to order:  ");
            int productId = scanner.nextInt();

            if (customerController.placeAnOrder(productId)) {
                System.out.println("Product has been added to your order");
            }
        }catch (InputMismatchException e){
            LOGGER.warning(e.getMessage());
            System.err.println("Invalid input format!");
            placeAnOrderMenu();
        }
    }

    private void printCurrentOrderDetails(){
        System.out.println("All products in your order:");
        customerController.GetAllProductsInCurrentOrder().forEach(System.out::println);
        System.out.println("Order total: " + customerController.calculateCurrentOrderTotal());
    }


    private void printAllProductsInTheRestaurant(){
        printSeparator(100);
        printAllPizzas();
        printSeparator(100);
        printAllDrinks();
        printSeparator(100);
        printAllDesserts();
    }

    private void printAllPizzas(){
        System.out.println("All pizzas in our restaurant:");
        customerController.getPizzas().forEach(System.out::println);
    }

    private void printAllDesserts(){
        System.out.println("All desserts in our restaurant:");
        customerController.getDesserts().forEach(System.out::println);
    }

    private void printAllDrinks(){
        System.out.println("All drinks in our restaurant:");
        customerController.getDrinks().forEach(System.out::println);
    }

    @Override
    public void printExceptionMessage(String message) {
        System.err.println(message);
        printSeparator(80);
        getChoice();
    }
}
