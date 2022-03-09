package hello.core.Autowired;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;

public class AllBeanTest {
    
    @Test
    void findAllBean(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
    
        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member,  10000, "fixDiscountPolicy");
    
        Assertions.assertThat(discountService).isInstanceOf(DiscountService.class);
        Assertions.assertThat(discountPrice).isEqualTo(1000);
    }

    // code를 클래스의 이름을 해서 찾을 수 있다.
    static class DiscountService{
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies){
            this.policies = policies;
            this.policyMap = policyMap;
        }

        public int discount(Member member, int price, String discountCode){
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }
    }
}
