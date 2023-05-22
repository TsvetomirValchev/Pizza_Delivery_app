package Users.util;

import Users.Admin;
import Users.Customer;
import db.AdminController;

import java.util.regex.Pattern;

public class UserAccountValidator {

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
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
            throw new IllegalArgumentException("\"Invalid password!\\nPassword must include:\\nat least 1 uppercase letter,\\nat least 1 lowercase letter,\\nat least 1 number.\"");
        }

    }

    private boolean isValidEmail() {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(customer.getEmail()).matches();
    }

    private boolean isValidUsername() {
        if (customer.getUsername().equals(new Admin().getUsername())){
            return false;
        }
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        return pattern.matcher(customer.getUsername()).matches();
    }

    private boolean isValidPassword() {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(customer.getPassword()).matches();
    }

    public boolean isEmailTaken(){
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

    boolean areCredentialsMatching(){
        for(Customer c: adminController.getAllCustomers().values()){
            if (c.getUsername().equals(customer.getUsername()) && c.getPassword().equals(customer.getPassword()) && c.getEmail().equals(customer.getEmail())){
                return true;
            }
        }
        return false;
    }
}
