package hello.core.member;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
// import org.springframework.data.jpa.repository.JpaRepository;

import lombok.RequiredArgsConstructor;


@Component
// @RequiredArgsConstructor
// public class JpaMemberRepository implements MemberRepository {
    
//     private final EntityManager em;

//     @Override
//     public void save(Member member){
//         em.persist(member);
//     }

//     @Override
//     public Member findById(Long memberId){
//         return em.find(Member.class, memberId);
//     }
    
// }

public class JpaMemberRepository {

}