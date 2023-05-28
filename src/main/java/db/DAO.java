package db;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DAO<T> {
     Database database = new Database();
     protected final String tableName;
     protected final String tablePrimaryKey;

     protected DAO(String tableName,String tablePrimaryKey){
          this.tableName = tableName;
          this.tablePrimaryKey = tablePrimaryKey;
     }

     protected abstract String buildInsertQuery(Object object);
     protected abstract void setInsertValues(PreparedStatement statement, Object object) throws SQLException;
     protected abstract Integer getKey(T object);
     protected abstract T mapResultSetToModel(ResultSet resultSet) throws SQLException;
     abstract String buildUpdateQuery(int variableIndex);
     abstract void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException;

     public void insert(Object object) throws SQLException {
          try (Connection connection = database.getConnection();
               PreparedStatement statement = connection.prepareStatement(buildInsertQuery(object))) {
               setInsertValues(statement, object);
               statement.executeUpdate();
          }
     }

     protected Map<Integer, T> readAll() throws SQLException {
          String query = "SELECT * FROM " + this.tableName;
          Map<Integer, T> entries = new HashMap<>();

          try (Connection connection = database.getConnection();
               Statement statement = connection.createStatement();
               ResultSet resultSet = statement.executeQuery(query)) {
               while (resultSet.next()) {
                    T object = mapResultSetToModel(resultSet);
                    entries.put(getKey(object), object);
               }
          }
          return entries;
     }

    protected void update(Object key, int variableIndex, Object updatedValue) throws SQLException {
          String query = buildUpdateQuery(variableIndex);
          try (Connection connection = database.getConnection();
               PreparedStatement statement = connection.prepareStatement(query)) {
               setUpdatedValues(statement, variableIndex, updatedValue);
               statement.setObject(2, key);
               statement.executeUpdate();
          }
    }

    protected void delete(Object key) throws SQLException {
          String query = "DELETE FROM " + this.tableName + " WHERE " + this.tablePrimaryKey + "= ? ";
          try (Connection connection = database.getConnection();
               PreparedStatement statement = connection.prepareStatement(query)) {
               statement.setObject(1, key);
               int rowsAffected = statement.executeUpdate();
               if (rowsAffected == 0) {
                    throw new SQLException("Entry with key " + key + " was not found!");
               }
          }
    }
}
