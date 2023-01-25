package priceProcessor;

import java.math.BigDecimal;
import java.util.function.Function;

import model.Order;
import model.OrderLine;

public class OrderLineAggregationPriceProcessor implements Function<Order, Order>{

    @Override
    public Order apply(Order order) {
        // TODO Auto-generated method stub
        return order.setAmount(order.getOrderLines().stream()
                .map(OrderLine::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
