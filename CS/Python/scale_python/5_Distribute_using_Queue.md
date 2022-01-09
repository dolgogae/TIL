# Distribute_using_Queue

큐를 사용한 분산 시스템 구현은 처리량이 높아진다는 장점과 지연 시간이 늘어난다는 단점 사이의 절충안이다.
기본적으로 큐와 워커 두개의 요소로 구성되고, 이 방식으로 애플리케이션이 동작하기 위해선 작업을 분산할 방법이 필요하다. 제대로 구현하려면 작업 부하를 수평으로 쉽게 확장할 수 있다.

## RQ(Redis Q)
레디스는 오픈 소스 인메모리 데이터베이스로 내구성 옵션을 갖춘 네트워크 기반의 키-값 저장소다.

RQ를 사용하기 전에 redis를 위한 환경설정이 필요하다.
```bash
wget http://download.redis.io/redis-stable.tar.gz
tar xvzf redis-stable.tar.gz
cd redis-stable
make

apt-get install -y redis
```

해당 명령어들로 레디스를 설치해주고, `redis-server`를 입력해주면 다음과 같은 실행창을 볼 수 있다.
```bash
$ redis-server
4424:C 12 Aug 11:39:13.998 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
4424:C 12 Aug 11:39:13.998 # Redis version=4.0.9, bits=64, commit=00000000, modified=0, pid=4424, just started
4424:C 12 Aug 11:39:13.998 # Warning: no config file specified, using the default config. In order to specify a config file use redis-server /path/to/redis.conf
                _._                                                  
           _.-``__ ''-._                                             
      _.-``    `.  `_.  ''-._           Redis 4.0.9 (00000000/0) 64 bit
  .-`` .-```.  ```\/    _.,_ ''-._                                   
 (    '      ,       .-`  | `,    )     Running in standalone mode
 |`-._`-...-` __...-.``-._|'` _.-'|     Port: 6379
 |    `-._   `._    /     _.-'    |     PID: 4424
  `-._    `-._  `-./  _.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |           http://redis.io        
  `-._    `-._`-.__.-'_.-'    _.-'                                   
 |`-._`-._    `-.__.-'    _.-'_.-'|                                  
 |    `-._`-._        _.-'_.-'    |                                  
  `-._    `-._`-.__.-'_.-'    _.-'                                   
      `-._    `-.__.-'    _.-'                                       
          `-._        _.-'                                           
              `-.__.-'                                               

4424:M 12 Aug 11:39:13.999 # Server initialized
4424:M 12 Aug 11:39:13.999 # WARNING overcommit_memory is set to 0! Background save may fail under low memory condition. To fix this issue add 'vm.overcommit_memory = 1' to /etc/sysctl.conf and then reboot or run the command 'sysctl vm.overcommit_memory=1' for this to take effect.
4424:M 12 Aug 11:39:13.999 # WARNING you have Transparent Huge Pages (THP) support enabled in your kernel. This will create latency and memory usage issues with Redis. To fix this issue run the command 'echo never > /sys/kernel/mm/transparent_hugepage/enabled' as root, and add it to your /etc/rc.local in order to retain the setting after a reboot. Redis must be restarted after THP is disabled.
4424:M 12 Aug 11:39:13.999 * Ready to accept connections
```
```bash
$ rq worker
 rq worker
11:40:59 Worker rq:worker:09d1792742324f01b338e318b9001d57: started, version 1.9.0
11:40:59 Subscribing to channel rq:pubsub:09d1792742324f01b338e318b9001d57
11:40:59 *** Listening on default...
11:40:59 Cleaning registries for queue: default
11:40:59 default: builtins.sum([42, 43]) (46526c77-fb54-4779-8110-a111f7d958bf)
11:40:59 default: Job OK (46526c77-fb54-4779-8110-a111f7d958bf)
```

```python
import time

from rq import Queue
from redis import Redis

q = Queue(connection=Redis())

job = q.enqueue(sum, [42, 43])
# 결과가 준비될 때까지 대기한다.
while job.result is None:
    time.sleep(1)

print(job.result)

###################################
# shell
$ python3 01_push-job.py 
85
```

총 세개의 터미널을 이용해 하나는 `redis-server` 다른 하나는 `rq worker`를 띄우고, python파일들을 실행하면 된다.
> 나는 docker container를 이용해 환경을 구성했다.

위의 `rq worker`는 다음과 같은 정보를 표시한다. 

1. 실행할 함수와 작업의 uuid를 표시한다. 
2. 작업이 정확하게 실행됐다. 
3. 작업 결과는 반드시 이 시간 안에 가져와야 한다. 

레디스는 TTL값 설정을 지원하며 이 경우는 500초가 지나면 레디스가 결과를 제거한다. 

RQ는 작업 부하를 조정할 때, 작업 유효 시간(ttl)이나 결과 유효시간(ttl_result)을 설정 할 수 있다. 

```python 
import time 

from rq import Queue 
from redis import Redis 
import requests 

q = Queue(name="http", connection=Redis()) 

job = q.enqueue(requests.get, "http://httpbin.org/delay/1", ttl=60, result_ttl=300) 

