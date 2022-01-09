# Docker Volume

도커 이미지로 컨테테이너를 생성하면 이미지는 읽기 전용이 되며 컨테이너의 변경 사항만 별도로 저장해서 각 컨테이너의 정보를 보존합니다. 따라서 도커 이미지(읽기 전용), 도커 컨테이너(쓰기 전용)로 나뉘어 있습니다. 생성된 이미지는 어떠한 경우로도 변경되지 않으며, 컨테이너 계층(쓰기 전용)에 원래 이미지에서 변경된 파일시스템 등을 저장합니다.  
여기서 큰 단점이 하나 있습니다. 컨테이너를 삭제하게 되면 변경된 정보들이 모두 사라진다는 점입니다. 이를 방지하기 위해 데이터를 영속적으로 활용할 수 있는 가장 쉬운 방법인 `볼륨`을 활용하는 것입니다.

```bash
$ docker run -d --name wordpressdb_hostvolume -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=wordpress -v /home/wordpress_db:/var/lib/mysql mysql:5.7

$ docker run -d -e WORDPRESS_DB_PASSWORD=password --name wordpress_hostvolume --link wordpressdb_hostvolume:mysql -p 80 wordpress
```

이전과 다른 점은 -v 옵션이 추가 됐고, 이는 [호스트의 공유 디렉터리]:[컨테이너의 공유 디렉터리] 형태입니다. 미리 `/home/wordpress_db` 파일을 생성하지 않았어도 자동으로 생성하게 되고, 해당 디렉터리에 데이터베이스 관련 파일이 있는지 확인합니다.
```bash
 ll /home/wordpress_db/
total 188488
drwxr-xr-x 6 vboxadd root       4096  7월 23 21:29 ./
drwxr-xr-x 4 root    root       4096  7월 23 21:29 ../
-rw-r----- 1 vboxadd vboxsf       56  7월 23 21:29 auto.cnf
-rw------- 1 vboxadd vboxsf     1676  7월 23 21:29 ca-key.pem
-rw-r--r-- 1 vboxadd vboxsf     1112  7월 23 21:29 ca.pem
-rw-r--r-- 1 vboxadd vboxsf     1112  7월 23 21:29 client-cert.pem
-rw------- 1 vboxadd vboxsf     1676  7월 23 21:29 client-key.pem
-rw-r----- 1 vboxadd vboxsf     1360  7월 23 21:29 ib_buffer_pool
-rw-r----- 1 vboxadd vboxsf 79691776  7월 23 21:29 ibdata1
-rw-r----- 1 vboxadd vboxsf 50331648  7월 23 21:29 ib_logfile0
-rw-r----- 1 vboxadd vboxsf 50331648  7월 23 21:29 ib_logfile1
-rw-r----- 1 vboxadd vboxsf 12582912  7월 23 21:29 ibtmp1
drwxr-x--- 2 vboxadd vboxsf     4096  7월 23 21:29 mysql/
drwxr-x--- 2 vboxadd vboxsf     4096  7월 23 21:29 performance_schema/
-rw------- 1 vboxadd vboxsf     1680  7월 23 21:29 private_key.pem
-rw-r--r-- 1 vboxadd vboxsf      452  7월 23 21:29 public_key.pem
-rw-r--r-- 1 vboxadd vboxsf     1112  7월 23 21:29 server-cert.pem
-rw------- 1 vboxadd vboxsf     1680  7월 23 21:29 server-key.pem
drwxr-x--- 2 vboxadd vboxsf    12288  7월 23 21:29 sys/
drwxr-x--- 2 vboxadd vboxsf     4096  7월 23 21:29 wordpress/
```
다음과 같이 mysql관련 데이터들이 생성된 것을 확인 할 수 있습니다.

```bash
$ docker stop wordpressdb_hostvolume

$ docker rm wordpressdb_hostvolume

$ ll /home/wordpress_db/
total 176200
drwxr-xr-x 6 vboxadd root       4096  7월 23 21:35 ./
drwxr-xr-x 4 root    root       4096  7월 23 21:29 ../
-rw-r----- 1 vboxadd vboxsf       56  7월 23 21:29 auto.cnf
-rw------- 1 vboxadd vboxsf     1676  7월 23 21:29 ca-key.pem
-rw-r--r-- 1 vboxadd vboxsf     1112  7월 23 21:29 ca.pem
-rw-r--r-- 1 vboxadd vboxsf     1112  7월 23 21:29 client-cert.pem
-rw------- 1 vboxadd vboxsf     1676  7월 23 21:29 client-key.pem
-rw-r----- 1 vboxadd vboxsf      763  7월 23 21:35 ib_buffer_pool
-rw-r----- 1 vboxadd vboxsf 79691776  7월 23 21:35 ibdata1
-rw-r----- 1 vboxadd vboxsf 50331648  7월 23 21:35 ib_logfile0
-rw-r----- 1 vboxadd vboxsf 50331648  7월 23 21:29 ib_logfile1
drwxr-x--- 2 vboxadd vboxsf     4096  7월 23 21:29 mysql/
drwxr-x--- 2 vboxadd vboxsf     4096  7월 23 21:29 performance_schema/
-rw------- 1 vboxadd vboxsf     1680  7월 23 21:29 private_key.pem
-rw-r--r-- 1 vboxadd vboxsf      452  7월 23 21:29 public_key.pem
-rw-r--r-- 1 vboxadd vboxsf     1112  7월 23 21:29 server-cert.pem
-rw------- 1 vboxadd vboxsf     1680  7월 23 21:29 server-key.pem
drwxr-x--- 2 vboxadd vboxsf    12288  7월 23 21:29 sys/
drwxr-x--- 2 vboxadd vboxsf     4096  7월 23 21:29 wordpress/
```

