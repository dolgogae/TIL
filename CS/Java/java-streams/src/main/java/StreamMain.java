import model.User;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamMain {
    
    public static void main(String[] arg){
        Stream<String> nameStream = Stream.of("Alice", "Bob", "Chalie");
        List<String> names = nameStream.collect(Collectors.toList());
        // System.out.println(names);

        String[] cityArray = new String[] {"San Jose", "Seoul", "Tokyo"};
        Stream<String> cityStream = Arrays.stream(cityArray);
        List<String> cityList = cityStream.collect(Collectors.toList());
        // System.out.println(cityList);

        Stream<Integer> numberStream = Stream.of(1,2,-3, -4);
        Stream<Integer> filteredNumberStream = numberStream.filter(x -> x>0);
        List<Integer> numberList = filteredNumberStream.collect(Collectors.toList());
        // numberStream.filter(x->x>0).collect(Collectors.toList());

        // map: 기존의 값을 간단한 수식으로 변경
        // filter: 조건에 맞는 값을 추출
        // sorted: 정렬
        Stream<Integer> numberStream2 = Stream.of(1, 2, 4, -2, -5);
        numberStream2.filter(x->x>0).map(x->x*2).collect(Collectors.toList());

        User user1 = new User(1, "Alice");
        User user2 = new User(2, "Bob");
        List<User> userList = Arrays.asList(user1, user2);
        List<User> sortedUserList = userList.stream()
            .sorted(Comparator.comparing(User::getName))
            .collect(Collectors.toList());
    }
}