# URL을 가져오는 동안 다른 작업을 가능 
while job.result is None: 
    time.sleep(1) 

    print(job.result) 
``` 

# Celery(셀러리) 

셀러리는 작업 결과를 저장하기 위한 백엔드를 필요로 하며 레디스, MongoDB, SQL, 일래스틱서치, 파일 등의 여러 솔루션을 지원한다. (자신만의 백엔드를 만드는 것도 가능하다.) 사용자 요청에 포함될 필요가 없는 불필요한 과정이나 매우 무거운 쿼리 실해을 포함하는 경우가 있다. 이 API에 포함된 외부 연동이나 무거운 작업들은 Celery Task로 정의해 브로커(RabbitMQ)와 컨슈머(Celery worker)를 이용해 비동기로 처리함으로써 사용자에게 가능한 빠른 응답 결과를 제공할 수 있을 것이다.

Task: 비동기로 호출할 수 있는 함수. 호출시 브로커 큐에 태스크를 넣는다. 이후, 워커가 태스크를 실행후 결과를 백엔드에 저장한다. 

```python 
import celery 

app = celery.Celery('03_celery-task', 
                    broker='redis://localhost', 
                    backend='redis://localhost') 

@app.task 
def add(x, y): 
    return x + y 

if __name__ == '__main__': 
    result = add.delay(4, 4) 
    print("Task state: %s" % result.state) 
    print("Result: %s" % result.get()) 
    print("Task state: %s" % result.state) 
``` 

위의 데코레이터는 app.task(celery queue)는 add 함수를 태스크로 등록 시킨다. 이 태스크는 셀러리 워커를 통해 애플리케이션에서 비동기로 실행된다. 

Celery 명령은 작업 큐와 워커를 조종하고 검사하는 다양한 명령을 제공한다.(위의 소스코드를 그냥 실행하게 된다면 실행할 워커가 없기 때문에 준비중이라고 나올 것이다.) 

> Celery Task로써 호출하려면 `add.delay() 같은 형태로 delay() 메소드를 호출한다.

```bash 
$ celery worker --app 03_celery-task 
``` 

위의 명령어로 worker를 실행해줘야 한다.(`rq worker`와 같은 역할) 

## 실패 처리 

태스크는 보통 DB나 Rest API 같은 외부 서비스에 의존 한다. 따라서, 일시적 현상일수도 있다는 것이다. 따라서 실패후 다시 한번 실행해보는게 중요하다. 

```python 
import celery 

app = celery.Celery('03_celery-task', 
                    broker='redis://localhost', 
                    backend='redis://localhost') 

@app.task(bind=True, retry_backoff=True, 
          retry_kwargs={'max_retries':5} 
def add(self, x, y): 
    try: 
        return x + y 
    except OverflowError as exc: 
        self.retry(exc=exc) 

if __name__ == '__main__': 
    result = add.delay(4, 4) 
    print("Task state: %s" % result.state) 
    print("Result: %s" % result.get()) 
    print("Task state: %s" % result.state) 
``` 

위의 소스코드와 같이 overflow가 났을 경우 최대 5번까지 재시도를 해본다. 

## 태스크 체인
셀러리는 체인처럼 여러 태스크를 연결할 수 있는 태스크 제인을 제공한다.
```python
import celery

app = celery.Celery('05_celery-chain',
                   broker='redis://localhost',
                   backend='redis://localhost')


@app.task
def add(x, y):
    return x + y


@app.task
def multiply(x, y):
    return x * y


if __name__ == '__main__':
    chain = celery.chain(add.s(4, 6), multiply.s(10))
    print("Chain: %s" % chain)
    result = chain()
    print("Task state: %s" % result.state)
    print("Result: %s" % result.get())
    print("Task state: %s" % result.state)
```

## 멀티 큐
큐 여러개를 이용해 태스크를 분산할 수 있다. 이 특징으로 실행할 작업을 좀 더 세밀하게 제어해서 분산할 수 있다.
```python
import celery


app = celery.Celery('06_celery-task-queue',
                   broker='redis://localhost',
                   backend='redis://localhost')


@app.task
def add(x, y):
    return x + y


if __name__ == '__main__':
    result = add.apply_async(args=[4, 6], queue='low-priority')
    print("Task state: %s" % result.state)
    print("Result: %s" % result.get())
    print("Task state: %s" % result.state)
```
기본 큐는 싱글 큐이지만 다음같이 `--queue` 옵션을 사용한다.

```bash
$ celery worker --app 06_celery-task-queue.py --queue celery,low-priority
```
워커에서는 celery와 low-priority 큐를 사용하고, 다른 워커들을 celery 큐만 사용하도록 설정하면, low-priority 큐는 워커가 작어할 시간이 있을 때만 작동하며, 다른 모든 워커는 보통의 우선순위로 celery 큐에서 항상 작업을 대기한다.

애플리케이션에서 필요로하는 큐의 수를 결정해줄 순 없다. 보통 작업의 우선순위를 분리 기준으로 삼는 것이 확실한 방법이다. 큐를 여러개 사용하면 작업 스케줄링을 더 세밀하게 조정할 수 있으므로 적극 활용하면 좋다.
