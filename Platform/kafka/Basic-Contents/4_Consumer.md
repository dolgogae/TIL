# Consumer

각각 고유의 속도로 Commit Log로부터 순서대로 Poll.  
다른 Consumer group에 속한 Consumer들은 서로 관련이 없고, Commit Log에 있는 Record를 동시에 다른 위치에서 read 가능.  
commit을 많이 한다면 그만큼 부하가 많이 가게 되는것이다.(다른서버끼리 네트워크로 통신을 하기 때문이다.)

- consumer offset: consumer가 자동, 수동으로 읽은 데이터의 위치를 commit하여 다시 읽음 방지.  
- > __consumer_offsets라는 interal topic에서 consumer offset을 저장하여 관리(ex. GroupB:MyTopic:P0:7)

Partition이 2개 이상인 경우 모든 메시지에 대한 **전체 순서 보장 불가능**
- partition별로 각각 동작하기 때문이다.
- partition을 하나로 하면 정렬이 되지만 처리 순서는 떨어진다.(병렬처리가 안되기 때문)
- 동일한 key를 가진 메시지는 동일한 partition에만 전달되어 key레벨의 순서 보장 가능.(운영중 partition 개수 변경시 순서 보장 불가능)

## 내부 구조됨
Kafka cluster -> Fetcher(completedFetches) --pull()-> ConsumerRecords  
                 \_________[컨슈머 애플리케이션]_____________________/  
`Fetcher`: 리더 파티션으로부터 레코드들을 미리 가져와서 대기  
`poll()`: Fetcher에 있는 레코드들을 리턴하는 레코드  
`ConsumerRecords`: 처리하고자 하는 레코드들의 모음. 오프셋이 포함  

## Consumer group

4개의 파티션이 있는 Topic을 conusme하는 4개의 Consumer가 하나의 Consumer group에 있다면 각 consumer는 정확히 하나의 partition에서 record를 consume.  
> 같은 group내의 consumer는 partition을 분배받음  

partition은 항상 consumer group내의 하나의 consumer에 의해서만 사용.  
consumer는 주어진 topic에서 0개 이상의 많은 partition을 사용 가능하다.

> 동일한 Topic에서 consume하는 여러 Consumer Group이 있을 수 있음.  

서로 다른 컨슈머 그룹은 다른 로직으로 되어있다. 목적에 따라 분리를 하는 개념이다.  
적당하게 할당되어서 컨슈머 그룹의 컨슈머가 파티션을 할당 받는다.  

### Consumer group을 사용하는 이유
데이터를 저장하는 과정에서 `리소스 수집 에이전트`가 필요하다.  
이 `리소스 수집 에이전트`가 서로 다른 DB들이 커플링 되어있으면 하나가 장애가 난다면 나머지들도 대기를 해야하는 경우가 있다.  
컨슈머 그룹은 이러한 점을 그룹으로써 분리함으로써 커플링을 해제할 수 있다.
> 동일한 파티션으로 다른 컨슈머 그룹을 사용함.
> 예를 들면, 같은 데이터를 엘라스틱 서치와 하둡에 넣어야 한다면 컨슈머 그룹을 분리하고 동일한 파티션에서 데이터를 가져오면 커플링을 해소할 수 있다.

## Cardinality
특정 데이터 집합에서 유니크한 값의 개수

- Key Cardinality: Consumer group의 개별 consumer가 수행하는 작업의 양에 영향. 
- 작업 부하가 고르게 되게함
- key는 Avro, json등 여러 필드가 있는 복잡한 객체일 수 있다.

## Consumer failure
consumer를 rebalanceing을 통해서 재배치된다.
다른 consumer(같은 group 내)가 fail된 consumer의 partition 하나를 가져가게 된다.

## Position
Last Commited Offset(Current Offset): Consumer가 최종 Commit한 Offset   
Current Position: Consumer가 읽어간 위치(처리중, Commit전).  
High Water Mark(복제완료된)): ISR(Leader-Follower)간에 복제된 Offset.  
Log End Offset: Producer가 메시지를 보내서 저장된, 로그의 맨 끝 Offset.

## Consumer Lag
Consumer lag = (Log End Offset) - (Last Commited Offset).
컨슈머가 정상 동작하는지 여부를 확인할 수 있기 때문에 컨슈머 애플리케이션을 운영한다면 필수적으로 모니터링해야 하는 지표이다.  
랙을 모니터링하고 만약 랙이 늘어나 지연이된다면, 파티션을 늘리고 컨슈머를 늘리면서 대응이 가능하다는 점이다.  

