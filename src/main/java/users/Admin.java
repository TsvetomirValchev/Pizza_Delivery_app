package users;

public class Admin extends User {

    public Admin(Integer id, String username, String password, String email) {
        super(id, username, password, email, AccountType.ADMIN);

    }

    @Override
    public String toString() {
        return " | username: " + getUsername() +
                " | e-mail: " + getEmail() +
                " | accountType: " + getAccountType().toString() + "|";
    }
}
