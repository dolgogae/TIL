# Replication

Partition을 복제하여 다른 broker상에서 복제물(Replicas)을 만들어서 장애 대비.  
메시지가 publishing 될때, 다른 broker상에 메시지를 복제하는 원리(원본 : Leader, 복제본 : Follower).  
> Producer, Consumer는 Leader에만 PUB/SUB한다.
> Replication은 서비스 용이 아니다.  
> Follower가 요청해서 메시지를 가져간다.  

Leader 장애 -> 새로운 Leader 선정 -> PUB/SUB이 선출된 파티션으로 한다.

## 하나의 broker에만 leader가 몰리면?
하나의 broker에만 부하가 집중된다(Hot Spot).  

**Hot spot을 방지하는 방법**
```shell
auto.leader.rebalance.enable = enable
leader.imbalance.check.interval.seconds = 300
```

## Rack Awareness
동일한 Rack, AZ상의 broker들에 동일한 rack name 지정.  
Replica는 최대한 Rack간의균형을 유지하여 Rack장애 대비.  
Auto Data Balancer/Self Balancing Cluster 동작때만 실행.  

```shell
broker.rack=ap-northeast-2a
```

## ISR
- [In-Sync-Replication](#6_ISR)

## Replica Failure

### Follower가 실패하는 경우
- Leader에 의해 ISR리스트에서 삭제
- Leader는 새로운 ISR을 사용하여 Commit

### Leader가 실패하는 경우
- Controller는 Follower중에서 새로운 Leader 선출
- Controller는 새 Leader와 ISR정보를 먼저 zookeeper에 push한 다음 로컬 캐싱을 위해 Broker에 push함

Partition에 Leader가 없을 시 사용할 수 없게 된다.  
retry를 설정해놓으면 다시 요청하게 된다.

## Replica Recovery
