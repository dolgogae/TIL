package com.sihun;

// 다형성
// 실용적인 용도: 다른 종류의 개체를 편하게 저장
// 어떤 개체가 다양한 형태로 변할 수 있는 능력
// 상속은 다형성에 필요한 선주 조건

// 장점
// 각 자료형의 코드가 클래스 안에 들어가니 캡슐화 높아짐
// 유지보수성도 높아진다.
// 클라이언트가 작성할 코드가 줄어듦

// 다형성은 늦은 바인딩
// 실제로 호출되는 메서드는 구현이 프로그램 실행 중에 결정된다는 의미(동적 바인딩)

// 이른 바인딩(정적 바인딩)
// C에서 배웠던 함수의 호출 방식
// 어떤 함수 구현을 호출해야할지 빌드 중에 결정 남

// C에서의 늦은 바인딩: 함수 포인터

// Object에 있는 메서드
// Java의 클래스는 모두 Object로부터 상속 받는다
// 1. toString()
// 사람이 읽기 편하게 해당 개체를 문자열로 표현
// 기본구현: getClass().getName() + "@" + Integer.toHexString(hashCode())
// Java 공식 문서는 모든 클래스에서 이 메서드를 오버라이딩하라 권장

// 2. equal()
// 기본 구현: this == obj
// 실제 개체 속의 데이터를 일일히 비교하지 않음
// 이때 hashCode()도 반드시 같이 오버라이딩해야 함

// 3. hashCode()
// 어떤 개체를 대표하는 해시값을 32비트 정수로 반환
// 기본 구현: 개체의 주소를 반환

import com.sihun.animal.Animal;
import com.sihun.animal.Cat;
import com.sihun.animal.Dog;

public class Main {
    public static void main(String[] args) {
        Animal dog = new Dog();

        // 실제 객체에 있는 메서드가 호출
        dog.shout();

        Animal[] pets = new Animal[2];

        pets[0] = new Cat();
        pets[1] = new Dog();

        for(int i=0; i<2; i++)
            pets[i].shout();
    }
}
