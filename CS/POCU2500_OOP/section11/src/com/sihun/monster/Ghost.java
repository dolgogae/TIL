package com.sihun.monster;

public class Ghost extends Monster{
    public Ghost(int hp, int attack, int defense) {
        super(hp, attack, defense);
    }
//
//    public void attack(Monster target){
//        int demage = 1;
//        // 구현
//
//        target.inflictDamage(demage);
//    }

    public int calculateDamage(Monster target){
        return target.getDefense() - target.getAttack();
    }
}
