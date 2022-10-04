package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * DDL: 데이터베이스 스키마 자동생성
 * 애플리케이션 개발시점에서 사용한다.
 * 운영장비에는 절대 create, create-drop, update를 사용하면 안된다.
 * 개발 초기 : create, update
 * 테스트 서버: update, validate
 * 스테이징 운영: validate, none
 */
@Entity
public class Member1 {
    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    // insertable: DB에 넣을 수 있는지
    // updatable: DB에 업데이트 쿼리를 넣을 수 있는지
    // nullable: null인지 아닌지
    // unique: unique 제약조건, 잘쓰지 않는다.
    // columnDefinition: 직접 조건을 써줄 수 있다.
    @Column(name = "name", insertable = false, updatable = false, nullable = false, unique = true, columnDefinition = "varchar(100) default 'EMPTY'")  // 런타임에 영향을 주지 않는다.
    private String username;
    private Integer age;

    // 필수로 STRING을 써야한다. ORDINAL 추가시 마이그레이션의 별도 작업이 필요.
    // ORDINAL을 하게 되면 숫자로 들어가기 때문에 기본에 ENUM에 변수가 추가되면 그 의미가 섞일수 있다.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 날짜 타입의 경우에 붙혀준다.
    // 현재는 필요가 없다. Java8의 경우에는 LocalDate, LocalDateTime을 자료형으로 하면 쓸 필요가 없다.
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // 문자일 경우 clob, 나머지는 blob으로 매핑
    @Lob
    private String description;

    // DB와 매핑을 안할 수 있는 멤버 변수
    @Transient
    private int temp;

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
