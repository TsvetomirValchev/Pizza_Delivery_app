package View;

import Products.PizzaIngredient.*;
import Products.Product;
import Users.Customer;
import View.abstraction.View;
import db.AdminController;
import logging.PizzaDeliveryLogger;

import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class AdminView implements View {

    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(AdminView.class.getName());
    private final AdminController adminController;

    public AdminView(AdminController adminController){
        this.adminController = adminController;
    }


    @Override
    public void printMenu() {
        System.out.println("What would you like to do?");
        System.out.println("1. Add a new product to the menu");
        System.out.println("2. Delete a product from the menu");
        System.out.println("3. Delete a customer's account");
        System.out.println("4. View all products offered by the restaurant");
        System.out.println("5. View all customers");
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
                case 1 -> addAProductMenu();
                case 2 -> deleteAProductMenu();
                case 3 -> deleteAccountMenu();
                case 4 -> readAllProductsInTheRestaurant();
                case 5 -> readAllCustomers();
                case 0-> System.out.println("Exiting..");
                default -> System.err.println("Enter a valid option!");
            }
            if(choice!=0){
                printSeparator(80);
            }
        }while (choice!=0);
    }

    private void deleteAccountMenu(){
           try {
               readAllCustomers();
               printSeparator(100);
               Scanner scanner = new Scanner(System.in);
               System.out.println("Enter the username of the account you want to delete: ");
               String username = scanner.nextLine();
               adminController.deleteCustomer(username);
           }catch (InputMismatchException e) {
               LOGGER.warning(e.getMessage());
               System.err.println(e.getMessage());
               deleteAccountMenu();
           }
        System.out.println("Account deleted!");
    }


    private void addAProductMenu(){
            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                printSeparator(100);
                System.out.println("What product do you want to add?");
                printSeparator(100);
                System.out.println("1. Pizza");
                System.out.println("2. Drink");
                System.out.println("3. Dessert");
                System.out.println("0. Do something else ");
                printSeparator(100);
                choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> addAPizzaMenu();
                    case 2 -> addADrinkMenu();
                    case 3 -> addADessertMenu();
                    case 0 -> getChoice();
                }
            }while (choice != 0);


    }

    private void deleteAProductMenu(){
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            printSeparator(100);
            System.out.println("What product do you want to remove?");
            printSeparator(100);
            System.out.println("1. Pizza");
            System.out.println("2. Drink");
            System.out.println("3. Dessert");
            System.out.println("0. Do something else ");
            printSeparator(100);
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> deleteAPizzaMenu();
                case 2 -> deleteADrinkMenu();
                case 3 -> deleteADessertMenu();
                case 0 -> getChoice();
            }
        }while (choice != 0);


    }

    private void addADrinkMenu(){
        try{
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the drink Diet friendly?");
            boolean diet = scanner.nextBoolean();
            adminController.createDrinkProduct(product.getId(), product.getName(), product.getPrice(), diet);

        }catch (InputMismatchException | NumberFormatException e){
            LOGGER.warning(e.getMessage());
            if(e instanceof  NumberFormatException){
                System.err.println("Invalid price!");
            }else{
                System.err.println("Invalid input!");
            }
            addADrinkMenu();
        }

    }

    private void addADessertMenu(){
        try{
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the dessert vegan?");
            boolean vegan = scanner.nextBoolean();
            adminController.createDessertProduct(product.getId(), product.getName(), product.getPrice(),vegan);

        }catch (InputMismatchException | NumberFormatException e){
            LOGGER.warning(e.getMessage());
            if(e instanceof  NumberFormatException){
                System.err.println("Invalid price!");
            }else{
                System.err.println("Invalid input!");
            }
            addADessertMenu();
        }

    }

    private void addAPizzaMenu(){
        try{
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            readAllIngredients("size");
            printSeparator(100);
            System.out.println("Choose the size of the pizza(by entering it's id):");
            int sizeId = scanner.nextInt();
            scanner.nextLine();
            Size size = new Size(sizeId,"");
            printSeparator(100);
            readAllIngredients("cheese");
            printSeparator(100);
            System.out.println("Choose the cheese(by entering it's id):");
            int cheeseId = scanner.nextInt();
            scanner.nextLine();
            Cheese cheese = new Cheese(cheeseId,"");
            printSeparator(100);
            readAllIngredients("meat");
            printSeparator(100);
            System.out.println("Choose the meat(by entering it's id):");
            int meatId = scanner.nextInt();
            scanner.nextLine();
            Meat meat = new Meat(meatId,"");
            printSeparator(100);
            readAllIngredients("sauce");
            printSeparator(100);
            System.out.println("Choose the sauce(by entering it's id):");
            int sauceId = scanner.nextInt();
            scanner.nextLine();
            Sauce sauce = new Sauce(sauceId,"");
            printSeparator(100);
            readAllIngredients("addon");
            printSeparator(100);
            System.out.println("Choose the addon(by entering it's id): ");
            int addonId = scanner.nextInt();
            scanner.nextLine();
            printSeparator(100);
            Addon addon = new Addon(addonId,"");
            adminController.createPizzaProduct(product.getId(),product.getName(),product.getPrice(),size,cheese,meat,sauce,addon);

        }catch (InputMismatchException | NumberFormatException e){
            LOGGER.warning(e.getMessage());
            if(e instanceof  NumberFormatException){
                System.err.println("Invalid price!");
            }else{
                System.err.println("Invalid input!");
            }
            addAPizzaMenu();
        }

    }

    private Product createAProductMenu(){
        String productName = null;
        Double price = null;
        int id = 0;
        try{

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the identification number of the product");
            id = scanner.nextInt();
            scanner.nextLine();
            printSeparator(100);
            System.out.println("Enter the name of the product");
            productName = scanner.nextLine();
            printSeparator(100);
            System.out.println("Enter the price of the product");
            price = scanner.nextDouble();
            printSeparator(100);

        }catch (InputMismatchException | NumberFormatException e){
            LOGGER.warning(e.getMessage());
            if(e instanceof  NumberFormatException){
                System.err.println("Invalid price!");
            }else{
                System.err.println("Invalid input!");
            }

        }
        return new Product(id,productName,price);
    }

    private void deleteAPizzaMenu(){
        try {
            readAllPizzas();
            printSeparator(100);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the pizza you want to delete: ");
            int productId = scanner.nextInt();
            adminController.deletePizza(productId);
        }catch (InputMismatchException e) {
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            deleteAPizzaMenu();
        }

    }

    private void deleteADrinkMenu(){
        try {
            readAllDrinks();
            printSeparator(100);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the drink you want to delete: ");
            int productId = scanner.nextInt();
            adminController.deleteDrink(productId);

        }catch (InputMismatchException e) {
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            deleteADrinkMenu();
        }

    }


    private void deleteADessertMenu(){
        try {
            readAllDesserts();
            printSeparator(100);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the dessert you want to delete: ");
            int productId = scanner.nextInt();
            adminController.deleteDessert(productId);
        }catch (InputMismatchException e) {
            LOGGER.warning(e.getMessage());
            System.err.println(e.getMessage());
            deleteADessertMenu();
        }

    }

    private void readAllIngredients(String tableName){
        adminController.getAllIngredients(tableName)
                .values()
                .forEach(System.out::println);
    }

    private void readAllCustomers(){
        adminController.getAllCustomers()
                .values()
                .stream()
                .sorted(Comparator.comparing(Customer::getUsername))
                .forEach(System.out::println);
    }

    private void readAllPizzas(){
        adminController.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }


    private void readAllDrinks(){
        adminController.getAllDrinks()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }

    private void readAllDesserts(){
        adminController.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }

    private void readAllProductsInTheRestaurant(){
        System.out.println("All pizzas in the restaurant:\n");
        readAllPizzas();
        printSeparator(100);
        System.out.println("All drinks in the restaurant:\n");
        readAllDrinks();
        printSeparator(100);
        System.out.println("All desserts in the restaurant:\n");
        readAllDesserts();


    }

    @Override
    public void printExceptionMessage(String message) {
        System.err.println(message);
        printSeparator(80);
        getChoice();
    }
}
