package View;

import db.CustomerService;
import products.Product;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import products.Size;

public class CustomerView extends View {

    private static final Logger LOGGER = LogManager.getLogger(CustomerView.class.getName());
    private final CustomerService customerService;

    public CustomerView(CustomerService customerService) {
        this.customerService = customerService;
    }


    @Override
    public void printMenu() {
        System.out.println("-------------------------------------------------------------------------------------");
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
        System.out.println("-------------------------------------------------------------------------------------");
    }

    @Override
    public void getChoice() {
        Scanner scan = new Scanner(System.in);
        int choice;
        do {
            printMenu();
            choice = scan.nextInt();
            switch (choice) {
                case 1 -> printAllPizzas();
                case 2 -> printAllDrinks();
                case 3 -> printAllDesserts();
                case 4 -> printCartDetails();
                case 5 -> addAProductToShoppingCartMenu();
                case 6 -> markOrderAsFinalizedMenu();
                case 7 -> printCurrentOrderDetails();
                case 8 -> markOrderAsReceivedMenu();
                case 0 -> System.out.println("Exiting..");
                default -> LOGGER.error("Enter a valid option!");
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
            LOGGER.error("Please select one of the options");
            markOrderAsReceivedMenu();

        }

    }

    private void addAProductToShoppingCartMenu() {
        try {
            Scanner scanner = new Scanner(System.in);
            printAllProductsInTheRestaurant();
            System.out.println("Enter the ID of the product you wish to add to your cart:  ");
            int productId = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter the size of you want: ");
            String sizeName = scanner.nextLine();
            customerService.placeAnOrder(productId, sizeName);
            markOrderAsFinalizedMenu();
        } catch (InputMismatchException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.error("Invalid input format!");
            addAProductToShoppingCartMenu();
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
            LOGGER.error("Please select one of the options");
            markOrderAsReceivedMenu();

        }

    }


    private void printProductDetails(boolean isOrder) {
        String orderType = isOrder ? "order" : "cart";
        List<Product> products = isOrder ? customerService.getAllProductsInCurrentlyDeliveringOrder() : customerService.getAllProductsInCart();

        System.out.println("All products in your " + orderType + ":");

        Map<Integer, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.putIfAbsent(product.getId(), product);
        }

        for (Product product : productMap.values()) {
            System.out.println("ID: " + product.getId() + " Product Name: " + product.getName());
            System.out.println("Ordered size and price:");
            Map<Size, Double> sizesAndPrices = isOrder ? customerService.getSizeAndPriceOrderedForOrderAwaitingDelivery()
                    : customerService.getSizeAndPriceOrderedForCart();
            for (Map.Entry<Size, Double> entry : sizesAndPrices.entrySet()) {
                Size size = entry.getKey();
                double price = entry.getValue();
                System.out.println(size.getName() + ", cost: " + price + " BGN");
            }
            System.out.println("----------------------");
        }

        String orderTotal = isOrder ? customerService.calculateCurrentOrderTotal() : customerService.calculateCartTotal();
        System.out.println("Order total: " + orderTotal);
    }


    private void printCurrentOrderDetails() {
        printProductDetails(true);
    }


    private void printCartDetails() {
        printProductDetails(false);
    }


    private void printAllProductsInTheRestaurant() {
        printAllPizzas();
        System.out.println();
        printAllDrinks();
        System.out.println();
        printAllDesserts();
    }

    private void printAllPizzas() {
        System.out.println("All pizzas in our restaurant:");
        customerService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }

    private void printAllDesserts() {
        System.out.println("All desserts in our restaurant:");
        customerService.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);

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
