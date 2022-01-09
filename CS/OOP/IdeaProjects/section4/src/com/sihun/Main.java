package com.sihun;

public class Main {
    public static void main(String[] args) {

    }
}

// 클래스 만들기와 private 멤버 변수가 캡슐화다.
// private 멤버 변수와 setter/getter가 데이터 추상화다.

// 캡슐화
// 1. 개체의 데이터(=멤버 변수)와 동작(=메서드)을 하나로 묶음
// 2. 내부의 데이터를 외부로부터 보호
// 사용자가 클래스 속을 알 필요가 없음 -> 추상화로 이어짐
// 함수를 분리할 때 적용했던 원칙을 클래스에도 적용 -> 중복코드가 있다면 private 메서드로



// 추상화
// 추상 자료형(abstract data type)쪽 관점
// - 사용자는 클래스를 자료형으로 사용
// - 그냥 클래스로부터 개체 생성 가능

// 절차적 데이터 추상화(procedural data abstraction)쪽 관점
// - 데이터를 직접 조작하는 대신 메서드를 호출
// - 동작적 개체 진영

// 어떤 구체적인 것에 직접 손대지 않겠다.

// 단점
// 동작 없이 데이터만 있는 클래스는 쓸데없는 코드만 늘어남
// 어떻게 추상화를 해야하는지 뚜렷한 객관적 기준이 없음
// - 사람은 뚜렷한 실체가 없는 개념을 이해하기 어려워함