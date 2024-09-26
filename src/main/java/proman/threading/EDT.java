package proman.threading;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class EDT {

    public static void invokeLater(Runnable runnable) {
        EventQueue.invokeLater(runnable);
    }

    public static void invokeAndWait(Runnable runnable) throws InterruptedException, InvocationTargetException {
        EventQueue.invokeAndWait(runnable);
    }

    public static boolean isEDT(){
        return EventQueue.isDispatchThread();
    }

}
