package com.sihun.monster;

public class Troll extends Monster{
    public Troll(int hp, int attack, int defense) {
        super(hp, attack, defense);
    }
//
//    public void attack(Monster target){
//        int demage = target.getHp() - (this.getAttack() - target.getDefense());
//
////        target.inflictDamage(demage);
//        // 자식 클래스가 부모의 룰을 따르지 않으면?
//    }
    public int calculateDamage(Monster target){
        return getAttack() - target.getDefense()/2;
    }
}
