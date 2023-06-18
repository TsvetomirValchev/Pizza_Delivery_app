package db;

import order.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import products.*;
import users.Customer;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class CustomerService extends Service {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class.getName());
    private final PizzaDAO pizzaDAO = new PizzaDAO();
    private final DAO<Drink> drinkDAO = new DrinkDAO();
    private final DAO<Dessert> dessertDAO = new DessertDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final Customer customer;

    public CustomerService(Customer customer) {
        this.customer = customer;
    }

    public void placeAnOrder(int productId, String sizeName) {
        try {
            if (getProductById(productId) == null) {
                throw new IllegalArgumentException("There is no product with such id");
            }
            if (!isOrderFinalized()) {
                addProductToCart(orderDAO.getProductSizeTableIdBySizeAndProductId(orderDAO.readSizeIdBySizeName(sizeName), productId));
            } else {
                orderDAO.insert(new Order(null,
                        customer.getId(),
                        Optional.empty(),
                        Optional.empty()));
                addProductToCart(orderDAO.getProductSizeTableIdBySizeAndProductId(orderDAO.readSizeIdBySizeName(sizeName), productId));
            }

        } catch (SQLException | IllegalArgumentException e) {
            if (e instanceof IllegalArgumentException) {
                LOGGER.error(e.getMessage());
            } else {
                LOGGER.debug(e.getMessage());
                LOGGER.error("Couldn't place an order!");
            }
        }
    }

    private void addProductToCart(int productSizeId) {
        try {
            orderDAO.InsertInOrderItemTable(getCartOrderIdByCustomerId(customer.getId()), productSizeId);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't add product to cart!");
        }
    }
    
    public String calculateCurrentOrderTotal() {
        double orderTotal = 0;
        Map<Size, Double> sizesAndPrices = getSizeAndPriceOrderedForOrderAwaitingDelivery();
        for (Double price : sizesAndPrices.values()) {
            orderTotal += price;
        }
        return orderTotal + " BGN";
    }

    public String calculateCartTotal() {
        double orderTotal = 0;
        Map<Size, Double> sizesAndPrices = getSizeAndPriceOrderedForCart();
        for (Double price : sizesAndPrices.values()) {
            orderTotal += price;
        }
        return orderTotal + " BGN";
    }


    public Map<Size, Double> getSizeAndPriceOrderedForCart() {
        try {
            return orderDAO.getOrderedSizesAndPrices(getCartOrderIdByCustomerId(customer.getId()));
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with getting ordered products' details");
        }
        return Collections.emptyMap();
    }

    public Map<Size, Double> getSizeAndPriceOrderedForOrderAwaitingDelivery() {
        try {
            return orderDAO.getOrderedSizesAndPrices(getCurrentlyDeliveringOrderIdByCustomerId(customer.getId()));
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with getting ordered products' details");
        }
        return Collections.emptyMap();
    }

    public List<Product> getAllProductsInCurrentlyDeliveringOrder() {
        try {
            return new ArrayList<>(
                    orderDAO.getAllProductsInOrder
                            (getCurrentlyDeliveringOrderIdByCustomerId(customer.getId()))
            );
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't get all product in the order cart!");
        }
        return Collections.emptyList();
    }

    public List<Product> getAllProductsInCart() {
        try {
            return new ArrayList<>(
                    orderDAO.getAllProductsInOrder
                            (getCartOrderIdByCustomerId(customer.getId()))
            );
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't get all product in the order cart!");
        }
        return Collections.emptyList();
    }

    public void markOrderAsReceived() {
        try {
            if (isUserCurrentlyWaitingDelivery()) {
                orderDAO.update(getCurrentlyDeliveringOrderIdByCustomerId(customer.getId()), 4, LocalDateTime.now());

            } else {
                LOGGER.error("You are not expecting delivery!");
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't update order status!");
        }

    }

    public void markOrderAsFinalized() {
        try {
            if (!isOrderFinalized()) {
                orderDAO.update(getCartOrderIdByCustomerId(customer.getId()), 3, LocalDateTime.now());
            } else {
                LOGGER.error("You do not have an order to finalize!");
            }
        } catch (SQLException e) {
            LOGGER.error("Couldn't update order status!");
        }

    }

    private int getCurrentlyDeliveringOrderIdByCustomerId(int customerId) {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == customerId && order.getOrderedAt().isPresent() && order.getDeliveredAt().isEmpty()) {
                    return order.getId();
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't find order!");
        }
        return 0;
    }

    private int getCartOrderIdByCustomerId(int customerId) {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == customerId && order.getOrderedAt().isEmpty()) {
                    return order.getId();
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't find order!");
        }
        return 0;
    }

    private Product getProductById(int productId) {
        try {
            for (Pizza pizza : pizzaDAO.readAll().values()) {
                if (pizza.getId() == productId) {
                    return pizza;
                }
            }

            for (Drink drink : drinkDAO.readAll().values()) {
                if (drink.getId() == productId) {
                    return drink;
                }
            }

            for (Dessert dessert : dessertDAO.readAll().values()) {
                if (dessert.getId() == productId) {
                    return dessert;
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("No product with such id exists.");
        }
        return null;
    }

    private boolean isUserCurrentlyWaitingDelivery() {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == (customer.getId()) && order.getOrderedAt().isPresent() && order.getDeliveredAt().isEmpty()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't load order details!");
        }
        return false;
    }

    private boolean isOrderFinalized() {
        try {
            for (Order order : orderDAO.readAll().values()) {
                if (order.getCustomerId() == (customer.getId()) && order.getOrderedAt().isEmpty()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Couldn't load order details!");
        }
        return true;
    }


}


