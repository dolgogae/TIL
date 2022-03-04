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
- Log End Offset: Producer가 메시지를 보내서 저장된, 로그의 맨 끝 Offset