### monitoring
`kafka-consumer-groups.sh`
- 특정 컨슈머 그룹의 상태를 확인이 가능하다. 
- 일회성에서 그치기 때문에 운영측면에서는 쓰기엔 부족하다.  

`metrics()`
- 자바 라이브러리 호출을 통해서 확인 가능하다.
- 컨슈머가 정상 작동시에만 확인 할 수 있다. 한계가 뚜렷하다.
- 모든 컨슈머 애플리케이션에 컨슈머 랙 모니터링 코드를 중복 작성해야 한다.
- 카프카 서드파티 애플리케이션의 랙 모니터링이 불가능하다.(telegraf, fluentd, logstash)

`외부 모니터링 툴`
- 데이터 독(Datadog), 컨플루언트 컨트롤 센터(Confluent Control Center)와 같은 카프카 클러스터 종합 모니터링 툴 사용
- 컨슈머 랙 모니터링만을 위한 툴로 오픈소스인 **버로우**가 있다.

## Rebalance

리밸런싱되는 시간은 매우 짧다.  
- 하나의 Partition은 지정된 Consumer goup내의 하나의 Consumer만 사용.
- 메시지: 동일 key -> 동일 consumer
- `partition.assignment.strategy`로 할당 방식 조정.
- Consumer group은 Group Coordinator라는 프로세스에 의해 관리.

### Consuming process

1. Consumer 등록 및 Group Coordinator 선택
Consumer에 `group.id`를 등록하게 되면, Kafka가 group을 자동으로 만들게 된다.  
이때, `__consumer_offset`이라는 Topic(default topic)에 모든 Offset이 저장되게 된다.  
**__consumer_offset partition의 Leader Broker는 Consumer Group의 Group Coordinator로 선택**
> hash(group.id) % offsets.topic.num.partitions

2. JoinGroup 요청 순서에 따라 Consumer 나열
Group Coordinator는 JoinGroup이라는 request를 날린다.
group.initial.rebalance.delay.ms 변수를 통해 그 시간동안 왔던 consumer를 group에 join 시킨다.  

3. Group Leader 결정 및 Partition 할당
최초의 요청을 보낸 consumer가 group leader가 된다.

4. Consumer -> Partition 맵핑정보를 Group Coordinator에게 다시 보낸다.

5. 각 Consumer에게 할당된 partiton 정보를 보낸다.

> Kafka는 가능한 많은 계산을 클라이언트가 수행하도록 하여 Broker의 부담을 줄이도록 설계되어 있다.  
> 위와 같이 rebalancing과 같은 작업은 consumer에게 넘겨 부담을 줄이도록 했다.  
> Consumer Rebalancing시 Consumer들은 메시지를 Consume하지 못한다. 따라서 불필요한 Rebalancing을 피해야한다.

### Consumer heartbeat
```shell
heartbeat.interval.ms=3 
# consumer는 poll()과 별도로 백그라운드 Thread에서 Heartbeats를 보낸다.

session.timeout.ms=10
# 위 변수의 시간동안 heartbeat가 수신되지 않으면 Consumer는 Consumer group에서 삭제된다.

max.poll.interval.ms=5
# poll()은 heartbeats와 상관없이 주기적 호출된다.
```

### 과도한 Rebalancing을 피하는 방법
1. Consumer group 멤버 고정
- 고유한 group.instance.id를 할당
- LeaveGroupRequest를 사용 x

2. session.timeout.ms 튜닝
- heartbeat.interval.ms를 session.timeout.ms의 1/3으로 설정
- group.min.session.timeout.ms, group.max.session.timeout.ms의 사이값
- 늘릴때의 장점: rejoin할 수 있는 시잔을 제공, 단점: consumer장애 감지 오래걸림

3. max.poll.interval.ms 튜닝
- Consumer에게 poll()한 데이터를 처리할 충분한 시간 제공


## Partition Assignment Strategy

`partition.assignment.strategy`로 할당 방식 조절(consumer 환경 변수)

- org.apache.kafka.clients.consumer.RangeAssignor: Topic별로 작동하는 `Default Assignor`
- org.apache.kafka.clients.consumer.RoundRobinAssignor: RR 방식으로 Consumer에게 Partition 할당 
- org.apache.kafka.clients.consumer.StickyAssignor: 최대한 많은 기존 partition할당을 유지하며 최대 균형
- org.apache.kafka.clients.consumer.CooperativeStickyAssignor: RangeAssignor와 비슷하지만 협력적 rebalancing 허용
- org.apache.kafka.clients.consumer.ConsumerPartitionAssignor: 인터페이스를 구현하면 사용자 지정 할당 전략 사용 가능

