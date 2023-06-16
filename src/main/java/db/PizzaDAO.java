package db;

import products.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PizzaDAO extends DAO<Pizza> {

    public PizzaDAO() {
        super("product", "id");
    }

    @Override
    protected Map<Integer, Pizza> readAll() throws SQLException {
        String query = """
                 SELECT p.id,
                 p.name,
                 p.type_id,
                 pt.type_name,
                 pi.ingredient_id,
                 i.ingredient_name,
                 i.ingredient_type_id,
                 it.ingredient_type_name,
                 ps.price,
                 ps.size_id,
                 s.size_name
                 FROM product p JOIN product_type pt on p.type_id = pt.id
                 JOIN product_ingredient pi on p.id = pi.product_id
                 JOIN ingredient i on pi.ingredient_id = i.id
                 JOIN ingredient_type it on i.ingredient_type_id = it.id
                 JOIN product_size ps on pi.product_id = ps.product_id
                 JOIN size s on ps.size_id = s.id
                 WHERE pt.type_name = 'pizza';
                ;""";
        Map<Integer, Pizza> entries = new HashMap<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Pizza object = mapResultSetToModel(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName
                + "(id,name,type_id)"
                + "VALUES(?, ?, ?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Pizza pizza) {
            statement.setInt(1, pizza.getId());
            statement.setString(2, pizza.getName());
            statement.setInt(3, pizza.getProductType().getId());
        }
    }

    @Override
    protected Integer getKey(Pizza object) {
        return object.getId();
    }


    @Override
    protected Pizza mapResultSetToModel(ResultSet resultSet) throws SQLException {
        int pizzaId = resultSet.getInt("id");
        List<Ingredient> ingredients = readAllIngredientsInsideProduct(pizzaId);
        List<Size> availableSizes = readAllAvailableSizes(pizzaId);

        return new Pizza(
                pizzaId,
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                new ProductType(resultSet.getInt("type_id"), resultSet.getString("type_name")),
                availableSizes,
                ingredients
        );

    }

    @Override
    protected String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "id",
                2, "name",
                3, "type_id"

        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE product_id=?";
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex) {
            case 1, 3 -> statement.setInt(1, (Integer) updatedValue);
            case 2 -> statement.setString(1, (String) updatedValue);
        }
    }


    public List<Ingredient> readAllIngredientsInsideProduct(Integer productId) throws SQLException {
        String query = "SELECT i.ingredient_name, i.id FROM product_ingredient " +
                "JOIN ingredient i ON i.id = product_ingredient.ingredient_id " +
                "WHERE product_id =" + productId;

        List<Ingredient> allIngredients = new ArrayList<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Ingredient ingredient = (new Ingredient(resultSet.getInt("id"),
                        resultSet.getString("ingredient_name"),
                        new IngredientType(resultSet.getInt("ingredient_type_id"),
                                resultSet.getString("ingredient_type_name"))));
                allIngredients.add(ingredient);
            }
        }
        return allIngredients;
    }

    //
    public Map<Integer, Ingredient> readAllIngredientsAvailable() throws SQLException {
        String query = "SELECT i.ingredient_name, i.id FROM ingredient i ";

        Map<Integer, Ingredient> allIngredients = new HashMap<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Ingredient ingredient = (new Ingredient(resultSet.getInt("id"),
                        resultSet.getString("ingredient_name"),
                        new IngredientType(resultSet.getInt("ingredient_type_id"),
                                resultSet.getString("ingredient_type_name"))));

                allIngredients.put(ingredient.getId(), ingredient);
            }
        }
        return allIngredients;
    }

    protected void deletePizzaIngredientList(int productId) throws SQLException {
        String query = "DELETE FROM product_ingredient WHERE product_id" + "= ? ";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, productId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Entry with key " + productId + " was not found!");
            }
        }
    }

}
