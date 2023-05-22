import db.OrderDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class OrderDAOTest {


    OrderDAO orderDAO = new OrderDAO();

    @Test
    @DisplayName("Should add specific orderId and and ItemId to order_item table.")
    void testAddProductToOrderCart() throws SQLException {
        orderDAO.InsertInOrderItemTable(1,5);
    }
}
