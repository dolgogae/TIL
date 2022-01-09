package com.sihun.logger;

// abstract가없음
// 메서드는 언제나 public
//. 누구라도 보고 명령할 수 있는 동작
// 미구현으로 인한 컴파일 오류는 실수를 방지
// 인터페이스는 같은 패키지만 사용가능하다.
public interface ILoggable {
    void log(String message);
}
