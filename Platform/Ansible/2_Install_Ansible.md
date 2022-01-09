# Install Ansible

ansible은 centos에 설치하는 것을 권장한다. 또한 ansible을 설치한다는 것은 ansible core node로 사용할 서버에 설치를 하는 것이고, 따로 배포할 노드는 ansible을 설치할 필요가 없다.  

가장 우선적으로 설치하게 되는 명령어는
```bash
# centos
yum install -y epel-release
yum install -y ansible
```

이후, 배포를 할 노드를 등록하는 과정을 거쳐야 한다.
해당 등록 노드는 ping, ssh 통신이 뚫렸다는 가정을 하겠다.

```bash
# /etc/ansible/hosts로 이동한다.
# 해당 에디터는 사용하기 편할걸로 사용한다.
$ cat >> /etc/ansible/hosts << EOF
[group1] # 내가 해당 노드들을 사용할 목적으로 묶는다
192.168.0.10
192.168.0.11
192.168.0.12
EOF
```

다음 과정을 거치면 ansible의 기본세팅이 끝났지만 아직 다 끝나지 않았다. 바로 core node에서 배포 node로 바로 접근할 수 있는 known_hosts를 등록 해줘야 한다.


간단한 방법으로 ssh-keygen을 통해서 key-pair를 등록해주는 것이다.

```bash
# core node에서 진행
$ ssh-keygen # 계속 엔터를 쳐준다.
```
해당 명령어를 치면 `~/.ssh/id_rsa.pub`파일이 생겼을 것이고 이 키를 각 노드에 인증을 붙혀주면 된다.

이후, 
```bash
$ ansible all -m ping -k
```
를 통해서 잘 등록이 된지 확인 할 수 있다.