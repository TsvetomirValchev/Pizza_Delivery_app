package products;

import java.util.*;

public class Product {
    private final Integer id;
    private final String name;
    private final ProductType productType;
    private final Map<Size, Double> sizesAndPrices;

    public Product(Integer id, String name, Map<Size, Double> sizesAndPrices, ProductType productType) {
        this.id = id;
        this.name = name;
        this.productType = productType;
        this.sizesAndPrices = sizesAndPrices;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Map<Size, Double> getSizesAndPrices() {
        return sizesAndPrices;
    }

    String buildSizesAndPricesString() {
        StringJoiner sizesAndPricesJoiner = new StringJoiner(", ");
        List<Size> sortedSizes = new ArrayList<>(getSizesAndPrices().keySet());
        sortedSizes.sort(Comparator.comparingInt(Size::getId));

        for (Size size : sortedSizes) {
            Double price = getSizesAndPrices().get(size);
            String sizePrice = size.getName() + ": " + price + " BGN";
            sizesAndPricesJoiner.add(sizePrice);
        }

        return sizesAndPricesJoiner.toString();
    }


}





