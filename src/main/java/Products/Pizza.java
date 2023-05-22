package Products;

import Products.PizzaIngredient.*;

public class Pizza extends Product{

private final Size size;
private final Cheese cheese;
private final Meat meat;
private final Sauce sauce;
private final Addon addon;


    public Pizza(Integer id, String name, Double price,Size size, Cheese cheese, Meat meat, Sauce sauce, Addon addon) {
        super(id, name, price);
        this.size = size;
        this.cheese = cheese;
        this.meat = meat;
        this.sauce = sauce;
        this.addon = addon;
    }

    public Size getSize() {
        return size;
    }

    public Cheese getCheese() {
        return cheese;
    }

    public Meat getMeat() {
        return meat;
    }

    public Sauce getSauce() {
        return sauce;
    }

    public Addon getAddon() {
        return addon;
    }


    @Override
    public String toString() {
        return  getId()+"."+
                getName() +" pizza \ningredients:" +" "+
                getSauce().getName()+","+
                getCheese().getName()+","+
                getMeat().getName()+","+
                getAddon().getName()+","+
                "\ncosts:" +getPrice()+" BGN";
    }
}

