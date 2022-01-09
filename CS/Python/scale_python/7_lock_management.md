# Lock Management

동시에 접근하지 못하도록 리소스를 보호하는 가장 쉬운 방법은 바로 잠금을 활용하는 것이다. 하지만 애플리케이션이 컴퓨터 여러 대에 분산되어 있다면 이 과정은 복잡해진다.  
분산 잠금 관리자는 여러 네트워크 노드에 분산될 수 있는 중앙 서비스로 구성되며 네트워크를 통해 잠금을 획드하고 해체할 수 있게 한다. 즉, 한개 노드 이상에서 잠금을 관리 할 수 있기 때문에 여러 노드 간에 동기화가 가능하다.

## 스레드 잠금
파이썬은 여러 개의 스레드가 하나의 리소스에 동시 접근하는 걸 막기 위해 threading.Lock을 제공한다. 
```python
import threading

stdout_lock = threading.Lock()

def print_something(something):
    with stdout_lock:
        print(something)

t = threading.Thread(target=print_something, arg=("hello",))
t.daemon = True
t.start()
print_something("thread_start")
```

잠금이 실행 순서를 강제하지는 않지만 여러 스레드 중 오직 한 스레드만 print를 사용할 수 있도록 해서 데이터 손상을 방지한다.
> 파이썬의 일부 데이터 타입은 *원자적 연산을 지원한다.  
> - list.appent
> - list.extent
> - list.__getitem__
> - list.pop
> - list.sort
> - x =y
> - setattr
> - dict.__setitem__
> - dict.update
> - dict.keys

어떤 스레드가 잠금을 획득하면 나머지 스레드는 잠금이 해제되기를 기다려야 하므로 그만큼 프로그램이 느려진다. 하지만 파이썬 데이터 타입의 일부는 원자적 연산을 제공하므로 안전하게 데이터를 추가할 수 있다.  

재귀함수와 같이 어떤 스레드에서 여러 번에 걸쳐 잠금을 획득해야 한다면, threading.RLock을 통해 재진입 가능한 잠금 사용이 가능하다.(동일 스레드에서 여러번 획득가능.)
```python
import threading

rlock = threading.RLock()

with rlock:
    with rlock:
        print("Double acquired")
```
threading.Event 객체를 통해서 threading.Event.set()이 호출되기 전까지 스레드를 실행하는 함수가 있다.
```python
import threading
import time

stop = threading.Event()

def background_job():
    while not stop.is_set():
        print("I'm still running!")
        stop.wait(0.1)

t = threading.Tread(target=backgroud_job)
t.start()
print("thread start!")
# 지연시간
time.sleep(1)
# 1초가 지난후 threading.Event를 True로 만들어서 thread 종료
stop.set()
t.join()
```


## 프로세스 잠금
프로세스를 사용해서 작업 부하를 분산할 때는 여러 개의 프로세스가 접근할 수 있는 전용 잠금이 필요하다.
- multiprocessing 패키지
- os.fork

공유 리소스에 대한 접근 보호의 기본 방법은 `multiprocessing.Lock`을 이용하는 것이다. 
```python
import multiprocessing
import time


def print_cat():
    # 조금 기다려서 임의의 결과가 나오도록 한다.
    time.sleep(0.1)
    print(" /\\_/\\")
    print("( o.o )")
    print(" > ^ <")

with multiprocessing.Pool(processes=3) as pool:
    jobs = []
    for _ in range(5):
        jobs.append(pool.apply_async(print_cat))
    for job in jobs:
        job.wait()
```
위와 같이 잠금없이 만들게 되면 출력결과가 뒤죽박죽 섞여 나올 것이다. 간섭이 일어나기 때문이다.
```bash
$ python3 04_multiprocessing-without-lock.py 
 /\_/\
( o.o )
 > ^ <
 /\_/\
 /\_/\
( o.o )
( o.o )
 > ^ <
 > ^ <
 /\_/\
( o.o )
 > ^ <
 /\_/\
( o.o )
 > ^ <

```
```python
import multiprocessing
import time

stdout_lock = multiprocessing.Lock()

def print_cat():
    # 조금 기다려서 임의의 결과가 나오도록 한다.
    time.sleep(0.1)
    with stdout_lock:
        print(" /\\_/\\")
        print("( o.o )")
        print(" > ^ <")

with multiprocessing.Pool(processes=3) as pool:
    jobs = []
    for _ in range(5):
        jobs.append(pool.apply_async(print_cat))
    for job in jobs:
        job.wait()
```
다음과 같이 `multiprocessing.Lock`을 걸어주게 되면 간섭을 받지 않고 동일하게 출력된다.

이러한 잠금은 운영 체제 간에 이식되지 않는다. 운영 체제간 서로 다른 프로세스 통신 메커니즘을 사용하기 때문에 호환되지 않는다.   

