package hello.core.order;

import org.junit.jupiter.api.Test;

import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImplTest {
    

    @Test
    void createOrder(){
        OrderServiceImpl orderService = new OrderServiceImpl(
            new MemoryMemberRepository(), 
            new RateDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemName", 1000);
    }
}
