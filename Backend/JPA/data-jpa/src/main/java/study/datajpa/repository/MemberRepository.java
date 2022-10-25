package study.datajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
