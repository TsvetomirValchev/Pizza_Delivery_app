package View;

import View.abstraction.View;
import db.CustomerService;
import products.Pizza;
import products.Product;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerView implements View {

    private static final Logger LOGGER = LogManager.getLogger(CustomerView.class.getName());
    private final CustomerService customerService;

    public CustomerView(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public void printMenu() {
        System.out.println("\nWelcome to the imaginary pizza restaurant!\n");
        System.out.println("1. View all pizzas");
        System.out.println("2. View all drinks");
        System.out.println("3. View all desserts");
        System.out.println("4. View your cart details");
        System.out.println("5. Add a product to your cart");
        System.out.println("6. Finalize order");
        System.out.println("7. Check order details");
        System.out.println("8. Mark your order as received");
        System.out.println("0. Exit");

    }

    @Override
    public void getChoice() {
        Scanner scan = new Scanner(System.in);
        int choice;
        do {
            printMenu();
            choice = scan.nextInt();
            printSeparator(80);
            switch (choice) {
                case 1 -> printAllPizzas();
                case 2 -> printAllDrinks();
                case 3 -> printAllDesserts();
                case 4 -> printCartDetails();
                case 5 -> AddAProductToShoppingCartMenu();
                case 6 -> markOrderAsFinalizedMenu();
                case 7 -> printCurrentOrderDetails();
                case 8 -> markOrderAsReceivedMenu();
                case 0 -> System.out.println("Exiting..");
                default -> System.err.println("Enter a valid option!");
            }
            if (choice != 0) {
                printSeparator(80);
            }
        } while (choice != 0);

    }

    private void markOrderAsReceivedMenu() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Is the order received?");
            System.out.println("1.Yes\n2.No");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    customerService.markOrderAsReceived();
                    System.out.println("Order has been delivered, thank you for choosing our restaurant!");
                    getChoice();
                }
                case 2 -> getChoice();
            }

        } catch (InputMismatchException e) {
            LOGGER.warn(e.getMessage());
            System.err.println("Please select one of the options");
            markOrderAsReceivedMenu();

        }

    }

    private void AddAProductToShoppingCartMenu() {
        try {
            Scanner scanner = new Scanner(System.in);

            printAllProductsInTheRestaurant();
            printSeparator(100);

            System.out.println("Enter the ID of the product you wish to add to your cart:  ");
            int productId = scanner.nextInt();

            if (customerService.placeAnOrder(productId)) {
                System.out.println("Product has been added to your order");
            }
            markOrderAsFinalizedMenu();
        } catch (InputMismatchException e) {
            LOGGER.warn(e.getMessage());
            System.err.println("Invalid input format!");
            AddAProductToShoppingCartMenu();
        }
    }

    private void markOrderAsFinalizedMenu() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Is the order finalized?");
            System.out.println("1.Yes\n2.No");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    customerService.markOrderAsFinalized();
                    System.out.println("Order has been finalized and is waiting for delivery...");
                    getChoice();
                }
                case 2 -> getChoice();
            }

        } catch (InputMismatchException e) {
            LOGGER.warn(e.getMessage());
            System.err.println("Please select one of the options");
            markOrderAsReceivedMenu();

        }

    }

    private void printCurrentOrderDetails() {
        System.out.println("All products in your order:");
        customerService.getAllProductsInCurrentlyDeliveringOrder().stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .forEach(System.out::println);
        System.out.println("Order total: " + customerService.calculateCurrentOrderTotal());
    }

    private void printCartDetails() {
        System.out.println("All products in your order:");
        customerService.getAllProductsInCart()
                .stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .forEach(System.out::println);
        System.out.println("Order total: " + customerService.calculateCartTotal());
    }


    private void printAllProductsInTheRestaurant() {
        printSeparator(100);
        printAllPizzas();
        printSeparator(100);
        printAllDrinks();
        printSeparator(100);
        printAllDesserts();
    }

    private void printAllPizzas() {
        System.out.println("All pizzas in our restaurant:");
        customerService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Pizza::getId))
                .forEach(System.out::println);
    }

    private void printAllDesserts() {
        System.out.println("All desserts in our restaurant:");
        customerService.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
        ;
    }

    private void printAllDrinks() {
        System.out.println("All drinks in our restaurant:");
        customerService.getAllDrinks()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }


}
