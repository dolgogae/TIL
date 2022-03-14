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

## Page
페이징에 대한 응답값

## Slice 
데이터 묶음의 일부 덩어리.(부분집합 느낌)
현재 슬라이스의 정보를 리턴해준다. -> getContent()

## Pageable
페이징에 대한 요청값을 리턴해준다.

# JPA Annotation

## @Entity
JPA가 관리하는 Entity 객체임을 선언

## @Id
DB Table의 pk

## @GeneratedValue
사용자가 생성하지 않고 생성된 값을 쓰겠다는 것.
- IDENTITY: mysql DB에서 많이 사용한다. DB의 auto increase 기능을 활용, id값을 사전에 받아오게 된다.
- TABLE: DB에서 id값을 추출해서 사용한다.
- SEQUENCE: oracle, postgresql에서 제공하는 sequence를 이용할 때 사용. 트랙잭션이 일어나는 구간에서 id를 채워넣게 된다.
- AUTO: defualt설정값으로 각 DB에 적합한 값을 자동으로 넘겨주게 되고, DB의존성없이 코딩이 가능하다. 일반적으로 DB는 고정이기 때문에 잘 설정하지 않는다.

## @Table
테이블에서 DB에 들어가는 초기값을 설정

## @Column

### name
DB의 컬럼명을 통해 변수와 매칭  
컬럼의 이름을 DB그대로 하는경우 변수 자체가 가독성이 없는 경우가 있어 그럴때 사용된다.

### nullable
false: not null
true: null

### unique
해당 컬럼에 유니크 속성을 추가한다.

### updatable vs insertable
해당 값을 저장할지 안할지 결정

### Transient
DB 데이터에 반영되지 않는다.

### Enumerated(value = EnumType.STRING)
enum은 실제 변수가 의미하는 string값으로 DB에 저장되지 않으므로 해당 어노테이션을 활용해 값을 넣어주어야 한다. enum 사용시 필수적.  

## Listener

@PrePersist // insert method가 실행되기전  
@PreUpdate // merge method가 실행되기전  
@PreRemove // delete method가 실행되기전  
@PostPersist  // insert method가 실행한후  
@PostUpdate // merge method가 실행된 후  
@PostRemove // delete method가 실행된 후  
@PostLoad // select method가 실행된 후  

## AuditingEntityListener(EnableJpaAuditing)

Jpa Auditing이란?
Java에서 ORM 기술인 JPA를 사용하여 도메인을 관계형 데이터베이스 테이블에 매핑할 때 공통적으로 도메인들이 가지고 있는 필드나 컬럼들이 존재합니다. 대표적으로 생성일자, 수정일자, 식별자 같은 필드 및 컬럼이 있습니다.

도메인마다 공통으로 존재한다는 의미는 결국 코드가 중복된다는 말과 일맥상통합니다.
데이터베이스에서 누가, 언제하였는지 기록을 잘 남겨놓아야 합니다. 그렇기 때문에 생성일, 수정일 컬럼은 대단히 중요한 데이터 입니다.

그래서 JPA에서는 Audit이라는 기능을 제공하고 있습니다. Audit은 감시하다, 감사하다라는 뜻으로 Spring Data JPA에서 시간에 대해서 자동으로 값을 넣어주는 기능입니다. 도메인을 영속성 컨텍스트에 저장하거나 조회를 수행한 후에 update를 하는 경우 매번 시간 데이터를 입력하여 주어야 하는데, audit을 이용하면 자동으로 시간을 매핑하여 데이터베이스의 테이블에 넣어주게 됩니다.