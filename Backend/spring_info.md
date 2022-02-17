# JPA Intro

### ORM

객체와 데이터 베이스 사이를 연결해주는 것.  
자연스럽데 DB와 소스코드를 연결해주어서 편리하게 사용할 수 있다.

- JPA는 JAVA 진영의 ORM 표준으로 지정되어 있다.

### Hibernate

JPA에 대한 실제 구현채. 즉, implementation.

### Spring Data JPA

spring에서 JPA를 더 간단하게 만들 수 있도록 한 것.

---

## @Entity Anotation
객체를 엔티티로 변경해주는 것.  
Entity에는 PK(Primary Key)가 반드시 필요 -> @Id, @Generated annotation을 활용

## JpaRepository 
jpa와 관련된 기능 사용 가능하게 하는 클래스
repository에 상속해준다.

## Jpa- findAll()
회원정보를 모두 저장하는 메서드이지만, 성능상 문제로 보통 사용하지 않는다.
- deleteAllInBatch도 마찬가로 성능상 문제로 잘 쓰이지 않는다.

## CrudRepository 
JPA에서 사용하는 많은 메서드를 포함한 클래스이다.  
JpaRepository에서 해당 클래스를 상속받고 있다.

