package products;

import java.util.List;

public class Product {

    private final Integer id;
    private final String name;
    private final Double price;
    private final ProductType productType;
    private final List<Size> availableSizes;

    public Product(Integer id, String name, Double price, ProductType productType, List<Size> availableSizes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.productType = productType;
        this.availableSizes = availableSizes;
    }

    //
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public List<Size> getAvailableSizes() {
        return availableSizes;
    }


    @Override
    public String toString() {
        return "№" + getId() + "." +
                getName() + " costs:" + getPrice();
    }
}