### **fastener**
`fasteners`는 파일 잠금을 기반으로 한 공통 솔루션을 파이썬으로 훌륭히 구현했다. 파일 경로가 잠금을 구분하는 식별자가 되어 여러 독립적인 프로세스에서 사용할 수 있으며, 이를 통해 프로세스 간의 리소스 접근을 보호한다.

```python
import time
import fasteners

lock = fasteners.InterProcessLock("/tmp/mylock")
with lock:
    print("Access locked")
    time.sleep(1)
```
다음과 같이 `fasteners.InterProcessLock`의 인자에 파일 경로를 넣게 되면 해당 경로로 lock을 걸어 독립을 유지시켜 준다.
보통은 `$TMPDIR`이나 `/var/run`과 같이 시스템 시작 시에 지워지는 임시 디렉터리를 사용한다.  
또한, 데코레이터도 제공한다.
```python
import time
import fasteners

@fasteners.interprocess_locked('/tmp/tmp_lock_file')
def locked_print():
    for i in range(10):
        print("I have the lock")
        time.sleep(0.1)

locked_print()
```

### **etcd**

etcd는 키와 값을 저장하고 여러 노드로 복사되며, 노드를 통해 키를 조회하거나 업데이트할 수 있다.(데이터 동기화를 위해 래프트 알고리즘을 사용: 노드 간의 일관성을 책임질 리더를 선출해서 분산 시스템에서 합의 문제를 해결.) 래프트는 etcd의 데이터가 모든 노드에서 동일하다는 일관성을 보장하므로 특정 노드에 문제가 발생하더라도 해당 노드가 정상으로 돌아 올 때까지 etcd 클러스터가 계속 작동할 수 있게 한다.

여러 서비스가 lock1이라는 키를 획득하고 싶을때, 키가 이미 존재한다면 트랜잭션을 지원해 거부 할 수 있다. 그리고 키가 삭제시 클라이언트에게 알림이 가서 다시 획득할 수 있다.
잠금을 획득한 클라이언트가 다운되면 잠금 해제가 불가능하다. 이 경우를 방지하고자 TTL(time-to-live)를 정의한다.
```python
import etcd3

client = etcd3.client()
lock = client.lock("foobar")

# type 1
lock.acquire()
try:
    print("do something")
finally:
    lock.release()

#type 2
with lock:
    print("do something")
```

서비스의 견고성을 높이려면 etcd를 여러 노드로 구성된 클러스터 형태로 배포한다. 이렇게 하면 애플리케이션이 연결한 서버가 다운되어도 클러스터의 나머지 노드가 작업을 계속할 수 있다. 

```python
import threading
import time

import cotyledon
import etcd3

class PrinterService(cotyledon.Service):
    name = "printer"

    def __init__(self, worker_id):
        super(PrinterService, self).__init__(worker_id)
        self._shutdown = threading.Event()
        self.client = etcd3.client()

    def run(self):
        while not self._shutdown.is_set():
            with self.client.lock("print"):
                print("I'm %s and I'm th only one printing" % self.worker_id)
                time.sleep(1)

    def terminate(self):
        sef._shudown.set()

# 매니저 실행
manager = cotyledon.ServiceManager()
# 실행할 네 개의 PrinterService 추가
manager.add(PrinterService, 4)
# 모두 실행
manager.run()
```



> etcd실행시에는 etcd daemon이 띄워져 있어야한다.
> ```bash
> $ sudo apt-get install -y etcd
> $ sudo systemctl enable etcd
> $ sudo systemctl start etcd
> ```

***
원자적 연산:
중간에 어떤한 방해도 받지 않고 어셈블리어 명령어 하나로 실행할 수 있다.
- 메모리에 접근하지 않거나 한번 접근하는 어셈블리어 명령은 원자적이다.

- inc나 dec같이 메모리에서 데이터를 읽고, 이를 갱시하고,
갱신한 값을 메모리에 다시 쓰는 읽기/수정/쓰기 어셈블리어
명령은 읽기와 쓰기 사이에 다른 프로세서가 메모리
버스를 점유하지 않는 한 원자적이다. 유니프로세서 시스템에서는
같은 프로세서에서 메모리 접근이 일어나므로 메모리버스를 훔치는
일은 발생하지 않는다.

- 연산 코드 앞에 lock(0xf0)접두어가 붙은 읽기/수정/쓰기
어셈블리 명령은 멀티프로세서 시스템에서도 원자적이다.
제어 유닛이 이 접두어를 만나면 명령어 수행이 끝날때 까지
메모리 버스를 잠근다. 따라서 잠긴 명령을 실행하는 동안에는
메모리에 다른 프로세서가 접근할수 없다.