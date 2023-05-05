package View.abstraction;

public interface View {

    void printMenu();
    void getChoice();
    void printExceptionMsg(String msg);

    default void printSeparator(int length){
        for (int i =0; i<length; i++){
            System.out.println("-");

        }
        System.out.println();
    }

}
