package View;

import Products.PizzaIngredient.*;
import Products.Product;
import View.abstraction.View;
import db.AdminController;
import logging.PizzaDeliveryLogger;

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

    }

    @Override
    public void getChoice() {

    }

    @Override
    public void printExceptionMsg(String msg) {

    }





    //TODO MAKE PRIVATE LATER!
    public void addADrinkMenu(){
        try{
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the drink Diet friendly?");
            boolean diet = scanner.nextBoolean();
            adminController.createDrinkProduct(product.getId(), product.getName(), product.getPrice(),diet);

        }catch (InputMismatchException | NumberFormatException e){
            LOGGER.warning(e.getMessage());
            if(e instanceof  NumberFormatException){
                System.err.println("Invalid price!");
            }else{
                System.err.println("Invalid input!");
            }

        }

    }

    //TODO MAKE PRIVATE LATER!
    public void addADessertMenu(){
        try{
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            System.out.println("Is the drink dessert vegan?");
            boolean vegan = scanner.nextBoolean();
            adminController.createDrinkProduct(product.getId(), product.getName(), product.getPrice(),vegan);

        }catch (InputMismatchException | NumberFormatException e){
            LOGGER.warning(e.getMessage());
            if(e instanceof  NumberFormatException){
                System.err.println("Invalid price!");
            }else{
                System.err.println("Invalid input!");
            }

        }

    }


    //TODO MAKE PRIVATE LATER!
    public void addAPizzaMenu(){
        try{
            Scanner scanner = new Scanner(System.in);
            Product product = createAProductMenu();
            adminController.getAllIngredients("size");
            printSeparator(100);
            System.out.println("Choose the size of the pizza(by entering it's id):");
            int sizeId = scanner.nextInt();
            scanner.nextLine();
            Size size = new Size(sizeId,"");
            printSeparator(100);
            adminController.getAllIngredients("cheese");
            printSeparator(100);
            System.out.println("Choose the cheese(by entering it's id):");
            int cheeseId = scanner.nextInt();
            scanner.nextLine();
            Cheese cheese = new Cheese(cheeseId,"");
            printSeparator(100);
            adminController.getAllIngredients("meat");
            printSeparator(100);
            System.out.println("Choose the meat(by entering it's id):");
            int meatId = scanner.nextInt();
            scanner.nextLine();
            Meat meat = new Meat(meatId,"");
            printSeparator(100);
            adminController.getAllIngredients("sauce");
            printSeparator(100);
            System.out.println("Choose the sauce(by entering it's id):");
            int sauceId = scanner.nextInt();
            scanner.nextLine();
            Sauce sauce = new Sauce(sauceId,"");
            printSeparator(100);
            adminController.getAllIngredients("addon");
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

}
