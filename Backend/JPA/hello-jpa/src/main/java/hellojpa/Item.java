package hellojpa;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


/**
 * 아무것도 달아주지 않고 상속만 할 경우에는 단일 테이블 전략으로 DB에 매핑된다.
 * 
 * Inheritance: JOINED - 조인 전략으로 매핑된다.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn    // 테이블에 DTYPE이 생긴다.
                        // 있는 것이 좋다. 왜냐면 DB입장에서 구분할 수 있기 때문이다.
public abstract class Item {
    
    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
