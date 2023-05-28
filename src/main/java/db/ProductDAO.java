package db;

import Products.Product;

import java.sql.*;
import java.util.Map;

public class ProductDAO extends DAO<Product>{


    public ProductDAO() {
        super("product", "id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName
                +"(id,name,price)"
                +"VALUES(?, ?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Product product) {
            statement.setInt(1,product.getId());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getPrice());
        }

    }

    @Override
    protected Integer getKey(Product object) {
        return object.getId();
    }

    @Override
    protected Product mapResultSetToModel(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price")
        );
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1,"id",
                2, "name",
                3, "price"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE id=?";
    }


    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex){
            case 1 -> statement.setInt(1, (Integer) updatedValue);
            case 2 -> statement.setString(2,(String) updatedValue);
            case 3 -> statement.setDouble(3,(Double) updatedValue);
        }
    }
}
