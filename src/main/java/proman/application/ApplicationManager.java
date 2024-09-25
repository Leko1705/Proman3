package proman.application;

public class ApplicationManager {

    public static String getApplicationName() {
        return "Proman";
    }


    public static Application getApplication() {
        return new NoApplication();
    }

}
