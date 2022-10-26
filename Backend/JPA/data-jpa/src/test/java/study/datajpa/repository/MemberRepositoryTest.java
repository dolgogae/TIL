package study.datajpa.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    
    @Autowired MemberRepository memberRepository;

    @Test
    public void testMember(){
        System.out.println("memberRepository = " + memberRepository.getClass());
        Member member = new Member("oh");
        Member saveMember = memberRepository.save(member);

        // 원래는 Optional로 받은 뒤에 예외처리를 해주는 것이 좋다.
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); // 같은 트랜잭션 안에서는 동일성을 보장하기 때문에 true가 나온다.
    }

    @Test
    void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
        
        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);
        
        // 전체 조회
        List<Member> members = memberRepository.findAll();
        Assertions.assertThat(members.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        // 삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo(m1.getUsername());
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(m1.getAge());
    }

    @Test
    void findByUsername(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        
        Assertions.assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    void findByUser(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        
        Assertions.assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> resultList = memberRepository.findListByUsername("AAA");
        Member result = memberRepository.findMemberByUsername("AAA");
        Optional<Member> resultOptional = memberRepository.findOptionalByUsername("AAA");
    }

    @Test
    void paging(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0,3,Sort.Direction.DESC, "username");

        // totalCount는 따로 필요가 없다. => 알아서 생성해준다.
        // pageable을 구현해서 넘기는 것은 PageRequest를 통해서 많이 날려준다.
        // list 타입으로 받아오는 것도 가능하다.
        
        // totalCount가 성능에 영향을 미치는 경우가 많다.
        // count 쿼리는 조인할 필요가 없다.
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Slice<Member> slice = memberRepository.findMemberByAge(age, pageRequest);


        // 만약 API로 제공하게 될 경우 다른것과 마찬가지로 page도 엔티티를 바로 노출하면 안된다.
        // 따라서 아래처럼 DTO를 별도로 만들어 제공하는 것이 좋다.
        page.map(member -> new MemberDto(member.getId(), member.getUsername(), null)); 

        List<Member> content = page.getContent();
        
        long totalElements = page.getTotalElements();

        // for(Member member: content){
        //     System.out.println("member = " + member);
        // }
        // System.out.println("totalElements = " + totalElements);

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

        Assertions.assertThat(slice.getNumber()).isEqualTo(0);
        Assertions.assertThat(slice.isFirst()).isTrue();
        Assertions.assertThat(slice.hasNext()).isTrue();
    }
}