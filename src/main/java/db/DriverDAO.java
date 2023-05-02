package db;

import Order.Driver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverDAO extends DAO<Driver>{
    protected DriverDAO() {
        super("delivery_driver", "id");
    }

    @Override
    protected String buildInsertQuery(Object object) {
        return "INSERT INTO" + this.tableName
                +"(first_name, last_name)"
                +"VALUES(?,?)";
    }

    @Override
    protected void setValues(PreparedStatement statement, Object object) throws SQLException {
        if(object instanceof Driver driver) {
            statement.setString(1,driver.getF_name());
            statement.setString(2,driver.getL_name());

        }

    }

    @Override
    protected Integer getKey(Driver object) {
        return object.getId();
    }

    @Override
    protected Driver mapReadResultSetToObject(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        return null;
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {

    }
}
