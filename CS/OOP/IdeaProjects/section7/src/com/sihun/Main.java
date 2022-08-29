package com.sihun;

// 상속
// 거의 모든 사람이 OOP의 핵심이라 여기는 특성
// OOP의 또 다른 매우 중요한 특성인 다형성의 기반

// 이미 존재하는 클래스를 기반으로 새 클래스를 만드는 방법

import com.sihun.Class.*;

public class Main {
    public static void main(String[] args) {
        Student student = new Student("sihun", "oh");
        Teacher teacher = new Teacher("johnson","lee", Department.math);

        student.getFullName(); // 상속이 없으면 error
        Person person;

        //=======================
        // is a 관계이기 때문에 가능(사람은 학생이다)
        // 자식을 부모에 대입하는건 암시적 캐스팅
        Person person1 = student;   // 캐스팅이라고 한다.
        Person person2 = teacher;

        // 컴파일 불가능
        // 프로그래머가 명시적으로 캐스팅 가능
        // Student student1 = person;
        // 단, 다른 클래스이면 컴파일러에서 잡아준다(형재자매간의 캐스팅은 불가능)
        Student actuallyStudent = (Student) person1;

        // 컴파일 에러가 나지 않는다.
//        Teacher teacher1 = (Teacher) person1;

        // 위 코드에서 person1의 실체를 알고 싶을때, RTTI(run time type identification)기능
        // java: instanceof
        // 부모 클래스인 경우도 검사해도 true
        Person person3 = new Student("Leon", "Kim");

        System.out.println(person3 instanceof Student); // true
        System.out.println(person3 instanceof Teacher); // false

        if(person3 instanceof Student){
            person3 = new Student("aa", "bb");
        }

        // getClass(): 실행중에 개체의 클래스 정보를 가져올 수 있다.
        
        Person p0 = new PartTimeTeacher("Sad", "Panda", Department.math, 10);
        Person p1 = new Teacher("Pope", "Kim", Department.math);

        if(p0.getClass() == p1.getClass()) {
            System.out.println(p0.getClass().getName());
        }

        // 실행중에 뭔가를 더 해야함
        // 성능 또는 메모리가 중요한 경우에는 별로인 기능
        // C/C++ 지원하지 않음
    }
}

// 상속(is a) vs 컴포지션(has a)
// 상속으로 해결할 수 있는 많은 문제를 컴포지션으로 가능
// OO에서 큰 결정사항: 상속 vs 컴포지션

// 실생활에서 개체들끼리 관계를 기준으로 선택할 것: 모든 경우에서 이런게 통하지는 않다.

// Object 클래스
// java의 모든 클래스는 Object클래스를 상속
// 모든 클래스의 최상위 클래스