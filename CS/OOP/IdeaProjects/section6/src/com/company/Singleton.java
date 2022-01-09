package com.company;

public class Singleton {
    private static Singleton instance;

    private Singleton(){ }

    public  static Singleton getInstance(){
        if(instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}

// static으로 못하는일
// 1. 다형성 사용 불가
// 2. 시그니쳐를 그대로 둔 채 멀티턴 패턴으로 바꿀 수 없다.
// 3. 개체를 생성 시점을 제어할 수 없다.
//    - static은 프로그램 실행 시에 초기화됨

// 싱글턴 개체의 생성시기
// 처음으로 getInstace() 호출시
// 다양한 개체에서 사용해서 어디서 생성할지는 모른다.

// 초기화 순서를 보장하기 위해 실무에서 이런 일을 하기로함
// 프로그램 시작시 여러 싱글턴의 getInstance()를 순서대로 호출
// 싱글턴을 정해진 순서대로 호출

