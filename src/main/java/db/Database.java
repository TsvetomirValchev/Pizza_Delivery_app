package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*Is this class useful? Should I just put it in the DAO since I couldn't think of a way to make it an interface(since i use the fields for tableName and tablePrimaryKey that are in the
 constructor of the abstract class)?*/
public class Database {

    private final String DB_USERNAME ;
    private final String DB_PASSWORD;
    private final String DB_URL;

    public Database(){
        try(FileInputStream is = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(is);
            DB_URL = props.getProperty("db.url");
            DB_USERNAME = props.getProperty("db.username");
            DB_PASSWORD = props.getProperty("db.password");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() throws SQLException {
        try{
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }catch (SQLException e){
            throw e;
        }
    }

}
