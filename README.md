自动创建TCR仓库工具

使用时请将本工具JAR文件和项目文件放在一起。

示例.gitlab-ci.yml文件：

```yaml
image: docker:dind
variables:
  #指定TCR的地址
  TCR_HOST: ccr.ccs.tencentyun.com
  #指定区域
  TCR_REGION: ap-nanjing
  #指定命名空间
  TCR_NAMESPACE: mcxk

stages:
  - prepare
  - build
#创建TCR镜像仓库
create-image:
  stage: prepare
  image: adoptopenjdk/openjdk11
  script:
    - export TCR_REPONAME=$(echo $CI_PROJECT_PATH | sed 's/\//_/g')
    - java -jar TCRCreator.jar
#处理你需要处理的东西
build:
  image: docker:latest
  stage: build
  services:
    - docker:dind
  before_script: #环境变量不同stage之间不共享，重新处理仓库名
    - export TCR_REPONAME=$(echo $CI_PROJECT_PATH | sed 's/\//_/g')
    - docker login -u "$TCR_USER" -p "$TCR_PASSWORD" $TCR_HOST
  script:
    - docker build --pull -t "$TCR_HOST/$TCR_NAMESPACE/$TCR_REPONAME" .
    - docker push "$TCR_HOST/$TCR_NAMESPACE/$TCR_REPONAME"

```
