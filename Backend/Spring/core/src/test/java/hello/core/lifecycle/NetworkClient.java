package hello.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

// 의존관계 주입이 언제 끝났는지 개발자가 알수 있는 방법이 있어야 한다.

public class NetworkClient {
    private String url;

    public NetworkClient(){
    }

    public void setUrl(String url){
        this.url = url;
    }

    // 서비스 시작시 호출
    public void connect(){
        System.out.println("connect: "+url);
    }

    public void call(String message){
        System.out.println("call: "+url+", message: "+message);
    }

    // 서비스 종료시 호출
    public void disconnect(){
        System.out.println("close"+url);
    }

    @PostConstruct
    public void init() throws Exception{
        connect();
        call("초기화 연결 메시지");
    }

    @PreDestroy
    public void close() throws Exception{
        disconnect();
    }
}
