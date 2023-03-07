package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * 권장 순서
     * v3과 같이 entity로 우선 접근 하는 것을 권장한다.
     * 이유로는 fetch join을 사용 가능하고, 
     * batch size의 경우도 코드를 거의 수정하지 않고 옵션을 변경해 최적화가 가능하다.
     * 반면 dto의 경우에는 성능 최적화를 할 경우에 굉장히 많은 양의 코드 수정이 들어가게 된다.
     * 
     * entity로 안될경우 dto로 하는 것을 권장한다.(v4~)
     * v4는 코드가 단순하다: roof를 돌려서 OrderItems를 채우면 된다.
     * v5는 코드가 복잡해진다: orderItems를 하나씩 쿼리를 날릴 필요가 없이 한번에 가져오고 메모리에 올려놓고 재조립한다.
     *                    v4에 대비해 성능이 훨씬 좋다고 할 수 있다.
     * v6는 쿼리 자체도 한번에 해결하게 된다. 1:N 관계로 같은 id의 row가 여러개로 조회된다. 페이징도 쉽지 않다.
     *     데이터가 많으면 중복 전송이 증가해서 v5에 비해서 성능차이도 미비하다고 할 수 있다.
     * 
     * 만약, entity 접근의 fetch join 경우로도 해결이 안되면 사실상 dto 접근보다는 다른 방법으로 해결할 것을 찾는 것도 좋다.
     */


    /**
     * entity를 직접 노출하는 것 -> 권장하지 않음
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for(Order order: all){
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            for(OrderItem orderItem : orderItems){
                orderItem.getItem().getName();
            }
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderRepository.findAllByCriteria(new OrderSearch()).stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 중복된 데이터가 조회된다. 
     * order-orderItem간의 join에서 중복된 데이터가 생성된다.(1:N 관계이기 때문이다)
     * 같은 orderId가 여러 row에 걸쳐서 나온다. 하나의 order에 여러개의 orderItem이 있기 때문이다.
     * 심지어 orderItem말고는 모두 같은 데이터가 중복되어 있다.
     * 
     * distinct를 쓰게되면 위에 적은 문제를 해결할 수 있다.
     * (쿼리 상으로는 결과값이 같지만 하이버네이트에서는 pk가 같으면 같은 걸로 생각한다.)
     * 
     * 페이징을 할 수 없는 문제가 생긴다.
     * 왜냐하면 실제쿼리에서는 4개의 결과값이 나오는데 그것은 우리가 의도했던 것이 아니기 때문이다.
     * 일대다 관계에서는 fetch join을 하면 안된다
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        return orderRepository.findAllWithItem().stream()
            .map(OrderDto::new)
            .collect(Collectors.toList());
    }

    /**
     * ToOne관계에 있는 것들은 fetch join으로 먼저 가져온다.
     * OrderItems를 지연로딩으로 가져오게 된다. 
     * 이후 batch size를 application.yml에 지정해준다.
     * batch를 사용하게 되면 쿼리를 한번에 날리게 된다. -> 네트워크를 덜 탄다.
     * 
     * bacth의 maximum은 1000개 정도로 생각하면 된다.
     * 보통 100개에서 1000개 사이로 정하는게 좋다.
     * 
     * 하지만 데이터 전체를 어차피 로딩해야하기 때문에 JVM에서 사용하는 메모리는 동일하다고 볼 수 있다.
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> collect = orders.stream()
            .map(OrderDto::new)
            .collect(Collectors.toList());

        return collect;
    }

    /**
     * ToOne관계를 먼저 가져온뒤에 ToMany관계를 쿼리를 따로 만들어서 넣어주어야 한다.
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    /**
     * 한번의 쿼리로 완성이 된다.
     * 페이징도 가능하지만 orderItems가 기준이 된다. 
     * 따라서 원하는 orderId로 페이징은 안된다고 보면 된다.
     */
    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6(){
        return orderQueryRepository.findAllByDto_flat();
        // List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        // return flats.stream().collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
        //                     mapping(o -> new OrderItemDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
        //             )).entrySet().stream()
        //             .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
        //             .collect(toList());
    }

    @Data
    static class OrderDto{

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        /**
         * Dto 내부에도 의존성 변수가 있으면 좋지 않다.
         * 완전히 entity와 끊는게 좋다.
         */
//        private List<OrderItem> orderItems;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            order.getOrderItems().forEach(o -> o.getItem().getName());
            this.orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.orderPrice = orderItem.getCount();
        }
    }
}
