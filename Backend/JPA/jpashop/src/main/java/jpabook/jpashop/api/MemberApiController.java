package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     *
     * @param member
     * @return
     * api 요청스펙에 맞게 dto를 만드는 것이 중요하다.
     * 그 이유로는 entity는 많은 사람들이 접근하는데,
     * entity에 의존하게 소스코드를 사용할 시에(validation이나 member변수 명 등..)
     * entity가 변경될 경우엔 에러가 나게 된다.
     * 또한, 실무에서는 회원등록 api같은 경우에도 여러개의 종류의 회원등록이 생길수 있다.(간편등록, kakao 등)
     * 따라서 api와 entity와 1:1 매핑을 하는 것보다 별도의 객체를 만들어주는 것이 중요하다.
     *
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest{
        private String name;

    }
}
