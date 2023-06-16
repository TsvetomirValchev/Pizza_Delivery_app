package products;


import java.util.List;


public class Pizza extends Product {


    List<Ingredient> ingredients;


    public Pizza(Integer id, String name, Double price, ProductType productType, List<Size> size, List<Ingredient> ingredients) {
        super(id, name, price, productType, size);
        this.ingredients = ingredients;
    }

    //
    public List<Ingredient> getIngredients() {
        return ingredients;
    }


    @Override
    public String toString() {


        return "\n№" + getId() + "." +
                getName() + " pizza \ningredients: " +
                getIngredients() + "\navailable sizes: " +
                "\ncosts: " + getPrice() + " BGN";

    }


}

