package db.abstraction;

import View.abstraction.View;
import logging.PizzaDeliveryLogger;


import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Controller {
    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(Controller.class.getName());

    protected abstract View getView();

    public void transmitException(Exception e, Level SeverityLevel,String message){
        logException(e,SeverityLevel,message);

    }

    public void logException(Exception e, Level SeverityLevel,String message){
        if(e.getMessage()!=null){
            LOGGER.log(SeverityLevel, e.getMessage());
        }
        else{
            LOGGER.log(SeverityLevel,message);
        }

    }


}
