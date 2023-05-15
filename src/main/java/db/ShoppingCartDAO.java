package db;

import Order.ShoppingCart;
import Products.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShoppingCartDAO extends DAO<ShoppingCart>{


    protected ShoppingCartDAO() {
        super("order_item","order_id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + tableName + "(order_id,product_id) VALUES(?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof ShoppingCart cart) {
            statement.setInt(1,cart.getOrderId());
            statement.setInt(2,cart.getProductId());
            
        }
    }

    @Override
    protected Integer getKey(ShoppingCart object) {
        return object.getOrderId();
    }

    @Override
    protected ShoppingCart mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        return null;
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {

    }
}
