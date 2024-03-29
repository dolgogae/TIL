# Kafka Streams

토픽에 적재된 데이터를 실시간으로 변환하여 다른 토픽에 적재하는 라이브러리이다.  

## 스트림즈를 이용하는 이유
스트림즈 라이브러리를 통해 제공하는 단 한 번의 데이터 처리, 장애 허용 시스템 등의  
특징들은 컨슈머와 프로듀서의 조합만으로 완벽하게 구현이 힘들다.  

## 내부구조
한개의 스레드에 여러개의 태스크가 있다.  
`태스크`는 스트림즈 애플리케이션을 실행하면 생기는 데이터 처리 최소 단위이다.  

### 스케일 아웃
안정적 운영을 위해 2개 이상의 서버로 구성하여 스트림즈 애플리케이션을 운영한다.  
하나의 서버가 장애가 발생하더라도 안전하게 스트림 처리가 가능하다.  

### 토폴로지
2개 이상의 노드들과 선으로 이루어진 집합을 뜻한다.
카프카 스트림즈는 트리형 토폴로지이다.  
토폴로지의 하나의 노드를 `프로세서`, 노드와 노드를 연결하는 엣지를 `스트림`이라고 부른다.  

**소스 프로세서**
데이터를 처리하기 위해 최초로 선언해야 하는 노드  
하나 이상의 토픽에서 데이터를 가져오는 역할  

**스트림 프로세서**
다른 프로세서가 반환한 데이터를 처리하는 역할  
변환, 분기처리 같은 로직이 데이터 처리의 일종이다.  

**싱크 프로세서**
데이터를 특정 카프카 토픅으로 저장하는 역할을 하며 스트림즈로 처리된 데이터의 최종 정착지이다.

## 스트림즈 DSL
스트림 프로세싱에 쓰일 만한 다양한 기능들을 자체 API로 만들어놓아 대부분의 변환 로직을 어렵지 않게 개발 가능  
일부 제공하지 않는 기능은 프로세서 API로 구현 가능하다.  
- 메시지 값을 기반으로 토픽 분기 처리
- 지난 10분간 들어온 데이터의 개수 집계

###KStream, KTable, GlobalKTable
토픽에 있는 데이터를 어떻게 사용할지 추상화한 개념이다.

`KStream`  
컨슈머로 토픽을 가독하는 것과 동일한 선상에서 사용하는 것.  

`KTable`  
메시지 키를 기준으로 묶어서 사용  
카프카 토픽의 데이터를 로컬의 rocksDB에 Materialized View로 만들어 두고 사용하기 때문에 메시지 키, 값을 기반으로 keyValueStore로 사용할 수 있다.
**유니크한 메시지 키를 기준으로 가장 최신 레코드를 사용한다.**
> 사용자의 주소 데이터 같은 경우에 최신의 데이터만 사용하기 때문에 KTable을 운영하면 좋다.

가장 최신의 값만 필요한 key-value 스토어와 비슷한 역할을 할 수 있다.

`코파티셔닝`  
KStream와 KTable을 조인하려면 반드시 코파티셔닝 되어 있어야한다.  
**조인을 하는 2개 데이터의 파티션 개수가 동일하고 파티셔닝 전략을 동일하게 맞추는 작업이다.**
코파티셔닝이 안되는 경우
- 파티션 개수가 동일하지 않음.
- 파티셔닝 전략이 같아야 한다.
- 조인이 안될 경우 TopologyException 발생

`GlobalKTable`  
위의 코파티션이 안되는 경우를 해결하기 위한 로직
스트림즈 애플리케이션의 모든 태스크에 동일하게 공유되어 사용된다.

`**join()**`  
사용자의 이벤트 데이터를 데이터베이스에 저장하지 않고도 조인하여 스트리밍 처리할 수 있다는 장점이 있다.  
이를 통해 이벤트 기반 스트리밍 데이터 파이프라인을 구성할 수 있는 것이다.  
**KStream**과 **KTable**을 조인하면 key-value를 매핑하여 KTable의 고유한 값을 통해서 KStream을 매핑해서 저장이 가능하다.
**KStream**과 **GlobalKTable**을 조인하면 코파티셔닝이 되어있지 않은 경우에 한다.  
> 코파티셔닝이 안되어있을때 다른 방법으로는 리파티셔닝을 통해서 파티션 개수를 맞춰주는 방법도 있다.  

### 윈도우 프로세싱
윈도우 연산은 특정 시간에 대응하여 취합 연산을 처리할 때 활용한다.  
모든 프로세싱은 메시지 키를 기준으로 취합한다. 따라서 해당 토픽에 동일한 파티션에는 동일한 메시지 키가 있는 레코드가 존재해야지만 정확한 취합이 가능하다.  
`텀블링 윈도우`  
서로 겹치지 않은 윈도우를 특정 간격으로 지속적 처리할 때 사용한다.  
벌크로 저장할 때 많이 사용된다.  
예를 들어, 매 5분간 접속한 고객의 수의 추이를 볼때 사용할 수 있다.  

`호핑 윈도우`  
일정 간격으로 겹치는 윈도우가 존재하는 윈도우 연산을 처리할 경우 사용한다.  
서로 다른 윈도우에서 여러번 연산될 수 있다.  

`슬라이딩 윈도우`  
데이터의 정확한 시간을 바탕으로 윈도우 사이즈에 포함된다.  
> 시스템 시간과 레코드 시간을 기준으로 한다.  

`세션 윈도우`
동일 메시지 키의 데이터를 한 세션에 묶어 연산할 때 사용한다.  
세션의 최대 만료시간에 따라 윈도우 사이즈가 달라진다.  

## 프로세서 API
- 메시지 값의 종류에 따라 토픽을 가변적으로 전송
- 일정한 시간 간격으로 데이터 처리

`Processor`, `Transformer`인터페이스로 구현한 클래스가 필요하다.  
Processor 인터페이스는 일정 로직이 이루어 진 뒤 다음 프로세서로 데이터가 넘어가지 않을때,  
Transformer 인터페이스는 일정 로직이 이루어진 뒤 다음 프로세서로 데이터를 넘길때 사용한다.  

## 스트림즈 주요옵션(자바 라이브러리)
###필수 옵션
`bootstrap.server`  
`application.id`: 스트림즈 애플리케이션을 구분하기 위한 고유한 아이디를 설정. 다른 로직 == 다른 스트림즈

###선택 옵션
`default.key.serde`: 레코드의 메시지 키를 직렬화, 역직렬화  
`default.value.serde`: 레코드의 메시지 값을 직렬화, 역직렬화  
`num.stream.threads`: 스트림 프로세싱 실행 시 실행될 스레드 개수 지정, default - 1  
`state.dir`: 상태기반 데이터 처리를 할 때 데이터를 저장할 디렉토리를 지정, default - /tmp/kafka-streams  