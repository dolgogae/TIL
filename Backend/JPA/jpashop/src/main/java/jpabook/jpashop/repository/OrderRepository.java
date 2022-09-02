package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    /**
     * Criteria
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);}
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대1000건
        return query.getResultList();
    }

    /**
     * 
     * @return
     * 한번의 쿼리로 모두 가져올 수 있다.
     */
    public List<Order> findAllWithMemberRepository() {
        return em.createQuery(
            "select o from Order o" + 
            " join fetch o.member m" + 
            " join fetch o.delivery d", Order.class).getResultList();
    }

    /**
     * 
     * @return
     * 식별자를 넣기 때문에 order를 그대로 new 객체의 인자로 넣어주면 안된다.
     * -> 다른 객체로 분리하는 것이 좋다. to OrderSimpleRepository
     */
    // public List<SimpleOrderDto> findOrderDtos() {
    //     return em.createQuery(
    //         "select new jpabook.jpashop.respoitory.OrderSimpleQeuryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" + 
    //         " join fetch o.member m" + 
    //         " join fetch o.delivery d", SimpleOrderDto.class).getResultList();
    // }
}
