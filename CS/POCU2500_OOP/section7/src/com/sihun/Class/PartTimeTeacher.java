package com.sihun.Class;

public class PartTimeTeacher extends Teacher{

    private int weeklyHours;

    public PartTimeTeacher(String firstName, String lastName, Department department){
        this(firstName, lastName, department,0);
    }

    public PartTimeTeacher(String firstName, String lastName, Department department, int weeklyHours){
        super(firstName, lastName, department);
        this.weeklyHours = weeklyHours;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }
}
