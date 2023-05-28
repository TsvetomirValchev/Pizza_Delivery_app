import db.Database;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseConnectionTest {
    Database database = new Database();

    @DisplayName("Database connection should be established")
    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = database.getConnection();
        assertNotNull(connection);
    }
}
