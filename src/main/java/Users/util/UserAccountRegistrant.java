package Users.util;

import Users.Admin;
import Users.Customer;
import View.LoginView;
import View.abstraction.View;
import db.AdminController;
import db.abstraction.Controller;

import java.util.logging.Level;

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
        try {
            UserAccountValidator validator = new UserAccountValidator(customer);

            if(!validator.areCredentialsMatching()) {
                validator.isValidUser();
                buildCustomer();
            }
        } catch (IllegalArgumentException e) {
            transmitException(e, Level.WARNING, e.getMessage());
        }
    }

    private void buildCustomer(){
        new AdminController(new Admin()).addCustomer(customer);
    }

}
