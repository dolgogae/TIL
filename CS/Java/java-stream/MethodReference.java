import java.util.function.Function;
import java.util.function.Predicate;

public class MethodReference {
    
    public static void main(String[] args){
        int a = Integer.parseInt("15");
        Function<String, Integer> str2int = Integer::parseInt;
        System.out.println(str2int.apply("20"));

        String str = "hello";
        str.equals("world");
        Predicate<String> equalsToHello = str::equals;
        equalsToHello.test("world");

        
    }
}
