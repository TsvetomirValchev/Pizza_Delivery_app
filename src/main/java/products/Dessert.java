package products;

import java.util.List;

public class Dessert extends Product {
    private final boolean isVegan;

    public Dessert(Integer id, String name, Double price, ProductType productType, List<Size> size, boolean isVegan) {
        super(id, name, price, productType, size);
        this.isVegan = isVegan;
    }

    public boolean isVegan() {
        return isVegan;
    }


    @Override
    public String toString() {
        String vegan = isVegan ? "Vegan " : "";
        return "\n№" + getId() + " " +
                vegan + getName() + "\ncost: " + getPrice() + " BGN";
    }
}
