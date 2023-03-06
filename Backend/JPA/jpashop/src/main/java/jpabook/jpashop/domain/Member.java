package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "memeber_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    /**
     * 회원 입장에서 주문을 굳이 알 필요가 없을 수도 있다.
     * mappedBy가 붙으면 연관관계의 주인이 아니다.
     * 여기에 값을 넣는다고 해서 Order에 매핑되는 FK의 값이 변경되지 않는다.
     */
    @JsonIgnore // 주문정보가 빠지게 된다. api 호출시에
    @OneToMany(mappedBy = "member") // 읽기전용이 된다.
    private List<Order> orders = new ArrayList<>();
}
