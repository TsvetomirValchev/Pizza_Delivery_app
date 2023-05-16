

import View.CustomerView;
import db.CustomerController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



public class CustomerViewTest {
    CustomerController customerController = new CustomerController("woshmc@abv.bg");
    CustomerView customerView = new CustomerView(customerController);






    @Test
    @DisplayName("Display all products in order")
    public void testDisplayAllProductsInOrder(){
        customerView.printCurrentOrderDetails();
    }



    @Test
    @DisplayName("Display all pizzas")
    public void testDisplayAllPizzas(){
       customerView.printAllPizzas();
    }

    @Test
    @DisplayName("Display all desserts")
    public void testDisplayAllDesserts(){
        customerView.printAllDesserts();
    }

    @Test
    @DisplayName("Display all drinks")
    public void testDisplayAllDrinks(){
        customerView.printAllDrinks();
    }



}
