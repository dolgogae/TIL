import model.Order;
import model.OrderLine;
import model.User;
import model.Order.OrderStatus;
import service.EmailService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// import static model.Order.*;

public class AdvancedStream {
    public static void main(String[] args) {
        Optional<Integer> max = Stream.of(5, 4, 6, 1, 2).max((x, y) -> x - y);
//        System.out.println(max.get());

        User user1 = new User()
                .setId(1001)
                .setName("Alice")
                .setEmail("alice@gmail.com")
                .setFriendsIds(Arrays.asList(2001, 2002, 2003, 2004))
                .setVerified(false);
        User user2 = new User()
                .setId(1002)
                .setName("Bob")
                .setEmail("bob@gmail.com")
                .setVerified(false)
                .setFriendsIds(Arrays.asList(2004, 2005, 2006));
        User user3 = new User()
                .setId(1003)
                .setName("Charlie")
                .setEmail("charlie@gmail.com")
                .setVerified(false)
                .setFriendsIds(Arrays.asList(2004, 2005, 2007));

        List<User> users = Arrays.asList(user1, user2, user3);
        User minUser = users.stream()
                .min((u1, u2) -> u1.getName().compareTo(u2.getName()))
                .get();
//        System.out.println(user);

        long positiveIntegerCount = Stream.of(1, -4, 5, -3, 6)
                .filter(x -> x > 0)
                .count();
//        System.out.println("Positive integers: " + positiveIntegerCount);

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        user1.setCreatedAt(now.minusDays(2));
        user2.setCreatedAt(now.minusHours(10));
        user3.setCreatedAt(now.minusHours(1));

        User user4 = new User()
                .setId(1004)
                .setName("David")
                .setEmail("david@gmail.com")
                .setCreatedAt(now.minusHours(27))
                .setVerified(true);
        List<User> userList = Arrays.asList(user1, user2, user3, user4);
        long unverifiedUsersIn24Hours = userList.stream()
                .filter(user -> user.getCreatedAt().isAfter(now.minusDays(1)))
                .filter(user -> !user.isVerified())
                .count();
        // System.out.println(unverifiedUsersIn24Hours);

        Order order1 = new Order()
                .setId(101)
                .setAmount(BigDecimal.valueOf(2000))
                .setStatus(OrderStatus.CREATED)
                .setOrderLines(Arrays.asList(
                        new OrderLine().setAmount(BigDecimal.valueOf(2000)),
                        new OrderLine().setAmount(BigDecimal.valueOf(3000))
                ));
        Order order2 = new Order()
                .setId(102)
                .setAmount(BigDecimal.valueOf(4000))
                .setStatus(OrderStatus.ERROR)
                .setOrderLines(Arrays.asList(
                        new OrderLine().setAmount(BigDecimal.valueOf(2000)),
                        new OrderLine().setAmount(BigDecimal.valueOf(3000))
                ));
        Order order3 = new Order()
                .setId(103)
                .setAmount(BigDecimal.valueOf(3000))
                .setStatus(OrderStatus.ERROR)
                .setOrderLines(Arrays.asList(
                        new OrderLine().setAmount(BigDecimal.valueOf(2000)),
                        new OrderLine().setAmount(BigDecimal.valueOf(3000))
                ));
        Order order4 = new Order()
                .setId(104)
                .setAmount(BigDecimal.valueOf(7000))
                .setStatus(OrderStatus.PROCESSED);
        List<Order> orders = Arrays.asList(order1, order2, order3, order4);

        // TODO: find order with highest amount in ERROR status
        BigDecimal maxErroredAmount = orders.stream()
                .filter(order -> order.getStatus() == OrderStatus.ERROR)
                .map(Order::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        // System.out.println(maxErroredAmount);

        /**
         * allMatch: 조건식에 모두 만족하는 것만 있을때 true 반환
         */
        List<Integer> numbers = Arrays.asList(3,-4,2,7,9);
        boolean allPositive = numbers.stream()
                .allMatch(number -> number > 0);
        // System.out.println("Are all numbers positive: " + allPositive);

        boolean allUserVerified = users.stream()
                .allMatch(User::isVerified);
        // System.out.println("Are all users verified: " + allUserVerified);

        // TODO: check if any order is in ERROR status
        boolean anyOrderInError = orders.stream()
                .anyMatch(o -> o.getStatus() == OrderStatus.ERROR);

        /**
         * findFirst: Stream이 비어있다면 비어있는 Optional 반환
         * findAny: 아무거나 하나
         */
        Optional<Integer> anyNegativeInteger = numbers.stream()
                .filter(n -> n < 0)
                .findAny();
        // System.out.print(anyNegativeInteger);

        Optional<Integer> firstNegativeInteger = numbers.stream()
                .filter(n -> n > 0)
                .findFirst();
        // System.out.println(firstNegativeInteger);

        /**
         * reduce
         * 1. 주어진 accumulator를 이용해 데이터를 합친다. stream이 비어있을 경우 빈 Optional을 반환
         * 2. 주어진 초기값과 accumulator를 이용.
         * 3. 합치는 과정에서 타입이 바뀔 경우 사용. Map+reduce로 대체 가능
         */
        int sum = numbers.stream()
                .reduce((x, y) -> x+y)
                .get();
        // System.out.println(sum);

        int min = numbers.stream()
                .reduce((x, y) -> x>y?x:y)
                .get();
        
        // 초기값이 있기 때문에 Optional을 반환하지 않는다.
        int product = numbers.stream()
                .reduce(1, (x, y)->x*y);
        // System.out.println(product);

        List<String> numberStrList = Arrays.asList("1", "2", "3", "-4");
        int sumOfNumberStrList = numberStrList.stream()
                .map(Integer::parseInt)
                .reduce(0, (x, y)->x+y);
        int sumOfNumberStrList2 = numberStrList.stream()
                .reduce(0, (number, str) -> number + Integer.parseInt(str), (num1, num2) -> num1 + num2);
        // System.out.println(sumOfNumberStrList + " " + sumOfNumberStrList2);

        int sumOfNumberOfFriends = users.stream()
                .map(User::getFriendsIds)
                .map(List::size)
                .reduce(0, (x, y) -> x+y);
        
        // TODO: find the sum of amounts
        List<Order> orderList = Arrays.asList(order1, order2, order3);
        BigDecimal sumOfAmount = orderList.stream()
                .map(o -> o.getOrderLines())
                .flatMap(List::stream)
                .map(OrderLine::getAmount)
                .reduce(BigDecimal.ZERO, (x,y)->x.add(y));
        
        List<Integer> numberList = Stream.of(3,4,5,7,-3,-2)
                .collect(Collectors.toList());
        Set<Integer> numberSet = Stream.of(3,4,5,7,-3,-2)
                .collect(Collectors.toSet());
        List<Integer> numberList2 = Stream.of(3,4,5,7,-3,-2)
                .collect(Collectors.mapping(x -> Math.abs(x), Collectors.toList()));
        
        int reducingSum = Stream.of(3,4,5,7,-3,-2)
                .collect(Collectors.reducing(0, (x, y) -> x+y));
        
        /**
         * toMap
         * keyMapper: 데이터를 map의 key로 변환
         * valueMapper: 데이터를 map의 value로 변환
         * id - object로 맵핑할 때 유용하다.
         */

         // TODO: stream의 integer가 key가 되고, "number of integer : {}"가 value가 되도록
        Map<Integer, String> numberMap = Stream.of(3,4,5,7,-3,-2)
                .collect(Collectors.toMap(Function.identity(), x -> "Number of Integer: " + x));
        // System.out.println(numberMap.get(3));

        Map<Integer, User> userIdToUserMap = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        // System.out.println(userIdToUserMap);

        // TODO: create a map from order id to order status
        Map<Long, OrderStatus> orderIdToOrderStatusMap = orders.stream()
                .collect(Collectors.toMap(Order::getId, Order::getStatus));
        
        /**
         * groupingBy
         * Stream안의 데이터에 classifier를 적용했을 때 결과값이 같은 값끼리 List로 모아서 Map의 형태로 반환
         * 이때 key는 classifier의 결과갑스. value는 해당되는 값들
         */
        numbers = Arrays.asList(12,3,101,203,402,342,346,677,677);
        Map<Integer, List<Integer>> unitDigitMap = numbers.stream()
                .collect(Collectors.groupingBy(number -> number%10));
        
        Map<Integer, Set<Integer>> unitedSet = numbers.stream()
                .collect(Collectors.groupingBy(number->number%10, Collectors.toSet()));
        
        Map<Integer, List<String>> unitDigitString = numbers.stream()
                .collect(Collectors.groupingBy(number->number%10, 
                        Collectors.mapping(number -> "unit digit is " + number, Collectors.toList())));
        
        // TODO: create a map form order status to the list of corresponding orders
        Map<OrderStatus, List<Order>> orderStatusMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus));
        
