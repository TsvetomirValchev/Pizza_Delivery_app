package db;

import Products.Pizza;
import Products.PizzaIngredient.*;
import Products.PizzaIngredient.abstraction.PizzaIngredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PizzaDAO extends DAO<Pizza>{

    public PizzaDAO(){
        super("pizza", "product_id");
    }

    @Override
    protected Map<Integer, Pizza> read() throws SQLException {
        String query = "SELECT product.id," +
                "product.name," +
                "product.price," +
                "pizza.product_id," +
                "pizza.size_id," +
                "pizza.cheese_id," +
                "pizza.meat_id," +
                "pizza.sauce_id," +
                "pizza.addon_id," +
                "pizza_size.size_name," +
                "pizza_cheese.cheese_name," +
                "pizza_meat.meat_name," +
                "pizza_sauce.sauce_name," +
                "pizza_addon.addon_name FROM pizza\n" +
                "JOIN product ON pizza.product_id = product.id \n" +
                "JOIN pizza_size ON pizza_size.id = pizza.size_id\n" +
                "JOIN pizza_cheese ON pizza_cheese.id = pizza.cheese_id \n" +
                "JOIN pizza_meat ON pizza_meat.id = pizza.meat_id\n" +
                "JOIN pizza_sauce ON pizza_sauce.id = pizza.sauce_id\n" +
                "JOIN pizza_addon ON pizza_addon.id = pizza.addon_id;";
        Map<Integer, Pizza> entries = new HashMap<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Pizza object = mapReadResultSetToObject(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO" + this.tableName
                +"(product_id,size_id,cheese_id,meat_id,sauce_id,addon_id)"
                +"VALUES(?,?,?,?,?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if(object instanceof Pizza pizza){
            statement.setInt(1,pizza.getProduct_id());
            statement.setInt(2,pizza.getSize().getId());
            statement.setInt(3,pizza.getCheese().getId());
            statement.setInt(4,pizza.getMeat().getId());
            statement.setInt(5,pizza.getSauce().getId());
            statement.setInt(6,pizza.getAddon().getId());
        }
    }

    @Override
    protected Integer getKey(Pizza object) {
        return object.getProduct_id();
    }


    @Override
    protected Pizza mapReadResultSetToObject(ResultSet resultSet) throws SQLException {

        return new Pizza(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getInt("product_id"),
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
            case 1 -> statement.setInt(1, (Integer) updatedValue);
            case 2 -> statement.setInt(2, (Integer) updatedValue);
            case 3 -> statement.setInt(3,(Integer) updatedValue);
            case 4 -> statement.setInt(4, (Integer) updatedValue);
            case 5 -> statement.setInt(5, (Integer) updatedValue);
            case 6 -> statement.setInt(6, (Integer) updatedValue);
        }
    }



}
