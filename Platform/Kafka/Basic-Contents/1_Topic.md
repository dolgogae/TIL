# Topic

Kafka 안에서 메시지가 저장되는 장소(논리적인 표현)  

## Basic

- producer: 메시지를 생산 및 Kafka의 Topic으로 보냄
- consumer: Topic의 메시지를 소비
- consumer group: 협력하는 consumer들의 모임(분산, 병렬 처리 가능)
- > produce와 consumer는 각각 고유의 속도로 메시지를 가져간다(Commit Log에 RW수행).  

- Commit Log: 추가만 가능하고 변경 불가능한 데이터 스트럭쳐. 데이터(메시지)는 항상 로그 끝에 추가(변경 x)
- Offset: commit log의 메시지 위치(LOG-END-OFFSET, CURRENT-OFFSET), 하나의 partition에서만 의미를 가진다.
- > consumer lag = LOG-END-OFFSET - CURRENT-OFFSET

## Topic 기본 개념

Partition : Commit Log, 병렬처리(Throughput 향상)을 위해서 다수의 partition 사용. **서로 독립적인 관계이다.**  
Segment : 메시지가 저장되는 실제 물리 파일  


Topic 생성시 Partiton 개수를 지정(개수 변경이 가능하나 운영시에는 권장하지 않는다.).  
Partition들은 Broker에 분산되어 Segment 파일로 구성.  
> Rolling Strategy: log.segment.byte(def 1G), log.roll.hours(def 168h)로 파일의 크기를 정한다.  

Partition에저장된 데이터는 변경 불가능(immutable).  

## Topic 관련 CLI

Topic 관련 명령어는 bin/kafka-topics.sh(confluent: kafka-topics) 파일을 기준으로 실행한다.  

CLI에서 --bootstarp-server 옵션에 변수로 broker 서버를 넣게되는데, 하나의 장애로 쉘이 실행되지 않는 것을 방지하기 위해서 모든 broker 노드를 변수로 넣는것을 추천한다.

```shell
# server dns my-kafka1,2,3


# topic 생성 명령 
$ kafka-topics.sh --create \
--bootstrap-server my-kafka1:9092,my-kafka2:9092,my-kafka3:9092 \
--partitions 3 \
--replication-factor 1 \
--config retention.ms=172800000 \
--topic hello.kafka
# --config 옵션은 명령에 포함하지 않은 추가적인 옵션이 실행가능하다.

# topic 리스트 명령어 
$ kafka-topics.sh --list \
--bootstrap-server my-kafka1:9092,my-kafka2:9092,my-kafka3:9092 \

# topic 상세 조회
$ kafka-topics.sh --describe \
--bootstrap-server my-kafka1:9092,my-kafka2:9092,my-kafka3:9092 \
--topic hello.topic

```

Topic의 config를 변경하기 위해서는 bin/kafka-config.sh를 이용해서 바꾸는 것이 좋다.  
kafka-topics.sh의 --alter 옵션은 추후 삭제될 예정이라고 한다.

```shell

# topic의 retention.ms 설정 변경 방법
$ kafka-config.sh --bootstrap-server my-kafka1:9092,my-kafka2:9092,my-kafka3:9092 \
--entity-type topics \
--entity-name hello.kafka \
--alter --add-config retention.ms=8640000

```