# Python with

Python의 with문은 자원을 획득하고 사용 후 반납해야 하는 경우 주로 사용합니다.
1. 자원획득
2. 자원사용
3. 자원반납

예를들어, 파일을 열고 사용했다면 다른 프로세스를 위해 닫고 반납해야 합니다. 데이터베이스 세션을 얻어 사용했다면 다른 프로세스를 위해 반납해야 합니다.

```python
class Hello:
    def __enter__(self):
        print("enter...")
        return self
    
    def sayHello(self, name):
        print("hello " + name)

    def __exit__(self, exc_type, exc_val, exc_tb):
        print("exit...")


with Hello() as h:
    h.sayHello("world")
    h.sayHello("Python")
```

```bash
$ python hello_with.py
enter...
hello world
hello Python
exit...
```

클래스의 객체를 만들고 사용후 소멸 시킵니다.(C++ 객체를 동적으로 할당시 필수적으로 해제해야하는 과정을 파이썬의 with로 대신한다고 생각하면 편하다.)