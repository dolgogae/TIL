package com.sihun.character;

import com.sihun.Sex;

public class Human {
    public String name;
    public int age = 20; // 0이 아닌 다른 값으로 초기화하고 싶을때
    // 개체의 상태를 변경하는 주체는 개체 자신이어야 적절
    // 접근제어자를 통해서 해결.
    
    // 1. public: 누구나 접근 가능
    // 2. protected: 자식들만 접근 가능
    // 3. private: 외부 접근 불가
    // - 클래스의 경우 내포 클래스에 한 해 붙일 수 있음
    
    // 멤버변수: private -> 멤버 변수 접근은 메서드를 통해서만(캡슐화, 추상화)
    // 메서드: public
    // private 메서드는?
    // 절차지향에서 함수를 만드는 이유랑 같다(중복, 효율적 관리...)

    // 접근제어자를 안 붙일 경우
    // 기본(default) 또는 패키지 접근 권한
    // 같은 패키지 안에 있는 서로 접근 가능
    // 용도
    // public 대신 패키지 접근 제어자를 사용할 수 있다면 그리 할 것
    // 'public 이 아닌 내포 클래스'를 최상위 클래스로 바꿀때 쓸 것

    public Sex sex;

    public Human(){}
    // 프로그래머가 생성자를 하나도 안 만들 경우 자동으로 생기는 생성자
    // 컴파일러가 알아서 매개변수 없는 생성자를 만들어 줌

    /*public Human(String name, int sex){
        this.name = name;
        this.sex = sex;
    }*/
    public Human(String name, int age, Sex sex){
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
    // 개체 생성시 자동으로 호출되는 특수한 함수
    // 반환형: 없음
    // 함수명: 클래스명과 동일
    // 위처럼 중복코드는 피하는게 좋다.
    public Human(String name, Sex sex){
        this(name, 0, sex);
        // 다음과 같이 중복을 피할 수 있다.
    }

    // 개체 생성후 대입하는 것이 좋지 않은 이유는?
    // 함수의 후조건의 문제
    // 생성자도 함수, 따라서 선조건과 후조건이 적용
    // 생성자의 후조건: 개체의 상태는 개체 생성과 동시에 유효하다.

    // 사용자를 고려안한 문제
    // 사용자: 내가 만든 클래스를 사용하는 코드 혹은 프로그래머
    // 사용자의 실수를 유발
    // 1. 클래스에 있는 어떤 멤버 변수를 초기화해야 하는가?
    // 2. 어떤 값으로 초기화해야 하는가?
    // 3. 나중에 멤버 변수가 추가될 때 기존의 초기화 코드들을 업데이트 안하면?

    // 즉, 외부에서 클래스 내부의 데이터를 알 필요가 없다.
    // 데이터 추상화(캡슐화의 일부)
    // 생성자뿐 아니라 메서드에도 적용

    public void walk()  { this.age += 1; }
    public void eat()   { this.age -= 1; }
    public void speak(){
        System.out.println("hello friend");
    }

    public void increaseAge(Human player, int age){
        player.age += 10;   // 원본이 바뀐다.
        age = 0;            // 안바뀐다.
    }

    //getter and setter
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
    
    // 장점
    // 1. 멤버 변수를 저장하지 않고 필요할 때마다 getter에서 계산 가능
    // 2. setter에서 추가적인 로직을 실행할 수 있음
    // 3. 상속을 통한 다형성 구현 가능
    
    // best practice
    // 1. 멤버변수는 private
    // 2. 새 개체는 유효하도록(생성자를 통해 이를 강제할 수 있음)
    // 3. getter는 자유롭게 추가
    //   - 사용자가 알 필요 없는 정보는 보여주지 않는게 정석
    //   - 그러나 보여줘도 큰 문제는 없으니 getter는 보통 자유롭게
    //   - 주의: 어떤 개체의 레퍼런스를 반활할 때는 문제될 수도 있음
    // 4. setter는 고민 후 추가
    //   - 개체의 사용자가 어떤 동작을 지시
    //   - 그 동작의 결과로 개체 안에 있는 어떤 상태가 바뀜(classroom 패키지)
    //   - setter는 데이터를 직접 바꾸므로 가능한 피하는게 좋음(개체가 불확실한 상태로 되는 경우를 최대한 막자)

}

































