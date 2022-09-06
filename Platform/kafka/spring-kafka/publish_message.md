# publish message

## KafkaTemplate
트랜잭션 사용하지 않으면, Singleton으로 생성 -> 지연현상 발생 가능성 있음  
producerPerThread=true : 각 스레드에서 별도의 생성자를 만들고 캐시처리  

기본적으로 비동기 처리  
Message<>, ProducerRecord<K,V> 이용  

- KafkaSendCallback: 쉽게 메시지를 확인할 수 있음

## RoutingKafkaTemplate
전송하는 토픽별로 옵션을 다르게 설정할 수 있다.  
토픽명을 정규식(Regular Expression)으로 표현 가능

## ReplyingKafkaTemplate
Consumer가 특정 데이터를 전달 받았는지 여부 확인 가능  
- KafkaHeader.CORRELATION_ID: 요청과 응답을 연결시키는데 사용
- KafkaHeader.REPLY_TOPIC: 응답 토픽
- KafkaHeader.REPLY_PARTITION: 응답 토픽의 파티션

`AggregatingReplyingKafkaTemplate`: 여러 응답을 한번에 처리

## Consumer

KafkaMessageListenerContainer
- single thread

ConcurrentMessageListenerContainer
- KafkaMessageListenerContainer 인스턴스를 1개 이상 사용하는 multi-thread
- start, stop등 메서드를 foreach로 순차적으로 실행

### @KafkaListener

spring boot 기준으로 KafkaAutoConfiguration으로 모든 것이 기본 설정되어 있다.(별도의 config 불필요)  
다양한 설정을 property로 손쉽게 가능하다.  

1. Payload Validator
- 수신하는 객체가 유효한지 아닌지 판단하는 도구  
LocalValidatorFactoryBean: @NotEmpty, @Min, @Max, @Email과 같은 기본적인 유효성 체크 사용 가능  

2. Retrying Deliveries
리스너에서 에러 발생시 Container Error Handler가 동작  
RetryingMessageListenerAdapter를 통해 retry 기능 호출  

3. Retry Stateful
BackOffPolicy를 이용해 재시도 하는 과정에서 Consumer Thread가 중지될 수 있음  
- 재시도하는 동안 poll()이 수행되지 않기 때문
- session.timeout.ms: 설정된 시간안에 heartbeat를 받지 못하면 Consumer group에서 제거하고 rebalance 발생
- max.poll.interval.ms: 설정단 시간안에 poll()이 호출되지 않으면 Consumer가 죽었다고 판단해 할당 파디션이 revoke되고 rebalance 발생