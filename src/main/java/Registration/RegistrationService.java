package Registration;

import db.AdminService;
import db.CustomerDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import users.Customer;

import java.sql.SQLException;


public class RegistrationService {

    private static final Logger LOGGER = LogManager.getLogger(AdminService.class.getName());
    private final Customer customer;
    CustomerDAO customerDAO = new CustomerDAO();

    public RegistrationService(Customer customer) {
        this.customer = customer;

    }

    public void signUp() {
        UserAccountValidator Validator = new UserAccountValidator(customer);
        try {
            if (!Validator.DoesUserAccountAlreadyExist()) {
                Validator.isValidUser();
                createAccount();
            }

        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage());
        }

    }


    private void createAccount() {
        try {
            customerDAO.insert(customer);
        } catch (SQLException e) {
            LOGGER.debug(e.getMessage());
            LOGGER.error("Something went wrong with making an account");
        }

    }

}
