# IaaC using Ansible

Ansible이란 인프라를 구축할 때 yaml파일을 이용해서 설치를 자동화 해주는 툴 정도라고 생각하면 된다. 기본적으로 ssh를 이용해 자동화 코드를 실행시킨다. 쉘 스크립트처럼 간단한 자동화부터 전체 인프라에 대한 구축을 하기도 한다.(예 TripleO-openstack) 

앤서블의 기본 구조는 한개의 Ansible core node를 이용해 ssh접속이 가능하게 한 서버에 필요로 하는 작업을 실행한다. `ansible-playbook`은 ansible의 명령어를 말 그대로 책처럼 yaml, yml에 정리해놔서 읽는 것이다. 

ansible의 구조를 보면 다음과 같습니다.

또한, ansible은 멱등성을 보장하기 때문에 몇번을 실행하더라도 똑같은 환경으로 구성되는 장점이 있다.