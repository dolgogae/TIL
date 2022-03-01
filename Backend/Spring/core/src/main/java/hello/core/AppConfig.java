package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;

// client 코드를 전혀 손댈 곳이 없다.
public class AppConfig {
    
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }

    // 저장 방식을 바꿀땐 해당 영역만 바꾸면 가능하다.
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService(){
        return new OrderServiceImpl(
            memberRepository(), 
            discountPolicy());
    }

    // 할인 정책 변경시 해당 코드만 고치면 가능하다.
    public DiscountPolicy discountPolicy(){
        return new RateDiscountPolicy();
        // return new FixDiscountPolicy();
    }
}
