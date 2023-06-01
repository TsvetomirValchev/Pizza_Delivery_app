package products;

import products.ingredient.*;

import java.util.List;
import java.util.stream.Collectors;

public class Pizza extends Product{
private final Size size;
private final List<Cheese> cheeses;
private final List<Meat> meats;
private final Sauce sauce;
private final List<Addon> addons;

    public Pizza(Integer id, String name, Double price,Size size,Sauce sauce, List<Meat> meats, List<Cheese> cheese, List<Addon> addons ) {
        super(id, name, price);
        this.size = size;
        this.sauce = sauce;
        this.meats = meats;
        this.cheeses = cheese;
        this.addons = addons;
    }

    public Size getSize() {
        return size;
    }

    public List<Cheese> getCheeses() {
        return cheeses;
    }

    public List<Meat> getMeats() {
        return meats;
    }

    public Sauce getSauce() {
        return sauce;
    }

    public List<Addon> getAddons() {
        return addons;
    }


    @Override
    public String toString() {
        return   "№"+getId()+"."+
                getName() +" pizza \ningredients:" +" "+
                getSauce().getName()+","+
                getMeats().stream().map(Meat::getName).collect(Collectors.joining(","))
                + "," + getCheeses().stream().map(Cheese::getName).collect(Collectors.joining(","))
                + "," + getAddons().stream().map(Addon::getName).collect(Collectors.joining(",")) +
                "\ncosts:" +getPrice()+" BGN";
    }
}

