package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    
    @Autowired MemberRepository memberRepository;

    
    @Test
    public void testMember(){
        Member member = new Member("oh");
        Member saveMember = memberRepository.save(member);

        // 원래는 Optional로 받은 뒤에 예외처리를 해주는 것이 좋다.
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); // 같은 트랜잭션 안에서는 동일성을 보장하기 때문에 true가 나온다.
    }
}
