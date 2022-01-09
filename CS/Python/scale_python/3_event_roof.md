# Event Roof
프로그램의 중앙에 위치한 제어 흐름을 의미한다. 어떤 메시지가 큐에 들어가면 이벤트 루프가 큐에서 메세지를 꺼내 적절한 함수로 전달한다.
예를 들어, 단일 스레드에서 돌아가지만 하나의 큐를 이용해 웹페이지상 애니메이션이 돌아가는 동시에 검색을 할 수 있고, 이런 작업을 가능하게 하는 것이 이벤트 루프이다.  
가장 많이 사용되는 이벤트 소스는 I/O다. 대부분 읽기, 쓰기는 본질적으로 블로킹 작업이므로 실행 속도는 느릴 수 밖에 없다. read작업이 완료될 때까지 몇 초간 기다려야 하며 그 동안 다른 작업은 할 수 없다.

```python
import asyncio


async def hello_world():
    print("hello world!")
    return 42

hello_world_coroutine = hello_world()
print(hello_world_coroutine)

event_loop = asyncio.get_event_loop()
try:
    print("entering event loop")
    result = event_loop.run_until_complete(hello_world_coroutine)
    print(result)
finally:
    event_loop.close()
```

asyncio를 사용해서 asyncio가 이벤트 루프를 생성하고 애플리케이션은 파일의 읽기 작업 준비나 소켓 쓰기 작업 준비처럼 특정 이벤트가 발생했을 때 호출될 함수를 등록한다. 이러한 유형의 함수를 코루틴(coroutine)이라고 부른다. 코루틴은 호출 측에 제어를 되돌려 줄 수 있는 특별한 유형의 함수로서, 호출 측에서 이벤트 루프를 계속 실행할 수 있게 한다.(def(함수) 앞에 async를 붙히면 이벤트 루프에 함수가 실행을 기다리는 상태가 된다.)  

코루틴은 다른 코루틴과 협력(cooperate)할 수 있다.
```python
import asyncio


async def add_42(number):
    print("Adding 42")
    return 42 + number


async def hello_world():
    print("hello world!")
    result = await add_42(23)
    return result

event_loop = asyncio.get_event_loop()
try:
    result = event_loop.run_until_complete(hello_world())
    print(result)
finally:
    event_loop.close()
```
위의 소스 코드에서 볼 수 있듯, `await`이라는 키워드를 사용해 다른 코루틴과 협력한다. `await`는 코루틴 add_42(23)을 이벤트 루프에 등록하고 제어권을 이벤트 루프에 되돌려 준다.  

---

- asyncio.sleep: time.sleep의 비동기식 구현. 코루틴을 지연할 때 사용한다. 함수가 아닌 코루틴이기 때문에 이벤트 루프에 제어권을 돌려준다.
- asyncio.gather: await 키워드를 한번만 써서 코루틴 여러 개를 기다릴 때 사용. 이 함수 사용시 모든 코루틴의 작업 결과가 필요하다는 점을 스케줄러에 명시적으로 알려서 이벤트 루프가 모든 코루틴을 동시에 실행하게끔 할 수 있다.

```python
import asyncio


async def hello_world():
    print("hello world!")


async def hello_python():
    print("hello Python!")
    await asyncio.sleep(0.1)


event_loop = asyncio.get_event_loop()
try:
    result = event_loop.run_until_complete(asyncio.gather(
        hello_world(),
        hello_python(),
    ))
    print(result)
finally:
    event_loop.close()
```
위의 코드에서 hello_world와 hello_python 코루틴은 모두 동시에 실행된다. 이벤트 루프 스케불러가 hello_world를 먼저 시작하면 hello_python도 완료 돼야만 진행을 계속할 수 있다. hello_python은 asyncio.sleep(0.1) 코루틴이 완료될 때까지 기다려야한다는 것을 스케줄러에 알린다. 그러면 스케줄러는 hello_python 실행을 이어 가기 전에 이 코루틴을 실행해서 0.1초간 지연한다. 해당 방식은 비동기 방식이기 때문에 지정된 시간까지 대기하는 동안 다른 일을 할 수 있다.

반면, hello_python 코루틴이 먼저 실행되면 asyncio.sleep(0.1) 코루틴이 이벤트 루프에 제어를 돌려준 이후에야 hello_world가 시작된다.

```python
import aiohttp
import asyncio


async def get(url):
    async with aiohttp.ClientSession() as session:
        async with session.get(url) as response:
            return response


loop = asyncio.get_event_loop()

coroutines = [get("http://example.com") for _ in range(8)]

results = loop.run_until_complete(asyncio.gather(*coroutines))

print("Results: %s" % results)
```

get 함수에 대한 여러개의 코루틴을 만들고 동시실행 시킨다. 웹 서버 응답이 오래 걸리면 이벤트 루프는 실행할 준비를 끝낸, 다음 코루틴을 전환해서 데이터를 읽는다.

아래 코드는 절대적인 시간이후 함수를 실행시키는 방법이다.(`call_later`)
```python
import asyncio


def hello_world():
    print("Hello world!")


loop = asyncio.get_event_loop()
loop.call_later(1, hello_world)
loop.run_forever()

##########################################
##########################################

import asyncio


loop = asyncio.get_event_loop()


def hello_world():
    loop.call_later(1, hello_world)
    print("Hello world!")


loop = asyncio.get_event_loop()
loop.call_later(1, hello_world)
loop.run_forever()
```

# 네트워크 서버

asyncio는 수천 개의 네트워크 연결을 다루는데 탁월해 네트워크 서버를 구현할 때 유용한단 프레임워크를 제공한다.
```python
import asyncio

SERVER_ADDRESS = ('127.0.0.1', 1234)


class YellEchoServer(asyncio.Protocol):
    def connection_made(self, transport):
        self.transport = transport
        print("Connection received from:",
            transport.get_extra_info('peername'))

    def data_received(self, data):
        self.transport.write(data.upper())

    def connection_lost(self, exc):
        print("Client disconnected")


event_loop = asyncio.get_event_loop()

factory = event_loop.create_server(YellEchoServer, *SERVER_ADDRESS)
server = event_loop.run_until_complete(factory)

try:
    event_loop.run_forever()
finally:
    server.close()
    event_loop.run_until_complete(server.wait_closed())
    event_loop.close()
```

우선 asyncio.Protocol을 상속받아야 한다. 클라이언트와 연결이 맺어지면 connection_made 함수가 호출되어 클라이언트의 정보를 얻을 수 있게된다.(`transport.get_extra_info`) 
connection_lost는 연결이 끊길때 호출되고, data_received는 데이터를 받았을 때, 호출된다.
```bash
root@6d513bf71fe1:/# nc localhost 1234
sihun
SIHUN
hello, python!
HELLO, PYTHON!
```

다음은 클라이언트를 구성한 소스코드이다.
```python
import asyncio

SERVER_ADDRESS = ('127.0.0.1', 1234)


class EchoClientProtocol(asyncio.Protocol):
    def __init__(self, message, loop):
        self.message = message
        self.loop = loop

    def talk(self):
        self.transport.write(self.message)

    def connection_made(self, transport):
        self.transport = transport
        self.talk()

    def data_received(self, data):
        self.talk()

    def connection_lost(self, exc):
        self.loop.stop()


loop = asyncio.get_event_loop()
loop.run_until_complete(loop.create_connection(
    lambda: EchoClientProtocol(b'Hello World!', loop),
    *SERVER_ADDRESS))
try:
    loop.run_forever()
finally:
    loop.close()
```
