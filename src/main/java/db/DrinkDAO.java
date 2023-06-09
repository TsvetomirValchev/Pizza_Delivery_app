package db;

import products.Drink;
import products.ProductType;
import products.Size;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DrinkDAO extends DAO<Drink> {

    protected DrinkDAO() {
        super("product", "id");
    }

    @Override
    protected Map<Integer, Drink> readAll() throws SQLException {
        String query = """
                SELECT p.id,
                p.name,
                p.type_id,
                p.isDiet,
                pt.type_name,
                ps.price
                FROM product p JOIN product_type pt on p.type_id = pt.id
                JOIN product_size ps on p.id = ps.product_id
                JOIN size s on ps.size_id = s.id
                WHERE type_name = 'drink'
                """;
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
        return "INSERT INTO " + this.tableName + "(id,name,type_id,isDiet) VALUES(?,?,?,?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Drink drink) {
            statement.setInt(1, drink.getId());
            statement.setString(2, drink.getName());
            statement.setInt(3, drink.getProductType().getId());
            statement.setBoolean(4, drink.isDiet());
        }
    }

    @Override
    protected Integer getKey(Drink object) {
        return object.getId();
    }

    @Override
    protected Drink mapResultSetToModel(ResultSet resultSet) throws SQLException {
        int drinkId = resultSet.getInt("id");
        Map<Size, Double> availableSizesAndPrices = fetchAllAvailableSizesAndPricesToProduct(drinkId);
        return new Drink(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                new ProductType(resultSet.getInt("type_id"), resultSet.getString("type_name")),
                availableSizesAndPrices,
                resultSet.getBoolean("isDiet")
        );
    }


    @Override
    protected String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "product_id",
                2, "name",
                3, "type_id",
                4, "isDiet"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE product_id=?";
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex) {
            case 1, 3 -> statement.setInt(1, (Integer) updatedValue);
            case 2 -> statement.setString(1, (String) updatedValue);
            case 4 -> statement.setBoolean(1, (boolean) updatedValue);
        }
    }
}
