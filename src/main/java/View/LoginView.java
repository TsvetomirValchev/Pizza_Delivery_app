package View;

import Users.Admin;
import Users.Customer;
import Users.util.UserAccountRegistrant;
import View.abstraction.View;
import db.AdminController;
import db.CustomerController;
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
        try {
            do{
            printMenu();
            choice = scanner.nextInt();
            switch (choice) {
                case 1 -> {
                    printSeparator(100);
                    printLoginMenu();
                     }
                case 2 -> {
                    printSeparator(100);
                    accountCreationChoice();


                    }
                case 0 -> System.out.println("Exiting!");
                default -> System.err.println("Invalid account details!");
                }
            }while (choice!=0);
        }catch (InputMismatchException e){
            LOGGER.warning(e.getMessage());
            System.err.println("Invalid input");
            getChoice();
        }



    }

    private void printLoginMenu(){
        Scanner scanner = new Scanner(System.in);

        String username = null;
        String password = null;
        try{
            System.out.println("Enter username:");
            username = scanner.nextLine();
            System.out.println("Enter password");
            password = scanner.nextLine();

        }catch (IllegalArgumentException e){
            LOGGER.warning(e.getMessage());
            System.err.println("Please enter valid account credentials");
            printLoginMenu();
        }

        if(username.equals(admin.getUsername())&&password.equals(admin.getPassword())){
            openAdminView();
        }
        else {
            Customer customer = new AdminController(admin).getCustomerByUsername(username);
            if(customer!=null && customer.getPassword().equals(password)){
                openCustomerView(customer);
            }else if(customer!=null){
                printSeparator(100);
                System.out.println("Wrong password!");
                printLoginMenu();
            }
            else {
                System.out.println("No account found with this username");
                accountCreationChoice();
            }

        }
    }
    private void accountCreationChoice(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Create an account?");
        System.out.println("1.Yes\n2.No");

        int choice = scanner.nextInt();

        switch (choice){
            case 1 -> {
                printRegistrationMenu();
                System.out.println("Account created!");
                getChoice();
            }
            case 2 ->{
                System.out.println("Account creation canceled!");
                getChoice();
            }
            default -> {
                System.out.println("Invalid Choice!");
                accountCreationChoice();
            }

        }

    }
    private void printRegistrationMenu(){
        Scanner scanner = new Scanner(System.in);
        Console console = System.console(); // to read a password without displaying the input

        try {
            System.out.println("Please enter your e-mail address: ");
            String email = scanner.nextLine();

            System.out.println("Please enter your username: ");
            String username = scanner.nextLine();

            String password = null;
            System.out.println("Please enter your password(1 uppercase letter,1 lowercase letter,1 number): ");
            if(console != null) {
                char[] passwordChars = console.readPassword();
                password = String.valueOf(passwordChars);
            }else{
                password = scanner.nextLine();
            }

            System.out.println("Please enter delivery address: ");
            String address = scanner.nextLine();

            Customer customer = new Customer(username,password,null,email,address);
            UserAccountRegistrant registrant = new UserAccountRegistrant(customer);
            registrant.RegisterCustomer();

        }catch (InputMismatchException e){
            LOGGER.warning(e.getMessage());
            System.err.println("Invalid data entered");
            printRegistrationMenu();
        }

    }
    private void openAdminView(){
        System.out.println("Welcome, master!");
        AdminController adminController = new AdminController(admin);
        View adminView = new AdminView(adminController);
        adminView.getChoice();

    }
    private void openCustomerView(Customer customer){
        System.out.println("Welcome " +customer.getUsername());
        CustomerController customerController = new CustomerController(customer.getEmail());
        View customerView = new CustomerView(customerController);
        customerView.getChoice();

    }

    @Override
    public void printExceptionMsg(String msg) {
        System.err.println(msg);
        accountCreationChoice();
    }

}
