# Caching

애플리케이션을 대규모로 확장해야 할 때 캐싱(caching)은 필수 요소이다. 
- 높은 계산 비용: 메모이제이션의 기본 원리
- 긴 지연시간: 지연 시간이 더 짧은 데이터 저장소에 저장해서 속도를 높일 수 있다.

## 로컬 캐싱
로컬 캐싱은 네트워크를 통해 원격 캐시에 접근할 필요가 없으므로 속도 면에서 큰 장점이 있다. key와 data를 묶어서 저장한다. 파이썬 딕셔너리는 캐싱을 구현하기 위한 가장 명확한 데이터 구조이다.

```python
cache = {}
def compute_length_or_read_in_cache(s):
    try:
        return cache[s]
    except KeyError:
        cache[s] = len(s)
        return cache[s]

```
다음과 같은 캐싱 방법은 약점이 있다. 저장소가 무한히 커지지 않도록 캐시 내의 항목들이 일정 시간이 지나면 만료되도록 구현해야 한다.
- LRU(Least Recently Used): 가장 오랫동안 사용하지 않은 항목을 먼저 제거한다.(마지막 접근 시간 저장 필요)
- LFU(Least Frequently Used): 자주 사용하지 않는 항목을 먼저 제거한다.(접근한 횟수 저장)
- TTL(Time to Live): 특정 시간보다 오래된 항목을 제거한다.

```python
import cachetools
cache = cachetools.LRUCache(maxsize=3)
cache['foo'] = 1
cache['bar'] = 42
```

```python
import time

import cachetools
import requests

cache = cachetools.TTLCache(maxsize=5, ttl=5)
URL = "http://httpbin.org/uuid"
while True:
    try:
        print(cache[URL])
    except KeyError:
        print("Paged not cached, fetching")
        page = requests.get(URL)
        cache[URL] = page.text
        print(page.text)
    time.sleep(1)
```
TTLCache 클래스는 여러 형태의 시간 정의를 허용하므로 필요하다면 초 단위 대신 다른 시간 단위(반복, ping, 요청 등)를 사용하도록 지정할 수 있다.

## 메모이제이션

함수 실행 결과를 저장해서 호출 속도를 높이는 기술. 어떤 전역 상태에도 의존하지 않는 함수일 때만 가능하다.
```python
import math
_SIN_MEMORIZED_VALUES = {}
def memorized_sin(x):
    if x not in _SIN_MEMORIZED_VALUES:
        _SIN_MEMORIZED_VALUES[x] = math.sin(x)
    return _SIN_MEMORIZED_VALUES[x]

memorized_sin(1)
```
간단한 메모이제이션 예제이다. sin 함수를 미리 계산한 결과를 저장해놓는 메모이제이션이다.

파이썬에서 메모이제이션은 데코레이터를 활용해서 쉽게 쓸 수 있다. 
```python
import functools
import math

@functools.lru_cache(maxsize=2)
def memorized_sin(x):
    return math.sin(x)

memorized_sin(1)

####################################
####################################

import cachetools.func
import math
import time

memorized_sin = cachetools.func.ttl_cache(ttl=5)(math.sin)

memorized_sin(1)
```

## 분산 캐시

`cachetools`나 `functools.lru_cache`가 제공하는 캐싱 시스템을 분산 시스템에 저장하기엔 데이터 저장이 분산 되지 않는다. 딕셔너리를 통해 저장하기 때문에 대규모 애플리케이션에 필요한 확장 및 공유 가능한 캐시 데이터 저장소를 제공하지 않는다.

### **memcached**

```python
import pymemcache.client import base

client = base.Client(('localhost', 11211))
client.set('some_key', 'some_value')
result = client.get('some_key')
print(result)
```

위의 예제는 네트워크를 통해 키-값 튜플을 저장하고 여러 개의 분산된 노드를 통해 데이터에 접근할 수 있다는 걸 보여준다. 만료 시간을 지정할 수 있으며, 지정한 시간이 지나면 캐시에서 키가 제거된다.
> 캐시 서버는 무한히 성장할 수 없다. 너무 많은 키를 가지고 있다면 그 중 일부를 비워야 한다. 또한, 어떤 키는 만료 시간이 되어 제거될 수 있다. 이 경우는 데이터가 손실되므로 원본 데이터를 다시 쿼리해야 한다.
```python
from pymemcache.client import base

def do_some_query():
    # 실제로는 데이터베이스나 REST API로 원격에서 데이터를 가져온다고 가정한다.
    return 42

client = base.Client(('localhost', 11211))
result = client.get('some_key')
if result is None:
    result = do_some_query()
    client.set('some_key', result)
print(result)
```
정상적인 동작을 통해 캐시가 비워진 상황을 다루는 건 애플리케이션으 필수 요소다. 콜드(cold) 캐시 상태도 반드시 처리해야하고, 하나의 요청으로 모두 채워져야 한다.
> 콜드캐시: 캐시가 비워져 있는 상태.

```python
from pymemcache.client import base
from pymemcache import fallback

def do_some_query():
    # 실제로는 데이터베이스나 REST API로 원격에서 데이터를 가져온다고 가정한다.
    return 42

# 'ignore_exc=True'로 설정해서 캐시 누락을 처리할 수 있도록 한다.
# 새 캐시에 데이터가 채워지면 이전 캐시 서버를 중지할 수 있다.
old_cache = base.Client(('localhost', 11211), ignore_exc=True)
new_cache = base.Client(('localhost', 11211))

client = fallback.FallbackClient((new_cache, old_cache))

result = client.get('some_key')
if result is None:
    result = do_some_query()
    client.set('some_key', result)
print(result)
```
`FallbackClient`는 생성자에 전달된 이전 캐시를 쿼리하여 순서를 유지한다. 이 경우, 새로운 캐시 서버가 항상 먼저 쿼리하고, 캐시 미스가 발생하면 이전 캐시 서버를 쿼리해서
 원본 데이터 소스로 연결되는 걸 방지한다. 어떤 키가 설정될 때는 새 캐시 서버에만 저장된다. 일정 시간이 지난 뒤에는 이전 캐시를 폐기하고 FallbackClient를 new_cache 클라이언트로 대체할 수 있다.

동시성 문제에 대해서 `memcached`는 CAS(check and set)를 사용해서 이 문제를 해결한다.

```python
def on_visit(client):
    result = client.get('visitors')
    if result is None:
        result = 1
    else:
        result += 1
    client.set('visitors', result)
```
해당 코드에서 클라이언트가 동시에 접속할때, 얻고자 하는 값이 호출 수라면 함수가 원하는 기대값보다 동시에 접속한 클라이언트만큼 더 나올 것이다.

```python
def on_visit(client):
    while True:
        result, cas = client.gets('visitors')
        if result is None:
            result = 1
        else:
            result += 1
        if client.cas('visitors', result, cas):
            break
```

`gets`함수는 `get`과 동일하지만 cas도 반환한다는 특징이 있다. cas는 만약 gets 함수 호출 후에 CAS값이 변경됐다면 작업이 실패한다. 이때는 성공할 때까지 루프를 반복한다.
그러므로 두 개의 클라이언트가 동시에 카운터를 변경하도록 시도한다면, 둘 중 하나만 작업이 성공하므로 카운터는 43이 된다. 다른 하나는 client.cas 함수를 호출할 때 False 값이 반환되므로 루프를 다시 반복하게 된다.
이처럼 동시성의 문제를 해결할 수 있다.