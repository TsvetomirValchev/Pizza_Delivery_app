package db;

import products.Ingredient;
import products.IngredientType;
import products.Product;
import products.Size;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DAO<T> {
    Database database = new Database();
    protected final String tableName;
    protected final String tablePrimaryKey;

    protected DAO(String tableName, String tablePrimaryKey) {
        this.tableName = tableName;
        this.tablePrimaryKey = tablePrimaryKey;
    }

    protected abstract String buildInsertQuery(Object object);

    protected abstract void setInsertValues(PreparedStatement statement, Object object) throws SQLException;

    protected abstract Integer getKey(T object);

    protected abstract T mapResultSetToModel(ResultSet resultSet) throws SQLException;

    abstract String buildUpdateQuery(int variableIndex);

    abstract void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException;

    public void insert(Object object) throws SQLException {
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(buildInsertQuery(object))) {
            setInsertValues(statement, object);
            statement.executeUpdate();

            if (object instanceof Product product) {
                Map<Size, Double> sizePriceMapping = product.getSizesAndPrices();
                for (Map.Entry<Size, Double> entry : sizePriceMapping.entrySet()) {
                    Size size = entry.getKey();
                    Double price = entry.getValue();
                    insertInProductSizeTable(product.getId(), size.getId(), price);
                }
            }
        }
    }

    protected Map<Integer, T> readAll() throws SQLException {
        String query = "SELECT * FROM " + this.tableName;
        Map<Integer, T> entries = new HashMap<>();

        try (Connection connection = database.getConnection(); Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                T object = mapResultSetToModel(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }

    protected void update(Integer key, int variableIndex, Object updatedValue) throws SQLException {
        String query = buildUpdateQuery(variableIndex);
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            setUpdatedValues(statement, variableIndex, updatedValue);
            statement.setObject(2, key);
            statement.executeUpdate();
        }
    }

    protected void delete(Integer key) throws SQLException {
        String query = "DELETE FROM " + this.tableName + " WHERE " + this.tablePrimaryKey + "= ? ";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, key);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Entry with key " + key + " was not found!");
            }
        }
    }

    public void insertInProductSizeTable(int productId, int sizeId, double price) throws SQLException {
        String query = "INSERT INTO product_size (product_id, size_id, price) VALUES(? , ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.setInt(2, sizeId);
            statement.setDouble(3, price);
            statement.executeUpdate();
        }

    }

    protected void deleteProductSizesAndPrices(int productId) throws SQLException {
        String query = "DELETE FROM product_size WHERE product_id" + "= ? ";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, productId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Entry with key " + productId + " was not found!");
            }
        }
    }

    protected Map<Size, Double> fetchAllAvailableSizesAndPricesToProduct(Integer product_id) throws SQLException {
        String query = "SELECT s.id,s.size_name, ps.price FROM product_size ps " +
                "JOIN product p ON p.id = ps.product_id " +
                "JOIN size s ON s.id = ps.size_id " +
                "WHERE product_id =" + product_id;

        Map<Size, Double> allAvailableSizesAndPrices = new HashMap<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Size size = (new Size(resultSet.getInt("id"),
                        resultSet.getString("size_name")));
                double price = resultSet.getDouble("price");
                allAvailableSizesAndPrices.put(size, price);
            }
        }
        return allAvailableSizesAndPrices;
    }


    protected Map<Integer, Size> readAllFromSizeTable() throws SQLException {
        String query = "SELECT s.id,s.size_name FROM size s ";
        Map<Integer, Size> allSizes = new HashMap<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Size size = (new Size(resultSet.getInt("id"),
                        resultSet.getString("size_name")));
                allSizes.put(size.getId(), size);
            }
        }
        return allSizes;
    }

    protected int readSizeIdBySizeName(String sizeName) throws SQLException {
        String query = "SELECT s.id FROM product_size " +
                "JOIN size s on product_size.size_id = s.id " +
                "WHERE size_name = '" + sizeName + "'";
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        return 0;
    }

    protected List<Ingredient> fetchAllIngredientsToProduct(Integer productId) throws SQLException {
        String query = "SELECT i.ingredient_name, i.id, i.ingredient_type_id, it.ingredient_type_name " +
                " FROM product_ingredient pi " +
                " JOIN ingredient i ON i.id = pi.ingredient_id " +
                " JOIN ingredient_type it ON i.ingredient_type_id = it.id " +
                " WHERE product_id =" + productId;

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


    protected Map<Integer, Ingredient> readAllIngredientsAvailable() throws SQLException {
        String query = "SELECT i.ingredient_name, i.id, it.ingredient_type_name, ingredient_type_id FROM ingredient i " +
                "JOIN ingredient_type it on it.id = i.ingredient_type_id ";

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


    protected void insertInProductIngredientTable(int productId, int ingredientId) throws SQLException {
        String query = "INSERT INTO product_ingredient (product_id, ingredient_id) VALUES(? , ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            statement.setInt(2, ingredientId);
            statement.executeUpdate();
        }

    }

}
