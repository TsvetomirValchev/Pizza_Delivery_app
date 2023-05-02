package Products;

public class Drink extends Product{
private final boolean isCarbonated;

    public Drink(Integer id, String name, Double price, boolean isCarbonated) {
        super(id, name, price);
        this.isCarbonated = isCarbonated;
    }

    public boolean isCarbonated() {
        return isCarbonated;
    }

    @Override
    public String toString() {
        String carbonated = isCarbonated ? "Carbonated" : "Not carbonated";
        return "Your" + carbonated + getName()+ "costs:"+getPrice();
    }
}
