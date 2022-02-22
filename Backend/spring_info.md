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

## resources/data.sql
jpa가 로딩될때 자동으로 한번 실행해주는 sql 파일이다.

## Query by Example(QBE)
entity를 example로 만들고 matcher를 추가해서 선언함으로서 필요한 쿼리를 만드는 방법

# SimpleJpaRepository
- JpaRepositoryImplementation <- JpaRepository를 상속받는다.
## save()
@Transactional : 자체적으로 transaction을 한다.  
분기문을 통해 insert를 실행하고 이외에는 update를 처리한다.
### isNew
getId가 null일 경우, new로 보고 있다.  
save() 메서드의 insert와 update를 구분하는 역할을 한다.
## saveAndFlush()
save() + flush()  
## saveAll()
save()를 반복문을 통해서 여러번 save()를 한다.  
## findById vs getOne
getOne은 실제 데이터 값을 구할때 세션을 통해 조회한다. - lazy fetch. 
findById는 직접 엔티티 객체를 가져온다. - eager fetch  
## existById
count 쿼리를 통해서 비교한다.
## deleteAll
findAll()실행 후, 가져온 리스트를 사용해 delete 쿼리를 사용한다. -> 쿼리가 여러번 실행
## deleteAllInBatch
쿼리를 실행해 한번의 쿼리로 테이블을 모두 지우는 형식 

# JPA query method

# Page
페이징에 대한 응답값

## Slice 
데이터 묶음의 일부 덩어리.(부분집합 느낌)
현재 슬라이스의 정보를 리턴해준다. -> getContent()

## Pageable
페이징에 대한 요청값을 리턴해준다.