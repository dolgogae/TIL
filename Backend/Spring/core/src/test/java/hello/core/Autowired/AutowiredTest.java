package hello.core.Autowired;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import hello.core.member.Member;

public class AutowiredTest {

    @Test
    void AutowiredOption(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
        
    }
    
    static class TestBean{
        // spring bean에 등록되지 않아서 true로 할때 오류가 난다.
        // false로 하면 의존관계가 없어면 자체가 호출이 안된다.
        @Autowired(required = false)
        public void setNoBean1(Member noBean1){
            System.out.println(noBean1);
        }

        // 호출은 되지만 null로 들어간다.
        @Autowired
        public void setNoBean2(@Nullable Member noBean2){
            System.out.println(noBean2);
        }

        // Optional.empty로 들어가진다.
        @Autowired
        public void setNoBean3(Optional<Member> noBean3){
            System.out.println(noBean3);
        }
    }
}
