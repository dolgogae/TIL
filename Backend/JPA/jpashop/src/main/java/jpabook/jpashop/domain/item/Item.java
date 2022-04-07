package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.CategoryItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // 하나의 테이블로 만들겠다.
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categorys = new ArrayList<>();

    private String name;
    private int price;
    private int stockQuantity;
}
