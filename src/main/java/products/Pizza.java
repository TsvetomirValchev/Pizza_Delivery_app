package products;

import products.ingredient.*;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;


public class Pizza extends Product {

    private final Sauce sauce;
    private final List<Addon> addons;
    private final List<Size> sizes;
    private final List<Cheese> cheeses;
    private final List<Meat> meats;

    public Pizza(Integer id, String name, Double price, Sauce sauce, List<Size> sizes, List<Meat> meats, List<Cheese> cheese, List<Addon> addons) {
        super(id, name, price);
        this.sauce = sauce;
        this.sizes = sizes;
        this.meats = meats;
        this.cheeses = cheese;
        this.addons = addons;
    }

    public List<Size> getSizes() {
        return sizes;
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

        String sizes = getSizes().stream()
                .map(Size::getName)
                .collect(Collectors.joining(", "));

        return "\n№" + getId() + "." +
                getName() + " pizza \ningredients: " +
                ingredientJoiner + "\navailable sizes: " + sizes +
                "\ncosts: " + getPrice() + " BGN";

    }


}

