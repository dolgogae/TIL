package study.datajpa.repository;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

/**
 * 사용자 정의 레포지토리의 이름은 규칙을 따라야한다.
 * 상속해줄 Spring data JPA의 이름에 Impl을 붙혀줘야 한다.(예시: MemberRepository + Impl)
 * 규칙을 바꿀수 있지만 안하는 것을 추천한다.
 * 
 * 복잡한 동적 쿼리를 쓸때 좋다.
 * 항상 사용자 정의 리포지토지가 필요한 것은 아니다.
 * 
 * 핵심 비즈니스 로직을 위한 레포지토리와 화면을 위한 레포를 분리하는 것이 좋다.
 * 그리고 사용자 정의 리포지토리가 아닌 별도의 @Repository를 이용해서 JPA만 사용하는 것도 좋다.
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        
        return em.createQuery("select m from Member m")
                .getResultList();
    }
    
}
