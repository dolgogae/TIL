package hello.core.scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import hello.core.scope.PrototypeTest.PrototypeBean;

public class SingletonWithPrototypeTest1{

    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean pb1 = ac.getBean(PrototypeBean.class);
        pb1.addCount();
        Assertions.assertThat(pb1.getCount()).isEqualTo(1);

        PrototypeBean pb2 = ac.getBean(PrototypeBean.class);
        pb2.addCount();
        Assertions.assertThat(pb2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUseProperty(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean cb1 = ac.getBean(ClientBean.class);
        Assertions.assertThat(cb1.logic()).isEqualTo(1); 
        
        ClientBean cb2 = ac.getBean(ClientBean.class);
        Assertions.assertThat(cb2.logic()).isEqualTo(1);

    }

    @Scope("singleton")
    static class ClientBean{

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }


    @Scope("prototype")
    static class PrototypeBean{
        private int count = 0;

        public void addCount(){
            ++count;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean init"+this);
        }

        @PreDestroy
        public void destory(){
            System.out.println("PrototypeBean destroy"+this);
        }
    }
}