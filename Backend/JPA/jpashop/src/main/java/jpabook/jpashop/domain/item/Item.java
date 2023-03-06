package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.CategoryItem;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)   // 모든 자식 클래스를 합쳐서 하나의 테이블로 만들겠다.
@DiscriminatorColumn
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    protected Long id;

    protected String name;
    protected int price;
    protected int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categories = new ArrayList<>();

    //===비즈니스 로직===//

    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
