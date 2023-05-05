package View;


import View.abstraction.View;
import db.AdminController;
import logging.PizzaDeliveryLogger;

import java.util.logging.Logger;

public class AdminView implements View {

    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(AdminView.class.getName());
    private final AdminController adminController;

    public AdminView(AdminController adminController){
        this.adminController = adminController;
    }


    @Override
    public void printMenu() {

    }

    @Override
    public void getChoice() {

    }

    @Override
    public void printExceptionMsg(String msg) {

    }
}
