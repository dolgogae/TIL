# CLI

## 주키퍼, 카프카 시작
```bash
./bin/zookeeper-server-start.sh ./config/zookeeper.properties
./bin/kafka-server-start.sh ./config/server.properties
```

```shell
# server.properties 설정값
listeners=PLAINTEXT://localhost:9092
advertised.listeners=PLAINTEXT://localhost:9092
log.dirs=/home/kafka-admin/kafka_2.12-3.3.1/data
```

## kafka-topics.sh
```shell
# 단순 생성
bin/kakfa-topic.sh --create --bootstrap-server my-kafka:9092 --topic hello.kafka
# 옵션을 통한 생성(partition)
bin/kakfa-topic.sh --create --bootstrap-server my-kafka:9092 --topic hello.kafka --partitions 10

# 토픽 설정 확인
bin/kakfa-topic.sh --bootstrap-server my-kafka:9092 --topic hello.kafka --describe
```

## kafka-configs.sh
토픽의 설정을 바꾸는 콘솔
```shell
# --alter 옵션을 사용해서 변경이 가능하다.
bin/kafka-configs.sh --bootstrap-server my-kafka:9092 --topic test --alter --add-config min.insync.replicas=2
bin/kafka-configs.sh --bootstrap-server my-kafka:9092 --topic test --alter --add-config partitions=10
```

## kafka-console-producer.sh
프로듀서를 실행하는 콘솔
```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 --topic hello.kafka
>hello
>kafka
>0
>1
>2
>3식
>4
>5 
>6

# 옵션을 사용해서 key와 message를 모두 넣어주는 방법
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 --topic hello.kafka --property "parse.key=true" --property "key.separator=:"
>k1:mymessagevalue
>k2:new
>k3:hi
>k1:no3
```

## kafka-console-consumer.sh
컨슈머를 실행하는 콘솔
```shell
$ bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic hello.kafka --from-beginning
mymessagevalue
no3
hi
hello
kafka
0
1
2
3
4
5
6
new

$ bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic hello.kafka --property print.key=true --property key.separator="-" --from-beginning
k1-mymessagevalue
k1-no3
k3-hi
null-hello
null-kafka
null-0
null-1
null-2
null-3
null-4
null-5
null-6
k2-new

$ bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 --topic hello.kafka --max-messages 1 --from-beginning
mymessagevalue
```

## kafka-consumer-groups.sh
컨슈머 그룹에 대한 명령을 내리는 콘솔
> `reset-offsets` 옵션의 종류
> --to-earliest: 가장 처음 오프셋(작은 번호)으로 리셋
> --to-latest: 가장 마지막 오프셋(큰 번호)으로 리셋
> --to-current: 현시점 기준 오프셋으로 리셋
> --to-datetime {YYYY-MM-DDTHH:mmSS.sss}: 특정 일시로 오프셋 리셋(레코드 타임스탬프 기준)
> --to-offset {long}: 특정 오프셋으로 리셋
> --shift-by {+/- long}: 현재 컨슈머 오프셋에서 앞뒤로 옮겨서 리
```shell
$ bin/kafka-consumer-groups.sh --bootstrap-server my-kafka:9092 --list
hello-group

$ bin/kafka-consumer-groups.sh --bootstrap-server my-kafka:9092 --group hello-group --describe

Consumer group 'hello-group' has no active members.

GROUP           TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID     HOST            CLIENT-ID
hello-group     hello.kafka     6          1               1               0               -               -               -
hello-group     hello.kafka     2          0               0               0               -               -               -
hello-group     hello.kafka     3          10              10              0               -               -               -
hello-group     hello.kafka     7          2               2               0               -               -               -
hello-group     hello.kafka     4          0               0               0               -               -               -
hello-group     hello.kafka     8          0               0               0               -               -               -
hello-group     hello.kafka     1          0               0               0               -               -               -
hello-group     hello.kafka     9          0               0               0               -               -               -
hello-group     hello.kafka     5          0               0               0               -               -               -

$ bin/kafka-consumer-groups.sh \
>  --bootstrap-server my-kafka:9092 \
>  --group hello-group \
>  --topic hello.kafka \
>  --reset-offsets --to-earliest --execute
```

## kafka-producer-perf-test.sh, kafka-consumer-pert-test.sh
카프카 프로듀서로 퍼포먼스를 테스트하는 콘솔
```shell
$ bin/kafka-producer-perf-test.sh \
 --producer-props bootstrap.servers=my-kafka:9092 \
 --topic hello.kafka \
 --num-records 10 \
 --throughput 1 \
 --record-size 100 \
 --print-metric
7 records sent, 1.3 records/sec (0.00 MB/sec), 28.9 ms avg latency, 184.0 ms max
latency.
10 records sent, 1.087666 records/sec (0.00 MB/sec), 20.60 ms avg latency, 184.00
ms max latency, 2 ms 50th, 184 ms 95th, 184 ms 99th, 184 ms 99.9th.

# 브로커와 컨슈머간 네트워크 테스트가 가능하다.
$ bin/kafka-consumer-perf-test.sh \
 --bootstrap-server my-kafka:9092 \
 --topic hello.kafka \
 --messages 10 \
 --show-detailed-stats
time, threadId, data.consumed.in.MB, MB.sec, data.consumed.in.nMsg, nMsg.sec,
rebalance.time.ms, fetch.time.ms, fetch.MB.sec, fetch.nMsg.sec
2022-01-16 22:19:49:410, 0, 0.0000, 0.0000, 1, 4.6729, 1642339189386,
-1642339189172, 0.0000, 0.0000
```

## kafka-reassign-partitions.sh
브로커 추가시에 파티션을 적절하게 분배하게 해주는 콘솔  
리더 파티션과 팔로워 파티션의 위치를 변경할 수 있다.  
브러커의 백그라운드 스레드가 일정한 간격으로 리더의 위치를 파악하고 필요시 리더 리밸런싱을 통해 리더의 위치가 알맞게 배분된다.  
```shell
$ cat partitions.json
{
 "partitions":
 [ { "topic": "hello.kafka", "partition": 0, "replicas": [ 0 ] } ]
 ,"version": 1
}
$ bin/kafka-reassign-partitions.sh --zookeeper my-kafka:2181 \
 --reassignment-json-file partitions.json --execute
```

## kafka-delete-record.sh
일정 offset 이전까지의 offset을 지우는 콘솔
```shell
$ cat delete.json
{
 "partitions": [
 {
 "topic": "hello.kafka", "partition": 0, "offset": 5
 }
 ], "version": 1
}
$ bin/kafka-delete-records.sh --bootstrap-server my-kafka:9092 \
 --offset-json-file delete.json
Executing records delete operation
Records delete operation completed:
partition: hello.kafka-0 low_watermark: 5
```