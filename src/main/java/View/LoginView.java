package View;

import Users.Admin;
import Users.Customer;
import Users.util.UserAccountRegistrant;
import View.abstraction.View;
import logging.PizzaDeliveryLogger;

import java.io.Console;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class LoginView implements View {
    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(LoginView.class.getName());
    private static final Admin admin = new Admin();
    @Override
    public void printMenu() {
        System.out.println("Welcome to our Pizza Restaurant!");
        System.out.println("1.Login");
        System.out.println("2.Register");
    }

    @Override
    public void getChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        try{
            printMenu();
            choice = scanner.nextInt();
            switch (choice){
                case 1 ->{
                    printSeparator(100);

                }
                case 2->{
                    printSeparator(100);

                    System.out.println("Account Created!");
                }
            }
        }catch (InputMismatchException e){
            LOGGER.warning(e.getMessage());
            System.err.println("Invalid input");
            getChoice();
        }
    }

    @Override
    public void printExceptionMsg(String msg) {
        System.err.println(msg);

    }

    public void RegisterMenu(){
        Scanner scanner = new Scanner(System.in);
        Console console = System.console(); // to read a password without displaying the input

        try {
            System.out.println("Please enter your e-mail address: ");
            String email = scanner.nextLine();

            System.out.println("Please enter your username: ");
            String username = scanner.nextLine();

            System.out.println("Please enter your password: ");
            char[] passwordChars = console.readPassword();
            String password = String.valueOf(passwordChars);

            System.out.println("Please enter delivery address: ");
            String address = scanner.nextLine();

            Customer customer = new Customer(username,password,null,email,address);
            UserAccountRegistrant registrant = new UserAccountRegistrant(customer);


        }catch (InputMismatchException e){

        }

    }
}
