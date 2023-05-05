package db;

import Order.Driver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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
        return new Driver(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getBoolean("is_free")
        );
    }

    @Override
    String buildUpdateQuery(int variableIndex) {
        Map<Integer, String> columnMap = Map.of(
                1, "first_name",
                2, "last_name",
                3, "is_free"


        );
        String columnName = columnMap.get(variableIndex);
        return "UPDATE " + this.tableName + " SET " + columnName + "=? WHERE id=?";
    }

    @Override
    void setUpdatedValues(PreparedStatement statement, int variableIndex, Object updatedValue) throws SQLException {
            switch (variableIndex){
                case 1 -> statement.setString(1,(String)updatedValue);
                case 2 -> statement.setString(2,(String)updatedValue);
                case 3 -> statement.setBoolean(3,(Boolean) updatedValue);
            }
    }
}
