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
 * Inheritance: JOINED - 조인 전략
 *              SINGLE_TABLE - 단일 테이블 전략
 *              TABLE_PER_CLASS - 각각의 테이블로 분리
 *
 * 조인 전략
 * 장점: 정규화가 되어 있고, 저장공간이 효율적이다.
 * 단점: 조회시 조인을 많이 사용하고, 조회쿼리가 복잡하다. 저장시에는 쿼리가 2번 나가게 된다.
 *
 * 단일 테이블 전략
 * 장점: 조회성능과 단순함
 * 단점: 쓸데없는 데이터까지 많이 들어간다. 테이블이 필요보다 커진다.
 *
 * 각각 테이블 분리
 * [추천하지 않는다.] 여러 테이블 함께 조
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
