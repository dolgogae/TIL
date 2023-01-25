package service.orderprocess;

import java.util.Optional;
import java.util.function.Consumer;

import javax.sound.sampled.Port;

import model.Order;

public class OrderProcessStep {
    private final Consumer<Order> processOrder;
    private OrderProcessStep next;
    
    public OrderProcessStep(Consumer<Order> processOrder){
        this.processOrder = processOrder;
    }

    public OrderProcessStep setNext(OrderProcessStep next){
        if (this.next == null){
            this.next = next;
        } else{
            this.next.setNext(next);
        }
        return this;
    }

    public void process(Order order){
        processOrder.accept(order);
        Optional.ofNullable(next)
            .ifPresent(nextStep -> nextStep.process(order));
    }
}
