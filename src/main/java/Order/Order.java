package Order;

import java.time.LocalDateTime;
import java.util.Optional;

public class Order{
    private final Integer id;
    private final int customerId;

    private final Optional<LocalDateTime>  orderedAt;
    private final Optional<LocalDateTime> deliveredAt;

    public Order(Integer id, int customerId, Optional<LocalDateTime>  orderedAt, Optional<LocalDateTime> deliveredAt) {
        this.id = id;
        this.customerId = customerId;
        this.orderedAt = orderedAt;
        this.deliveredAt = deliveredAt;
    }

    public Integer getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Optional<LocalDateTime>  getOrderedAt() {
        return orderedAt;
    }

    public Optional<LocalDateTime> getDeliveredAt() {
        return deliveredAt;
    }

    @Override
    public String toString() {

        return "Order #"+getId()
                +" for:" +getCustomerId()
                +" Ordered at: " + getOrderedAt().orElse(null)
                +" Delivered at: " + getDeliveredAt().orElse(null);


    }
}
