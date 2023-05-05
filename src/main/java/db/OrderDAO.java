package db;

import Order.Order;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderDAO extends DAO<Order>{

    protected OrderDAO() {
        super("orders", "id");
    }

    @Override
    protected Map<Integer, Order> read() throws SQLException {
        String query = """
                SELECT o.id, o.ordered_at, o.delivered_at, o.customer_id, o.assigned_driver_id,d.first_name,d.last_name,c.username FROM orders AS o
                JOIN customer c ON c.id = o.customer_id
                JOIN delivery_driver d ON o.assigned_driver_id = d.id""";

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
        LocalDateTime deliveryTime = null;
        Timestamp deliveryTimestamp = resultSet.getTimestamp("delivered_at");
        if (deliveryTimestamp != null){
            deliveryTime = deliveryTimestamp.toLocalDateTime();
        }

        return new Order(
                resultSet.getInt("id"),
                resultSet.getInt("customer_id"),
                resultSet.getInt("assigned_driver_id"),
                resultSet.getTimestamp("ordered_at").toLocalDateTime(),
                Optional.ofNullable(deliveryTime)
        );
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
