package db;

import Order.Order;
import Products.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderDAO extends DAO<Order>{

    protected OrderDAO() {
        super("orders", "id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName +
                "(customer_id,assigned_driver_id,ordered_at,delivered_at) VALUES(?, ?, ?, ?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Order order) {
            statement.setInt(1, order.getCustomerId());
            statement.setTimestamp(2, Timestamp.valueOf(order.getOrderedAt()));
            if (order.getDeliveredAt().isPresent()) {
                statement.setTimestamp(3, Timestamp.valueOf(order.getDeliveredAt().get()));
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }
        }
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
                resultSet.getTimestamp("ordered_at").toLocalDateTime(),
                Optional.ofNullable(deliveryTime)
        );
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "customer_id",
                2, "ordered_at",
                3, "delivered_at"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + tableName + " SET " + columnName + "=? WHERE " + tablePrimaryKey + "=?";
    }


    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex){
            case 1-> statement.setInt(1,(Integer) updatedValue);
            case 2 -> statement.setTimestamp(2,Timestamp.valueOf((LocalDateTime) updatedValue));
            case 3 -> statement.setTimestamp(3,Timestamp.valueOf((LocalDateTime) updatedValue));
        }
    }

    @Override
    protected Integer getKey(Order object) {
        return object.getId();
    }





    public List<Product> getAllProductsInOrder(int OrderId) throws SQLException {
        String query = "SELECT product_id, name, price FROM order_item " +
                "JOIN orders ON order_id = orders.id " +
                "JOIN product p on p.id = order_item.product_id" +
                " WHERE order_id = " + OrderId ;

        List<Product> allProducts = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                allProducts.add(new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")));
            }
        }
        return allProducts;
    }

    public List<Order> getOrderByProductId(int productId) throws SQLException {
        String query = "SELECT order_id,customer_id,ordered_at,delivered_at FROM order_item " +
                "JOIN orders ON order_id = orders.id " +
                "JOIN product p on p.id = order_item.product_id" +
                " WHERE product_id = " + productId ;

        List<Order> allOrdersWithProductInThem = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                LocalDateTime deliveryTime = null;
                Timestamp deliveryTimestamp = resultSet.getTimestamp("delivered_at");

                if (deliveryTimestamp != null){
                    deliveryTime = deliveryTimestamp.toLocalDateTime();
                }

                allOrdersWithProductInThem.add(new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("customer_id"),
                        resultSet.getTimestamp("ordered_at").toLocalDateTime(),
                        Optional.ofNullable(deliveryTime)));
            }
        }
        return allOrdersWithProductInThem;
    }


    public void InsertInOrderItemTable(int productId, int orderId) throws SQLException {
        String query = "INSERT INTO order_item(order_id, product_id) VALUES(? , ?)";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(query))
             {
                statement.setInt(1, productId);
                statement.setInt(2, orderId);
                statement.executeUpdate();
            }

    }



}
