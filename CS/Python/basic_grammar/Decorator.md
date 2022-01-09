# Decorator 

```python 
@decorator 
def func(): 
print("blah blah") 
``` 

데코레이터는 간단히 말해서 대상 함수를 wrapping하고, 이 wrapping된 함수의 앞 뒤 추가적으로 꾸며질 구문들을 손쉽게 재사용 가능하게 해주는 것이다.ㅁ 

```python 
import datetime 

def func(): 
print datetime.datetime.now() 
``` 
``` 
print datetime.datetime.now() 
``` 

다음 소스코드처럼 함수 호출 전 후 시간을 보고 싶은 함수가 있고, 만약 

```python 
import datetime 

def func(): 
print datetime.datetime.now() 
``` 
``` 
print datetime.datetime.now() 

def func2(): 
print datetime.datetime.now() 
``` 
``` 
print datetime.datetime.now() 

def func3(): 
print datetime.datetime.now() 
``` 
``` 
print datetime.datetime.now() 
``` 

여러 개의 함수에 동일한 기능을 넣고 싶다면 위의 소스코드와 같이 단순한 작업이 반복 될 것이다. 

하지만, 데코레이터를 사용하게 된다면 

```python 
import datetime 

def datetime_deco(func): 
def decorator(): 
print datetime.datetime.now() 
func() 
print datetime.datetime.now() 
return decorated 

@datetime_deco 
def func1(): 
``` 
``` 

def func2(): 
``` 
``` 

. 
. 
. 
``` 

다음과 같이 함수를 재활용 함으로써 가독성과 소스코드도 간편하게 바꿀 수 있다. 

- 데코레이터의 역할을 정의하고, 데코레이터가 적용될 함수를 인자로 받는다. 
- 함수 내부에 또 한번 함수를 선언(nested function)하여 여기에 추가적인 작업을 선언해 주는 것이다. 
- Nested 함수를 return 해주면 된다. 
- 함수 중간에 기능을 넣을 수 없고, 앞 뒤의 반복적인 역할만 끼워넣으줌을 유의하자
