package proman.application;

public interface Application {

    default void boot(){
        reboot();
    }

    void reboot();

    void exit(boolean askIfSure);

    default void exit(){
        exit(true);
    }
}
