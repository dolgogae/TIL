import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.plaf.synth.SynthUI;

import model.Order;
import model.OrderLine;
import priceProcessor.OrderLineAggregationPriceProcessor;
import priceProcessor.TaxPriceProcessor;

public class AdvancedFunctionalProgramming {
    public static void main(String[] args){
        /**
         * Scope
         * 변수에 접근할 수 있는 범위
         * 함수안에 함수가 있을 때 내부 함수에서 외부 함수에 있는 변수에 접근이 가능
         * Closure : 내부 함수가 존재하는 한 내부 함수가 사용한 변수도 사용이 가능하다.
         * Curry: 여러개의 매개변수를 받는 함수를 중첩된 여러 개의 함수로 쪼개어 매개 변수를 한번에 받지 않고 여러 단계에 걸쳐 나눠받을 수 있게 하는 기술
         */

        // hello변수는 필요상 함수가 끝나도 존재해야 한다.
        Supplier<String> supplier = getStringSupplier();
        // System.out.println(supplier.get());

        BiFunction<Integer, Integer, Integer> add = (x, y) -> x+y;
        Function<Integer, Function<Integer, Integer>> curriedAdd = x -> y -> x+y;

        // 3이라는 값을 항상 기억하고 있어서 3을 더해주는 function을 만들 수 있다.
        Function<Integer, Integer> addThree = curriedAdd.apply(3);

        /**
         * Lazy Evaluation
         * Lambda의 계산은 그 결과값이 필요할 때가 되어서야 계산된다.
         * 불필요한 계산을 줄이거나 해당 코드의 실행순서를 의도적으로 나눌 수 있다.
         */

        // returnFalse는 실행되지 않는다.
        if(returnTrue() || returnFalse()){
            System.out.println("true");
        }
        // 모든 함수를 호출하게 된다. 하지만 둘 중 하나만 해도 충분하다.
        if(or(returnTrue(), returnFalse())){
            System.out.println("true");
        }
        // 다음처럼 하면 불필요한 호출이 줄어들게 된다.
        if(lazyOr(()->returnTrue(), ()->returnFalse())){
            System.out.println("true");
        }

        // stream은 종결처리(collect)가 이루어지기 전까지 lazy evaluation을 하게 된다.
        // 미루고 미뤄 필요할 때 계산을 하게 된다.
        Stream<Integer> integerStream = Stream.of(3,-2,5,8,-3,10)
            .filter(x -> x>0)
            .peek(x -> System.out.println("peeking " + x))
            .filter(x -> x%2 == 0);
        System.out.println("Before collect");

        List<Integer> integers = integerStream.collect(Collectors.toList());
        System.out.println("After collect: " + integers);

        /**
         * Function Composition
         * 여러개의 함수를 하나의 새로운 함수로 만드는 것.
         */
        Function<Integer, Integer> multiplyByTwo = x -> x*2;
        Function<Integer, Integer> addTen = x -> x+10;

        Function<Integer, Integer> composedFunction = multiplyByTwo.andThen(addTen);
        System.out.println(composedFunction.apply(3));

        Order unprocessedOrder = new Order()
                .setId(1001L)
                .setOrderLines(Arrays.asList(
                    new OrderLine().setAmount(BigDecimal.valueOf(1000)),
                    new OrderLine().setAmount(BigDecimal.valueOf(2000))
                ));
        List<Function<Order, Order>> priceProcessors = getPriceProcessor(unprocessedOrder);
        
        Function<Order, Order> mergePriceProcessors = priceProcessors.stream()
                .reduce(Function.identity(), Function::andThen);

        Order processedOrder = mergePriceProcessors.apply(unprocessedOrder);
        System.out.println(processedOrder);
    }

    public static List<Function<Order, Order>> getPriceProcessor(Order order){
        return Arrays.asList(new OrderLineAggregationPriceProcessor(), 
                            new TaxPriceProcessor(new BigDecimal("9.375")));
    }

    public static boolean lazyOr(Supplier<Boolean> x, Supplier<Boolean> y){
        return x.get() || y.get();
    }

    public static boolean or(boolean x, boolean y){
        return x || y;
    }

    public static boolean returnTrue(){
        System.out.println("Returning true");
        return true;
    }

    public static boolean returnFalse(){
        System.out.println("Returing false");
        return false;
    }

    public static Supplier<String> getStringSupplier(){
        String hello = "Hello";
        Supplier<String> supplier = () -> {
            String world = "world";
            return hello + world;
        };

        return supplier;
    }
}
