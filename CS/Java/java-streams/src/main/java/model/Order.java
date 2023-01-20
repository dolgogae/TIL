package model;

import java.math.BigDecimal;
import java.util.List;

public class Order {

    private long id;
    private BigDecimal amount;
    private OrderStatus status;
    private List<OrderLine> orderLines;

    public enum OrderStatus {
        CREATED, ERROR, PROCESSED
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }
    public Order setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
        return this;
    }

    public long getId() {
        return id;
    }

    public Order setId(long id) {
        this.id = id;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Order setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Order setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}
