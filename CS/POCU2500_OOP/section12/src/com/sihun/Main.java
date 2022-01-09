package com.sihun;

import com.sihun.logger.ConsoleLogger;

public class Main {
    public static void main(String[] args) throws Exception {
        ConsoleLogger logger = new ColsoleLogger();
    }
    // 함수 구현은 시그니처만 지정한다.
}

// 구체 클래스
// 상태와 동작을 모두 포함
// 동작에 다양한 접근 권한 부여
// 개체 생성 가능
// 다중상속 x

// 인터페이스
// 동작에 대한 설명만 포함
// 모든 동작은 public
// 개체 생성 불가능
// 다중상속 가능