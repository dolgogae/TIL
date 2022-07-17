# Spring Boot Web Application

## WAS(Web Application Server)
- HTTP 기반 
- 웹 서버 기능 포함 + 정적인 파일 제공
- 프로그램 코드 실행후 애플리케이션 로직 수행
- Tomcat 등

## WS vs WAS
웹서버 : 정적 리소스, WAS : 어플리케이션 로직.  
자바는 서블릿 컨테이너 기능을 제공하면 WAS

** WAS는 애플리케이션 코드를 실행하는데 더 특화  **

## 웹시스템 구성 - WEB, WAS, DB

웹 서버는 애플리케이션 로직같은 동적인 처리가 필요하면 WAS에 위임.  
효율적인 리소스 관리
- 정적 리소스가 많이 필요 -> WEB서버 증설
- 동적 리소스가 많이 필요 -> WAS서버 증설

정적 리소스만 제공하는 웹서버는 잘 죽지 않음, 반면 WAS는잘 죽음.  
WAS, DB장애시 WEB서버 오류 화면 제공 기능

## 서블릿

1. TCP/IP 연결
2. HTTP 요청메시지 파싱
3. HTTP 메시지 바디 내용 파싱
4. 저장 프로세스 실행
5. HTTP 응답 메시지 생성 시작
6. TCP/IP 응답 전달 및 종료
=> 비즈니스 로직 이외의 모든 것을 담당한다.

싱글톤 객체로 생성한다.  
공유 변수 사용시 주의해야 한다.  
동시 요청을 위한 멀티 스레드 지원.  

### HTTP 요청시
- WAS는 Request, Response 객체를 새로 만들어서 서블릿 객체 출
- Request 객체에서 HTTP 요청 정보를 편리하게 꺼내서 사용
- Response 객체에 HTTP 응답 정보를 편리하게 입력
- WAS는 Response 객체에 담겨있는 내용으로 HTTP 응답 정보를 생성

## 스레드 풀

필요한 스레드를 스레드 풀에 보관하고 관리.  
스레드 풀에 생성 가능한 스레드의 최대치를 관리한다.  
WAS는 멀티 스레드 부분을 처리해준다.

> 장점.  
> 스레드가 미리 생성되어, 비용(CPU) 절약 및 응답속도 빠름.  
> 한번에 많은 요청이 와도 서버가 터지지 않는다.  

### 성능 튜닝
가장 중요한건 max thread 수를 잘 설정하는 것이다.
-> 성능테스트 : 실제 서비스와 유사하게 성능 테스트 시도 / 툴: 아파치 ab, nGrinder

## HttpServletRequest, HttpServletResponse
Http요청 메시지, Http응답 메시지를 편리하게 사용하도록 도와주는 객체이다.  
HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해해야 한다.

## HTTP 요청 데이터

### **GET - 쿼리 파라미터**
메시지에 바디없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달  
ex) 검색, 필터, 페이징등에서 많이 사용하는 방식  

### **POST - HTML form**
메시지 바디에 쿼리 파라미터 형식으로 전달 (username=hell&age=20)  
ex) 회원 가입, 상품 주문, HTML form 사용  

### **HTTP message body에 데이터 직접 요청**
HTTP API에서 주로 사용(json, xml, text)  
주로 json을 많이 사용  
POST, PUT, PATCH  