# Docker Network

도커는 컨테이너에 내부 IP를 순차적으로 할당하며, 이 IP는 컨테이너를 재시작할 때마다 변경될 수 있습니다. 컨테이너를 시작할 때마다 호스트에 veth...(자동생성)라는 네트워크 인터페이스를 생성함으로써 이뤄진다.  
도커를 생성한 호스트에 ifconfig 명령어를 입력하면 떠 있는 컨테이너 갯수만큼 인터페이스가 있는 것을 확인 할 수 있다.

```bash
$ docker ps -a
CONTAINER ID   IMAGE          COMMAND                  CREATED          STATUS                      PORTS                                     NAMES
a780c10b42f9   ubuntu:18.04   "bash"                   42 minutes ago   Exited (0) 39 minutes ago                                             myvolume_2
9c24b390dd5b   ubuntu:18.04   "bash"                   43 minutes ago   Up 38 minutes                                                         myvolume_1
f0d6bcfd32b9   wordpress      "docker-entrypoint.s…"   24 hours ago     Up 24 hours                 0.0.0.0:49153->80/tcp, :::49153->80/tcp   wordpress
2b6acce48603   mysql:5.7      "docker-entrypoint.s…"   24 hours ago     Up 24 hours                 3306/tcp, 33060/tcp                       wordpressdb


$ ifconfig
docker0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.17.0.1  netmask 255.255.0.0  broadcast 172.17.255.255
        inet6 fe80::42:f8ff:fed0:ad4e  prefixlen 64  scopeid 0x20<link>
        ether 02:42:f8:d0:ad:4e  txqueuelen 0  (Ethernet)
        RX packets 11911  bytes 526353 (526.3 KB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 14115  bytes 166595513 (166.5 MB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

...

veth400d452: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet6 fe80::3451:54ff:fe0f:2cfb  prefixlen 64  scopeid 0x20<link>
        ether 36:51:54:0f:2c:fb  txqueuelen 0  (Ethernet)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 35  bytes 3860 (3.8 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

veth953a931: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet6 fe80::4a2:f1ff:fe23:fc08  prefixlen 64  scopeid 0x20<link>
        ether 06:a2:f1:23:fc:08  txqueuelen 0  (Ethernet)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 129  bytes 14982 (14.9 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

vethdef0223: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet6 fe80::2f:23ff:fe3c:ad35  prefixlen 64  scopeid 0x20<link>
        ether 02:2f:23:3c:ad:35  txqueuelen 0  (Ethernet)
        RX packets 0  bytes 0 (0.0 B)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 132  bytes 15349 (15.3 KB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0
...

```
![도커 네트워크 구조](../images/image02.png)  
다음과 같이 STATUS UP인 컨테이너 갯수만큼 veth 인터페이스가 올라와있다.  

컨테이너를 생성하면 기본적으로 docker0 브릿지를 통해 외부와 통신할 수 있는 환경을 사용할 수 있지만 사용자의 선택에 따라 여러 네트워크 드라이버를 쓸 수도 있다. 도커가 자체적으로 제공하는 대표적인 네트워크 드라이버로는 bridge, host, none, container, overlay가 있다. 서드파티 플러그인 솔루션으로는 weave, flannel, openvswitch 등이 있고, 더 확장된 네트워크 구성을 위해 활용된다.  

```bash
$ docker network ls
NETWORK ID     NAME      DRIVER    SCOPE
59d08ee1cf2a   bridge    bridge    local
c7732347b35e   host      host      local
bdc4f4f54bf7   none      null      local
```
다음과 같이 bridge, host, null 세개의 네트워크가 기본적으로 있다. 
- bridge: 컨테이너 생성시 자동으로 연결되는 docker0 브리지를 활용하도록 설정. 172.17.0.x IP대역을 컨테이너에 순차적으로 할당된다.

## Bridge Network

bridge 네트워크 docker0이 아닌 사용자 정의 브리지를 통해 새로 생성해 각 컨테이너에 연결하는 네트워크 구조이다. 컨테이너는 연결된 브리지를 통해 외부와 통신할 수 있습니다.

```bash
$ docker network create --driver bridge mybridge

$ docker run -it --name mynetwork_bridge --net mybridge ubuntu:18.04
root@ffbcfb6c835d:/# ifconfig
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.18.0.2  netmask 255.255.0.0  broadcast 172.18.255.255
        ether 02:42:ac:12:00:02  txqueuelen 0  (Ethernet)
        RX packets 4367  bytes 23505649 (23.5 MB)
```
다음과 같이 172.18 대역의 IP가 생성된 것을 확인 할 수 있다. 이렇게 생성된 사용자 정의 네트워크는 docker network disconnect, connect를 통해 컨테이너에 유동적으로 붙이고 뗄 수 있습니다.