        Map<OrderStatus, BigDecimal> orderStatusToSumOfAmountMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getStatus, 
                        Collectors.mapping(Order::getAmount, 
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

        /**
         * partitioning
         * true인 그룹과 false인 그룹으로 나뉜다.
         */
        Map<Boolean, List<Integer>> numberPartitions = numbers.stream()
                .collect(Collectors.partitioningBy(x->x%2==0));
        // System.out.println("Even numbers: "+ numberPartitions.get(true));
        // System.out.println("Even numbers: "+ numberPartitions.get(false));

        Map<Boolean, List<User>> userPartitions = users.stream()
                .collect(Collectors.partitioningBy(user -> user.getFriendsIds().size() > 5));
        // System.out.println(userPartitions);

        EmailService emailService = new EmailService();
        for(User user: userPartitions.get(true)){
            emailService.sendPlayWithFriendsEmail(user);
        }
        for(User user: userPartitions.get(false)){
            emailService.sendMakeWithFriendsEmail(user);
        }

        /**
         * forEach
         * 제공된 action을 Stream의 각 데이터에 적용해주는 종결처리 메서드
         */
        numbers.stream().forEach(number-> System.out.println("The number is "+ number));
        // iterable에도 forEach가 있어서 생략가능하다.
        numbers.forEach(number-> System.out.println("The number is "+ number));
        
        users.stream()
                .filter(user -> !user.isVerified())
                .forEach(emailService::sendVerifyYourEmail);
        
        // 인덱스 사용 반복문은 IntStream을 사용할 수 있다.
        IntStream.range(0, users.size()).forEach(i->{
            User user = users.get(i);
            System.out.println("Do an operation user " + user.getName() + "at index " + i);
        });

        /**
         * parallelStream
         * 여러개의 스레드를 이용하여 stream의 처리 과정을 병렬화
         * 데드락을 조심해야하고 뮤텍스나 세마포어를 사용하면 순차처리보다 느려질 수 있다.
         */
        long startTime = System.currentTimeMillis();
        users.stream()
                .filter(user -> !user.isVerified())
                .forEach(emailService::sendVerifyYourEmail);
        long endTime = System.currentTimeMillis();
        // System.out.println("Sequential: " + (endTime - startTime) + "ms");

        long startTimeParallel = System.currentTimeMillis();
        users.stream().parallel()
                .filter(user -> !user.isVerified())
                .forEach(emailService::sendVerifyYourEmail);
        long endTimeParallel = System.currentTimeMillis();
        // System.out.println("Sequential: " + (endTimeParallel - startTimeParallel) + "ms");
        
        // 각 리스트의 인스턴스가 어떤 순서로 처리될지 모른다.
        // 순서에 민감한 로직이라면 parallel을 사용하면 안된다.
        List<User> processedUsers = users.parallelStream()
                .map(user -> {
                    System.out.println("Capitalize user name for user "+ user.getId());
                    user.setName(user.getName().toUpperCase());
                    return user;
                })
                .map(user -> {
                    System.out.println("Set isVerified to true for user "+ user.getId());
                    user.setVerified(true);
                    return user;
                })
                .collect(Collectors.toList());
        
    }
}
