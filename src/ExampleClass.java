/**
 * Created by Przemohawryl on 12.05.2017.
 */
public final class ExampleClass {
    public static void main(String[] args) {
        String upper = upper("Hawryluk, Przemyslaw");
        System.out.println(upper + " = " + countMultipler(upper, upper.length()));
    }

    private static String upper(String s) {
        log("upper", 2.1, 5);
        String aux = s.toUpperCase();
        return aux;
    }

    public static int countMultipler(String s, int i) {
        int length = s.length() * i;
        return length;
    }

    private static void log(String str, double d, int i) {
        System.out.println("Double is " + d + " and integer is " + i + "\nBut string is " + str);
    }
}
