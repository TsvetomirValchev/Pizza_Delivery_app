package products;

import products.ingredient.*;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pizza extends Product {
    private final Size size;
    private final List<Cheese> cheeses;
    private final List<Meat> meats;
    private final Sauce sauce;
    private final List<Addon> addons;

    public Pizza(Integer id, String name, Double price, Size size, Sauce sauce, List<Meat> meats, List<Cheese> cheese, List<Addon> addons) {
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
        StringJoiner ingredientJoiner = new StringJoiner(",");

        ingredientJoiner.add(getSauce().getName());

        getMeats().stream()
                .map(Meat::getName)
                .filter(Objects::nonNull)
                .forEach(ingredientJoiner::add);

        getCheeses().stream()
                .map(Cheese::getName)
                .filter(Objects::nonNull)
                .forEach(ingredientJoiner::add);

        getAddons().stream()
                .map(Addon::getName)
                .filter(Objects::nonNull)
                .forEach(ingredientJoiner::add);

        return "\n№" + getId() + "." +
                getName() + " pizza \ningredients: " +
                ingredientJoiner +
                "\ncosts: " + getPrice() + " BGN";

    }


}

