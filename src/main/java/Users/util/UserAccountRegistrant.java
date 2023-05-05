package Users.util;

import Users.Customer;
import View.LoginView;
import View.abstraction.View;
import db.abstraction.Controller;

public class UserAccountRegistrant extends Controller {

    private final LoginView loginView = new LoginView();
    private Customer customer;


    public UserAccountRegistrant(Customer customer){
        this.customer = customer;
    }


    @Override
    protected View getView() {
        return loginView;
    }

    public void RegisterCustomer(){

    }

}
