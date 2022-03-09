package hello.core.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.beans.factory.annotation.Autowired?;
import org.springframework.stereotype.Component;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
// final이 붙은 생성자를 만들어준다.
// @RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    // private DiscountPolicy discountPolicy = new FixDiscountPolicy();
    // private DiscountPolicy discountPolicy = new RateDiscountPolicy();
    // DiscountPolicy 뿐만 아니라 FixDiscountPolicy에도 의존하기 때문에 소스코드를 변경해야한다.
    
    // 필드에 바로 주입하는 방법이 있다.
    // 사용하지 않는게 좋다.
    // @Autowired
    private final MemberRepository memberRepository;
    // @Autowired
    private final DiscountPolicy discountPolicy;


    // 생성자를 통해서만 DI가 일어난다.
    // setter를 통해 주입 받으면 불변성이 무너진다.
    // 생성자가 하나 있으면 Autowired를 생략해도 된다.
    // 생성자가 아닌 일반 메서드에도 주입이 가능하다.(setter와 같다고 생각하면 된다.)
    // @Autowired
    // public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy){
    //     this.memberRepository = memberRepository;
    //     this.discountPolicy = discountPolicy;
    // }

    // @Autowired
    // public void setMemberRepository(MemberRepository memberRepository){
    //     this.memberRepository = memberRepository;
    // }

    // @Autowired
    // public void setDiscountPolicy(DiscountPolicy discountPolicy){
    //     this.discountPolicy = discountPolicy;
    // }

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, 
                            @MainDiscountPolicy DiscountPolicy discountPolicy){
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트용
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
