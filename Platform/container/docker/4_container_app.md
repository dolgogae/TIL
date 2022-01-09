# Container Application

컨테이너에 애플리케이션을 하나만 동작시키면 컨테이너 간의 독립성을 보장함과 동시에 애플리케이션의 버전 관리, 소스코드 모듈화 등이 더욱 쉬워진다.  
예를 들어, 컨테이너 하나에 데이터베이스와 웹서버를 설치할 수 있지만, 데이터베이스와 웹서버를 분리하는 편이 도커 이미지 관리와 컴포넌트의 독립성을 유지하기가 쉽다. 

```bash
$ docker run -d --name wordpressdb -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=wordpress mysql:5.7

$ docker run -d -e WORDPRESS_DB_PASSWORD=password --name wordpress --link wordpressdb:mysql -p 80 wordpress
```
다음과 같이 데이터 베이스와 웹서버 컨테이너를 생성해줍니다.

```bash
$ docker ps -a
CONTAINER ID   IMAGE          COMMAND                  CREATED          STATUS                        PORTS                                     NAMES
f0d6bcfd32b9   wordpress      "docker-entrypoint.s…"   3 minutes ago    Up 3 minutes                  0.0.0.0:49153->80/tcp, :::49153->80/tcp   wordpress
2b6acce48603   mysql:5.7      "docker-entrypoint.s…"   5 minutes ago    Up 5 minutes                  3306/tcp, 33060/tcp                       wordpressdb
```

다음 정보를 보면 49153번 포트로 바인딩이 되어있다.  
따라서 [호스트 IP]:49153으로 접근하면 워드프레스 컨테이너가 성공적으로 생성된지 확인 할 수 있을 것이다.
> ## **-d** 옵션  
> 앞선 -it 옵션을 주게되면 표준 입출력이 활성화된, 상호작용이 가능한 셸 환경을 사용할 수 있습니다.   
> 그러나 -d 옵션으로 run을 실행하면 입출력이 없는 상태로 컨테이너를 실행합니다. 컨테이너 내부에서 프로그램이 터미널을 차지하는 foreground로 실행돼 사용자의 입력을 받지 않습니다. Detached 모드인 컨테이너는 반드시 컨테이너에서 프로그램이 실행돼야 하며, foreground 프로그램이 실행되지 않으면 컨테이너는 종료됩니다.  
> 위의 예시에서는 mysql은 하나의 터미널을 차지하는 mysqld를, 워드프레스는 하나의 터미널을 차지하는 apache2-foreground를 실행하므로 -d 옵션을 지정해 백그라운드로 설정한 것입니다.
> ```bash
> $ docker run -d --name detach_test ubuntu:18.04
> ```  
> 위의 명령어를 실행하게 되면 STATUS는 UP상태가 아닐것이다.  
> ```bash
> docker run -d -e WORDPRESS_DB_PASSWORD=password --name wordpress --link wordpressdb:mysql -p 80 wordpress
> ```
> 반대로 다음을 실행하면, mysqld 프로그램이 foreground로 실행된 로그를 볼 수 있습니다.  

> ## **-e** 옵션  
> 컨테이너 내부의 환경변수를 설정합니다. 컨테이너화된 어플리케이션은 환경변수에서 값을 가져와 쓰는 경우가 많으므로 자주 사용하는 옵션이다.  
> ```bash
> -e MYSQL_ROOT_PASSWORD=password
> ```
> 해당 옵션은 컨테이너에서 $MYSQL_ROOT_PASSWORD라는 환경변수를 password로 설정을 한 것이다.

> ## **--link** 옵션  
> A 컨테이너에서 B 컨테이너로 접근하는 방법 중 가장 간단한 것은 NAT로 할당받은 내부 IP를 쓰는 것입니다. 도커 엔진은 컨테이너에게 내부 IP를 172.17.0.2,3,4...와 같이 순차적으로 할당합니다. 이는 컨테이너를 시작할 때마다 재할당하는 것이므로 매번 변경되는 컨테이너의 IP로 접근하기는 어렵습니다.
> ```bash
> --link wordpressdb:mysql 
> ```  
> 다음에서는 wordpressdb의 IP주소는 몰라도 mysql이라는 호스트명으로 접근할 수 있게 됩니다.(단순히 리눅스에서 `/etc/hosts` 파일에 도메인을 등록하는 것과 비슷한 개념이라고 생각하면 좋을 것 같다.)