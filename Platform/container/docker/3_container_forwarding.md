# Container Forwarding

컨테이너는 가상 머신과 마찬가지로 가상 IP주소를 할당 받는다. 기본적으로 도커는 컨테이너에 172.17.0.x의 IP를 순차적으로 할당한다.

```bash
$ docker run -it --name network_test centos:8

$ ifconfig
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.17.0.2  netmask 255.255.0.0  broadcast 172.17.255.255
        ether 02:42:ac:11:00:02  txqueuelen 0  (Ethernet)
        RX packets 1356  bytes 13629568 (12.9 MiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 1207  bytes 70081 (68.4 KiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

lo: flags=73<UP,LOOPBACK,RUNNING>  mtu 65536
        inet 127.0.0.1  netmask 255.0.0.0
        loop  txqueuelen 1000  (Local Loopback)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 0  bytes 0 (0.0 B)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
```
현재 컨테이너는 외부에서는 접근 할 수 없으며 도커가 설치된 호스트에서만 접근할 수 있습니다. 외부에 컨테이너를 노출하기 위해선 eth0의 IP와 포트를 호스트의 IP와 바인딩해야 합니다.

```bash
$ docker run -it --name test_webserver -p 80:80 centos:8
```
-p옵션에서 [호스트의 포트]:[컨테이너의 포트]의 형식으로 인자값을 넘겨주게 된다. [호스트의 포트]는 호스트에서 컨테이너로 접근하는 포트를 의미하고 [컨테이너의 포트]는 호스트로부터 컨테이너가 받아들이는 포트를 의미하게 된다.  

호스트에서 인터페이스가 여러 개이고 그 중 하나의 IP주소를 이용해 바인딩 하고 싶으면 
```bash
$ docker run -it --name test_webserver -p 192.168.1.5:8008:80 centos:8
```
다음과 같이 명령어를 입력하면 되고, -p 옵션을 여러개 두어서 여러 포트를 이용해 접근하는 것도 가능하다.

