# Kafka Operation

## 1. Log Retention

log.cleanup.policy : delete, compact, delete/compact

### Delete Cleanup Policy
```shell
log.cleanup.policy=delete  
log.retention.ms=604800 # 7 days
# log 보관 주기

log.retention.check.interval.ms=300 # 5 min
# log segment 체크주기

```

`Topic message를 모두 삭제하는 방법`
1. Consumer, Producer shutdown.  
2. Topic의 retention.ms를 0으로 세팅
```shell
kafka-config.sh --zookeeper ${zookeeper ip address} --alter --entity-name topics --entity-name myTopics --add-config retention.ms=0
```
3. Cleanup Thread가 동작할 동안 대기(default 5분마다 동작)

4. 메시지 삭제 확인후, 원복
```shell
kafka-config.sh --zookeeper ${zookeeper ip address} --alter --entity-name topics --entity-name myTopics --delete-config retention.ms
```

**절대 Log File을 직접 삭제하면 안된다.**  

### Compact Cleanup Policy

각 key의 최신 데이터만 유지  
Compact정책은 partition별로 특정 key의 최신 value만 유지하며 압축.  
**중복제거용 기술이 아니다.**

`동작원리`
1. Compaction전에 Cleaner Point를 지정한다.
2. Cleaner Point를 기준으로 이전의 Segment file을 Tail, 이후의 Segment file을 Head로 잡는다.
3. Tail과 Head를 비교 후, 중복되는 key가 있다면 Tail의 Offset 정보를 지우게 된다.

```shell
log.cleaner.min.cleanable.ratio=0.5
# Head 영역 데이터가 Tail 영역보다 크면(기본값 50%), Cleaner 시작

log.cleaner.io.max.bytes.per.second=무제한
# Log Cleaner의 R/W의 처리량을 제한하여 시스템 리소스 보호


# 동일한 key를 갖는 메시지가 많을 경우, 더 빠른 처리를 위해서 아래의 파라미터를 증가 시켜야한다.
log.cleaner.threads=1
log.cleaner.dedupe.buffer.size=134217728
```

`Tombstone Message`
특정 Key를 지우려면 동일한 Key에 null value를 가지는 메시지를 Topic으로 보내게 된다.  
**log.cleaner.delete.retention.ms**를 통해서 메시지를 완전히 지우기 전 보관기간을 조정한다.  

## Cluster Upgrade

고유한 ID 부여후 zookeeper ensamble에 연결만 해주면 된다. 하지만 partition까지 넘어가지는 않는다.  
따라서, **kafka-reassign-partitions** 도구를 사용해야 한다.