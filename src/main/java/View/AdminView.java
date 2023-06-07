package View;

import products.ingredient.*;
import products.Product;
import users.Customer;
import View.abstraction.View;
import db.AdminService;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdminView implements View {

    private static final Logger LOGGER = LogManager.getLogger(AdminView.class.getName());
    private final AdminService adminService;

    public AdminView(AdminService adminService) {
        this.adminService = adminService;
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
        do {
            printMenu();
            choice = scan.nextInt();
            printSeparator(80);
            switch (choice) {
                case 1 -> addAProductMenu();
                case 2 -> deleteAProductMenu();
                case 3 -> deleteAccountMenu();
                case 4 -> readAllProductsInTheRestaurant();
                case 5 -> readAllCustomers();
                case 0 -> System.out.println("Exiting...");
                default -> LOGGER.error("Enter a valid option!");
            }
            if (choice != 0) {
                printSeparator(80);
            }
        } while (choice != 0);
    }

    private void deleteAccountMenu() {
        try {
            readAllCustomers();
            printSeparator(100);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the username of the account you want to delete: ");
            String username = scanner.nextLine();
            adminService.deleteCustomer(username);
        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            deleteAccountMenu();
        }
        System.out.println("Account deleted!");
    }

    private void addAProductMenu() {
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
        } while (choice != 0);


    }

    private void deleteAProductMenu() {
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
        } while (choice != 0);


    }

    private void addADrinkMenu() {
        try {
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the drink Diet friendly?");
            boolean diet = scanner.nextBoolean();
            adminService.createDrinkProduct(product.getId(), product.getName(), product.getPrice(), diet);

        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                LOGGER.error("Invalid price!");
            } else {
                LOGGER.error("Invalid input!");
            }
            addADrinkMenu();
        }

    }

    private void addADessertMenu() {
        try {
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the dessert vegan?");
            boolean vegan = scanner.nextBoolean();
            adminService.createDessertProduct(product.getId(), product.getName(), product.getPrice(), vegan);

        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                LOGGER.error("Invalid price!");
            } else {
                LOGGER.error("Invalid input!");
            }
            addADessertMenu();
        }

    }

    private void addAPizzaMenu() {
        try {
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            readAllIngredients("size");
            printSeparator(100);
            System.out.println("Choose the size of the pizza(by entering it's id):");
            int sizeId = scanner.nextInt();
            scanner.nextLine();
            Size size = new Size(sizeId, "");
            readAllIngredients("sauce");
            printSeparator(100);
            System.out.println("Choose the sauce(by entering it's id):");
            int sauceId = scanner.nextInt();
            scanner.nextLine();
            Sauce sauce = new Sauce(sauceId, "");
            printSeparator(100);
            List<Cheese> cheeses = selectCheese();
            printSeparator(100);
            List<Meat> meats = selectMeat();
            printSeparator(100);
            List<Addon> addons = selectAddon();
            adminService.createPizzaProduct(product.getId(), product.getName(), product.getPrice(), size, sauce, meats, cheeses, addons);

        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                LOGGER.error("Invalid price!");
            } else {
                LOGGER.error("Invalid input!");

            }
            addAPizzaMenu();
        }
    }

    private List<Meat> selectMeat() {
        Scanner scanner = new Scanner(System.in);
        printSeparator(100);
        List<Meat> meats = new ArrayList<>();
        int input;
        do {
            System.out.println("Choose the meat(by entering it's id):");
            readAllIngredients("meat");
            int meatId = scanner.nextInt();
            scanner.nextLine();
            Meat meat = new Meat(meatId, "");
            meats.add(meat);
            System.out.println("Do you want to add more meat? (1.yes/0.no)");
            input = scanner.nextInt();
        } while (input == 1);
        return meats;
    }

    private List<Cheese> selectCheese() {
        Scanner scanner = new Scanner(System.in);
        printSeparator(100);
        List<Cheese> cheeses = new ArrayList<>();
        int input;
        do {
            System.out.println("Choose the cheese(by entering it's id):");
            readAllIngredients("cheese");
            int cheeseId = scanner.nextInt();
            scanner.nextLine();
            Cheese cheese = new Cheese(cheeseId, "");
            cheeses.add(cheese);
            System.out.println("Do you want to add more cheese? (1.yes/0.no)");
            input = scanner.nextInt();
        } while (input == 1);
        return cheeses;
    }

    private List<Addon> selectAddon() {
        Scanner scanner = new Scanner(System.in);
        printSeparator(100);

        List<Addon> addons = new ArrayList<>();
        int input;
        do {
            System.out.println("Choose the addon(by entering it's id):");
            readAllIngredients("addon");
            int addonId = scanner.nextInt();
            scanner.nextLine();
            Addon addon = new Addon(addonId, "");
            addons.add(addon);
            System.out.println("Do you want to add more addons? (1.y/0.n)");
            input = scanner.nextInt();
        } while (input == 1);
        return addons;
    }

    private Product createAProductMenu() {
        String productName = null;
        Double price = null;
        int id = 0;
        try {
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

        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                LOGGER.error("Invalid price!");

            } else {
                LOGGER.error("Invalid input!");

            }
            createAProductMenu();
        }
        return new Product(id, productName, price);
    }

    private void deleteAPizzaMenu() {
        try {
            readAllPizzas();
            printSeparator(100);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the pizza you want to delete: ");
            int productId = scanner.nextInt();
            adminService.deletePizza(productId);
        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with deleting the pizza.");
            deleteAPizzaMenu();
        }

    }

    private void deleteADrinkMenu() {
        try {
            readAllDrinks();
            printSeparator(100);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the drink you want to delete: ");
            int productId = scanner.nextInt();
            adminService.deleteDrink(productId);

        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error(e.getMessage());
            deleteADrinkMenu();
        }

    }


    private void deleteADessertMenu() {
        try {
            readAllDesserts();
            printSeparator(100);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the dessert you want to delete: ");
            int productId = scanner.nextInt();
            adminService.deleteDessert(productId);
        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error(e.getMessage());
            deleteADessertMenu();
        }

    }

    private void readAllIngredients(String tableName) {
        adminService.getAllIngredients(tableName)
                .values()
                .forEach(System.out::println);
    }

    private void readAllCustomers() {
        adminService.getAllCustomers()
                .values()
                .stream()
                .sorted(Comparator.comparing(Customer::getUsername))
                .forEach(System.out::println);
    }

    private void readAllPizzas() {
        adminService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }


    private void readAllDrinks() {
        adminService.getAllDrinks()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }

    private void readAllDesserts() {
        adminService.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }

    private void readAllProductsInTheRestaurant() {
        System.out.println("All pizzas in the restaurant:\n");
        readAllPizzas();
        printSeparator(100);
        System.out.println("All drinks in the restaurant:\n");
        readAllDrinks();
        printSeparator(100);
        System.out.println("All desserts in the restaurant:\n");
        readAllDesserts();

    }


}