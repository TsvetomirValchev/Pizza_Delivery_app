package Users.util;

import Users.Admin;
import Users.Customer;
import db.AdminController;

import java.util.regex.Pattern;

public class UserAccountValidator {

    private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]{3,30}$";
    private static final String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
    private static final AdminController adminController = new AdminController(new Admin());

    private final Customer customer;

    public UserAccountValidator(Customer customer) {
        this.customer = customer;
    }

    void isValidUser(){
        if(!isValidEmail()){
            throw new IllegalArgumentException("Invalid email");
        }
        if(isEmailTaken()){
            throw new IllegalArgumentException("There is already an account with that email!");
        }
        if(!isValidUsername()){
            throw new IllegalArgumentException("Invalid username!");
        }
        if(isUsernameTaken()){
            throw new IllegalArgumentException("This username is taken!");
        }
        if (!isValidPassword()){
            throw new IllegalArgumentException("Invalid password!/nPassword must include: at least 1 uppercase letter,1 lowercase letter and 1 number!");
        }

    }

    public boolean isValidEmail() {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(customer.getEmail()).matches();
    }

    public boolean isValidUsername() {
        if (customer.getUsername().equals(new Admin().getUsername())){
            return false;
        }
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        return pattern.matcher(customer.getUsername()).matches();
    }

    public  boolean isValidPassword() {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(customer.getPassword()).matches();
    }

    private boolean isEmailTaken(){
        for(Customer c: adminController.getAllCustomers().values()){
            if (c.getEmail().equals(customer.getEmail())){
                return true;
            }
        }
        return false;
    }

    private boolean isUsernameTaken(){
        for(Customer c: adminController.getAllCustomers().values()){
            if (c.getUsername().equals(customer.getUsername())){
                return true;
            }
        }
        return false;
    }

    private boolean areCredentialsCorrect(){
        for(Customer c: adminController.getAllCustomers().values()){
            if (c.getUsername().equals(customer.getUsername()) && c.getPassword().equals(customer.getPassword())){
                return true;
            }
        }
        return false;

    }
}
