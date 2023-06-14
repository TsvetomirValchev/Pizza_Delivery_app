package db;

import users.AccountType;
import users.Customer;
import users.User;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDAO extends DAO<User> {

    public UserDAO() {
        super("user", "id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName +
                "(username, password, email, account_type) VALUES(?, ?, ?, ?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof User user) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getAccountType().toString());
        }
    }


    @Override
    protected User mapResultSetToModel(ResultSet resultSet) throws SQLException {
        String accountType = resultSet.getString("account_type");
        return new User(
                resultSet.getInt(this.tablePrimaryKey),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("email"),
                AccountType.valueOf(accountType)

        );
    }

    @Override
    protected Integer getKey(User object) {
        return object.getId();
    }

    @Override
    protected void setUpdatedValues(PreparedStatement statement, int propertyIndex, Object updatedValue) throws SQLException {
        switch (propertyIndex) {
            case 1 -> statement.setInt(1, (int) updatedValue);
            case 2, 3, 4, 5 -> statement.setString(1, (String) updatedValue);
        }
    }

    @Override
    protected String buildUpdateQuery(int propertyIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "id",
                2, "username",
                3, "password",
                4, "email",
                5, "account_type"

        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE id=?";
    }


    protected Map<Integer, Customer> readOnlyCustomers() throws SQLException {
        String query = "SELECT * FROM user" + " WHERE account_type = 'customer'";
        Map<Integer, Customer> entries = new HashMap<>();

        try (Connection connection = database.getConnection(); Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Customer object = (Customer) mapResultSetToModel(resultSet);
                entries.put(getKey(object), object);
            }
        }
        return entries;
    }


}
