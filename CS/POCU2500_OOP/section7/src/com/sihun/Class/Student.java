package com.sihun.Class;

// 1. 메모리에 개체 생성(heap 메모리)
// 2. 부모 생성자 호출
// 3. 자식 생성자 호출

public class Student extends Person {
    private Major major;

    public Student(String firstName, String lastName){
        super(firstName, lastName);
    }

    public Major getMajorOrNot() {
        return this.major;
    }

    public Major setMajor(Major major){
        return this.major = major;
    }
}
