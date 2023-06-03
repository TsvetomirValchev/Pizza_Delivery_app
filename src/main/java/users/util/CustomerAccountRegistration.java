package users.util;

import db.AdminService;
import users.Customer;


public class CustomerAccountRegistration {

    private final Customer customer;

    public CustomerAccountRegistration(Customer customer) {
        this.customer = customer;
    }

   public void RegisterCustomer(){
        try {
            UserAccountValidator validator = new UserAccountValidator(customer);
                if(!validator.areCredentialsMatching()) {
                        validator.isValidUser();
                        buildCustomer();
                }
            }
        catch (IllegalArgumentException e)
            {
                System.err.println(e.getMessage());
            }
        }
        private void buildCustomer(){
            new AdminService().addCustomer(customer);
        }
    }


