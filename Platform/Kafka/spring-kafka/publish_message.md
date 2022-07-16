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
