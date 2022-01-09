package com.sihun.animal;

public class Dog extends Animal{

    // java 어노테이션
    // 프로그램에 대한 메타데이터 제공
    // 컴파일러에게 정보를 제공
    // 컴파일 또는 배포 중에 어노테이션을 기반으로 어떤 처리를 할 수 있음
    // 실행 중에도 어노테이션을 기바능로 어떤 처리 가능
    @Override
    public void shout(){
        System.out.println("Wal~");
    }
}
