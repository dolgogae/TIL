# Broker

Partition에 대한 RW를 관리하는 소프트웨어.  
- Kafka server(Bootstrap server)
- Topic내의 partition들을 분산, 유지 및 관리
- ID로 식별됨(partition ID와는 무관)
- > 최소 3대 이상의 Broker를 하나의 Cluster로 구성해야하고 **4대 이상**을 권장한다(안정성을 위해서).

Client는 특정 Broker에 연결하면 전체 클러스터에 연결됨.  
- 하지만 특정 broker의 장애를 대비해 전체 Broker list를 파라미터로 입력 권장
- 각각의 Broker는 모든 Broker, Topic, Partition에 대해 알고 있음(Metadata)

## Controller Broker
- Cluster에서 하나의 브로커가 Controller가 된다. 
- Zookeeper를 통해 Broker Liveness 모니터링
- Partition Leader와 Replica 정보를 Cluster내의 다른 Broker들에게 전달
- Zookeeper에 Replicas 정보의 복사본을 유지한 다음 더 빠른 액세스를 위해 클러스터의 모든 Broker들에게 동일한 정보 캐시
- Partition Leader 장애시 Leader election 수행
- Controller 장애시 Zookeeper가 재선출


# Zookeeper

Zookeeper는 Broker를 관리하는 소프트웨어  
변경사항에 대해 Kafka에 알림. -> Topic생성/제거, Broker 추가/제거
- 멀티 kafka broker들 간의 정보 공유, 동기화

홀수의 서버로 작동하게 설계.  
- 1개의 Leader, 나머지 Follower

**Quorum 기반 알고리즘**
- 합의체가 의사를 진행하거나 의결하는데 필요한 최소한도의 인원수
- 분산 환경에서 예상치 못한 장애 발생시 일관성 유지.

분산형 Config 정보 유지, 분산 동기화 서비스를 제공하고 대용량 분산 시스템을 위한 네이밍 레지스트리 제공 소프트웨어.  
