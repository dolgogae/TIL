# Kafka_Environment

Broker는 분리된 전용 서버에 설치하는 것을 권장한다.
- N개의 Broker에서는 Replication factor(RF)는 N개만 가능하다.
- 중요한 Topic의 경우에는 RF를 보통 3을 사용한다.
    - if) 3개의 Broker중 1개의 Broker가 Down되면 3개의 RF를 생성하지 못하므로 4개를 추천한다.
- 데이터 유실 방지를 위해 min.insync.replicas는 2를 사용한다.

Broker는 자체가 CPU를 많이 사용하진 않지만 처리량에 따라서 Thread가 증가하므로 CPU 사용이 증가한다.
- Reference Architecture에는 12-core Dual을 권장한다.(총 24 Core)
- num.io.threads = 8(default) : 디스크보다 크게 설정
- num.network.threads = 3(default) : TLS를 사용할 경우 2배로 설정
- num.recovery.threads.per.data.dir = 1(default) : Broker 시작시 빠른 기동을 위해서, core수까지만 설정
- num.replicas.fetchers = 1(default) : Source Broker 메시지를 복제하는데 사용되는 Thread수. 빠르게 복제하기 위해 값을 증가. Broker의 CPU사용률과 네트워크 사용률이 올라감.

Broker는 많은 수의 partition을 지원해 여러개의 파일을 열어볼 수 있어야한다
```shell
$ ulimit -n 100000
```

Broker는 JVM Heap을 많이 사용하지 않음. 대부분의 운영환경은 6~12G까지 할당.  
대부분의 메모리는 OS Page Cache를 많이 사용한다.(많을수록 성능에 유리)
- 운영환경용 Broker 메모리는 최소 32G 이상을 권장하며, 처리량에 따라 64G이상 권장

## Zookeeper
```shell
dataDir=/data/zookeeper
# 디렉터리가 생성되어 있어야 한다.

clientPort=2181
# zookeeper가 올라갈 port

server.<myid>=<hostname>:<leaderport>:<electionport>
# myid: zookeeper 식별 id로 ${dataDir}/myid 파일에 값이 저장되어 있어야 한다.
# electionport: zookeeper leader를 선출하는데 사용되는 port
```


## Kafka
```shell
zookeeper.connect=zookeeper1:2181,zookeeper2:2181,zookeeper3:2181
broker.id=1
# broker를 구분하는 id(각각 고유해야 함)

log.dirs=/data/broker
# 실제 세그먼트 파일이 쓰이는 폴더

listeners=PLANTEXT://broker1:9092
# Broker가 수신할 URI의 쉼표로 구분된 목록
```