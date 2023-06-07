package db;

import products.Pizza;
import products.ingredient.*;
import products.ingredient.abstraction.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PizzaDAO extends DAO<Pizza> {

    public PizzaDAO() {
        super("pizza", "product_id");
    }

    @Override
    protected Map<Integer, Pizza> readAll() throws SQLException {
        String query = """
                 SELECT product.id,
                 product.name,
                 product.price,
                 p.product_id,
                 p.size_id,
                 p.sauce_id,
                 size.size_name,
                 c.cheese_name,
                 m.meat_name,
                 sauce.sauce_name,
                 a.addon_name FROM pizza p
                 JOIN product ON p.product_id = product.id
                 JOIN size ON size.id = p.size_id
                 JOIN sauce ON sauce.id = p.sauce_id
                 JOIN pizza_addon pa on p.product_id = pa.pizza_id
                 JOIN addon a on a.id = pa.addon_id
                 JOIN pizza_cheese pc on p.product_id = pc.pizza_id
                 JOIN cheese c on pc.cheese_id = c.id
                 JOIN pizza_meat pm on p.product_id = pm.pizza_id
                 JOIN meat m on pm.meat_id = m.id
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
                + "(product_id,size_id,sauce_id)"
                + "VALUES(?, ?, ?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Pizza pizza) {
            statement.setInt(1, pizza.getId());
            statement.setInt(2, pizza.getSize().getId());
            statement.setInt(3, pizza.getSauce().getId());
        }
    }

    @Override
    protected Integer getKey(Pizza object) {
        return object.getId();
    }


    @Override
    protected Pizza mapResultSetToModel(ResultSet resultSet) throws SQLException {
        int pizzaId = resultSet.getInt("product_id");

        List<Meat> meats = fetchMeatForPizza(pizzaId);
        List<Addon> addons = fetchAddonForPizza(pizzaId);
        List<Cheese> cheese = fetchCheeseForPizza(pizzaId);

        return new Pizza(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                new Size(resultSet.getInt("size_id"), resultSet.getString("size_name")),
                new Sauce(resultSet.getInt("sauce_id"), resultSet.getString("sauce_name")),
                meats,
                cheese,
                addons
        );
    }

    @Override
    protected String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "product_id",
                2, "size_id",
                3, "addon_id"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE product_id=?";
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex) {
            case 1, 2, 3 -> statement.setInt(1, (Integer) updatedValue);
        }
    }


    public Map<Integer, Ingredient> readAllIngredients(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;

        Map<Integer, Ingredient> allIngredients = new HashMap<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Ingredient ingredient = (new Ingredient(resultSet.getInt("id"),
                        resultSet.getString(tableName + "_name")));
                allIngredients.put(ingredient.getId(), ingredient);
            }
        }
        return allIngredients;
    }

    public void insertInPizzaMeatTable(int pizzaId, int meatId) throws SQLException {
        String query = "INSERT INTO pizza_meat (pizza_id, meat_id) VALUES(? , ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pizzaId);
            statement.setInt(2, meatId);
            statement.executeUpdate();
        }

    }

    public void insertInPizzaCheeseTable(int pizzaId, int cheeseId) throws SQLException {
        String query = "INSERT INTO pizza_cheese (pizza_id, cheese_id) VALUES(? , ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pizzaId);
            statement.setInt(2, cheeseId);
            statement.executeUpdate();
        }

    }

    public void insertInPizzaAddonTable(int pizzaId, int addonId) throws SQLException {
        String query = "INSERT INTO pizza_addon (pizza_id, addon_id) VALUES(? , ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pizzaId);
            statement.setInt(2, addonId);
            statement.executeUpdate();
        }

    }


    private List<Meat> fetchMeatForPizza(int pizzaId) throws SQLException {
        List<Meat> meats = new ArrayList<>();
        String query = "SELECT meat_name, meat.id FROM meat " +
                "INNER JOIN pizza_meat " +
                "ON meat.id = pizza_meat.meat_id " +
                "WHERE pizza_meat.pizza_id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pizzaId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String meatName = resultSet.getString("meat_name");
                int meatId = resultSet.getInt("id");
                meats.add(new Meat(meatId, meatName));
            }
        }

        return meats;
    }


    private List<Addon> fetchAddonForPizza(int pizzaId) throws SQLException {
        List<Addon> addons = new ArrayList<>();
        String query = "SELECT addon_name, addon.id FROM addon " +
                "INNER JOIN pizza_addon " +
                "ON addon.id = pizza_addon.addon_id " +
                "WHERE pizza_addon.pizza_id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pizzaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String addonName = resultSet.getString("addon_name");
                int addonId = resultSet.getInt("id");
                addons.add(new Addon(addonId, addonName));
            }
        }

        return addons;
    }


    private List<Cheese> fetchCheeseForPizza(int pizzaId) throws SQLException {
        List<Cheese> cheese = new ArrayList<>();
        String query = "SELECT cheese_name, cheese.id FROM cheese " +
                "INNER JOIN pizza_cheese " +
                "ON cheese.id = pizza_cheese.cheese_id " +
                "WHERE pizza_cheese.pizza_id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, pizzaId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String cheeseName = resultSet.getString("cheese_name");
                int cheeseId = resultSet.getInt("id");
                cheese.add(new Cheese(cheeseId, cheeseName));
            }
        }

        return cheese;
    }

    protected void deletePizzaIngredientList(String tableName, int pizzaId, String primaryKey) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + primaryKey + "= ? ";
        try (Connection connection = database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, pizzaId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Entry with key " + pizzaId + " was not found!");
            }
        }
    }

}
