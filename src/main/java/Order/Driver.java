package Order;

public class Driver {


    private final int id;
    private final String f_name;
    private final String l_name;
    private final boolean isFree;

    public Driver(int id, String f_name, String l_name, boolean isFree) {
        this.id = id;
        this.f_name = f_name;
        this.l_name = l_name;
        this.isFree = isFree;
    }

    public int getId() {
        return id;
    }

    public String getF_name() {
        return f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public boolean isFree() {
        return isFree;
    }


    @Override
    public String toString() {
        String status = isFree ? "Free" : "Busy";
        return "Driver:" +
                " " + f_name +
                " " + l_name
                +"is "+ status;
    }
}