다음과 같이 컨테이너를 삭제해도 호스트에는 여전히 남아있는 것을 확인 할 수 있습니다.  
만약, `/home/wordpress_db`가 존재하는 경우에는 새로운 컨테이너가 저 볼륨을 이용해 띄웠을 때, 덮어쓰게 된다.  

## Volume Container

`--volumes-from` 옵션을 주게되면 올라와있는 컨테이너의 볼륨을 공유 받는다는 옵션이다.
```bash
$ docker run -it --name volume_override -v /home/wordpress_db:/home/testdir_2 alicek106/volume_test
Unable to find image 'alicek106/volume_test:latest' locally
latest: Pulling from alicek106/volume_test
56eb14001ceb: Pull complete
7ff49c327d83: Pull complete
6e532f87f96d: Pull complete
3ce63537e70c: Pull complete
587f7dba3172: Pull complete
Digest: sha256:e0287b5cfd550b270e4243344093994b7b1df07112b4661c1bf324d9ac9c04aa
Status: Downloaded newer image for alicek106/volume_test:latest

$ ls /home/testdir_2/
auto.cnf    ca.pem           client-key.pem  ib_logfile0  ibdata1  performance_schema  public_key.pem   server-key.pem  wordpress
ca-key.pem  client-cert.pem  ib_buffer_pool  ib_logfile1  mysql    private_key.pem     server-cert.pem  sys


$ docker run -it --name volumes_from_container --volumes-from volume_override ubuntu:18.04

$ ls /home/testdir_2/
auto.cnf    ca.pem           client-key.pem  ib_logfile0  ibdata1  performance_schema  public_key.pem   server-key.pem  wordpress
ca-key.pem  client-cert.pem  ib_buffer_pool  ib_logfile1  mysql    private_key.pem     server-cert.pem  sys
```

위에서 같이 volume_override의 컨테이너의 `/home/testdir_2`를 volumes_from_contianer가 그대로 받게 된다.

## Docker Volume

도커 자체에서 제공하는 볼륨 기능을 활용해 데이터를 보존할 수 있는 방법이다.(`docker volume create`)
```bash
$ docker volume create myvolume
```
이 볼륨은 로컬 호스트에 저장되며 도커 엔진에 의해 생성되고 삭제됩니다.  
[볼륨의 이름]:[컨테이너의 공유 디렉터리]의 형식으로 입력을 하며
```bash
$ docker run -it --name myvolume_1 -v myvolume:/root/ ubuntu:18.04
root@9c24b390dd5b:/# echo "hello, volume" >> /root/volume
root@9c24b390dd5b:/# exit
exit


$ docker run -it --name myvolume_2 -v myvolume:/root/ ubuntu:18.04

root@a780c10b42f9:/# cat /root/volume
hello, volume
```
다음과 같이 volume을 공유하며 생성하면, 같은 정보들이 마운트 되는 것도 확인 할 수 있다. 그리고 여러개의 컨테이너에 공유될 수 있습니다.
```bash
$ docker exec -it myvolume_2 bash

root@a780c10b42f9:/# echo "hello, volume 22" >> /root/volume22
root@a780c10b42f9:/# exit
exit

$ docker exec -it myvolume_1 bash

root@9c24b390dd5b:/# ll /root/
total 28
drwx------ 2 root root 4096 Jul 23 13:28 ./
drwxr-xr-x 1 root root 4096 Jul 23 13:24 ../
-rw------- 1 root root  129 Jul 23 13:28 .bash_history
-rw-r--r-- 1 root root 3106 Apr  9  2018 .bashrc
-rw-r--r-- 1 root root  148 Aug 17  2015 .profile
-rw-r--r-- 1 root root   14 Jul 23 13:25 volume
-rw-r--r-- 1 root root   17 Jul 23 13:28 volume22

root@9c24b390dd5b:/# cat /root/volume22
hello, volume 22
```
다음과 같이 어느 파일에서 써도 같은 볼륨을 바라보고 있는 컨테이너는 볼륨이 공유된다.  
`docker inspect` 명령어의 경우는 해당 볼륨의 정보를 출력해 줄 수 있다.(--type 옵션에 image를 넣게 되면 이미지의 정보가 뜬다.)

```bash
$ docker inspect --type volume myvolume
[
    {
        "CreatedAt": "2021-07-23T22:28:49+09:00",
        "Driver": "local",
        "Labels": {},
        "Mountpoint": "/var/lib/docker/volumes/myvolume/_data",
        "Name": "myvolume",
        "Options": {},
        "Scope": "local"
    }
]
```

기본적으로 컨테이너 생성시 -v 옵션을 지정해주게 되면 볼륨이 16진수 형태의 이름으로 생성이 되고 `docker inspect` 명령어로 현재 내 컨테이너가 어느 볼륨을 바라보고 있는지 확인 할 때 유용하게 쓰인다.

```bash
# 모든 볼륨을 한번에 삭제하는 명령어
$ docker volume prune
```

이처럼 컨테이너가 아닌 외부에 데이터를 저장하고 컨테이너는 그 데이터로 동작하도록 설계하는 것을 **Stateless**하다고 말한다. 컨테이너 자체는 상태가 없고 상태를 결정하는 데이터는 외부로부터 제공받습니다. 컨테이너가 삭제돼도 데이터는 보존되므로 Stateless한 컨테이너 설계는 도커를 사용할 때 매우 바람직한 설계입니다.(반대의 상태는 **Statefull**하다고 한다. -> 지양하는 것이 좋다.)