# In-Sync-Replication(ISR)

High Water Mark라고 하는 지점까지 동일한 Replicas의 목록.  
리더 파티션과 팔로워 파티션이 모두 싱크가 된 상태를 뜻한다.  

```shell
replica.lag.max.messages=4
# 최대 4개 정도의 차이만 나는 Replica들의 모임.
# 그 이상 차이가 나면 OSR이 된다.

unclean.leader.election.enable=true
# 유실을 감수함. 복제가 안된 팔로워 파티션을 리더로 승급.
unclean.leader.election.enable=true
# 유실을 감수하지 않음. 해당 브로커가 복구될 때까지 중단.
```
* OSR(Out-of-Sync): 잘따라잡고 있지 못한 replicas.  

`replica.lag.messages의 문제점`
갑자기 record가 확 늘어나는 경우에 전부 OSR이 되는 경우가 생긴다.  
불필요한 error가 발생하게 된다. 

```shell
replica.lag.time.max.ms=10000
# 갯수가 아닌 시간으로 판단한다.
```

ISR은 Broker가 관리한다.
1. Follower가 너무 느리면 Leader는 ISR에서 Follower를 제거하고 Zookeeper에 ISR을 유지
2. Controller는 Partition Metadata에 대한 변경 사항에 대해서 Zookeeper로부터 수신 

## 메시지가 복제되는 과정
1. Producer가 Leader에 메시지를 Publishing
2. follower의 broker에 있는 Fetch Thread가 Leader에 새롭게 추가된 메시지를 복제
3. 이후에 Fetch Thread가 다시 요청을 한 뒤 null을 받게 되면 Leader Partition의 High Water Mark를 변경하게 된다.
4. 다시 Fetch Thread가 fetch 수행을 하고 Follow Partition의 High Water Mark를 업데이트 하게 된다.

## ISR - Commited
ISR 목록의 모든 Replicas가 메시지를 성공적으로 가져오면 `Committed`라고 한다.
- Consumer는 Commited(ISR상 복제가 완료된 위치) 메시지만 읽을 수 있음.  
- Leader는 메시지를 Commit할 시기를 결정
- Committed 메시지는 모든 Follower에서 동일한 Offset을 갖도록 보장.
- 어떤 Replica가 Leader에 관계없이 모든 Consumer는 해당 Offset에서 같은 데이터를 볼 수 있음.  

> Committed Offset은 replication-offset-checkpoint라는 파일에 기록

## Leader Epoch
새 Leader가 선출된 시점을 Offset으로 표시  
> leader-epoch-checkpoint 파일에 체크포인트를 기록.  

## ISR 주요 옵션(자바 라이브러리)
