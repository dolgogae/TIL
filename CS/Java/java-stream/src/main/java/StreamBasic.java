import java.util.function.BiFunction;
import java.util.function.Function;

import utils.*;

public class StreamBasic{
    public static void main(String[] args){
        Function<Integer, Integer> myAdder = new Adder();

        // input 파라미터 타입 유추 가능시에 생략가능
        // input 파너터가 하나일 때 괄호 사용가능
        // 바로 리턴하는 경우 중괄호 생략가능
        Function<Integer, Integer> myAdderLambda = (Integer x) -> {
            return x + 10;
        };
        Function<Integer, Integer> myAdderLambdaTemp = x -> x + 10;

        BiFunction<Integer, Integer, Integer> add = (x,y) -> x+y;

        Integer result = myAdder.apply(5);
        System.out.println(result);

        TriFunction<Integer, Integer, Integer, Integer> triAdd = (x, y, z) -> x+y+z;
        Integer triResult = triAdd.apply(2, 3, 4);
        System.out.println(triResult);
    }
}