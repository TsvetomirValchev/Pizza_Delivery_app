package products;

import java.util.List;

public class Drink extends Product {
    private final boolean isDiet;

    public Drink(Integer id, String name, Double price, ProductType productType, List<Size> size, boolean isDiet) {
        super(id, name, price, productType, size);
        this.isDiet = isDiet;
    }

    public boolean isDiet() {
        return isDiet;
    }

    @Override
    public String toString() {
        String isDiet = this.isDiet ? "Diet " : "";
        return "\n№" + getId() + " " + isDiet + getName() + "\ncost: " + getPrice() + " BGN";
    }
}
