package jpabook.jpashop.repository.order.simplequery;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import jpabook.jpashop.repository.SimpleOrderDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderSimpleRepository {

    private final EntityManager em;

    /**
     * order를 직접 넣어주게 되면, 식별자로 변환되어서 제대로 되지 않는다.
     * 따라서 아래처럼 하나하나 값을 변수로 넣어주어야 한다.
     * 
     * 나가는 쿼리를 보면 내가 원하는 컬럼만 select하게 된다.
     */
    public List<SimpleOrderDto> findOrderDtos() {
        return em.createQuery(
            "select new jpabook.jpashop.respoitory.OrderSimpleQeuryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" + 
            " join fetch o.member m" + 
            " join fetch o.delivery d", SimpleOrderDto.class).getResultList();
    }
}
