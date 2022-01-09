package com.sihun.Class;

// 부모 자체로도 개체 생성 가능
// 부모는 자식을 호출 불가

import java.util.Locale;

public class Person {
    private String firstName;
    private String lastName;
    // 클래스 내부, 같은 패키지에 속한 클래스, 자식 클래스만 접근 가능
    // 클래스의 경우 내포된(nested) 클래스에 한 해 붙일 수 있음.
    protected String email;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = String.format("%c%s@sihun.com",
                firstName.toUpperCase().charAt(0), lastName.toLowerCase());
    }

    public String getFullName(){
        return String.format("%s %s", firstName, lastName);
    }

    public void changeName(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }
}
