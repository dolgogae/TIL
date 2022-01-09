package com.sihun;

import com.sihun.monster.Ghost;
import com.sihun.monster.Monster;

public class Main {
    public static void main(String[] args) {
        // 몬스터는 개체를 생성할 실체가 없다.-> 추상적인 클래스
//        Monster monster01 = new Monster(10,3,2);

        Monster monster01 = new Ghost(10,3,2);
    }
}
