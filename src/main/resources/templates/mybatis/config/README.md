## 基于SpringBoot 2.7.8 + MybatisPlus 3.5.2 + Swagger Ui 3.0.3
### 简介
LQY技术框架 - Mybatis分支 旨在构建出一套标准的开发脚手架，解决技术体系、编码规范不统一的问题


### 框架目录结构
```text
│  pom.xml //maven 配置
│  README.md //说明文档
│  
├─docker
│  │  Dockerfile //Docker编译配置
│  │  
│  └─docker-k8s
│          Dockerfile //K8s Docker编译配置
│          lqy-data-k8s-append.yaml //K8s 部署文件
│          
├─log //日志存放目录
│              
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com
│  │  │      └─fpcloud
│  │  │          └─#【name】 //自定义包名，可根据项目修改为自己对应包名
│  │  │              │  #【name】Application.java //启动类
│  │  │              │  
│  │  │              ├─beans //全局bean注入包
│  │  │              ├─configuration //全局配置
│  │  │              │      ApiDocConfiguration.java //Api文档发布配置
│  │  │              │      WebAppConfiguration.java //程序网络相关、跨域等信息配置
│  │  │              │      
│  │  │              ├─controller //Api接口发布 - 所有对外开放的接口都从此包下发布
│  │  │              ├─generator //自动生成代码
│  │  │              │      FpcGenerator.java //可根据配置自动生成Mybatis Plus 代码
│  │  │              │      
│  │  │              ├─mapper //生成数据库的表基础操作映射文件 - dao
│  │  │              │  └─xml //生成的数据库表对应xml文件
│  │  │              ├─pojo //生成的数据表对应实体Vo文件
│  │  │              ├─service //业务处理接口
│  │  │              │  └─impl //业务处理实现接口
│  │  │              └─utils //工具类
│  │  │                      JsonObject.java //JSON处理工具类
│  │  │                      
│  │  └─resources //资源存放目录
│  │      │  application-dev.yml //开发环境使用
│  │      │  application-prod.yml //生产环境使用
│  │      │  application.yml //基础配置
│  │      │  ehcache.xml //sql查询二级缓存
│  │      │  logback.xml //日志配置 - 根据项目修改文件内对应目录
│  │      │  
│  │      ├─generator //自动生成配置文件存放
│  │      │      mybatis-plus.properties //自动生成代码配置文件，可修改jdbc等信息生成项目所需文件
│  │      │      
│  │      ├─static //静态资源存放
│  │      └─templates //模板存放地址 - 可以发布FreemarkerTemplate
│  └─test //测试类
│      └─java
│          └─com
│              └─fpcloud
│                      #【name】ApplicationTests.java
```

### 使用方法
#### 1、拉取 #【name】 源码
#### 2、打开 拉取的项目修改 项目名、maven中项目信息和对应包名以及数据源配置application-dev.yml

#### 3、点击 DEBUG 运行项目，运行成功后会出现以下字样
```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
```
#### 4、输入项目地址测试是否启用成功
* 项目默认地址：[http://127.0.0.1:8080](http://127.0.0.1:8080)
* 项目文档默认地址：[http://127.0.0.1:8080/doc.html](http://127.0.0.1:8080/doc.html)
* 项目健康状态查看地址(在K8S中可使用探针确定是否存活)：[http://127.0.0.1:8080/actuator/health](http://127.0.0.1:8080/actuator/health)

#### 5、Docker部署
```
docker stop  #【name】
docker rm  #【name】
docker rmi  #【name】:v1.0.0
cd /home/ten/#【name】
docker build --rm -t #【name】:v1.0.0 .
docker run  -d  -p 8080:8080  -it --privileged=true  --restart=on-failure:10 --name #【name】 #【name】:v1.0.0

```