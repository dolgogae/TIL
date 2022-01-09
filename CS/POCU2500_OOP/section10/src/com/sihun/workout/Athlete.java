package com.sihun.workout;

// 클래스 앞에 붙는 final
// 더이상 상속하지 못함
// 자식 클래스가 존재 불가
// 오버라이딩도 할 수 없음

public class Athlete {
    private int height;
    private double weight;

    public Athlete(int height, int weight){
        this.height = height;
        this.weight = weight;
    }

    // 자식에서 값을 바꾸지 못하게 한다.
    public final int getHeight() {
        return height;
    }

    public final double getWeight() {
        return weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean equals(Object obj){
        if(obj == this)
            return true;

        if(obj == null || !(obj instanceof Athlete))
            return false;

        Athlete a = (Athlete) obj;
        return this.weight == a.weight && this.height == a.height;
    }

}
