package db;

import logging.PizzaDeliveryLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class Database {
    private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(DAO.class.getName());
    private final String DB_USERNAME ;
    private final String DB_PASSWORD;
    private final String DB_URL;

    public Database(){
        try(FileInputStream is = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(is);
            DB_URL = props.getProperty("db.url");
            DB_USERNAME = props.getProperty("db.username");
            DB_PASSWORD = props.getProperty("db.password");


        } catch (IOException e) {
            LOGGER.severe(e+ "Couldn't load properties!");
            throw new RuntimeException(e);
        }
    }

    Connection getConnection() throws SQLException {
        try{
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }catch (SQLException e){
            LOGGER.severe(e.toString());
            throw e;
        }
    }

}
