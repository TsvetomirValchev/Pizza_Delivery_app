package products;

public class IngredientType {
    private final Integer id;
    private final String name;

    public IngredientType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }


}
