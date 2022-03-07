# Apache_Kafka

(Message==Record==Event==Data)  
기존의 Messaging Platform(MQ)으로 처리가 불가능한 이벤트의 수가 있어서 만들어졌다.  
1. 이벤트 스트림을 안전하게 전송
2. 이벤트 스트림을 디스크에 저장
3. 이벤트 스트림을 분석 및 처리

Event가 사용되는 모든 곳에서 사용
- Messaging system
- 애플리케이션에서 발생하는 로그 수집
- Realtime Event Stream Processing
- DB 동기화(MSA 기반의 분리된 DB간 동기화)
- 실시간 ETL

## Topic
Kafka내에서 메시지가 저장되는 장소, 논리적인 표현

### Partition
Commit log, 하나의 Topic은 하나 이상의 Partition으로 구성  
병렬처리를 위해 다수의 partition 사용
> segment: 메시지가 저장되는 실제 물리 file(segment file이 지정된 크기보다 크거나 지정된 기간보다 오래되면 새 파일이 열리고 메시지는 새 파일이 추가됨)

### Producer
Topic의 메시지를 가져와서 소비하는 애플리케이션

### Consumer
메세지를 생산해서 Kafka의 Topic으로 메시지를 보내는 애플리케이션

### Consumer Group
Topic의 메시지를 사용하기 위해 협력하는 Consumer들의 집합

producer와 Consumer는 각각의 고유의 속도로 `Commit Log`에 RW를 수행한다.

### Commit log
추가만 가능하고 변경 불가능한 데이터 스트럭처. 데이터는 항상 로그 끝에 추가되고 변경되지 않음.
> offset: Commit log에서 이벤트의 위치
> - LOG-END-OFFSET: Write하는 offset
> - CURRENT-OFFSET: 처리하는 offset

---
1. Topic 생성시 Partition개수를 지정, 개수를 변경 가능하나 운영시에는 변경 권장하지 않는다.
2. Partition 번호는 0부터 시작하고 오름차순
3. *Topic내의 Partition들은 서로 독립적이다.*
4. 이벤트의 위치를 나타내는 offset이 존재
5. Offset은 하나의 Partition에서만 의미를 가짐
6. Offset은 증가하고 0으로 돌아가지 않는다.
7. Partition에 저장된 데이터는 변경이 불가능
8. Partition에 write되는 데이터는 맨 끝에 추가되어 저장됨
9. Partition은 segment file들로 구성됨
    - Rolling정책: log.segmentbytes(default 1GB), log.roll.hours(default 168 hours)

---

## Broker
Partition에 대한 RW를 관리하는 SW
- Kafka Server
- Topic 내의 Partition들을 분산, 유지 관리
- ID로 식별된다.
- Topic의 일부 Partition들을 포함.

### Kafka Cluster
여러개의 Broker들로 구성됨

> 최소 3대 이상의 Broker를 하나의 Cluster로 구성해야 함.(**4대 이상 권장**)
> 모든 Kafka Broker는 Bootstrap서버라고 부름

특정 Broker에 연결하면 전체 클러스터에 연결됨.
- 하나의 Broker는 모든 Broker, Topic, Partition을 알고 있다.

## Zookeeper
Broker를 관리(Broker 들의 목적/설정을 관리)하는 SW.  
-> 2022년에 Zookeeper를 제거한 정식 버전 출시 예정.

홀수의 서버로 작동하게 설계됐다.(최소 3대, 권장은 5대) -> Quorum으로 인해.

**1개의 Leader가 있고 나머지는 Follower이다.**

분산형 Configuration 정보 유지, 분산 동기화 서비스를 제공하고 대용량 분산 시스템을 위한 네이밍 레지스트리를 제종하는 SW.  
분산 작업을 제어하기 위한 Tree형태의 데이터 저장소.

`Quorum`은 합의체가 의사를 진행시키거나 의결을 하는데 필요한 최소한도의 인원수를 뜻함.
- 분산 시스템 데이터의 일관성을 유지하기 위해서

## Record
- Header : Metadata
- key, value(Json, String, Avro, Protobuf)  

Byte Array로 저장.

> producer에서 받은 데이터를 Serializer를 통해 Byte Array로 변경하고 Deserializer를 통해 Consumer에게 준다.

Producer Record -> send() -> Serializer -> Partitioner(어느 파티션으로) -> Compress(optional) -> RecordAccumulator -> Kafka -> 성공/실패 응답

### Partitioner Algorithm
Partition = Hash(Key) % Number of Partitions
> key가 null일때는 Batch단위로 Roud Robin을 한다.

## Consumer

