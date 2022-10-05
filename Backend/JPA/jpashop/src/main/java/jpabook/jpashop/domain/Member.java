package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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
     */
    @JsonIgnore // 주문정보가 빠지게 된다. api 호출시에
    @OneToMany(mappedBy = "member") // 읽기전용이 된다.
    private List<Order> orders = new ArrayList<>();
}
