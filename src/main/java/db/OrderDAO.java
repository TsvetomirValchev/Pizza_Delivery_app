package db;

import order.Order;
import products.Product;
import products.ProductType;
import products.Size;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class OrderDAO extends DAO<Order> {

    protected OrderDAO() {
        super("orders", "id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName +
                "(customer_id,ordered_at,delivered_at) VALUES(?, ?, ?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Order order) {
            statement.setInt(1, order.getCustomerId());
            if (order.getOrderedAt().isPresent()) {
                statement.setTimestamp(2, Timestamp.valueOf(order.getOrderedAt().get()));
            } else {
                statement.setNull(2, Types.TIMESTAMP);
            }
            if (order.getDeliveredAt().isPresent()) {
                statement.setTimestamp(3, Timestamp.valueOf(order.getDeliveredAt().get()));
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }
        }
    }

    @Override
    protected Order mapResultSetToModel(ResultSet resultSet) throws SQLException {
        LocalDateTime deliveryTime = getLocalDateTimeFromTimestamp(resultSet.getTimestamp("delivered_at"));
        LocalDateTime orderedAtTime = getLocalDateTimeFromTimestamp(resultSet.getTimestamp("ordered_at"));

        return new Order(
                resultSet.getInt("id"),
                resultSet.getInt("customer_id"),
                Optional.ofNullable(orderedAtTime),
                Optional.ofNullable(deliveryTime)
        );
    }


    @Override
    protected String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "id",
                2, "customer_id",
                3, "ordered_at",
                4, "delivered_at"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + tableName + " SET " + columnName + "=? WHERE " + tablePrimaryKey + "=?";
    }


    @Override
    protected void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex) {
            case 1, 2 -> statement.setInt(1, (Integer) updatedValue);
            case 3, 4 -> statement.setTimestamp(1, Timestamp.valueOf((LocalDateTime) updatedValue));

        }
    }

    @Override
    protected Integer getKey(Order object) {
        return object.getId();
    }

    protected List<Product> getAllProductsInOrder(int orderId) throws SQLException {
        String query = "SELECT p.id, p.name, ps.price, ps.size_id, pt.type_name, p.type_id " +
                "FROM order_item oi " +
                "JOIN product_size ps ON oi.product_size_id = ps.product_id " +
                "JOIN product p ON ps.product_id = p.id " +
                "JOIN product_type pt on p.type_id = pt.id " +
                "JOIN product_ingredient pi on p.id = pi.product_id " +
                "WHERE oi.order_id = ?";

        List<Product> allProducts = new ArrayList<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int sizeId = resultSet.getInt("size_id");
                    ProductType productType = new ProductType(resultSet.getInt("type_id"), resultSet.getString("type_name"));
                    Size size = new Size(sizeId, "");
                    Map<Size, Double> sizesAndPrices = new HashMap<>();
                    sizesAndPrices.put(size, price);
                    Product product = new Product(productId, name, sizesAndPrices, productType);
                    allProducts.add(product);
                }
            }
        }
        return allProducts;
    }


    protected List<Order> getOrderByProductId(int productId) throws SQLException {
        String query = "SELECT o.id AS order_id, o.customer_id, o.ordered_at, o.delivered_at FROM order_item oi " +
                "JOIN orders o ON oi.order_id = o.id " +
                "WHERE oi.product_size_id IN (SELECT ps.size_id FROM product_size ps WHERE ps.product_id = ?)";

        List<Order> allOrdersWithProductInThem = new ArrayList<>();
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LocalDateTime deliveryTime = getLocalDateTimeFromTimestamp(resultSet.getTimestamp("delivered_at"));
                    LocalDateTime orderedAtTime = getLocalDateTimeFromTimestamp(resultSet.getTimestamp("ordered_at"));

                    allOrdersWithProductInThem.add(new Order(
                            resultSet.getInt("order_id"),
                            resultSet.getInt("customer_id"),
                            Optional.ofNullable(orderedAtTime),
                            Optional.ofNullable(deliveryTime)));
                }
            }
        }
        return allOrdersWithProductInThem;
    }

    protected Map<Size, Double> getOrderedSizesAndPrices(Integer OrderId) throws SQLException {
        Map<Size, Double> sizesAndPrices = new HashMap<>();

        String query = "SELECT ps.size_id, s.size_name, ps.price " +
                "FROM size s " +
                "JOIN product_size ps ON s.id = ps.size_id " +
                "JOIN order_item oi ON oi.product_size_id = ps.id " +
                "WHERE oi.order_id = " + OrderId;

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int sizeId = resultSet.getInt("size_id");
                String sizeName = resultSet.getString("size_name");
                double price = resultSet.getDouble("price");
                Size size = new Size(sizeId, sizeName);
                sizesAndPrices.put(size, price);
            }
        }

        return sizesAndPrices;
    }


    private LocalDateTime getLocalDateTimeFromTimestamp(Timestamp timestamp) {
        return (timestamp != null) ? timestamp.toLocalDateTime() : null;
    }

    protected void InsertInOrderItemTable(int orderId, int productSizeId) throws SQLException {
        String query = "INSERT INTO order_item(order_id, product_size_id) VALUES(? , ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.setInt(2, productSizeId);
            statement.executeUpdate();
        }

    }

    protected void DeleteProductFromCompletedOrder(int productId) throws SQLException {
        String query = "DELETE FROM order_item WHERE product_size_id " +
                "IN (SELECT id FROM product_size WHERE product_id = ?)";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }

    }

    protected boolean hasActiveOrders(int customerId) throws SQLException {
        String query = "SELECT COUNT(*) AS active_orders FROM orders WHERE customer_id = ? AND delivered_at IS NULL";

        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int activeOrders = resultSet.getInt("active_orders");
                    return activeOrders > 0;
                }
            }
            return false;
        }
    }


    protected Integer getProductSizeTableIdBySizeAndProductId(int sizeId, int productId) throws SQLException {
        String query = "SELECT ps.id FROM product_size ps " +
                "WHERE ps.size_id =" + sizeId + " AND ps.product_id =" + productId;

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        return 0;
    }


}
