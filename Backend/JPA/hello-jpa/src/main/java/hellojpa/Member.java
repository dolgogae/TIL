package hellojpa;

import org.hibernate.annotations.NotFound;
import org.hibernate.internal.build.AllowPrintStacktrace;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/*
 * @SequenceGenerator: sequence와 매핑하는 annotation
 * @TableGenerator: table전략과 매핑하는 annotation
 */

/**
 * Sequence 전략
 * DB에서 sequence를 일단 가져와야한다. => next value 쿼리가 날아가게 된다.(그 후에 영속성 컨텍스트에 저장)
 * allocationSize: 미리 DB에 50를 쌓아놓는다. => next value 쿼리를 최소화 시킬 수 있다.(메모리에서 호출)
 */
//@SequenceGenerator(
//        name = "member_seq_generator",
//        sequenceName = "member_seq",
//        initialValue = 1, allocationSize = 1
//)
//@TableGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES",
//        pkColumnValue = "MEMBER_SEQ", allocationSize = 50   // 한번의 next call에서 50개씩 받아온다.
//)
@Entity // JPA와 매핑해준다.
@Table(name = "MBR") // 실제 DB Table과 매핑된다.
public class Member extends BaseEntity {

    public Member() {
    }

    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * @GeneratedValue 전략
     * identity: DB에 위임하는 것.(DB에서 자동생성) => em.persist를 호출하는 시점에서 select 쿼리가 날라간다.
     *                                           `-> DB에서 pk값을 받아와야하기 때문에
     * sequence: oracle에서 많이 사용된다. sequence에서 값을 가져온뒤 저장한다(sequence는 DB에 있는 개념..)
     * table: DB table의 전략으로 매핑된다. 성능상의 이슈가 있다. 잘 쓰이지 않는다.
     *
     * 기본키 제약조건
     * not null, 유일, 변경불가
     * 미래까지 조건을 만족하는 자연키를 찾기 어렵다.(변경 불가라는 조건에서) => 대리키를 이용하는 것이 좋은 전략이다.
     * 권장: Long + 대체키 + 키 생성전략 사용
     */
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    /**
     * 회원과 팀은 team 1:N member 관계이다.
     * 따라서 member 입장에서는 ManyToOne으로 해주어야 한다.
     * JoinColumn은 테이블에서 매칭되는 컬러값이다.
     *
     * 지연 로딩(lazy loading): 한번에 모든 정보를 긁어오는것이 아닌 호출 시에 해당 값을 채워 넣는 개념.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    /**
     * 1:1 연관관계는 DB상 주 테이블에 주인을 주는 것이 좋다.
     * 예시에서는 member가 locker를 가지므로 member를 주인으로 하는 것이 좋다.
     * 반대 방향으로 한다면 양방향을 설정해주어야 한다.
     */
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
        // 세팅하는 시점에 넣어주면 실수를 줄일 수 있다.
        team.getMembers().add(this);
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * toString을 만들거나 JSON을 생성할 때, 무한 호출을 주의해야한다.
     * member->team->member->team->...
     */
//    @Override
//    public String toString() {
//        return "Member{" +
//                "id=" + id +
//                ", username='" + username + '\'' +
//                ", team=" + team +
//                ", locker=" + locker +
//                '}';
//    }
}