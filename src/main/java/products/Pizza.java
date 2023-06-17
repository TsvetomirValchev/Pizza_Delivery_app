package products;


import java.util.List;
import java.util.Map;
import java.util.StringJoiner;


public class Pizza extends Product {
    private final List<Ingredient> ingredients;

    public Pizza(Integer id, String name, ProductType productType, Map<Size, Double> sizePriceMap, List<Ingredient> ingredients) {
        super(id, name, sizePriceMap, productType);
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }


    @Override
    public String toString() {
        StringJoiner ingredientJoiner = new StringJoiner(", ");
        for (Ingredient ingredient : getIngredients()) {
            ingredientJoiner.add(ingredient.getName());
        }
        
        return "№" + getId() + ". " + getName() + " pizza\nIngredients: " + ingredientJoiner +
                "\nAvailable sizes and prices:\n" + buildSizesAndPricesString();
    }

}


