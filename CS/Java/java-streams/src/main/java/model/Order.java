package model;

import java.math.BigDecimal;

public class Order {

    private long id;
    private BigDecimal amount;
    private OrderStatus status;

    public enum OrderStatus {
        CREATED, ERROR, PROCESSED
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
