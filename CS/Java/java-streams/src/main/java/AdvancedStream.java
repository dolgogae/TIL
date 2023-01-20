import model.Order;
import model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static model.Order.*;

public class AdvancedStream {
    public static void main(String[] args) {
        Optional<Integer> max = Stream.of(5, 4, 6, 1, 2).max((x, y) -> x - y);
//        System.out.println(max.get());

        User user1 = new User()
                .setId(1001)
                .setName("Alice")
                .setEmail("alice@gmail.com")
                .setVerified(false);
        User user2 = new User()
                .setId(1002)
                .setName("Bob")
                 .setEmail("bob@gmail.com")
                .setVerified(false);
        User user3 = new User()
                .setId(1003)
                .setName("Charlie")
                .setEmail("charlie@gmail.com")
                .setVerified(false);

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
        System.out.println(unverifiedUsersIn24Hours);

        Order order1 = new Order()
                .setId(101)
                .setAmount(BigDecimal.valueOf(2000))
                .setStatus(OrderStatus.CREATED);
        Order order2 = new Order()
                .setId(102)
                .setAmount(BigDecimal.valueOf(4000))
                .setStatus(OrderStatus.ERROR);
        Order order3 = new Order()
                .setId(103)
                .setAmount(BigDecimal.valueOf(3000))
                .setStatus(OrderStatus.ERROR);
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
        System.out.println(maxErroredAmount);

    }
}
