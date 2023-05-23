package Products;

public class Dessert extends Product{
    private final boolean isVegan;

    public Dessert(Integer id, String name, Double price, boolean isVegan) {
        super(id, name, price);
        this.isVegan = isVegan;
    }

    public boolean isVegan() {
        return isVegan;
    }


    @Override
    public String toString() {
        String vegan = isVegan ? "Vegan " : "";
        return   "№"+getId()+"."+
                vegan + getName()+ " cost: "+getPrice();
    }
}
