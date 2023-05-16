package db;

import Products.Dessert;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DessertDAO extends DAO<Dessert>{

    protected DessertDAO() {
        super("dessert", "product_id");
    }

    @Override
    protected Map<Integer, Dessert> readAll() throws SQLException {
        String query = """
                SELECT product.id,
                product.name,
                product.price,
                dessert.isVegan
                FROM dessert JOIN product on product.id = dessert.product_id""";
        Map<Integer, Dessert> entries = new HashMap<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Dessert object = mapReadResultSetToObject(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }
    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName + "(isVegan) VALUES(?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if(object instanceof Dessert dessert){
            statement.setInt(1,dessert.getId());
            statement.setBoolean(2,dessert.isVegan());
        }
    }

    @Override
    protected Integer getKey(Dessert object) {
        return object.getId();
    }

    @Override
    protected Dessert mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return new Dessert(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getBoolean("isVegan")
        );
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "product_id",
                2, "isVegan"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE product_id=?";
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        if (variableIndex == 1) {
            statement.setInt(1, (Integer) updatedValue);
            statement.setBoolean(2, (boolean) updatedValue);
        }
        else System.out.println("The only thing you can update is the isVegan value");
    }
}
