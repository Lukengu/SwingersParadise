package swingersparadise.app.solutions.novatech.pro.swingersparadise.utils;

public class ArrayUtils {

    public static int getPosition(String s, String[] a) {

        for( int i = 0; i < a.length; i++) {

            if(a[i].equals(s)){
                return i;
            }
        }
        return 0;
    }

}
