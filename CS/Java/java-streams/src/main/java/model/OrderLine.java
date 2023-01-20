package model;

import java.math.BigDecimal;

public class OrderLine {

    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderLine setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }
}
