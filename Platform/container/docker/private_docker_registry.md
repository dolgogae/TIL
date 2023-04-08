# Private Docker Registry

## Docker registry
private한 docker registry를 만들고 싶다면, 컨테이너를 이용해 그 컨테이너를 이미지를 저장하는 전용 컨테이너로 사용하는 것이다. 그 것을 Docker Hub에서는 `registry`라는 컨테이너 이미지로 제공하고 있다.
```bash
# registry image 가져오기
$ docker pull registry

# registry 실행
$ docker run -dit --name docker-registry -p 5000:5000 registry
$ docker ps -a
CONTAINER ID   IMAGE          COMMAND                  CREATED             STATUS                         PORTS                                       NAMES
b54de0b87947   registry       "/entrypoint.sh /etc…"   14 seconds ago      Up 13 seconds                  0.0.0.0:5000->5000/tcp, :::5000->5000/tcp   docker-registry
```

registry의 기본 포트는 5000번으로 설정돼 있고 -p(포트 옵션)에서 5000:xxxx으로 지정해 줘야 한다.  

## Docker Image push
Docker Hub에서 예시 이미지인 hello-world 이미지를 이용해 현재 띄워져 있는 registry에 해당 이미지를 tag 시켜준다.
```bash
$ docker pull hello-world

# localhost에 hello-world를 만들어준다.
$ docker tag hello-world localhost:5000/hello-world
```

위와 같이 이미지를 만든 후, registry 내에 push를 해준다.
> push와 pull의 차이  
> - pull는 docker의 이미지 리스트에 이미지를 끌어와 docker images에 저장을 하는 것이다.  
> - push는 이미지를 registry라는 컨테이너에 이미지 파일 자체를 저장하는 것을 의미한다.  

```bash
$ docker push localhost:5000/hello-world
Using default tag: latest
The push refers to repository [localhost:5000/hello-world]
f22b99068db9: Pushed 
latest: digest: sha256:1b26826f602946860c279fce658f31050cff2c596583af237d971f4629b57792 size: 525


$ curl http://localhost:5000/v2/_catalog
{"repositories":["hello-world"]}


$ curl -X GET http://localhost:5000/v2/hello-world/tags/list
{"name":"hello-world","tags":["latest"]}
```
위와 같이 이미지가 registry 컨테이너에 잘 저장된 것을 curl을 통해 확인 할 수 있다.  


  
  
## 원격으로 Docker image를 push 하기
sa