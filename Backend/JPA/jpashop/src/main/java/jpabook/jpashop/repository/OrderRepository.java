package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.domain.QMember.*;
import static jpabook.jpashop.domain.QOrder.order;

@Repository
//@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em){
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


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

    public List<Order> findAll(OrderSearch orderSearch){
        return query.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        if(StringUtils.hasText(memberName)){
            return null;
        }
        return member.name.like(memberName);
    }

    private BooleanExpression statusEq(OrderStatus orderStatus){
        if(orderStatus == null){
            return null;
        }
        return order.status.eq(orderStatus);
    }

    /**
     * 
     * @return
     * 한번의 쿼리로 모두 가져올 수 있다.
     */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
            "select o from Order o" + 
            " join fetch o.member m" + 
            " join fetch o.delivery d", Order.class).getResultList();
    }

    /**
     * 
     * @return
     * order - orderItem과의 join의 경우는 
     * order가 orderItem의 갯수만큼 중복해서 조회하게 된다.
     * 따라서 JPA에서 가져오는 data가 N배가 되어버린다.(1:N 관계이기 때문이다.)
     * 
     * 이 문제점을 해결하기 위해서는 distinct를 써주면 된다.
     * DB의 distinct와는 다르게 JPA 자체적으로 order만 같은 id 값이면 중복을 제거해준다
     * (DB는 모든 컬럼이 같은 것의 중복제거만 해준다.)
     * 
     * 1:N의 fetch join은 한개까지만 할 수 있다.
     * 여러개를 하게 된다면 JPA가 못맞출수 있다.
     */
    public List<Order> findAllWithItem() {
        return em.createQuery("select distinct o from Order o"+
                                " join fetch o.member m"+
                                " join fecch o.delivery d"+
                                " join fetch o.orderItem oi"+
                                " join fetch oi.item i", Order.class)
                // .setFirstResult(1)
                // .setMaxResults(100)     // 다음과 같이 페이징을 할 수 있지만 문제점이 있다.
                                        // 쿼리에서 limit offset 이 생성되지 않는다.
                                        // warning log를 확인하면 collection fetch가 있다. -> 메모리에서 페이지 소팅을 해버린다.
                                        // fetch join을 하는 순간 1:N 관계에서 중복제거를 JPA가 메모리상에서 하기 때문에
                                        // 기존의 소팅 기준이 틀어지게 된다. 따라서 JPA는 메모리 상에서 페이징을 하게 되어서
                                        // 데이터가 많은 경우에는 많은 양의 자원을 요구하게 된다.
                                        // 1:N fetch join의 경우에는 페이징을 절대 하면 안된다.
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        
        return em.createQuery(
            "select o from Order o" + 
            " join fetch o.member m" + 
            " join fetch o.delivery d", Order.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
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