자동이나 수동으로 데이터를 읽은 위치를 commit하여 다시 읽음을 방지  
`__consumer_offsets`이라는 Internal Topic에서 관리

Consumer가 하나일 경우, 모든 Partition의 Record를 Consume한다.  

### ConsumerGroup
동일한 group.id를 가지는 Consumer들.  
Partition은 하나의 Consumer에 의해서만 사용됨.  

> partition 2개 이상인 경우 모든 메시지에 대한 전체 순서 보장은 불가능하다.  
> partition이 하나일 경우 전체 순서를 보장한다.   
> 하지만, 대부분의 경우 key를 구분할 수 있는 메시지들의 순서보장이 필요한 경우가 많음.  
> 동일한 key를 가진 메시지는 동일한 partition에만 전달되어 key레벨의 순서 보장 가능  
> **운영중 Partition 개수를 변경하면 순서 보장이 불가능하다.**

### Cardinality
특정 데이터 집합에서 유니크한 값의 개수.  

Partition전체에 Record를 고르게 배포하는 key를 만드는 것이 중요하다.

### failure, Rebalancing
한개의 Consumer가 장애가 나면, Consumer group의 멀쩡한 Consumer가 가져와 처리한다.

---
## Broker에 장애가 난다면?

### Replication

Partition을 복제하여 다른 Broker상에서 복제물(Replicas)을 만들어서 장애를 미리 대비함.

Producer가 Partiton에 넣을 때 다른 Broker에 복제를 한다.
> 원본 Partition: Leader partition  
> 복사 Partition: Follower partition  
> Replication Factor: Leader, Follower를 모두 합친 갯수  
> Follower는 Broker 장애시 안정성을 제공하기 위해서만 존재

Leader 장애 발생 -> replica중에서 하나를 Leader로 바꿈 -> producer, consumer는 바뀐 Leader와 상호작용

Leader는 한개의 Broker에만 하면 안된다. 하나의 Broker에 부하가 집중되기 때문
- auto.leader.rebalance.enable: enable
- leader.imbalance.check.interval.seconds: 300sec # 300초 텀으로 leader 불균형 체크
- leader.imbalance.per.broker.percentage: 10  # 다른 broker보다 10%이상 많이 가져가면 불균형

### In-Sync Replicas(ISR)
high water mark라고 하는 지점까지 동일한 Replicas(Leader, Follower)의 목록

replica.lag.max.messages로 ISR 판단시 나타날 수 있는 문제점  
: 메시지 유입량이 갑자기 늘어날 경우, 지연으로 판단하고 OSR로 상태를 변경시킴

replica.lag.time.max.ms  
: Leader로 Fetch 요청을 보내는 Interval을 체크.

최근
1. Follower가 너무 느리면 Leader는 ISR에서 Follower를 제거하고 Zookeeper에 ISR을 유지
2. Controller는 Partition Metadata에 대한 변경 사항에 대해서 Zookeeper로부터 수신

> Kafka Cluster 내의 Broker중 하나가 Controller가 됨  
> Controller는 Zookeeper를 통해 Broker Liveness를 모니터링  
> Controller는 Leader와 Replica 정보를 Cluster내의 다른 Broker들에게 전달  
> Controller는 Zookeeper에 Replicas 정보의 복사본을 유지한 다음 더 빠른 액세스를 위해 클러스터의 모든 Broker들에게 동일한 정보를 캐시함  
> Controller가 Leader 장애시 Leader Election을 수행

### Leader Epoch
새 Leader가 선출된 시점을 Offset으로 표시  
Controller가 새 Leader를 선택하면 Leader Epoch를 업데이트하고 해당 정보를 ISR목록의 모든 구성원에게 보냄  
`leader-epoch-checkpoint` 파일에 체크포인트를 기록

---

- Last Committed Offset: Consumer가 마지막으로 Commit한 Offset
- Current Position: Consumer가 마지막으로 읽어간 위치(처리중)
- High Water Mark: ISR간에 복제된 Offset
- Log End Offset: Producer가 메시지를 보내서 저장된, 로그의 맨 끝 Offset"'

---

## Producer는 메시지가 잘 받았는지 아는 방법

ack를 통해서 확인할 수 있다.
- acks=0: ack가필요하지 않음. 잘 사용하진 않지만 메시지 손실이 다소 있더라도 빠르게 메시지를 보내야 하는 경우
- acks=1(default): Leader가 메시지를 수신하면 ack를보냄. 최대 한번 전송(at most once)을 보장한다.
- acks=-1(acks=all): 모든 replica까지 commit되면 ack를 보낸다.

