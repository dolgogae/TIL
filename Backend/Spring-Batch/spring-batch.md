# Spring-batch

## Job
Job은 배치의 실행단위를 의미.  
Job은 N개의 Step을 순차적으로 실행할 수 있다.

## Step
- Chunk 기반: 하나의 큰 덩어리를 n개씩 나눠서 실행
- Task 기반: 하나의 작업 기반으로 실행

### Chunk 기반
ItemReader --> ItemProcessor or ItemWriter  
: DB나 file에서 데이터를 읽는다.

ItemProcessor --> ItemWriter
: input 객체를 output 객체로 filtering, processing

ItemWriter
: 배치 처리 대상 객체를 처리

