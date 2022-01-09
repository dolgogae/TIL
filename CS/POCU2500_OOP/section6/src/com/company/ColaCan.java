package com.company;

public class ColaCan {
    private int remainingMl;
    private static int numCreated;

    public ColaCan(int initialMl){
        ++numCreated;
        // 해당 정적 멤버변수는 클래스단위에서 동작한다.
    }

    public static void printStats(){
        System.out.println("# Cola Produced: " + numCreated);
        // remainingMl을 접근에 못함
        // 클래스에 속한 매서드가 개체에 속한 멤버(함수/변수)에 접근 불가
        // static은 하나인데 개체는 여러개가 생길수 있다. -> 어느 개체인지 모른다.
        //System.out.println("ml left: "+ this.remainingMl); // error
    }
}

// static 멤버 변수 및 멤버 함수는 클래스에 속함
// static 아닌 것은 개체에 속함
// 비정적 -> 정적: 접근 가능
// 정적 -> 비정적: 접근 불가능

// static이 C의 전역변수보다 좋은점
// 1. 접근 범위를 제어할 수 있다.
// 2. 클래스 내부에 위치해서 이름 충돌이 적다.