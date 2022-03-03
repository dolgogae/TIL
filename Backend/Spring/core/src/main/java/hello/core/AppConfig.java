package hello.core;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

// client 코드를 전혀 손댈 곳이 없다.
@Configuration  //spring에서 구성정보를 담당한다고 등록
public class AppConfig {

    // @Bean memberService -> new MemoryMemberRepository()
    // @Bean orderService -> new MemoryMemberRepository()
    // new가 두번 호출되는데 싱글톤이 깨질까?

    
    @Bean // 각 구성정보를 spring컨테이너에 등록한다.
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    // 저장 방식을 바꿀땐 해당 영역만 바꾸면 가능하다.
    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService(){
        return new OrderServiceImpl(
            memberRepository(), 
            discountPolicy());
    }

    // 할인 정책 변경시 해당 코드만 고치면 가능하다.
    @Bean
    public DiscountPolicy discountPolicy(){
        return new RateDiscountPolicy();
        // return new FixDiscountPolicy();
    }
}
