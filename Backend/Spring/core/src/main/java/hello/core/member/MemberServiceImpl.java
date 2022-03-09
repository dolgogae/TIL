package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberServiceImpl implements MemberService{

    // 구현체에 의존적이다.
    private final MemberRepository memberRepository;

    @Autowired // 등록된 빈을 이용해 자동으로 찾아와 등록해준다.
    public MemberServiceImpl(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트용
    public  MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
