package com.sihun.logger;

// extends와 같은 개념이지만 implements 키워드를 사용한다.
// 인터페이스는 다중 상속 가능
// 인터페이스는 실체가 없기 때문에 중복돼도 상관없음.
// 여러 인터페이스를 받을 때 서로 이름이 같지만 반환형이 다르면 컴파일 오류가 난다. -> 함수 오버로딩 문제
public class ConsoleLogger implements ILoggable, ISavable{
    // 인터페이스의 구현을 빼먹으면 컴파일 오류
    @Override
    public void log(String message){
        System.out.println("Hello world");
    }
    
    @Override
    public void save(String filename){
        //~~
    }
}
