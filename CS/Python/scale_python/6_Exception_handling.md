# Exception Handling

다른 컴퓨터들 간에 분산된 어플리케이션이라면, 에러는 더 이상 예외적으로 발생하는 것이 아니며 소프트웨어의 일반적인 현상으로 다뤄야한다. 네트워크로 분산된 환경에서는 실패 가능성이 조금이라도 있다면 에라가 발생한다고 간주해야 한다. 

## retry(재시도)
이름 그대로 소스코드에서 에러가 났을 때, 다시 한번 시도해보는 것이다. 
```python
import time
import random

def do_something():
    if random.randint(0, 1) == 0:
        print("Failure")
        raise RuntimeError
    print("Success")

#type1
while True:
    try:
        do_something()
    except:
        pass
    else:
        break

#type2
while True:
    try:
        do_something()
    except:
        time.sleep(1)
    else:
        break
```
위와 아래의 차이는 함수와 함수 재실행 사이의 중단 시간을 주고 안주고의 차이로 볼 수 있고, 이 방법의 단점은 시스템에 매초마다 연결을 시도한다는 점에서 시스템 정체를 유발한다는 것이다.

### 지수 백오프 알고리즘
재시도 간격을 조절해 모든 부하가 동시에 몰리지 않도록 하는 것.
```python
import time
import random

def do_something():
    '''
    '''

attempt = 0
while True:
    try:
        do_something()
    except:
        time.sleep(2**attempt)
        attempt += 1
    else:
        break
```

다음과 같이 `attempt` 변수를 이용해 부하를 주는 것이다.

## Tenacity
분산 애플리케이션에서는 대부분은 재시도를 통한 패턴을 가지고 있다. 애플리케이션이 네트워크를 통해 여러 노드로 분산되면, 발생 가능한 실패 시나리오를 곧바로 처리할 수 있어야 한다.

`Tenacity`에서는 데코레이터를 이용해 어떤 함수에도 쉽게 적용 가능하다

```python
import tenacity
import random

def do_something():
    if random.randint(0, 1) == 0:
        print("Failure")
        raise RuntimeError
    print("Success")

tenacity.Retrying()(do_something)
```
위와 같은 코드는 사실 tenacity를 쓰는 이유가 크지 않다. 지연 없이 작업을 시작하기 때문이다.

```python
import tenacity
import random

def do_something():
    if random.randint(0, 1) == 0:
        print("Failure")
        raise RuntimeError
    print("Success")

@tenacity.retry(wait=tenacity.wait_fixed(1))
def do_something_and_retry():
    do_something()

@tenacity.retry(
    wait=tenacity.wait_exponential(multiplier=0.5, max=30, exp_base=2),)
def do_something_and_retry_exp():
    do_something()

do_something_and_retry()
do_something_and_retry_exp()
```

데코레이터가 붙은 두 함수처럼 고정된 지연시간이나 지수함수의 지연시간을 지정할 수 있다.
또한, wait_random_exponential 구현도 제공한다.
```python
import tenacity
import random

def do_something():
    if random.randint(0, 1) == 0:
        print("Failure")
        raise RuntimeError
    print("Success")

@tenacity.retry(
    wait=tenacity.wait_fixed(10) + tenacity.wait_random(0, 3)
)
def do_something_and_retry():
    do_something()

do_something_and_retry()
```

또한 특정 예외일때만 재시도할 수도 있다.(예시는 RuntimeError)
```python
@tenacity.retry(
    wait=tenacity.wait_fixed(1),
    retry=tenacity.retry_if_exception_type(RuntimeError)
)
def do_something_and_retry():
    do_something()

do_something_and_retry()
```

비트 연산자를 통해 여러 개의 에러를 조합할 수 있다.(결과값이 없을때도 에러가 나게 만들었다.)
```python
@tenacity.retry(
    wait=tenacity.wait_fixed(1),
    stop=tenacity.stop_after_delay(60),
    retry=(tenacity.retry_if_exception_type(RuntimeError) |
            tenacity.retry_if_result(
                lambda result: result is None
            )
    )
)
def do_something_and_retry():
    do_something()

do_something_and_retry()
```

데코레이터를 사용하지 않고 사용하기
```python
import tenacity
import random

def do_something():
    if random.randint(0, 1) == 0:
        print("Failure")
        raise RuntimeError
    print("Success")
    return True

r = tenacity.Retrying(
    wait=tenacity.wait_fixed(1),
    retry=tenacity.retry_if_exception_type(IOError)
)
r.call(do_something)
```