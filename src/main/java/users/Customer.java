package users;

public class Customer extends User{
    private final Integer id;
    private final String email;
    private final String address;

    public Customer(String username, String password,Integer id,String email,String address) {
        super(username, password);
        this.id = id;
        this.email = email;
        this.address = address;

    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }


    @Override
    public String toString() {
        return   " | username: "+ getUsername()
                +" | e-mail: " + getEmail()
                +" | delivery address: "+ getAddress()+" |";
    }
}
