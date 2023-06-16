package products;

public class Ingredient {
    private final Integer id;
    private final String name;


    private final IngredientType ingredientType;

    public Ingredient(Integer id, String name, IngredientType ingredientType) {
        this.id = id;
        this.name = name;
        this.ingredientType = ingredientType;
    }


    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public IngredientType getIngredientType() {
        return ingredientType;
    }


    @Override
    public String toString() {
        return "id:" + getId() + " " + (getName() != null ? getName() : "none");
    }

}
