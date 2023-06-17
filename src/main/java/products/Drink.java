package products;

import java.util.Map;

public class Drink extends Product {
    private final boolean isDiet;

    public Drink(Integer id, String name, ProductType productType, Map<Size, Double> sizePriceMap, boolean isDiet) {
        super(id, name, sizePriceMap, productType);
        this.isDiet = isDiet;
    }

    public boolean isDiet() {
        return isDiet;
    }

    @Override
    public String toString() {
        String isDiet = this.isDiet ? "Diet " : "";


        return "№" + getId() + " " + isDiet + getName() +
                "\nPrices: " + buildSizesAndPricesString();
    }

}



