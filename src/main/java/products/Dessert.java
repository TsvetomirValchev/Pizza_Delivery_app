package products;

import java.util.Map;

public class Dessert extends Product {
    private final boolean isVegan;

    public Dessert(Integer id, String name, ProductType productType, Map<Size, Double> sizePriceMap, boolean isVegan) {
        super(id, name, sizePriceMap, productType);
        this.isVegan = isVegan;
    }

    public boolean isVegan() {
        return isVegan;
    }

    @Override
    public String toString() {
        String vegan = isVegan ? "Vegan " : "";


        return "№" + getId() + " " + vegan + getName() +
                "\nAvailable sizes and prices:\n" + buildSizesAndPricesString();
    }

}



