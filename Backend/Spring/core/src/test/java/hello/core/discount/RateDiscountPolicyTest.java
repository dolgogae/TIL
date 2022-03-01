package hello.core.discount;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hello.core.AppConfig;
import hello.core.member.Grade;
import hello.core.member.Member;

public class RateDiscountPolicyTest {
    DiscountPolicy discountPolicy;

    @BeforeEach
    void beforeEach(){
        AppConfig appConfig = new AppConfig();
        discountPolicy = appConfig.discountPolicy();
    }


    @Test
    @DisplayName("VIP는 10퍼센트 할인")
    void vip_o(){
        Member member = new Member(1L, "memberVip", Grade.VIP);

        int discount = discountPolicy.discount(member, 10000);

        Assertions.assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("일반 회원은 할인 없음.")
    void basic_o(){
        Member member = new Member(1L, "memberVip", Grade.BASIC);

        int discount = discountPolicy.discount(member, 10000);

        Assertions.assertThat(discount).isEqualTo(0);
    }
    
}