재전송 변수
- retries: 메시지를 send하기위해 재시도 하는 횟수
- retry.backoff.ms: 재시도 사이에 추가되는 대기시간
- request.timeout.ms: producder가 응답을 기느리는 최대시간
- delivery.timeout.ms: send()후성공 또는 실패를 보고하는 시간의 상한
---

## Producer Batct 처리
Batch처리는 RPC(Remote Producer Call) 수를 줄여서 Broker가처리하는 작업이 줄어들기 때문에 더 나은 처리량을 제공한다.
- linger.ms: 메시지가 함께 Batch 처리될 때까지 대기 시간
- batch.size: 보내기 전 Batch의 최대 크기
- max.in.flight.requests.per.connection=5(default): Batch를 동시에 날릴 수 있는 최대 갯수(producer -> broker)
  - 하나의 배치가 실패하고 다음 배치가 성공하는 경우 순서의 역전이 일어난다. -> enable.idempotence를 사용하면 순서를 맞춰준다.(OutOfOrderSequenceException)

send() -> batch -> await send -> retires -> in flight

메시지는 partition에 기록된다. 
partition은 Log Segment file로 구성(성능을 위해 Log Segment는 OS Page Cache에 기록됨)

producer -(send)-> broker process -(write)-> OS Page cache -(flush)-> disk
위 과정에서 Zero-copy가 가능하다.(전송 데이터가 User Space에 복사되지 않고, CPU개입없이 Page Cache와 Network Buffer사이에서 직접 전송되는 것.)  
쉽게 생각해서 broker에서 따로 메시지를 저장할 필요가 없다. (byte code를 그대로 전달하기 때문에)  
-> 운영체제의 Backgroud flush기능을 더 효율적으로 허용하는 것을 선호(비활성화 추천)

## Replica Failure

### Follower 장애시
replica.lag.time.max.max 이내에 Follower가 fetch 하지 않으면 ISR에서 제거함

### Leader 장애시
zookeeper가 장애 감지후, controller가 새로운 leader선출 후, zookeeper에 기록.  
Controller가 새로운 ISR을 Leader에 주입

partition에 leader가 없으면 선출될 때까지 partition을 사용할 수 없다.

## Availability vs Durability

### Topic 파라미터 

unclean.leader.election.enable(default:false)

: ISR리스트에 없는 Replica를 leader로 선출할 것인지에 대한 옵션. 
  ISR리스트에 Replica가 하나도 없으면 leader선출 안함
  ISR리스트에 없는 Replica를 leader로 선출함 - 데이터 유실됨


min.insync.replicas(default:1)
: 최소 요구되는 ISR의 개수에 대한 옵션

> 데이터 유실이 없게 하려면?  
> Topic: replication.factor는 2보다 커야함(최소 3 이상).  
> Producer: acks는 all이어야 함  
> Topic: min.insync.replicas는 1보다 커야함(최소 2 이상).  
>   
> 데이터 유실이 다소 있더라도 가용성을 높게 하려면?  
> Topic: unclean.leader.election.enable를 true로 설정

## partition 할당 시
하나의 partition은 지정된 Consumer Group내의 하나의 Consumer만 사용.  
동일 key는 동일 consumer 사용  
partition.assignment.strategy로 할당 방식 조정.  

Group Coordinator에서 Consumer group의 group leader에게 파티션을 담당할 Consumer를 배정받는 방식이다.

## Rebalancing Trigger
- Consumer가 Consumer Group에서 탈퇴
- 신규 Consumer가 Consumer Group에 합류
- Consumer가 Topic 구독을 변경
- Consumer group은 Topic의 메타데이터 변경 사항을 인지
> Consumer Rebalancing시 Consumer들은 메시지를 Consume하지 못함. 따라서, 불필요한 Rebalancing은 반드시 피해야함

heartbeat.interval.ms: Consumer는 poll()과별도로 백그라운드 Thread에서 Heartbeat를 보냄.
session.timeout.ms: 아래 시간 동안 Heartbeat가 수신되지 않으면 Consumer는 Consumer group에서 삭제
max.poll.interval.ms: poll()은 Heartbeat와 상관없이 주기적으로 호출되어야 함.

Rebalancing은 성능 최적화에 필수
1. consumer group 멤버 고정
2. session.timeout.ms 튜닝
3. max.poll.interval.ms 튜닝


## Partition Assignment Strategy

**partition.assignment.strategy**로 할당 방식 조정

### org.apache.kafka.client.consumer.RangeAssignor
Topic별로 작동하는 Default Assignor  
순서대로 할당하고, Topic이 더 적으면 Consumer가 노는게 생길수도 있다.

### org.apache.kafka.client.consumer.RoundRobinAssignor
Round Robin 방식으로 Consumer에게 할당.  
Topic이 달라도 RR이 그대로 배정된다.  
할당 불균형이 발생할 수도 있다.

