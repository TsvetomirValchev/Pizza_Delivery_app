package Order;

public class ShoppingCart {

    private final int OrderId;
    private final int productId;


    public ShoppingCart(int orderId, int productId) {
        OrderId = orderId;
        this.productId = productId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public int getProductId() {
        return productId;
    }

}
