package jpabook.jpashop.repository.order.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    
    private final EntityManager em;

    /**
     * orderItems에 대한 쿼리를 따로 날리지 않고 끝낸다.
     * 이후에 따로 쿼리를 만들어서 넣어주어야한다.
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> results = findOrders();

        results.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return results;
    }


    /**
     * 쿼리를 한번 날린 이후에 맵으로 변형을 한뒤 
     * 메모리에서 매칭을 해서 값을 반환해준다.
     * 
     * 쿼리는 총 루트 1번, 컬렉션 1번으로 2번 실행된다.
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> results = findOrders();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(results));

        results.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return results;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> results){
        return results.stream()
        .map(o -> o.getOrderId())
        .collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds){
        List<OrderItemQueryDto> orderItems = em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi"+
            " join oi.item i"+
            " where oi.order.id in :orderIds", OrderItemQueryDto.class
        ).setParameter("orderIds", orderIds)
        .getResultList();

        return orderItems.stream()
            .collect(Collectors.groupingBy(OrderItemQueryDto -> OrderItemQueryDto.getOrderId()));
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId){
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi"+
            " join oi.item i"+
            " where oi.order.id = :orderId", OrderItemQueryDto.class
        ).setParameter("orderId", orderId)
        .getResultList();
    }

    private List<OrderQueryDto> findOrders(){
        return  em.createQuery(
                    "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o"+
                    " join o.member m"+
                    " join o.delivery d", OrderQueryDto.class
                ).getResultList();
    }


    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.qeury.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderprice, oi.count)"+
            " from Order o"+
            " join o.member m"+
            " join o.delivery d"+
            " join o.orderItems oi"+
            " join oi.item i", OrderFlatDto.class
        ).getResultList();

    }


}
