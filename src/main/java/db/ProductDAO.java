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
    protected Product mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getDouble("price")
        );
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "name",
                2, "price"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE id=?";
    }


    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex){
            case 1 -> statement.setString(1,(String) updatedValue);
            case 2 -> statement.setDouble(2,(Double) updatedValue);
        }
    }
}
