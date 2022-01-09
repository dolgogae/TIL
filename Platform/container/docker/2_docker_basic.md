## Docker Image 

이미지는 컨테이너를 생성할 때 필요한 요소이며, 여러 개의 계층으로 된 바이너리 파일로 존재하고, 컨테이너를 생성하고 실행할 때 읽기 전용으로 사용됩니다.  

Docker에서 사용하는 이미지의 이름은 기본적으로 `[저장소이름]/[이미지 이름]:[태그]`의 형태로 구성됩니다.
- 저장소(Repository): 이미지가 저장된 장소를 의미. 기본적으로 [도커허브](https://docker.io)의 공식 이미지를 뜻한다.
- 이미지 이름: 해당 이미지가 어떤 역할을 하는지 나타낸다.
- 태그: 이미지의 버전 관리 혹은 리비전 괸리에 사용.(태그를 생략하면 도커 엔진은 이미지의 태그를 latest로 인식한다.)  

## Docker Container
도커 이미지는 기본적인 리눅스 운영체제부터 아파치 웹 서버, DB, Hadoop...등 여러 종류가 있다. 이미지 목적에 맞는 파일시스템과 시스템 자원 및 네트워크를 사용할 수 있는 독립된 공간이 생성된다. 생성된 컨테이너는 독립된 파일시스템을 제공받고 호스트와 분리돼있기 때문에 특정 어플리케이션 삭제와 생성에 다른 컨테이너와 호스트는 영향이 없다.  
  
##
##


# Docker Container 사용하기
기본적으로 docker는 root 권한이 필요하기 때문에 root에 접속 후 사용하는 것을 권장한다.
(계정에서 접근해야 할 때는 sudo를 꼭 붙혀주자.)
```bash
$ sudo -i
```

`docker run` 명령어는 컨테이너를 생성하고 실행하는 역할을 한다.  
```bash
$ docker run -i -t ubuntu:18.04
sudo docker run -i -t ubuntu:18.04
[sudo] password for sihun: 
Unable to find image 'ubuntu:18.04' locally
18.04: Pulling from library/ubuntu
e7ae86ffe2df: Pull complete 
Digest: sha256:3b8692dc4474d4f6043fae285676699361792ce1828e22b1b57367b5c05457e3
Status: Downloaded newer image for ubuntu:18.04
root@77951e6a15bf:/#
```
docker run의 -i : 상호입출력, -t : tty활성화(tty: 유닉스, 리눅스 계열의 운영체제에서 표준 입력에 연결된 단말의 파일 이름을 인쇄하는 명령)  

`docker pull` 명령어는 이미지를 내려받을 때 사용합니다.
```bash
root@sh:~# docker pull centos:7
7: Pulling from library/centos
2d473b07cdd5: Pull complete 
Digest: sha256:0f4ec88e21daf75124b8a9e5ca03c37a5e937e0e108a255d890492430789b60e
Status: Downloaded newer image for centos:7
docker.io/library/centos:7


root@sh:~# docker images
REPOSITORY   TAG       IMAGE ID       CREATED        SIZE
ubuntu       18.04     fbf60236a8e3   7 days ago     63.1MB
centos       7         8652b9f0cb4c   8 months ago   204MB

# ubuntu 이미지는 조금 전 docker run 명령어를 실행할 때 pull 됐다.
```

컨테이너 생성시 run 명령어 대신 create 명령어를 사용할 수 있다. `--name` 옵션을 통해 컨테이너의 이름을 설정할 수 있다.
``` bash
$ docker create -i -t --name mycentos centos:7
c96833dafac007ac4dbc797019b564c6314cf42c50cec4a94c5855b3afc035b3


# 해당 명령어를 통해 현재 실행중인 container를 확인 할 수 있다.
$ docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED          STATUS                     PORTS     NAMES
c96833dafac0   centos:7       "/bin/bash"   8 seconds ago    Created                              mycentos
77951e6a15bf   ubuntu:18.04   "bash"        11 minutes ago   Exited (0) 8 minutes ago             quizzical_galois
# 우분투 이미지에는 따로 이미지에 이름을 설정하지 않아 랜덤으로 이름이 생성된다.

# 해당 명령어를 통해 container에 접속 할 수 있습니다.(NAMES는  CONTAINER ID를 통해 대체 가능)
$ docker attach mycentos


# 이름 변경도 가능.
$ docker rename quizzical_galois myubuntu
$ docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED          STATUS                      PORTS     NAMES
c96833dafac0   centos:7       "/bin/bash"   4 minutes ago    Created                               mycentos
77951e6a15bf   ubuntu:18.04   "bash"        15 minutes ago   Exited (0) 12 minutes ago             myubuntu
```

`docker rm` 명령어를 사용해서 container를 삭제할 수 있다.(실행중인 container는 `docker stop`을 통해 멈추고 삭제.)
```bash
$ docekr stop mycentos

$ docker rm mycentos 
mycentos

$ docker ps -a
CONTAINER ID   IMAGE          COMMAND   CREATED          STATUS                      PORTS     NAMES
77951e6a15bf   ubuntu:18.04   "bash"    18 minutes ago   Exited (0) 15 minutes ago             myubuntu
```

`docker rmi` 명령어를 통해 pull 된 이미지를 제거할 수 있다.(해당 이미지를 이용한 컨테이너가 실행 중일 땐 마찬가지로 `docker stop`을 통해 멈추고 삭제.)  

전체 삭제를 하고 싶을 경우는 -af 옵션을 주면된다.
```bash
$ docker rm -af
$ docker rmi -af
```
`Ctrl + P, Q`를 누르면 컨테이너를 종료하지 않고 나갈수 있다.
