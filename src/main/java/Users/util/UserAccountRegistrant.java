package Users.util;

import Users.Admin;
import Users.Customer;
import View.LoginView;
import View.abstraction.View;
import db.AdminController;
import db.abstraction.Controller;

public class UserAccountRegistrant extends Controller {
    private final LoginView loginView = new LoginView();
    private final Customer customer;

    public UserAccountRegistrant(Customer customer){
        this.customer = customer;
    }

    @Override
    protected View getView() {
        return loginView;
    }

    public void RegisterCustomer(){
        UserAccountValidator validator = new UserAccountValidator(customer);
        System.out.println("Email address: " + customer.getEmail());
        validator.isValidUser();
        buildCustomer();
    }

    private void buildCustomer(){
        new AdminController(new Admin()).addCustomer(customer);
    }

}
