package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * entity에는 presentation logic이 있으면,
     * entity - presentation(api) 간의 양방향 의존관계가 생긴다.
     * entity를 직접 반환하면 안된다.(아래 postmapping이유와 비슷한 이유)
     *
     * array가 넘어가게 되면, Json의 기본 틀이 깨지게된다.
     * 추가 데이터가 있을때 스펙을 확장할수 없거나 유연성이 떨어진다.
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    /**
     * 가급적 필요한 것만 노출하는 것이 좋다.
     * 유지보수하는 입장에서 api spec과 dto가 동일한게 좋다.
     */
    @GetMapping("/api/v2/members")
    public Result MemberV2(){
        List<Member> findMembers = memberService.findMembers();

        List<MemberDto> collect = findMembers.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    /**
     * api 요청스펙에 맞게 dto를 만드는 것이 중요하다.
     * 그 이유로는 entity는 많은 사람들이 접근하는데,
     * entity에 의존하게 소스코드를 사용할 시에(validation이나 member변수, api 스펙변경명 등..)
     * entity가 변경될 경우엔 에러가 나게 된다.
     * 또한, 실무에서는 회원등록 api같은 경우에도 여러개의 종류의 회원등록이 생길수 있다.(간편등록, kakao 등)
     * 따라서 api와 entity와 1:1 매핑을 하는 것보다 별도의 객체를 만들어주는 것이 중요하다.
     *
     * entity를 파라미터로 받지 말고, 가급적 외부에도 노출하지 않는 것이 좋다.
     *
     * **@Valid 어노테이션의 경우에는 도메인에 걸어놓은 제약조건에 대해서 체크해주는 것이다.
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

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());

        log.info("request name {}", request.getName());
        /**
         * update를 할때 반환값을 member로 하는 것보다
         * command와 query를 분리하는 것이 역할을 분리하는 것이
         * 유지 보수 입장에서 좋다.
         */
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(member.getId(), member.getName());
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
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
