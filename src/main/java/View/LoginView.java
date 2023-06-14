package View;

import Registration.RegistrationService;
import db.AdminService;
import db.CustomerService;
import users.AccountType;
import users.Customer;


import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import users.User;


public class LoginView extends View {

    private static final Logger LOGGER = LogManager.getLogger(LoginView.class.getName());


    @Override
    public void printMenu() {
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Welcome!");
        System.out.println("1.Login");
        System.out.println("2.Register");
        System.out.println("0.Exit");
        System.out.println("-------------------------------------------------------------------------------------");
    }

    @Override
    public void getChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        try {
            do {
                printMenu();
                choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> printLoginMenu();
                    case 2 -> accountCreationChoice();
                    case 0 -> System.out.println("Exiting!");
                    default -> LOGGER.error("Invalid account details!");
                }
            } while (choice != 0);
        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Invalid input");
            getChoice();
        }

    }

    private void printLoginMenu() {
        Scanner scanner = new Scanner(System.in);

        String username = null;
        String password = null;

        try {
            System.out.println("Enter username: ");
            username = scanner.nextLine();
            System.out.println("Enter password: ");
            password = scanner.nextLine();

        } catch (IllegalArgumentException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Please enter valid account credentials");
            printLoginMenu();
        }

        User user = new AdminService().getUserByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.getAccountType().equals(AccountType.ADMIN)) {
            openAdminView();
        } else if (user != null && user.getPassword().equals(password) && user.getAccountType().equals(AccountType.CUSTOMER)) {
            Customer customer = new Customer(user);
            openCustomerView(customer);
        } else if (user != null) {
            LOGGER.error("Wrong password!");
            printLoginMenu();
        } else {
            LOGGER.error("No account found with this username");
            accountCreationChoice();
        }

    }


    private void accountCreationChoice() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Create an account?");
        System.out.println("1.Yes\n2.No");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> {
                printRegistrationMenu();
                System.out.println("Account created!");
                getChoice();
            }
            case 2 -> {
                System.out.println("Account creation canceled!");
                getChoice();
            }
            default -> {
                System.out.println("Invalid Choice!");
                accountCreationChoice();
            }

        }

    }

    private void printRegistrationMenu() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Please enter your e-mail address: ");
            String email = scanner.nextLine();

            System.out.println("Please enter your username(at least 3 characters long): ");
            String username = scanner.nextLine();


            System.out.println("Please enter your password(1 uppercase letter,1 lowercase letter,1 number): ");
            String password = scanner.nextLine();

            Customer customer = new Customer(null, username, password, email);
            RegistrationService registrationService = new RegistrationService(customer);
            registrationService.signUp();

        } catch (InputMismatchException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Invalid data entered");
            printRegistrationMenu();
        }

    }

    private void openAdminView() {
        System.out.println("Welcome, master!");
        AdminService adminService = new AdminService();
        View adminView = new AdminView(adminService);
        adminView.getChoice();
    }

    private void openCustomerView(Customer customer) {
        System.out.println("Welcome, " + customer.getUsername());
        CustomerService customerService = new CustomerService(customer);
        View customerView = new CustomerView(customerService);
        customerView.getChoice();
    }


}
