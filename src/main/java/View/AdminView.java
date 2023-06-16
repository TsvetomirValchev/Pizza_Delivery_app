package View;

import products.Ingredient;
import products.IngredientType;
import products.Product;
import db.AdminService;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import products.Size;
import users.User;

public class AdminView extends View {

    private static final Logger LOGGER = LogManager.getLogger(AdminView.class.getName());
    private final AdminService adminService;

    public AdminView(AdminService adminService) {
        this.adminService = adminService;
    }


    @Override
    public void printMenu() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("What would you like to do?");
        System.out.println("1. Add a new product to the menu");
        System.out.println("2. Delete a product from the menu");
        System.out.println("3. Delete a customer's account");
        System.out.println("4. Add a new admin account");
        System.out.println("5. View all products offered by the restaurant");
        System.out.println("6. View all accounts");
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
                case 1 -> addAProductMenu();
                case 2 -> deleteAProductMenu();
                case 3 -> deleteAccountMenu();
                case 4 -> addNewAdminAccountMenu();
                case 5 -> printAllProductsInTheRestaurant();
                case 6 -> printAllUsers();
                case 0 -> System.out.println("Exiting...");
                default -> System.err.println("Enter a valid option!");
            }
        } while (choice != 0);
    }


    private void addNewAdminAccountMenu() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please enter your e-mail address: ");
            String email = scanner.nextLine();

            System.out.println("Please enter your username(at least 3 characters long): ");
            String username = scanner.nextLine();

            System.out.println("Please enter your password(1 uppercase letter,1 lowercase letter,1 number): ");
            String password = scanner.nextLine();

            adminService.createAdminAccount(username, password, email);

        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Invalid data entered");

        }
    }

    private void deleteAccountMenu() {
        try {
            printAllUsers();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the username of the account you want to delete: ");
            String username = scanner.nextLine();
            adminService.deleteUser(username);
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
            System.out.println("What product do you want to add?");
            System.out.println("1. Pizza");
            System.out.println("2. Drink");
            System.out.println("3. Dessert");
            System.out.println("0. Do something else ");
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
            System.out.println("What product do you want to remove?");
            System.out.println("1. Pizza");
            System.out.println("2. Drink");
            System.out.println("3. Dessert");
            System.out.println("0. Do something else ");
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
            System.out.println("All currently available drinks:");
            printAllDrinks();
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the drink Diet friendly?");
            boolean isDiet = scanner.nextBoolean();
            adminService.addDrink(product.getId(), product.getName(), product.getPrice(), product.getAvailableSizes(), isDiet);

        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                System.err.println("Invalid price!");
            } else {
                System.err.println("Invalid input!");
            }
            addADrinkMenu();
        }

    }

    private void addADessertMenu() {
        try {
            System.out.println("All currently available desserts:");
            printAllDesserts();
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the dessert vegan?");
            boolean isVegan = scanner.nextBoolean();
            adminService.addDessert(product.getId(), product.getName(), product.getPrice(), product.getAvailableSizes(), isVegan);

        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                System.err.println("Invalid price!");
            } else {
                System.err.println("Invalid input!");
            }
            addADessertMenu();
        }

    }

    private void addAPizzaMenu() {
        try {
            System.out.println("All currently available pizzas:");
            printAllPizzas();
            Product product = createAProductMenu();
            List<Size> sizes = selectSizes();

            List<Ingredient> ingredients = selectIngredients();
            adminService.addPizza(product.getId(), product.getName(), product.getPrice(), sizes, ingredients);

        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                System.err.println("Invalid price!");
            } else {
                System.err.println("Invalid input!");

            }
            addAPizzaMenu();
        }
    }

    private List<Size> selectSizes() {
        Scanner scanner = new Scanner(System.in);
        List<Size> sizes = new ArrayList<>();
        int input;
        do {
            System.out.println("Choose the size(by entering it's id):");
            int sizeId = scanner.nextInt();
            scanner.nextLine();
            Size size = new Size(sizeId, "");
            sizes.add(size);
            System.out.println("Do you want to add more sizes? (1.y/0.n)");
            input = scanner.nextInt();
        } while (input == 1);
        return sizes;
    }

    private List<Ingredient> selectIngredients() {
        Scanner scanner = new Scanner(System.in);
        List<Ingredient> ingredients = new ArrayList<>();
        int input;
        do {
            System.out.println("Choose the size(by entering it's id):");
            int ingredientId = scanner.nextInt();
            scanner.nextLine();
            Ingredient ingredient = new Ingredient(ingredientId, null, null);
            ingredients.add(ingredient);
            System.out.println("Do you want to add more ingredients? (1.y/0.n)");
            input = scanner.nextInt();
        } while (input == 1);
        return ingredients;
    }

    private Product createAProductMenu() {
        String productName = null;
        Double price = null;
        Size size = new Size(null, "");
        int id = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the identification number of the product:");
            id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter the name of the product:");
            productName = scanner.nextLine();
            System.out.println("Enter the price of the product:");
            price = scanner.nextDouble();
            System.out.println("Enter the size of the product:");


        } catch (InputMismatchException | NumberFormatException e) {
            LOGGER.debug(e.getMessage());
            if (e instanceof NumberFormatException) {
                System.err.println("Invalid price!");
            } else {
                System.err.println("Invalid input!");
            }
            createAProductMenu();
        }
        return null;
    }

    private void deleteAPizzaMenu() {
        try {
            printAllPizzas();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the pizza you want to delete:");
            int productId = scanner.nextInt();
            adminService.deletePizza(productId);
        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            System.err.println("Something went wrong with deleting the pizza.");
            deleteAPizzaMenu();
        }

    }

    private void deleteADrinkMenu() {
        try {
            printAllDrinks();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the drink you want to delete: ");
            int productId = scanner.nextInt();
            adminService.deleteDrink(productId);

        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            System.err.println(e.getMessage());
            deleteADrinkMenu();
        }

    }


    private void deleteADessertMenu() {
        try {
            printAllDesserts();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the product id of the dessert you want to delete: ");
            int productId = scanner.nextInt();
            adminService.deleteDessert(productId);
        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            System.err.println(e.getMessage());
            deleteADessertMenu();
        }

    }


    private void printAllAvailableIngredients() {
        adminService.getAllIngredientsAvailable()
                .values()
                .stream()
                .sorted(Comparator.comparing(ingredient -> ingredient.getIngredientType().getName()))
                .forEach(System.out::println);
    }


    private void printAllUsers() {
        adminService.getAllUsers()
                .values()
                .stream()
                .sorted(Comparator.comparing(User::getUsername))
                .forEach(System.out::println);
    }

    private void printAllPizzas() {
        adminService.getAllPizzas()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }


    private void printAllDrinks() {
        adminService.getAllDrinks()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }

    private void printAllDesserts() {
        adminService.getAllDesserts()
                .values()
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .forEach(System.out::println);
    }

    private void printAllProductsInTheRestaurant() {
        System.out.println("All pizzas in the restaurant:\n");
        printAllPizzas();
        System.out.println();
        System.out.println("All drinks in the restaurant:\n");
        printAllDrinks();
        System.out.println();
        System.out.println("All desserts in the restaurant:\n");
        printAllDesserts();

    }


}