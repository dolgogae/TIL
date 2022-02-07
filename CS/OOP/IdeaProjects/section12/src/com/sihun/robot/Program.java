package com.sihun.robot;

public class Program {
    public static void main(String[] args) {
        Robot robot = new Robot(300);
        robot.damage(50);

        Robot cloned = (Robot)robot.clone();
        
        // 주소를 공유하기 때문에 savePoint도 영향(Clone없을때)
        robot.damage(50);

    }
}
