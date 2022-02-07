package com.sihun.robot;

public final class Robot implements Cloneable{
    private int hp;
    private Head head;

    public Robot(int initialHp){
        this.hp = initialHp;
    }

    public int getHp(){
        return this.hp;
    }

    public void damage(int amount){
        this.hp = Math.max(0, this.hp - amount);
    }

    // 클래스를 복사
    // Object를 반환하기 때문에 캐스팅 필요
    protected Object clone() throws CloneNotSupportedException{
        // return super.clone();

        // 얕은 복사로는 안에 있는 가지고 있는 객체를 복사하지 못함
        // 따라서 안의 객체도 복사를 해줘야한다.
        Robot cloned = (Robot) super.clone();
        cloned.head = (Head) head.clone();

        return cloned;
    }
}
