package products.ingredient.abstraction;

public class Ingredient { //how do I make this abstract?
        final Integer id;
        final String name;
        public Ingredient(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
        public Integer getId(){
            return id;
        };
        public String getName(){
            return name;
        };
        @Override
        public String toString() {
            return "id:"+getId()+" "+getName();
        }

}
