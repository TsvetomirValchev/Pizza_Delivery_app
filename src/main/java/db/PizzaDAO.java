package db;

import products.Pizza;
import products.ingredient.*;
import products.ingredient.abstraction.PizzaIngredient;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class PizzaDAO extends DAO<Pizza>{

    public PizzaDAO(){
        super("pizza", "product_id");
    }

    @Override
    protected Map<Integer, Pizza> readAll() throws SQLException {
        String query = """
                SELECT product.id,
                product.name,
                product.price,
                pizza.product_id,
                pizza.size_id,
                pizza.cheese_id,
                pizza.meat_id,
                pizza.sauce_id,
                pizza.addon_id,
                size.size_name,
                cheese.cheese_name,
                meat.meat_name,
                sauce.sauce_name,
                addon.addon_name FROM pizza
                JOIN product ON pizza.product_id = product.id\s
                JOIN size ON size.id = pizza.size_id
                JOIN cheese ON cheese.id = pizza.cheese_id\s
                JOIN meat ON meat.id = pizza.meat_id
                JOIN sauce ON sauce.id = pizza.sauce_id
                JOIN addon ON addon.id = pizza.addon_id;""";
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
                +"(product_id,size_id,cheese_id,meat_id,sauce_id,addon_id)"
                +"VALUES(?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if(object instanceof Pizza pizza){
            statement.setInt(1, pizza.getId());
            statement.setInt(2, pizza.getSize().getId());
            statement.setInt(3, pizza.getCheese().getId());
            statement.setInt(4, pizza.getMeat().getId());
            statement.setInt(5, pizza.getSauce().getId());
            statement.setInt(6, pizza.getAddon().getId());
        }
    }

    @Override
    protected Integer getKey(Pizza object) {
        return object.getId();
    }


    @Override
    protected Pizza mapResultSetToModel(ResultSet resultSet) throws SQLException {
        return new Pizza(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                new Size(resultSet.getInt("size_id"),resultSet.getString("size_name")),
                new Cheese(resultSet.getInt("cheese_id"),resultSet.getString("cheese_name")),
                new Meat(resultSet.getInt("meat_id"),resultSet.getString("meat_name")),
                new Sauce(resultSet.getInt("sauce_id"),resultSet.getString("sauce_name")),
                new Addon(resultSet.getInt("addon_id"), resultSet.getString("addon_name"))
        );
    }

    @Override
    protected String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "product_id",
                2, "size_id",
                3, "cheese_id",
                4, "meat_id",
                5,"sauce_id",
                6, "addon_id"
        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE product_id=?";
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
        switch (variableIndex) {
            case 1,2,3,4,5,6-> statement.setInt(1, (Integer) updatedValue);
        }
    }

    public Map<Integer, PizzaIngredient> readAllIngredients(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;

        Map<Integer, PizzaIngredient> allIngredients = new HashMap<>();
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                PizzaIngredient ingredient = (new PizzaIngredient(resultSet.getInt("id"),
                        resultSet.getString(tableName + "_name")));
                allIngredients.put(ingredient.getId(), ingredient);
            }
        }
       return allIngredients;
    }




}
