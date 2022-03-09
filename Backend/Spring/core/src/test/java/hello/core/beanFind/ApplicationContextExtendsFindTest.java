package hello.core.beanFind;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;

public class ApplicationContextExtendsFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Testconfig.class);

    @Test
    @DisplayName("부모 타입으로 조회시 자식이 둘 이상 있으면, 중복 오류가 발생한다.")
    void findBeanByParentTypeDuplicate(){
        assertThrows(NoUniqueBeanDefinitionException.class,
            ()->ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("자식이 둘 이상 있으면 빈 이름으로 지정하면 된다.")
    void findBeanByParentTypeBeanName(){
        DiscountPolicy ratDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
        Assertions.assertThat(ratDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType(){
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        Assertions.assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기")
    void findAllBeanByParentType(){
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        Assertions.assertThat(beansOfType.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("부모 타입으로 모두 조회하기 - Object")
    void findAllBeanByObjectType(){
        // 스프링 안의 여러가지 빈이 모두 출력된다.
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
        Assertions.assertThat(beansOfType.size()).isEqualTo(16);
    }

    @Configuration
    static class Testconfig{
        @Bean
        public DiscountPolicy rateDiscountPolicy(){
            return new RateDiscountPolicy();
        }
        @Bean
        public DiscountPolicy fixDiscountPolicy(){
            return new FixDiscountPolicy();
        }
    }
}
