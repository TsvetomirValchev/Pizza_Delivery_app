import View.LoginView;


public class Main {
    public static void main(String[] args) {
        System.setProperty("log4j2.debug", "true");
        new LoginView().getChoice();

    }
}
