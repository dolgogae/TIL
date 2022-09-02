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

    public List<SimpleOrderDto> findOrderDtos() {
        return em.createQuery(
            "select new jpabook.jpashop.respoitory.OrderSimpleQeuryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" + 
            " join fetch o.member m" + 
            " join fetch o.delivery d", SimpleOrderDto.class).getResultList();
    }
}
