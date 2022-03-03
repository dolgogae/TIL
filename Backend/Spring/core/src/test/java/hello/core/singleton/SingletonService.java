package hello.core.singleton;

import hello.core.member.MemberService;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance(){
        return instance;
    }

    private SingletonService(){ }

    public void logic(){
        // singleton object logic
    }
}
