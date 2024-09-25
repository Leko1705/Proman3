package proman.utils;

import proman.threading.BGT;
import proman.threading.EDT;

public class Assertions {


    public static void assertRange(int value, int min, int max) {
        if (value < min || value >= max)
            throw new IndexOutOfBoundsException("value out of bounds for range [" + min + "," + max + ")");
    }

    public static void assertEDT(String msg){
        if (!EDT.isEDT()){
            throw new AssertionError(msg);
        }
    }

    public static void assertBGT(String msg){
        if (!BGT.isBGT()){
            throw new AssertionError(msg);
        }
    }

}
