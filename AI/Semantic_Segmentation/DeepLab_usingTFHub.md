# DeepLab using TF Hub

## installation
```bash
$ git clone https://github.com/tensorflow/models.git
$ git clone https://github.com/cocodataset/panopticapi.git

$ cd research
# $(GIT_CLONE_DIR)/models/research
# $(GIT_CLONE_DIR) is line2 git clone directory

$ export PYTHONPATH=$PYTHONPATH:`pwd`:`pwd`/slim

$ PANOPTICAPI_DIR="$(GIT_CLONE_DIR)/panopticapi/"
$ touch ${PANOPTICAPI_DIR}/panopticapi/__init__.py
$ export PYTHONPATH=$PYTHONPATH:${PANOPTICAPI_DIR}/panopticapi
```
위처럼 git에서 오픈소스로 구현된 모델들을 다운로드 받아줍니다.  

## Dependency
- Numpy
- Pillow
- tf-slim
- matplotlib
- tensorflow 1.15.0

## Testing the Installation
```bash
$ bash local_test.sh
```
wget의 다운로드 속도가 많이 느리면 
```bash
$ sudo apt-get install -y axel
```
axel 패키지를 다운받아 빠르게 다운 받을 수 있다.  
local_test.sh와 ./datasets/download_and_convert_voc2012.sh 두 개의 쉘 파일에서 wget을 axel로 수정해준다. 또한 wget에 있던 옵션을 지워줘야 한다.