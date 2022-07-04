# Producer

메시지, 레코드, 이벤트, 데이터

## Record
- Header: Topic, Partition, Timestamp, etc... (Metadata)
- Body : Key, Value(Avro, Json의 다양한 형태 가능)

Kafka는 Record를 **Byte Array**로 저장.  
Serialize --> Byte Array(Broker) --> Deserialize.  

## High-level arch.

Producer Record -> send() -> Serializer -> partitioner(어느 파티션) -> Comperss(optional) -> RecordAccumulator 
-> kafka -> 성공 -> metadata return 
    \
     `-> 실패 -> 재시도


### Partitioner
Partition = Hash(Key) % # of Partitions  
key가 null일 경우 : RR 형태(2.4이전), Sticky(2.4 이후)
> sticky: 하나의 Batch(Partition을 나눈 단위)가 닫힐때 까지 보내고 랜덤으로 Partition선택.  


## Ack
요청이 성공할때를 정의하는 데 사용되는 Producer에 설정하는 변수
1. acks=0: ack가 필요하지 않음. 자주 사용하지 않음. 손실이 있더라도 빠르게 보내야하는 경우
2. acks=1(default): Leader가 메시지를 수신하면 ack를 보냄. `At most once(최대한번)` 전송을 보장
3. acks=-1: acks=all과 동일. Leader가 모든 Replica까지 Commit되면 ack를 보냄 `At least once(최소한번)` 전송을 보장

```shell
retries=MAX_INT
# 메시지 send하기 위해 재시도하는 횟수

retry.backoff.ms=100
# 재시도 사이에 추가되는 대기 시간

request.timeout.ms=30000
# producer가 응답을 기다리는 최대 시간

delivery.timeout.ms=120000
# send()후 성공 또는 실패를 보고하는 시간의 상한
```
> delivery.timeout.ms 조정으로 재시도 동작을 제어.  
> acks=0에서는 무의미 하다.

## Batch
Batch처리는 RPC(Remote Procedure Call)수를 줄여서 Broker가 처리하는 작업이 줄어들기 때문에 더 나은 처리량을 제공.

```shell
linger.ms=0 # 즉시보냄
# 메시지가 함께 Batch처리될 때까지 대기 시간

batch.size=1000000
# 보내기 전 batch의 최대 크기
```
> batch처리의 일반적인 설정은 linger.ms=100 및 batch.size=1000000

### Message send 순서 보장
진행중인 여러 요청을 재시도 하면 순서가 변경될 수 있음.  

```shell
max.in.flight.requests.per.connection=5 # default
# 여러개의 배치가 동시에 날아갈 수 있는 갯수
```

**batch0 실패, batch1 성공 시 순서가 틀어진다.**  
이를 예방하기 위해서 `enable.idempotence`를 사용하면 하나의 batch 실패하면, 같은 partition으로 들어오는 후속 batch들도 `OutOfOrderSequenceException`과 함께 실패

## Page Cache & Flush

Partition은 Log Segment file로 구성.  
성능을 위해 Log Segment는 OS Page Cache에 기록된다.  
Disk에 저장되는 포맷과 OS Page Cache에 써진것과 정확히 동일해 `Zero Copy`가 가능
> Zero-Copy: 전송 데이터가 User Space에 복사되지 않고, CPU의 개입없이 Page Cache와 network buffer 사이에서 직접 전송되는 것을 의미.

Producer --(send)-> Broker Process --(write)-> OS Page Cache --(flush)-> Disk.  

OS가 데이터를 디스크로 Flush 하기전에 Broker의 시스템에 장애 발생시 손실이 일어난다.  
> Replication이 없다면 영구적 손실이 된다.  

Kafka는 운영체제의 background flush 기능을 더 효율적으로 허용하는 것을 선호.  