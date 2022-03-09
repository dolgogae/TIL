package hello.core.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class PrototypeTest {
    
    @Test
    void prototypeBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean pb1 = ac.getBean(PrototypeBean.class);
        PrototypeBean pb2 = ac.getBean(PrototypeBean.class);
        
        Assertions.assertThat(pb1).isNotSameAs(pb2);

        // close에 대한 로그가 남지 않는다.
        ac.close();

        // 수작업으로 닫아 줘야 한다.
        pb1.destroy();
        pb2.destroy();
    }

    @Scope("prototype")
    static class PrototypeBean{
        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean init");

        }
        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean destroy");
        }
    }

}
