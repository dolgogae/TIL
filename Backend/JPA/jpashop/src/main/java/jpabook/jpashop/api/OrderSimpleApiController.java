package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.SimpleOrderDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleRepository;
import lombok.RequiredArgsConstructor;


/**
 * x to one
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    
    private final OrderRepository orderRepository;
    private final OrderSimpleRepository orderSimpleRepository;

    /**
     * Member와 Order가 서로 멤버변수로 가지고 있기 때문에,
     * 서로 타고 들어가서 무한 루프에 빠지게 된다.
     * 
     * 양방향이 걸리는 것에 @JsonIgnore를 붙혀줘야 한다.
     * 
     * LAZY fetch일 경우 member DB에 접근을 하지 않는다.
     * 그렇다면 proxy로 ByteBuddyIntercepter 클래스를 넣어놓게 된다.
     * 하지만 Jackson에서 객체에 member를 뽑으려고 보니 없기때문에 에러가 뜬다. 
     * Hibernate5Model을 bean객체로 등록을 하면 해결할 수 있다.(본 프로젝트는 main 함수에 bean등록)
     * 
     * 하지만 개발을 할때 쓰면 안되는 방법이니 이런게 있다고 참고만 하자
     * 이유: Entity를 API에 연결하는 것을 좋은 방법이 아니다.
     * 
     * 또한 추가적인 문제점으로는 필요없는 정보의 모든 Lazy 로딩을 건드리기 때문에 query를 너무 많이 날린다.
     *
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        /**
         * 해당 roof를 돌기 전에는 아직 member에 값이 없다.(Lazy로딩이기 때문에)
         * 이를 하이버네이트에서는 프록시 객체를 넣어준다.
         * Hibernate5Module의 config대신 이 방법을 쓰는 것도 있지만, 역시 애초에 entity를 직접 노출하지 않는 것이 좋다.
         * 아래처럼 강제로 초기화 해주는 방법도 있다.
         */
        for (Order order: all){
            order.getMember().getName(); // Lazy 강제 초기화
            order.getMember().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }

    /**
     * DTO로 변환해서 return해주는 방식으로 변경하였다.
     * 
     * 앞선 방법보다 좋지만 query가 너무 많이 나간다.
     * SQL한번으로 2개의 주문이 나온다.
     * -> member와 delivery로 query가 2번씩 더 나간다.
     * 
     * N+1문제
     * 1번의 쿼리로 N개의 쿼리가 추가 호출되는 문제. -> 지연로딩으로 인해 그렇다.
     * 1(order) + 2(member) + 2(delivery)
     */
    @GetMapping("/api/v2/simple-order")
    public List<SimpleOrderDto> orderV2(){
        return orderRepository.findAllByCriteria(new OrderSearch()).stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * 다음처럼 fetch join을 쓰게 되면 한번의 쿼리로 모든 데이터를 긁어올 수 있다.
     * 하지만 roof를 도는 점에서 조금의 성능의 저하가 있을 수 있다.
     */
    @GetMapping("/api/v3/simple-order")
    public List<SimpleOrderDto> orderV3(){
        return orderRepository.findAllWithMemberDelivery().stream()
            .map(o -> new SimpleOrderDto(o))
            .collect(Collectors.toList());
    }

    /**
     * v4가 조금의 성능에 있어서 최적화가 있다.
     * 하지만, v4가 무조건 v3보다 좋다고 할 순 없다.
     * v4는 재사용성 측면에서 단점이 있다. 또한, api spec이 변경될 경우에는 메서드를 수정해야한다.
     * 
     * 다음과 같은 커스텀으로 된 jpql의 경우에는 수정이 있을 수 있으니 별도의 클래스로 분리하는 것도 좋다.
     */
    @GetMapping("/api/v4/simple-order")
    public List<SimpleOrderDto> orderV4(){
        return orderSimpleRepository.findOrderDtos();
    }

    /**
     * 대부분의 경우는 v4와 v3는 성능 차이가 많이 나지 않는다.(어차피 join하는 데에서 시간이 많이 든다.)
     * api가 호출이 굉장히 많은 경우에 v4를 선택하는 것이 좋은 선택일 수 있다.
     * 데이터 사이즈가 엄청 클 경우에는 유의미한 성능향상이 있을 수 있다.
     * 
     * simpleqeury(v4)용도를 따로 뽑아서 만드는 방법도 있다.
     * 왜냐하면 일반 OrderRepository가 의존성을 띄지 않기 때문이다.
     */
}