1. RangeAssignor
각 partiton마다 순서대로 할당해준다. parition 번호가 더 작다면 노는 consumer가 생길수 있다.

2. RoundRobinAssignor
RoundRobin 방식으로 consumer group내에서 순서대로 계속 배정하는 방법이다.  
재할당후 consumer가 동일한 partition을 가지는지 보장을 못한다.  

3. StickyAssignor
가능한 균형적 할당 보장  
**Consumer들에게 할당된 Topic Partiton의 수는 최대 1만큼 다르다.**  
재할당이 발생할 경우, 기존 할당을 최대한 많이 보존하여 유지  
RR은 Consumer 하나가 죽을 경우 모두 재할당하지만, StickyAssignor는 기존 할당은 유지하면서 나머지 부분만 재할당 한다.

4. CooperativeStickyAssignor

> 기존에 사용되던 Rebalancing은 `Eager Rebalancing` 프로토콜  
> **Eager Rebalancing** : Consumer가 추가되면 단순히 partition의 재배정이 일어난다.  
> 재조정이 완료되는 기간동안 Consume을 할 수 없는 상황이 나타난다.
> 변하지 않는 partiton의 경우 중단 시킬 이유가 없다.-> 여기서 나온 개념이 Cooperative Rebalancing Protocol이다.

새로온 Consumer에게 배정될 partition만 revoke(중단)시킨다는 개념이다.  
하지만, Consumer는 자신의 partition중 어느 것이 재할당 될지 모른다.  
여기서 CooperationStickyAssignor가 2번 rebalancing을 시키게 된다. 
1번째 rebalancing때에는 어느것도 revoke 시키지 않고, 어떤 것이 나갈지 정하게 된다.(아무것도 변화가 일어나지 않는다.).  
2번째 rebalancing때에는 위처럼 revoke 시킬 partition만 떼어내서 할당해준다.  

## 멀티스레드 컨슈머
컨슈머를 운영할때 하나의 프로세스에 컨슈머 스레드를 여러개 둘지 여러개의 프로세스에 컨슈머 스레드를 하나씩 둘지를 결정해야한다.  
위에서 후자의 장점으로는 각각이 독립된 프로세스라서 더 안정적이고 장애에 대응이 좋지만, 그만큼 리소스가 많이 든다.  
전자의 장점으로는 후자의 장단점의 반대가 될 것이다.  

## Consumer 관련 CLI

Consumer 관련 CLI는 kafka-console-consumer.sh 쉘 파일을 이용한다.

`--max-message`: 최대 컨슘 메시지 개수를 설정할 수 있다. 수없는 메시지를 보내기 되면 원하는 데이터만큼을 한번에 받기가 어렵다.(테스트용
`--partition`: 특정 파티션만 컨슘이 가능하다.

```shell

# message consume
$ kafka-console-consumer.sh --bootstrap-server my-kafka1:9092,my-kafka2:9092,my-kafka3:9092 \
--topic hello.kafka \
--property parse.key=true \
--property key.separator="-" \
--group hello-group \  
--from-beginning

# consumer group 조회
$ kafka-consumer-groups.sh --list \
--bootstrap-server my-kafka1:9092,my-kafka2:9092,my-kafka3:9092 

# consumer group 상세 조회
$ kafka-consumer-groups.sh --describe \
--bootstrap-server my-kafka1:9092,my-kafka2:9092,my-kafka3:9092 \
--group hello-group

```

## 컨슈머 주요옵션(자바 라이브러리)
### 필수 옵션
`bootstrap.servers`
`key.deserializer`
`value.deserializer`

### 선택 옵션
`group.id`: 컨슈머 그룹 아이디 지정, default - null
`auto.offset.reset`: 컨슈머 그룹이 특정 파티션을 읽을때 저장된 컨슈머 오프셋이 없는 경우 어느 오프셋부터 읽을지 선택하는 옵션, default - latest
> latest, earliest, none 중 한개를 선택할 수 있다.  
> 
`enable.auto.commit`: 자동 커밋 / 수동 커밋, default - true
`auto.commit.interval.ms`: 자동 커밋일 경우 오프셋 커밋 간격 지정, default - 5000ms
`max.poll.records`: poll() 메서드를 통해 반환되는 레코드 개수를 지정, default - 500
`session.timeout.ms`: 컨슈머가 브로커와 연결이 끊기는 최대 시간, default - 10000
`heartbeat.interval.ms`: 하트비트를 전송하는 시간 간격, default - 3000
`max.poll.interval.ms`: poll() 메서드 호출 간격의 최대 시간, default - 300000
`isolation.level`: 트랜잭션 프로듀서가 레코드를 트랜잭션 단위로 보낼 경우 사용한다.