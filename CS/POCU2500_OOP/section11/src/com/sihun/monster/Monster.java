package com.sihun.monster;

public abstract class Monster {
    private int hp;
    private int attack;
    private int defense;

    public Monster(int hp, int attack, int defense){
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
    }

    public int getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public boolean isAlive(){
        return hp>0?true:false;
    }

    public final void attack(Monster target){
        int demage = calculateDamage(target);
        target.hp = Math.max(0, target.hp - demage);

//        monster.inflictDamage(demage);
    }

//    protected void inflictDamage(int demage){
//        this.hp = Math.max(0, this.hp - demage);
//    }

//        public int calculateDamage(Monster target){
//            // 자식들이 오버라이딩 해야함
//            // 만약 자식이 구현을 하지 않았다면?
//            // 실체가 없기 때문에 추상메서드이다.
//            return 0;
//        }
    public abstract int calculateDamage(Monster target);
}
