# CPU expand
파이썬에서 스레드는 여러 함수를 동시에 실행하기에 좋은 방법이다. 시스템이 싱글 CPU만 지원한다면 운영체제 스케줄러에 따라 스레드가 차례로 실행된다.  
기본으로, 메인 스레드가 하나 존재하며 파이썬 애플리케이션을 실행하는 역할을 한다. 메인 스레드 이외의 다른 스레드 실행을 위해서 파이썬은 threading 모듈을 제공한다.
```python
import threading

def print_something(something):
    print(something)

t = threading.Thread(target=print_something, args=("hello",))
t.start()
print("thread started")
t.join()
```
위 코드에서 스레드 실행 순서는 보장되지 않기 때문에 출력 결과는 매번 다르게 나타난다. join 함수를 호출하면 두 번째 스레드가 완료될 때까지 메인 스레드는 대기한다. 모든 스레드를 join하지 않으면 다른 스레드가 완료되기 전에 메인 스레드가 먼저 끝나서 종료될 수 있다. 이때는 프로그램이 블록된 것처럼 보이며 KeyboardInterrupt에도 응답하지 않는다.  

이처럼 프로그램이 스레드 완료를 기다릴 수 없는 상황이라면 스레드를 데몬으로 만들 수 있다.
```python
import threading

def print_something(something):
    print(something)

t = threading.Thread(target=print_something, args=("hello",))
t.daemon = True
t.start()
print("thread started")
```

```python
import random
import threading

results = []

def compute():
    results.append(sum([random.randint(1, 100) for i in range(1000000)]))

workers = [threading.Thread(target=compute) for i in range(8)]
for worker in workers:
    worker.start()
for worker in workers:
    worker.join()
print("Results: %s" % results)
```

```bash
$  python 03_multithreading_worker.py 
Results: [50539318, 50556029, 50462972, 50554635, 50549419, 50577596, 50471554, 50529482]
```
다음 소스코드에서는 코어가 네 개라면 최대 400%의 CPU를 쓸 수 있어야 한다. 하지만 CPU 사용량은 그에 미치지 못할 것이다. 이는 GIL을 통한 병목현상 때문이다.  
GIL은 멀티 스레드를 실행할 때 CPython의 성능을 제한한다. 그러므로 스레드는 병렬 컴퓨팅을 수행하거나 네트워크나 파일처럼 느린 입출력을 처리할 때 유용하다. 이러한 작업은 메인 스레드를 차단하지 않고 병렬로 실행할 수 있기 때문이다.


# Use Proess
멀티 스레딩은 GIL의 제약 때문에 완벽한 확장성 솔루션이 아니다. 따라서 스레드 대신 프로세스를 사용하는 것이 더 좋은 방법이다. multiprocessing 패키지를 이용하는 것이 os.fork를 이용하는 것이 범용성적인 측면에서 유리하다. multiprocessing 패키지는 운영체제에 상관없이 새 프로세스를 생성할 수 있는 동일한 인터페이스를 제공하는 데 장점이 있다.
```python
import random
import multiprocessing

def compute(results):
    results.append(sum([random.randint(1,100) for i in range(1000000)]))

with multiprocessing.Manager() as manager:
    results = manager.list()
    workers = [multiprocessing.Process(target=compute, args=(results, )) for x in range(8)]

    for worker in workers:
        worker.start()
    for worker in workers:
        worker.join()
    print("Results: %s" % results)
```
 각 프로세스는 새로운 독립적인 파이썬이므로 데이터는 복사되며 각 프로세스는 고유한 전역 상태를 가진다. `multiprocessing.Manager()` 클래스는 동시 접근에 안전한 공유 데이터 구조를 만드는 방법을 제공한다.

 ```bash
$ time python 03_multithreading_worker.py 
Results: [50506771, 50542348, 50494607, 50513247, 50502474, 50513529, 50451518, 50464363]

real	0m8.046s
user	0m10.080s
sys	0m3.012s


$ time python 04_multithreading_precess.py 
Results: [50539023, 50533092, 50469520, 50510408, 50496073, 50482564, 50480963, 50460329]

real	0m0.682s
user	0m4.184s
sys	0m0.152s
 ```

