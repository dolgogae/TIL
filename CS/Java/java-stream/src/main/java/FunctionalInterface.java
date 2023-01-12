import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import model.User;

public class FunctionalInterface {
    public static void main(String args[]){

        /**
         * Supplier
         * return 값만 있을 때 쓰인다
         * */ 
        Supplier<String> myStringSupplier = () -> "hello stream";
        // System.out.println(myStringSupplier.get());

        Supplier<Double> myRandomSupplier = Math::random;
        // System.out.println(myRandomSupplier.get());

        /**
         * Consumer
         * return 값이 없을때 쓰인다.
         */
        Consumer<String> myStringConsumer = str -> {
            System.out.println("myString: " + str);
        };
        // myStringConsumer.accept("hello");
        List<Integer> integerInputs = Arrays.asList(1,2,3,4,5);
        Consumer<Integer> myIntegerProcess = x -> System.out.println("Processing Integer: " + x);
        process(myIntegerProcess, integerInputs);

        /**
         * Predicate
         * 비교식의 결과를 리턴하는 stream이다.
         */
        Predicate<Integer> isPositive = x -> x > 0;
        // System.out.println(isPositive.test(10));

        predicateInteger(integerInputs, isPositive);

        /**
         * Comparator
         */
        User user1 = new User(1, "Alice");
        User user2 = new User(2, "Charlie");
        User user3 = new User(3, "Bob");
        List<User> users = Arrays.asList(user1,user2,user3);
        Comparator<User> idComparator = (u1, u2) -> u1.getId() - u2.getId();
        Collections.sort(users, idComparator);
        System.out.println(users);
    }

    private static List<Integer> predicateInteger(List<Integer> integerInputs, Predicate<Integer> isPositive) {

        List<Integer> ret = new ArrayList<>();
        for(Integer input: integerInputs){
            if(isPositive.test(input)){
                ret.add(input);
            }
        }
        return ret;
    }

    private static void process(Consumer<Integer> myIntegerProcess, List<Integer> integerInputs) {

        for(Integer input: integerInputs){
            myIntegerProcess.accept(input);
        }
    }

}