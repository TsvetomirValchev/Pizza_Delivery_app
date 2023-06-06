package db;

import users.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CustomerDAO extends DAO<Customer> {

    protected CustomerDAO() {
        super("customer", "id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO " + this.tableName +
                "(username, password, email, address) VALUES(?, ?, ?, ?)";
    }

    @Override
    protected void setInsertValues(PreparedStatement statement, Object object) throws SQLException {
        if (object instanceof Customer customer) {
            statement.setString(1, customer.getUsername());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getAddress());
        }
    }

    @Override
    protected Customer mapResultSetToModel(ResultSet resultSet) throws SQLException {
        return new Customer(
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getInt(this.tablePrimaryKey),
                resultSet.getString("email"),
                resultSet.getString("address")
        );
    }

    @Override
    protected Integer getKey(Customer object) {
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
                5, "address"

        );
        String columnName = columnMap.get(propertyIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE id=?";
    }


}
