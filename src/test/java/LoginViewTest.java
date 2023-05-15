import View.LoginView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginViewTest {


    @Test
    @DisplayName("should give a login View")
    public void getLoginViewTest(){
        new LoginView().getChoice();
    }


}
