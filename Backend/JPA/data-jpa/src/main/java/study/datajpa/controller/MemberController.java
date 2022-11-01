package study.datajpa.controller;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id ){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 다음처럼 member를 바로 받아주면 도메인 컨버터가 작동해 자동으로 엔티티를 찾아준다.
     * 조회용으로만 가능하다. 간단한 것만 가능(데이터 변경하면 예외상황 고려를 많이 해야한다.)
     */
    @GetMapping("/members/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    /**
     * PagingAndSortingRepository의 findAll 메서드를 통해 페이징을 한다.
     * path변수로 page={num}&size={num}&sort={column},desc을 통해서 원하는 데이터만 가져올 수 있다.
     * 페이지의 기본값은 20개이다.
     * 
     * default 설정은 yml 파일이나 @PageableDefault 를 통해서 가능하다.
     */
    @GetMapping("/members")
    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable){
        Page<Member> members = memberRepository.findAll(pageable);
        return members; 
    }

    // DTO를 노출
    @GetMapping("/members")
    public Page<MemberDto> list2(@PageableDefault(size = 5) Pageable pageable){
        Page<Member> members = memberRepository.findAll(pageable);
        // Page<MemberDto> map = members.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        // Page<MemberDto> map = members.map(member -> new MemberDto(member));
        Page<MemberDto> map = members.map(MemberDto::new);
        return map; 
    }

    @PostConstruct
    public void init(){
        for(int i = 0; i <100; i++){
            memberRepository.save(new Member("member"+i));
        }
    }
}
