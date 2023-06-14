package users;

public class Customer extends User {

    public Customer(Integer id, String username, String password, String email) {
        super(id, username, password, email, AccountType.CUSTOMER);
    }

    public Customer(User user) {
        super(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), AccountType.CUSTOMER);
    }

    @Override
    public String toString() {
        return " | username: " + getUsername() +
                " | e-mail: " + getEmail() +
                " | accountType: " + getAccountType().toString() + "|";
    }
}
