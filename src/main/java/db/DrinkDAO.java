package db;

import Products.Drink;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DrinkDAO extends DAO<Drink>{

    protected DrinkDAO() {
        super("drink", "product_id");
    }

    @Override
    protected Map<Integer, Drink> readAll() throws SQLException {
        String query = """
                SELECT product.id,
                product.name,
                product.price,
                drink.isDiet
                FROM drink JOIN product  on product.id = drink.product_id""";
        Map<Integer, Drink> entries = new HashMap<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Drink object = mapResultSetToModel(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }
    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName + "(product_id, isDiet)"+ "VALUES(?, ?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if(object instanceof Drink drink){
            statement.setInt(1, drink.getId());
            statement.setBoolean(2, drink.isDiet());
        }
    }

    @Override
    protected Integer getKey(Drink object) {
        return object.getId();
    }

    @Override
    protected Drink mapResultSetToModel(ResultSet resultSet) throws SQLException {
        return new Drink(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getBoolean("isDiet")
        );
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
       Map<Integer, String> columnMap = Map.of(
               1,"product_id",
               2, "isDiet"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE product_id=?";
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex) {
            case 1 -> statement.setInt(1, (Integer) updatedValue);
            case 2 -> statement.setBoolean(1,(boolean) updatedValue);
        }
    }
}
