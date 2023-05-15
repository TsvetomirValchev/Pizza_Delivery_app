import Users.Admin;
import View.AdminView;
import View.LoginView;
import db.AdminController;

public class Main {
    public static void main(String[] args) {
        //new LoginView().getChoice();


        AdminController adminController = new AdminController(new Admin());
        AdminView adminView = new AdminView(adminController);
        //adminView.addAPizzaMenu();
        adminView.addADrinkMenu();
        //adminView.addADessertMenu();

    }
}
