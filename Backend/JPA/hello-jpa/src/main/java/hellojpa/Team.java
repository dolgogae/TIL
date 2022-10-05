package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    /**
     * team 1개에 member가 여러개이므로 OneToMany
     * mappedBy는 Member class에서 team으로 맵핑이 되어있다는 뜻이다.
     *
     * 서로다른 단방향 연관관계 2개를 합쳐놓아 양방향 연관관계라고 할 수 있다.(DB 테이블과는 차이점이 생긴다.)
     * 여기서 생기는 문제로 member에 있는 team을 변경해야하나, team에 있는 member를 변경해야하나라는 딜레마가 생긴다.
     * => 둘중 하나로만 외래키를 관리해야 한다.
     * => 연관관계의 주인을 정해야한다. 이 주인만이 외래키를 등록, 수정이 가능하다.(나머지는 읽기만 가능)
     * => 주인은 mappedBy를 사용하지 않는다.
     *
     * 테이블에서 외래키가 있는 곳을 주인으로 정하는 것이 좋다.
     * team을 바꾸게 되면 member에도 update 쿼리가 날아가고 복잡해지게 된다.
     * N쪽이 연관관계의 주인이 된다.
     *
     * 만약 team이 연관관계의 주인이 된다면 1:N 연관관계가 된다.
     * 하지만 update 쿼리가 많이 날아가게 된다는 점이 있다.
     * 실무에선 테이블이 한두개가 아닌데 team이 아닌 member에도 update 쿼리가 날아간다는 점에서 관리 추적이 힘들 수 있다.
     */
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
    // @JoinColumn(name = "team_id")
    // private List<Member> members = new ArrayList<>();

    public void addMember(Member member){
        member.setTeam(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