```bash
$ docker network disconnect mybridge mynetwork_bridge
$ docker network connet mybridge mynetwork_bridge
```
하지만 none 네트워크, host 네트워크 등과 같은 특별한 네트워크 모드에서는 사용할 수 없습니다.  

```bash
$ docker network create --driver=bridge --subnet=172.72.0.0/16 --ip-range=172.72.0.0/24 --gateway=172.72.0.1 my_custom_network
```
다음과 같이 세부 설정을 옵션으로 주어 만드는 것도 가능하다.  

## Bridge Network

bridge 네트워크 docker0이 아닌 사용자 정의 브리지를 통해 새로 생성해 각 컨테이너에 연결하는 네트워크 구조이다. 컨테이너는 연결된 브리지를 통해 외부와 통신할 수 있습니다.

```bash
$ docker network create --driver bridge mybridge

$ docker run -it --name mynetwork_bridge --net mybridge ubuntu:18.04
root@ffbcfb6c835d:/# ifconfig
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500
        inet 172.18.0.2  netmask 255.255.0.0  broadcast 172.18.255.255
        ether 02:42:ac:12:00:02  txqueuelen 0  (Ethernet)
        RX packets 4367  bytes 23505649 (23.5 MB)
```
다음과 같이 172.18 대역의 IP가 생성된 것을 확인 할 수 있다. 이렇게 생성된 사용자 정의 네트워크는 docker network disconnect, connect를 통해 컨테이너에 유동적으로 붙이고 뗄 수 있습니다.

```bash
$ docker network disconnect mybridge mynetwork_bridge
$ docker network connet mybridge mynetwork_bridge
```
하지만 none 네트워크, host 네트워크 등과 같은 특별한 네트워크 모드에서는 사용할 수 없습니다.  

```bash
$ docker network create --driver=bridge --subnet=172.72.0.0/16 --ip-range=172.72.0.0/24 --gateway=172.72.0.1 my_custom_network
```
다음과 같이 세부 설정을 옵션으로 주어 만드는 것도 가능하다.  

## Host Network

호스트 네트워크는 컨테이너를 띄운 호스트의 네트워크를 그대로 가져가는 것이다.
```bash
root@sihun:~# docker run -it --name host-network --net host ubuntu:14.04
root@sihun:/# 
root@sihun:/# 
root@sihun:/# 
root@sihun:/# ifconfig
br-3719e4fff449 Link encap:Ethernet  HWaddr 02:42:4f:4e:6d:7f  
          inet addr:172.18.0.1  Bcast:172.18.255.255  Mask:255.255.0.0
          inet6 addr: fe80::42:4fff:fe4e:6d7f/64 Scope:Link
          UP BROADCAST MULTICAST  MTU:1500  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:56 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0 
          RX bytes:0 (0.0 B)  TX bytes:9485 (9.4 KB)

docker0   Link encap:Ethernet  HWaddr 02:42:ad:6c:3f:4d  
          inet addr:172.17.0.1  Bcast:172.17.255.255  Mask:255.255.0.0
          UP BROADCAST MULTICAST  MTU:1500  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0 
          RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)

enp42s0   Link encap:Ethernet  HWaddr 2c:f0:5d:de:04:9a  
          UP BROADCAST MULTICAST  MTU:1500  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000 
          RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)

lo        Link encap:Local Loopback  
          inet addr:127.0.0.1  Mask:255.0.0.0
          inet6 addr: ::1/128 Scope:Host
          UP LOOPBACK RUNNING  MTU:65536  Metric:1
          RX packets:2515 errors:0 dropped:0 overruns:0 frame:0
          TX packets:2515 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000 
          RX bytes:265446 (265.4 KB)  TX bytes:265446 (265.4 KB)

wlo1      Link encap:Ethernet  HWaddr e0:d4:64:dc:a9:01  
          inet addr:192.168.0.4  Bcast:192.168.0.255  Mask:255.255.255.0
          inet6 addr: fe80::cc2e:138:8674:645/64 Scope:Link
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:824959 errors:0 dropped:0 overruns:0 frame:0
          TX packets:181989 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000 
          RX bytes:1200711135 (1.2 GB)  TX bytes:25368552 (25.3 MB)

```

현재 내 컴퓨터는 와이파이 네트워크를 쓰고 있는데 wlo1을 보면 그대로 쓰는걸 알 수 있다.
(별도의 도커 네트워크를 만들 필요도 없다.)

## 논네트워크

말 그대로 네트워크를 붙이지 않은 컨테이너이다. 옵션은 `--net none`으로 준다.
