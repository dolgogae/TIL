# Exactly Once Semantics(EOS)

At most once semantics(최대 한번) : ack=1
At least onse semantics(최소 한번) : ack=-1

**Exactly Once Semantics(정확히 한번)** : ack=all에서 중복되는 데이터를 방지하기 위해서
- 클라이언트(Idenpotent Producer)에서 생성되는 중복 메시지 방지
- Transaction 기능 활용하여 모든 메시지가 모두 write되었는지 안되었는지 판단.
- 돈과 관련된 데이터는 횟수가 중요하기 때문에 해당 옵션을 사용해야 한다.

## Transaction Coordinator 사용
특별한 Transaction log를 관리하는 Broker Thread.  
Producer ID, Sequence Number, Transaction ID를 할당하고 클라이언트가 이 정보를 메시지 Header에 포함해 메시지를 고유하게 식별한다.  
`Sequence Number`는 Broker가 중복된 메시지를 skip할 수 있게함
- Transaction log: 새로운 Interanl kafka Topic으로 모든 Transaction의 영구적이고 복제된 record를 저장하는 Transaction Coordinator의 상태 저장소
- TranactionalId : Producer를 고유하게 식별하기 위해 사용

### Idempotent Producer 
- Producer 파라미터중 `enable.idempotence=true` 설정  
- 성능에 영향이 없다(헤더에 데이터만 추가하는 정도라서).  

### Transaction
- 각 Producer에 `transaction.id`를 설정
- Transaction API를 사용
- Consumer에서 `isolation.level=read_committed`로 설정(필요시에만 수정)

## Consumer Config
```shell
isolation.level=read_committed # default: read_uncommitted
# Non-Traction

ppt보고적기
```