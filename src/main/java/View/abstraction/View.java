package View.abstraction;

public interface View {

    void printMenu();
    void getChoice();
    void printExceptionMessage(String message);

    default void printSeparator(int length){
        for (int i =0; i<length; i++){
            System.out.print("-");

        }
        System.out.println();
    }

}
