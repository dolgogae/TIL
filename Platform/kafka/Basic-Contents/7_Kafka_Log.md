# Kafka Log

Kafka Log Segment File은 Data File이라고 부르기도 한다.  
server.properties 파일 안에서 `log.dirs` 파라미터로 정의한다.  
ex) test_topic의 partition0 -> /data/kafka/kafka-log-a/test_topic-0에 저장.  

## 파일명의 의미

000000000000AAAAAA.* 파일은 000000000000AAAAAA offset부터 000000000000BBBBBB offset까지의 메시지를 저장/관리 한다   
(AAAAAA, BBBBBB는 test_topic-0에 저장된 파일 이름)

leader-epoch-checkpoint : Leader Epoch와 관련 offset 정보를 저장  

### 파일 타입(형식)
.log : 메시지와 메타데이터 저장  
.index: 각 메시지의 offset을 log segment 파일의 byte 위치에 매핑  
.timeindex: 각 메시지의 timestamp를 기반으로 메시지를 검색하는데 사용  

.snapshot: Idempotent Producer 사용시 생성  
.txnindex: Transaction Producer 사용시 생성  

### Segment File
Segment가 새롭게 rolling 되는 옵션
```shell
log.segment.byte # default 1G
log.roll.ms      # default 168시간
log.index.size.max.bytes # default 10M
```

`__consumer_offset`의 Segment 파일은 별도로 관리된다.
```shell
offsets.topic.segment.bytes # default 100M
```


## Checkpoint file

`log.dirs` 에 위치.  

- replication-offset-checkpoint : 마지막으로 저장된 메시지의 id(High Water Mark) 

- recovery-point-offset-checkpoint : 데이터가 디스크로 flush된 지점 -> 메시지 손실 파악하기 위해
