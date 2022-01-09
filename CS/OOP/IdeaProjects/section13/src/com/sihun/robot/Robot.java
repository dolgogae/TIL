package com.sihun.robot;

public class Robot {
    private int hp;
    private Head head;

    public Robot(int initialHp, Head head){
        this.hp = hp;
        // Head의 생성자를 바꿈으로서 컴파일이 안되므로 결합도가 높다.
//        this.head = new Head(forAngle);

        // Robot은 Head속이 어떻게 구현되어 있는지 모름
        // 느슨한 커플링으로 변경됨.
        // 의존성 주입(dependency injection)

        // Simple, Smart head 모두 호환이 가능하다.
        this.head = head;
    }
}
