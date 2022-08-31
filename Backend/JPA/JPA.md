## 상속 도메인일 경우

### 조인 전략
`@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`
장점
- 테이블 정규화
- 외래 키 참조 무결성 제약조건 활용가능
- 저장공간 효율화


단점
- 조회시 조인을 많이 사용, 성능 저하 -> 크리티컬 하지 않음
- 조회시 쿼리가 복잡함
- 데이터 저장시 INSERT SQL 2번 호출 -> 크리티컬 하지 않음

### 단일 테이블 전략
`@Inheritance(strategy = InheritanceType.JOINED)`
장점
- Join이 필요 없으므로 일반적 조회성능 빠름
- 조회 쿼리 단순


단점
- 자식 엔티티가 매핑한 컬럼은 모두 null 허용
- 단일 테이블에 모든 것을 저장하므로 테이블이 커질 수 있다.

### MappedSuperclass
공통 매핑 정보가 필요할때(등록일, 수정일 등)

## Proxy
Memeber를 조회할때 Team도 조회를 해야할까?의 문제

> em.getReference(): 데이터베이스 조회를 미루는 가짜(Proxy) 엔티티 객체 조회  
>            `-> 실제 사용할 때 쿼리를 날린다.

실제 엔티티를 상속받아서 만들어져 겉모양이 동일하다. 
실제 객체의 참조를 보관한다.

> instane of를 이용해서 비교해야한다.  
> Proxy는 원래 객체와 == 사용시 항상 true임을 보장해야한다.  
