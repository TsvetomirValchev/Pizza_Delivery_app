package View.abstraction;

public interface View {
    void printMenu();

    void getChoice();

    default void printSeparator(int length) {
        for (int i = 0; i <= length; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
