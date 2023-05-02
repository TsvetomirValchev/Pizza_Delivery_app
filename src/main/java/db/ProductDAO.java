package db;

import Products.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class ProductDAO extends DAO<Product>{


    protected ProductDAO() {
        super("product", "id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO" + this.tableName
                +"(name,price)"
                +"VALUES(?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Product product) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
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
