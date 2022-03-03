package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class StatefulServiceTest {

    @Test
    void statefulServiceSingleton(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = new StatefulService();
        StatefulService statefulService2 = new StatefulService();

        // ThreadA: A사용자 10000원 주문
        int orderA = statefulService1.order("UserA", 10000);
        // ThreadB: B사용자 20000원 주문
        int orderB = statefulService2.order("UserB", 20000);

        // ThreadA: 사용자A 주문 금액 조회
//        int price = statefulService1.getPrice();
//        System.out.println("price: "+price);

//        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(10000);
        Assertions.assertThat(orderA).isEqualTo(orderB);
    }

    static class TestConfig{

        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}
