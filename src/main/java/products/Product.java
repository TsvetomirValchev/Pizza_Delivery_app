package products;

public class Product {

    private final Integer id;
    private final String name;
    private final Double price;

    public Product(Integer id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return  "№"+getId()+"."+
                getName() + " costs:" + getPrice();
    }
}
