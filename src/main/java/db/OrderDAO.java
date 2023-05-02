package db;

import Order.Order;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class OrderDAO extends DAO<Order>{

    protected OrderDAO() {
        super("orders", "id");
    }

    @Override
    protected Map<Integer, Order> read() throws SQLException {
        String query = "SELECT * FROM orders "
                + "JOIN customer c ON c.id = orders.customer_id"
                + "\nJOIN delivery_driver d ON orders.assigned_driver_id = d.id";

        Map<Integer, Order> entries = new HashMap<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Order object = mapReadResultSetToObject(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return null;
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {

    }

    @Override
    protected Order mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        return null;
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {

    }

    @Override
    protected Integer getKey(Order object) {
        return null;
    }
}
