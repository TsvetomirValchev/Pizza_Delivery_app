package db;

import logging.PizzaDeliveryLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class DAO<T> {
     private static final Logger LOGGER = PizzaDeliveryLogger.getLogger(DAO.class.getName());
     private String DB_USERNAME;
     private String DB_PASSWORD;
     private String DB_URL;
     protected final String tableName;
     protected final String tablePrimaryKey;

     protected DAO(String tableName,String tablePrimaryKey){
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
          this.tableName=tableName;
          this.tablePrimaryKey=tablePrimaryKey;
     }

     protected abstract String buildInsertQuery(Object object);
     protected abstract void setValues(PreparedStatement statement, Object object) throws SQLException;
     protected abstract Integer getKey(T object);
     protected abstract T mapReadResultSetToObject(ResultSet resultSet) throws SQLException;
     abstract String buildUpdateQuery(int variableIndex);
     abstract void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException;

     Connection getConnection() throws SQLException {
          try{
               return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
          }catch (SQLException e){
               LOGGER.severe(e.toString());
               throw e;
          }
     }

     public void create(Object object) throws SQLException {
          try (Connection connection = getConnection();
               PreparedStatement statement = connection.prepareStatement(buildInsertQuery(object))) {
               setValues(statement, object);
               statement.executeUpdate();
          }
     }

     protected Map<Integer, T> readAll() throws SQLException {
          String query = "SELECT * FROM " + this.tableName;
          Map<Integer, T> entries = new HashMap<>();

          try (Connection connection = getConnection();
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery(query)) {
               while (resultSet.next()) {
                    T object = mapReadResultSetToObject(resultSet);
                    entries.put(getKey(object), object);
               }
          }
          return entries;
     }

    protected void update(Object key, int variableIndex, Object updatedValue) throws SQLException {
          String query = buildUpdateQuery(variableIndex);
          try (Connection connection = getConnection();
               PreparedStatement statement = connection.prepareStatement(query)) {
               setUpdatedValues(statement, variableIndex, updatedValue);
               statement.setObject(2, key);
               statement.executeUpdate();
          }
     }

     protected void delete(Object key) throws SQLException {
          String query = "DELETE FROM " + this.tableName + " WHERE " + this.tablePrimaryKey + " = ? ";
          try (Connection connection = getConnection();
               PreparedStatement statement = connection.prepareStatement(query)) {
               statement.setObject(1, key);
               int rowsAffected = statement.executeUpdate();
               if (rowsAffected == 0) {
                    throw new SQLDataException("Entry with key " + key + " was not found!");
               }
          }
     }
}