### org.apache.kafka.client.consumer.StickyAssignor
최대한 많은 기존 Partition 할당을 유지하면서 최대 균형을 이루는 할당을 보장
1. 가능한한 균형적으로 할당을 보장: Consumer들에게 할당된 Topic Partition의 수는 최대 1만큼 다름.
2. 재할당이 발생했을 때, 기존 할당을 최대한 많이 보존하여 유지
RR과 비슷하지만, 재할당시 기존의 Partition을 건드리지 않고 나머지만 재할당한다.

### org.apache.kafka.client.consumer.CooperativeStickyAssignor

#### 시간에 따른 Consumer Rebalancing 과정
1. Consumer들이 JoinGroup 요청을 Group Coordinator에 보내며 리밸런싱이 시작된다.
2. JoinGroup의 응답이 Consumer들에 전송.
3. 모든 구성원은 Broker에 SyncGroup 요청을 보내야 함.
4. Broker는 SyncGroup응답에서 Consumer에 Partition을 할당함.

변하지 않으면 Consume을 계속할 수 있게끔 하는 것. 전체 재 조정중 떼어낸 Partition만 멈추고 나머지는 계속 Consume을 하면 된다.
> 문제) Consumer는 자신의 Partition중 어느 것이 다른 곳으로 재할당되어야 하는지 알지 못함.  
> Broker가 SyncGroup의 Response를 줄때, 어느 partition을 떼어낼지 알려준다.  

### org.apache.kafka.client.consumer.ConsumerPartitionAssignor: 사용자 커스텀


## Kafka Log File
각 Partition은 Segment File들로 구성됨.  
Kafka Log Segment File은 Data File이라고 부르기도 함.
Segment File이 생성되는 위치는 각 Broker의 ₩server.properites₩파일 안에서 ₩log.dirs₩ 파라미터로 정의함.  
> 0000000000123453.* 파일과 0000000000777532.* 0000000000123453 offset부터 다음 파일 전까지 메시지 저장/관리를 한다.  
> .log: 메시지와 metadata 저장  
> .index: 각 메시지의 Offset을 Log Segment 파일의 Byte위치에 매핑  
> .timeindex: 각 메시지의 timestamp를 기반으로 메시지를 검색하는 데 사용  
> leader-epoch-checkpoint: Leader관련 Offset저장  
> 
> 특별 파일
> .snapshot: Idempotent Producer 사용시   
> .txnindex: Transactional Producer 사용시.  

Partition은 하나 이상의 Segment File로 구성.  

아래의 파리미터 중 하나라도 해당되면 새로운 Segment File로 Rolling
- log.segment.bytes(default 1GB)
- log.roll.ms(default 168시간)
- log.index.size.max.bytes(default 10MB)

__consumer_offset의 Segment File Rolling 파라미터는 별도
- offsets.topic.segment.bytes(default 100MB)

Broker에는 각 2개의 checkpoint File이 존재함.
- replication-offset-checkpoint: 마지막으로 Commit된 메시지의 ID인 High Water Mark시작시 Follower가 이를 사용하여 Commit되지 않은 메시지를 Truncate
- recovery-point-offset-checkpoint: 데이터가 디스크로 Flush된 지점 복구 중 Broker는 이 시점 이후의 메시지가 손실되었는지 여부를 확인.

## Exactly Once Semantics(EOS)
Java Client에서만 Fully Supported
- Producer, Consumer
- Kafka Connect
- Kafka Streams API
- Confluent Rest Proxy
- Confluent ksqlDB

At-Most-Once Semantics(최대 한번)
At-Least-Once Semantics(최소 한번)
Exactly-Once Semantics(정확히 한번)

중복 메시지로 인한 중복 처리 방지
- 클라이언트(Idempotent Producer)에서 생성되는 중복 메시지 방지
- Transaction 기능을 사용하여, 하나의 트랜잭션 내의 모든 메시지가 모두 write 되었는지 또는 전혀 안됐는지 확인.

Transaction Coordinator사용
- 특별한 Transaction Log를관리하는 Broker Thread.  

Idempotent Producer
- producer 파라미터 중 enable.idempotence를 true로 설정
- 메시지 중복을 방지
- 성능에 영향이 별로 없음

Transaction
- 각 producer에 고유한 transactional.id를 설정
- Producer를 Transaction API를 사용하여 개발
- Consumer에서 isolation.level을 read_commited로 설정

producer -(send)-> Broker가 Ack를 못받음 -(retry x)-> 중복 메시인 것을 확인 후, duplicate를 보낸다.
