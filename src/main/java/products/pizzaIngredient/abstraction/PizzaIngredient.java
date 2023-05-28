package products.pizzaIngredient.abstraction;

public class PizzaIngredient { //ask about how to make it abstract
        final Integer id;
        final String name;

        public PizzaIngredient(Integer id, String name) {
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