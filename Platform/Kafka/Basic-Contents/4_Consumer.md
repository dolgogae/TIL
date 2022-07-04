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