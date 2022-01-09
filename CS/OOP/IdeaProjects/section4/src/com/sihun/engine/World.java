package com.sihun.engine;

import com.sihun.Sex;
import com.sihun.character.Human;

public class World {
    public void spawnNPC(){
        Human npc = new Human();
//        npc.age = 20; // if) public이 안붙어 있다면 : 오류
    }
    public void doAdam(){
        Human adam = new Human();
        // adam은 주소를 가지고 있다.
        // 자바에서는 이것을 참조형이라고 함
        // 자바는 기본 자료형을 제외하면 모두 포인터

        System.out.printf("Adam's age is %d", adam.age);
        // 출력값 >> 0
        // java는 0에 준하는 값으로 초기화 해줌
        // int: 0, float: 0.0, 참조형: null
        // java는 실수 방지가 중요
        // 0이 아닌값으로 초기화 하고싶다면 선언문에 대입!!

        adam.sex = Sex.FEMALE;
        adam.name = "ADAM";
        adam.age = 20;
        // 개체의 멤버에 접근할 때 . 연산자를 사용(기본이 참조형이라서)

        System.out.printf("Adam's age is %d", adam.age);

        adam.walk();
        System.out.printf("Adam's age is %d", adam.age);
        adam.eat();
        System.out.printf("Adam's age is %d", adam.age);
        adam.speak();
        System.out.printf("Adam's age is %d", adam.age);
        // adam 개체 메모리 해제 코드?
        // java에는 free()가 없다.
        // java는 가비지 컬렉션(garbage collection, GC)기능을 지원
        // jvm에 내장된 가비지 컬렉터가 알아서 카비지를 치워줌
        // 가비지란? 더이상 사용되지 않는 개체(즉, 힙 메모리)
        // 프로그래머가 직접 메모리를 해제하지 않는다.

        // 단점.
        // 메모리를 수집하는 시점을 알 수 없음 -> 상황에따라 느려질수 있다.
        // 모든 개체의 사용 여부를 판단하는게 그리 빠른 연산이 아님
        // 이러한 이유로 자원이 한정적인 시스템에는 적합하지 않음
        // 자동 메모리 관리하에 발생하는 메모리 누수도 존재
    }
}
