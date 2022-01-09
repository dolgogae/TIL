package com.sihun.Class;

public class FullTimeTeacher extends Teacher{
    private int officeNumber = 0;

    public FullTimeTeacher(String firstName, String lastName, Department department){
        super(firstName, lastName, department);
    }

    public int getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(int officeNumber) {
        this.officeNumber = officeNumber;
    }
}
