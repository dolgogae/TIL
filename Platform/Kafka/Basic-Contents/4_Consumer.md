# Consumer

각각 고유의 속도로 Commit Log로부터 순서대로 Poll.  
다른 Consumer group에 속한 Consumer들은 서로 관련이 없고, Commit Log에 있는 Record를 동시에 다른 위치에서 read 가능.  

- consumer offset: consumer가 자동, 수동으로 읽은 데이터의 위치를 commit하여 다시 읽음 방지.  
- > __consumer_offsets라는 interal topic에서 consumer offset을 저장하여 관리(ex. GroupB:MyTopic:P0:7)

Partition이 2개 이상인 경우 모든 메시지에 대한 **전체 순서 보장 불가능**
- partition별로 각각 동작하기 때문이다.
- partition을 하나로 하면 정렬이 되지만 처리 순서는 떨어진다.(병렬처리가 안되기 때문)
- 동일한 key를 가진 메시지는 동일한 partition에만 전달되어 key레벨의 순서 보장 가능.(운영중 partition 개수 변경시 순서 보장 불가능)

## Consumer group

4개의 파티션이 있는 Topic을 conusme하는 4개의 Consumer가 하나의 Consumer group에 있다면 각 consumer는 정확히 하나의 partition에서 record를 consume.(같은 group내의 consumer는 partition을 분배받음)  
partition은 항상 consumer group내의 하나의 consumer에 의해서만 사용.  
consumer는 주어진 topic에서 0개 이상의 많은 partition을 사용 가능하다.

> 동일한 Topic에서 consume하는 여러 Consumer Group이 있을 수 있음.  

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
> Consumer lag = (Log End Offset) - (Last Commited Offset).  


## Rebalance

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

- org.apache.kafka.clients.consumer.RangeAssignor: Topic별로 작동하는 Default Assignor
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

