package users;

public class User {

    private final Integer id;
    private final String username;
    private final String password;
    private final String email;
    private final AccountType accountType;

    public User(Integer id, String username, String password, String email, AccountType accountType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return " | username: " + getUsername() +
                " | e-mail: " + getEmail() +
                " | accountType: " + getAccountType().toString() + "|";
    }
}
