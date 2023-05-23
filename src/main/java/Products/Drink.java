package Products;

public class Drink extends Product{
private final boolean isDiet;

    public Drink(Integer id, String name, Double price, boolean isCarbonated) {
        super(id, name, price);
        this.isDiet = isCarbonated;
    }

    public boolean isDiet() {
        return isDiet;
    }

    @Override
    public String toString() {
        String isDiet = this.isDiet ? "Diet " : "";
        return  "№"+getId()+"."+
                isDiet + getName()+ " cost: "+getPrice();
    }
}
