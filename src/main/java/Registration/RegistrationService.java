package Registration;

import db.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import users.Customer;


public class RegistrationService {

    private static final Logger LOGGER = LogManager.getLogger(AdminService.class.getName());
    private final Customer customer;

    public RegistrationService(Customer customer) {
        this.customer = customer;
    }

    public void signUp() {
        UserAccountValidator Validator = new UserAccountValidator(customer);
        try {
            if (!Validator.DoesUserAccountAlreadyExist()) {
                Validator.isValidUser();
                new AdminService().addCustomer(customer);
            }

        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage());
        }

    }

}
