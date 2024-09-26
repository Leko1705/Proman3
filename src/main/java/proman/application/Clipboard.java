package proman.application;

public class Clipboard {

    private static Object inClipboard;

    public static void copy(Object obj) {
        inClipboard = obj;
    }

    public static Object get() {
        return inClipboard;
    }

    public static <T> T get(Class<T> clazz) {
        if (inClipboard.getClass().isAssignableFrom(clazz))
            return safeCast(inClipboard);
        return null;
    }

    public static void clear() {
        inClipboard = null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T safeCast(Object obj){
        return (T) obj;
    }

}