위처럼 멀티 스레드와 멀티 프로세스를 비교했을 때, 시간 차이가 많이 나는 것을 확인 할 수 있다. GIL을 통한 병목현상이 해결됐기 때문이다.  
multiprocessing 라이브러리는 pool 매커니즘을 제공하므로 함수형 방식으로 작성할 수 있다.
```python
import random
import multiprocessing

def compute(n):
     return sum([random.randint(1, 100) for i in range(1000000)])

pool = multiprocessing.Pool(precess=8)
print("Results: %s" % pool.map(compute, range(8)))
```

multiprecessing.Pool을 사용하면 프로세스를 수동으로 관리할 필요가 없다. pool은 주문형 방식으로 프로세스를 시작하고 작업이 완료되면 결과를 가져오며 재사용이 가능하므로 높은 fork 시스템 호출 수도 줄일 수 있다.

# concurrent.futures
concurrent.futures는 먼저 실행자를 선택해야 한다.
- concurrent.futures.ThreadPoolExecutor: 쓰레드 기반
- concurrent.futures.ProcessPoolExecutor: 프로세스 기반

프로그램이 스레드나 프로세스에서 실행할 작업을 스케줄하면, concurrent.futures 모듈은 스케줄된 작 각 작업에 대해 Future 객체를 반환한다. 이 Future객체는 해당 작업이 미래 언젠가 완료 될 것이라는 약속이다.
```python
from concurrent import futures
import random


def compute():
    return sum(
        [random.randint(1, 100) for i in range(1000000)])


with futures.ThreadPoolExecutor(max_workers=8) as executor:
    futures = [executor.submit(compute) for _ in range(8)]

results = [f.result() for f in futures]

print("Results: %s" % results)

###########################################################
###########################################################

from concurrent import futures
import random


def compute():
    return sum(
        [random.randint(1, 100) for i in range(1000000)])


with futures.ProcessPoolExecutor() as executor:
    futures = [executor.submit(compute) for _ in range(8)]

results = [f.result() for f in futures]

print("Results: %s" % results)
```

위의 코드는 순서대로 쓰레드와 프로세스를 futures를 사용한 예제 코드이다. 밑의 futures.ProcessPoolExecutor()는 기본적으로 `multiprocessing.cpu_count`를 호출해 워커 수를 결정하기 때문에 max_workers를 별도로 설정할 필요는 없다.

## futurist
futurist는 실행자에 관한 통계를 제공한다.
```python
import futurist
from futurist import waiters
import random


def compute():
    return sum(
        [random.randint(1, 100) for i in range(10000)])


with futurist.ThreadPoolExecutor(max_workers=8) as executor:
    futures = [executor.submit(compute) for _ in range(8)]
    print(executor.statistics)

results = waiters.wait_for_all(futures)
print(executor.statistics)

print("Results: %s" % [r.result() for r in results.done])


## 실행결과
<ExecutorStatistics object at 0x7f471af5d948 (failures=0, executed=7, runtime=0.09, cancelled=0)>
<ExecutorStatistics object at 0x7f471af5d990 (failures=0, executed=8, runtime=0.11, cancelled=0)>
Results: [507089, 504486, 506749, 505771, 506689, 506299, 504114, 503236]
```
또한 futurist는 `check_and_reject` 인수를 사용해 작업 거부를 할 수 있다.
```python
import futurist
from futurist import rejection
import random


def compute():
    return sum(
        [random.randint(1, 100) for i in range(1000000)])


with futurist.ThreadPoolExecutor(
        max_workers=8,
        check_and_reject=rejection.reject_when_reached(16)) as executor:
    futures = [executor.submit(compute) for _ in range(20)]
    print(executor.statistics)

results = [f.result() for f in futures]
print(executor.statistics)

print("Results: %s" % results)
```
위의 코드는 메모리 오버플로우를 피하기 위해 큐의 최대 크기를 제한한다. 백로그 크기를 16으로 제한했기 때문에 컴퓨터 속도에 따라 실행자가 작업을 빠르게 처리하지 못하면 `futures.RejectedSubmission`예외가 발생 할 수 있다. 따라서 함수 실행을 주기적으로 스케줄링 해주는 futurist.periodics.PeriodicWorker 클래스와 자주 사용된다.

