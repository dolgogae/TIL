package com.sihun.Class;

public class Teacher extends Person{
    private Department department;

    public Teacher(String firstName, String lastName, Department department){
        super(firstName,lastName);
        this.department = department;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setEmail(String email){
        super.email = email;
//        this.email = email; 도 가능하다.
    }
}
