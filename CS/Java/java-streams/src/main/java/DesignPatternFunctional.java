import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import designpatternmodel.DUser;
import model.Order;
import model.OrderLine;
import model.Price;
import model.Order.OrderStatus;
import service.BasicPriceProcessor;
import service.DiscountPriceProcessor;
import service.PriceProcessor;
import service.TaxPriceProcessor;
import service.emailservice.EmailProvider;
import service.emailservice.EmailSender;
import service.emailservice.MakeMoreFriendEmailProvider;
import service.emailservice.VerifyYourEmailAddressEmailProvider;
import service.orderprocess.OrderProcessStep;
import service.userservice.InternalUserService;
import service.userservice.UserService;
import service.userservice.UserServiceFunctionalWay;

public class DesignPatternFunctional {
    public static void main(String[] args){

        /**
         * Builder Pattern
         */
        // DUser user = DUser.builder(1, "Alice")
        //         .withEmailAddress("alice@email.com")
        //         .withVerified(true)
        //         .build();
        DUser user1 = DUser.builder(1, "Alice")
                .with(builder -> {
                    builder.email = "alice@email.com";
                    builder.verified = false;
                    builder.friendsIds = Arrays.asList(201,202,203,204,211,212,213,214);
                }).build();
        DUser user2 = DUser.builder(2, "Bob")
                .with(builder -> {
                    builder.email = "bob@email.com";
                    builder.verified = true;
                    builder.friendsIds = Arrays.asList(212,213,214);
                }).build();
        
        DUser user3 = DUser.builder(3, "Charlie")
                .with(builder -> {
                    builder.email = "charlie@email.com";
                    builder.verified = true;
                    builder.friendsIds = Arrays.asList(201,202,203,204,211,212);
                }).build();
        // System.out.println(user);

        /**
         * Decorator Pattern
         */
        Price unprocessedPrice = new Price("Original Price");

        PriceProcessor basicPriceProcessor = new BasicPriceProcessor();
        PriceProcessor discountPriceProcessor = new DiscountPriceProcessor();
        PriceProcessor taxPriceProcessor = new TaxPriceProcessor();

        // 원하는대로 추가하여 사용 가능하다.
        PriceProcessor decoratedPriceProcessor = basicPriceProcessor
                .andThen(discountPriceProcessor)
                .andThen(taxPriceProcessor);
        Price processedPrice = decoratedPriceProcessor.process(unprocessedPrice);
        // System.out.println(processedPrice.getPrice());

        // 바로정의해서 추가하는 것도 가능하다.
        // 하지만 재활용이 불가능해서 어느것이 나은지는 판단해야한다.
        // + 복잡한 기능을 데코레이터로 넣으면 가독성이 떨어지지 않을까?
        PriceProcessor decoratedPriceProcessor2 = basicPriceProcessor
                .andThen(taxPriceProcessor)
                .andThen(price -> new Price(price.getPrice() + ", then apply another procedure"));
        
        /**
         * Stategy Pattern
         */
        List<DUser> users = Arrays.asList(user1, user2, user3);

        EmailSender emailSender = new EmailSender();
        EmailProvider verifyEmailAddressEmailProvider = new VerifyYourEmailAddressEmailProvider();
        EmailProvider makeMoreFriendsEmailProvider = new MakeMoreFriendEmailProvider();

        emailSender.setEmailProvider(verifyEmailAddressEmailProvider);
        users.stream()
                .filter(user -> !user.isVerified())
                .forEach(emailSender::sendEmail);
        emailSender.setEmailProvider(makeMoreFriendsEmailProvider);
        users.stream()
                .filter(user -> !user.isVerified())
                .filter(user -> user.getFriendsIds().size() <= 5)
                .forEach(emailSender::sendEmail);
        emailSender.setEmailProvider(user->"'Play with friends' email for " + user.getName());
        users.stream()
                .filter(user -> user.isVerified())
                .filter(user -> user.getFriendsIds().size() > 5)
                .forEach(emailSender::sendEmail);
        
        /**
         * Template Method
         * 상위 클래스는 알고리즘의 뼈대만을 정의하고 알고리즘의 각 단계는 하위 클래스에게 정의를 위임하는 패턴
         */
        UserService userService = new UserService();
        InternalUserService internalUserService = new InternalUserService(); 

        userService.createUser(user1);
        internalUserService.createUser(user1);

        // 기존보다 유연하게 적용이 가능하다.
        UserServiceFunctionalWay userServiceFunctionalWay = new UserServiceFunctionalWay(
                user -> {
                    System.out.println("Validating user "+ user.getName());
                    return user.getName() != null && user.getEmail().isPresent();                            
                }, 
                user -> {
                    System.out.println("Writing user "+user.getName()+" to DB");
                });
        userServiceFunctionalWay.createUser(user1);

        /**
         * Chain of Responsibility Pattern
         */
        OrderProcessStep initailizeStep = new OrderProcessStep(order -> {
            if(order.getStatus() == OrderStatus.CREATED){
                System.out.println("Start processing order " + order.getId());
                order.setStatus(OrderStatus.IN_PROGRESS);
            }
        });
        OrderProcessStep setOrderAmountStep = new OrderProcessStep(order->{
            if(order.getStatus() == OrderStatus.IN_PROGRESS){
                System.out.println("Setting amount of order "+ order.getId());
                order.setAmount(order.getOrderLines().stream()
                    .map(OrderLine::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            }
        });
        OrderProcessStep verifyOrderStep = new OrderProcessStep(order->{
            if(order.getStatus() == OrderStatus.IN_PROGRESS){
                System.out.println("Verifying order " + order.getId());
                if(order.getAmount().compareTo(BigDecimal.ZERO) <= 0){
                    order.setStatus(OrderStatus.ERROR);
                }
            }
        });
        OrderProcessStep processPaymentStep = new OrderProcessStep(order->{
            if(order.getStatus() == OrderStatus.IN_PROGRESS){
                System.out.println("Processing payment of order" + order.getId());
                order.setStatus(OrderStatus.PROCESSED);
            }
        });
        OrderProcessStep handleErrorStep = new OrderProcessStep(order->{
            if(order.getStatus() == OrderStatus.ERROR){
                System.out.println("Sending out 'Failed to process order' alert for order" + order.getId());
            }
        });
        OrderProcessStep completeProcessingOrderStep = new OrderProcessStep(order->{
            if(order.getStatus() == OrderStatus.PROCESSED){
                System.out.println("Finished processing order " + order.getId());
            }
        });

        OrderProcessStep chainedPrderProcessStep = initailizeStep
                .setNext(setOrderAmountStep)
                .setNext(verifyOrderStep)
                .setNext(processPaymentStep)
                .setNext(handleErrorStep)
                .setNext(completeProcessingOrderStep);
        
        Order order = new Order()
                .setId(1001L)
                .setStatus(OrderStatus.CREATED)
                .setOrderLines(Arrays.asList(
                    new OrderLine().setAmount(BigDecimal.valueOf(1000)),
                    new OrderLine().setAmount(BigDecimal.valueOf(2000))
                ));
        chainedPrderProcessStep.process(order);
    }
}