```python
import time

from futurist import periodics

# 1초마다 작업의 경과를 표시
@periodics.periodic(1)
def every_one(started_at):
    print("1: %s" % (time.time() - started_at))


w = periodics.PeriodicWorker([
    (every_one, (time.time(),), {}),
])

# 4초마다 통계를 출력
@periodics.periodic(4)
def print_stats():
    print("stats: %s" % list(w.iter_watchers()))


w.add(print_stats)
w.start()
```


# Daemon Process
파이썬에선 멀티 프로세싱이 효율적이라는 것을 알 수 있다. concurrent.futures의 `ProcessPoolExecutor`를 활용해 데몬을 만들 수 있다.

## 코틀리든(cotyledon) 라이브러리
```python
import threading
import time

import cotyledon


class PrinterService(cotyledon.Service):
    name = "printer"

    def __init__(self, worker_id):
        super(PrinterService, self).__init__(worker_id)
        self._shutdown = threading.Event()

    def run(self):
        while not self._shutdown.is_set():
            print("Doing stuff")
            time.sleep(1)

    def terminate(self):
        self._shutdown.set()


# manager 생성
manager = cotyledon.ServiceManager()
# 2개의 PrinterService를 실행하기 위해 추가
manager.add(PrinterService, 2)
# manager에 추가된 작업을 모두 실행
manager.run()
```
다음 소스코드에서는 PrinterService 클래스를 지속적으로 실행시킨다. run은 메인 루프를 포함하고, terminate는 서비스를 종료할때 호출된다.
코틀리든은 주 프로세스를 실행해서 자식 프로세스를 관리한다. 또한, 독특한 프로세스 이름을 부여해서 프로세스 목록에서 쉽게 구분할 수 있다. 프로세스 중 하나가 죽거나 크래시되면 코틀리든이 자동으로 프로세스를 다시 실행한다.(파이썬을 사용하는 모든 언어에서 지원하기 때문에 os에 의존적이지 않다.)

위의 예제와 다르게 독립적이지 않은 프로세스 경우는 
```python
import multiprocessing
import time

import cotyledon


class Manager(cotyledon.ServiceManager):
    def __init__(self):
        super(Manager, self).__init__()
        queue = multiprocessing.Manager().Queue()
        self.add(ProducerService, args=(queue,))
        self.add(PrinterService, args=(queue,), workers=2)


class ProducerService(cotyledon.Service):
    def __init__(self, worker_id, queue):
        super(ProducerService, self).__init__(worker_id)
        self.queue = queue

    def run(self):
        i = 0
        while True:
            self.queue.put(i)
            i += 1
            time.sleep(1)


class PrinterService(cotyledon.Service):
    name = "printer"

    def __init__(self, worker_id, queue):
        super(PrinterService, self).__init__(worker_id)
        self.queue = queue

    def run(self):
        while True:
            job = self.queue.get(block=True)
            print("I am Worker: %d PID: %d and I print %s"
                % (self.worker_id, self.pid, job))


Manager().run()
```

다음과 같이 큐 객체에 모든 서비스를 전달해 내부적인 잠금을 통해 여러 스레드와 프로세스에서 안전히 사용할 수 있음을 보장한다.
