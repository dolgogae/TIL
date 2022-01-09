# Tensorflow-gpu installation using Docker

도커는 컨테이너를 사용하여 Tensorflow 설치를 나머지 시스템에서 격라하는 가상 환경을 만듭니다. TensorFlow 프로그램은 호스트 머신과 리소스를 공유(디렉터리 액세스, GPU 사용, 인터넷 연결 등)할 수 있는 이 가상 환경 내에서 실행됩니다. 

## Enviroment
|정보|내용|
|---|---|
|CPU| AMD Ryzen 5 5600X 6-Core Processor|  
|Memory| 32GB |  
|GPU|NVIDIA GeForce RTX 3070|


## 1. Docker 설치
[Docker Intro](https://github.com/dolgogae/TIL/blob/master/container/docker/1_docker_intro.md)

## 2. Docker 세팅 및 Nvidia container Toolkit 설치
```bash
$ curl https://get.docker.com | sh && sudo systemctl --now enable docker

$ distribution=$(. /etc/os-release;echo $ID$VERSION_ID) \
   && curl -s -L https://nvidia.github.io/nvidia-docker/gpgkey | sudo apt-key add - \
   && curl -s -L https://nvidia.github.io/nvidia-docker/$distribution/nvidia-docker.list | sudo tee /etc/apt/sources.list.d/nvidia-docker.list
```
## 3. nvidia-docker2 설치
```bash
$ sudo apt-get update
$ sudo apt-get install -y nvidia-docker2
$ sudo systemctl restart docker
$ sudo docker run --rm --gpus all nvidia/cuda:11.0-base nvidia-smi

Unable to find image 'nvidia/cuda:11.0-base' locally
11.0-base: Pulling from nvidia/cuda
54ee1f796a1e: Pull complete 
f7bfea53ad12: Pull complete 
46d371e02073: Pull complete 
b66c17bbf772: Pull complete 
3642f1a6dfb3: Pull complete 
e5ce55b8b4b9: Pull complete 
155bc0332b0a: Pull complete 
Digest: sha256:774ca3d612de15213102c2dbbba55df44dc5cf9870ca2be6c6e9c627fa63d67a
Status: Downloaded newer image for nvidia/cuda:11.0-base
Sat Jul 24 12:34:25 2021       
+-----------------------------------------------------------------------------+
| NVIDIA-SMI 470.57.02    Driver Version: 470.57.02    CUDA Version: 11.4     |
|-------------------------------+----------------------+----------------------+
| GPU  Name        Persistence-M| Bus-Id        Disp.A | Volatile Uncorr. ECC |
| Fan  Temp  Perf  Pwr:Usage/Cap|         Memory-Usage | GPU-Util  Compute M. |
|                               |                      |               MIG M. |
|===============================+======================+======================|
|   0  NVIDIA GeForce ...  Off  | 00000000:2B:00.0  On |                  N/A |
|  0%   41C    P8    22W / 220W |    621MiB /  7979MiB |      1%      Default |
|                               |                      |                  N/A |
+-------------------------------+----------------------+----------------------+
                                                                               
+-----------------------------------------------------------------------------+
| Processes:                                                                  |
|  GPU   GI   CI        PID   Type   Process name                  GPU Memory |
|        ID   ID                                                   Usage      |
|=============================================================================|
+-----------------------------------------------------------------------------+

```
## 4. Tensorflow Docker 이미지 다운로드
|태그|설명|
|---|---|
|lastest|Tensorflow CPU 바이너리 이미지의 최신 출시입니다.|
|nightly|Tensorflow 이미지 나이틀리 빌드입니다.(불안정)|
|version|Tensorflow 바이너리 이미지의 버전을 지정합니다.|
|devel|Tensorflow master 개발 환경의 나이틀리 빌드입니다. Tensorflow 소스 코드가 포함되어 있습니다.|
|custom-op|TF 맞춤 작업 개발을 위한 특수 실험용 이미지 입니다.|
|tag-gpu|GPU를 지원하는 지정된 태그 출시입니다.|
|tag-jupyter|Jupyter를 포함하는 지정된 태그 출시입니다.|  

```bash
$ docker pull tensorflow/tensorflow                     # latest stable release
$ docker pull tensorflow/tensorflow:devel-gpu           # nightly dev release w/ GPU support
$ docker pull tensorflow/tensorflow:latest-gpu-jupyter  # latest release w/ GPU support and Jupyter
```

## 5. GPU 지원 컨테이너
```bash
$ docker run --gpus all -it --rm tensorflow/tensorflow:latest-gpu \
   python -c "import tensorflow as tf; print(tf.reduce_sum(tf.random.normal([1000, 1000])))"

$ docker run --gpus all -it tensorflow/tensorflow:latest-gpu bash
________                               _______________                
___  __/__________________________________  ____/__  /________      __
__  /  _  _ \_  __ \_  ___/  __ \_  ___/_  /_   __  /_  __ \_ | /| / /
_  /   /  __/  / / /(__  )/ /_/ /  /   _  __/   _  / / /_/ /_ |/ |/ / 
/_/    \___//_/ /_//____/ \____//_/    /_/      /_/  \____/____/|__/


WARNING: You are running this container as root, which can cause new files in
mounted volumes to be created as the root user on your host machine.

To avoid this, run the container by specifying your user's userid:

$ docker run -u $(id -u):$(id -g) args...

root@238fbff7ac7a:/# 
root@238fbff7ac7a:/# nvcc -V
nvcc: NVIDIA (R) Cuda compiler driver
Copyright (c) 2005-2021 NVIDIA Corporation
Built on Sun_Feb_14_21:12:58_PST_2021
Cuda compilation tools, release 11.2, V11.2.152
Build cuda_11.2.r11.2/compiler.29618528_0
```

## 6. 컨테이너 - cuda, cudnn 연결확인
```bash
root@238fbff7ac7a:/# python
Python 3.6.9 (default, Jan 26 2021, 15:33:00) 
[GCC 8.4.0] on linux
Type "help", "copyright", "credits" or "license" for more information.
>>> from tensorflow.python.client import device_lib
>>> print(device_lib.list_local_devices())
[name: "/device:CPU:0"
device_type: "CPU"
memory_limit: 268435456
locality {
}
incarnation: 15464971271721971337
, name: "/device:GPU:0"
device_type: "GPU"
memory_limit: 5967773696
locality {
  bus_id: 1
  links {
  }
}
incarnation: 7355931000454480349
physical_device_desc: "device: 0, name: NVIDIA GeForce RTX 3070, pci bus id: 0000:2b:00.0, compute capability: 8.6"
]

# GPU가 정상적으로 잘 떠있는 것을 확인 할 수 있다.
```