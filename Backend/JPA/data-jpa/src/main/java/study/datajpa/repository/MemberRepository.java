package study.datajpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

/**
 * 구현체는 spring data jpa가 만들어서 인젝션을 해주게 된다.
 * spring에서 프록시 객체를 통해서 알아서 구현체를 만들게 된다.
 * 
 * JpaRepository는 package org.springframework.data.jpa.repository내의 패키지에 있다.
 * JpaRepository가 상속받는 class인 PagingAndSortingRepository는 package org.springframework.data.repository 내에 있다.
 * 
 * PagingAndSortingRepository는 공통된 spring data의 특성을 갖고 있고,
 * JpaRepository는 jpa에 특화된 인터페이스이다.
 * 
 * 만약 공통의 기능이 아닌 다른 기능이 필요할 땐?
 * 이름을 통해서 쿼리를 만들어 주는 기능이 있다.
 * 
 * 추가적인 장점으로는 컴파일 타임에서 오타를 잡아줄 수 있다.
 */
// @Repository --> 생략 가능
public interface MemberRepository extends JpaRepository<Member, Long>{
 
    /**
     * @param username
     * @param age
     * @return
     * 관례를 통해 이름을 가지고 쿼리를 만들어준다.
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 네임드 쿼리를 우선순위로 찾고 없을 경우엔 관례를 통해 찾는다.
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 이 기능 사용시에 어플리케이션 로딩 시점(컴파일 타임)에서 오류를 잡을 수 있다.
    // 이름이 없는 네임드 쿼리라고 생각하면 된다.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // 다음과 같이 DTO를 통해서 반환이 가능하다.
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 컬렉션 바인딩은 다음과 같이 할 수 있다.
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // 단건 조회의 경우에는 null로 반환해버린다.
    // Optional로 해결해주는 것이 좋다.
    Member findMemberByUsername(String username);
    
    // 리스트가 만약 결과가 없다면 null이 아닌 비어있는 컬렉션을 반환해준다.
    List<Member> findListByUsername(String username);

    // 2건 이상이 반환될때 예외를 터트려준다.
    Optional<Member> findOptionalByUsername(String username);

    // 다음처럼 count 쿼리를 별도로 분리해서 성능상의 이점을 볼 수 있다.
    // 보통 pageing에서는 totalCount가 성능에 영향을 많이 미치는데 여기서 join 이후에 count할 필요까진 없기 때문에 분리하는 게 좋다.
    @Query(value = "selet m from Member m left join m.team t", countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findMemberByAge(int age, Pageable pageable);

    // excuteResult와 같은 기능을 한다. 업데이트 갯수를 반환하도록 하는 역할(변경한다는 것을 알려주는 것)
    // clearAutomatically => test에 설명
    @Modifying(clearAutomatically = true) 
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
