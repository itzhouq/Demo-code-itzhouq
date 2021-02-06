### Dubbo 学习笔记

Dubbo 文档非常全，而且是中文，学习起来很方便，记录一下运行入门 demo 的过程。



## 一、准备注册中心

Dubbo 适配多种注册中心，推荐使用 zookeeper。[官网下载](https://mirrors.bfsu.edu.cn/apache/zookeeper/zookeeper-3.6.2/apache-zookeeper-3.6.2-bin.tar.gz)

- 解压

```bash
tar -xvf apache-zookeeper-3.6.1-bin.tar.gz
```

- 修改配置文件

```bash
cd ./apache-zookeeper-3.6.1/conf
➜  conf ls
configuration.xsl log4j.properties zoo_sample.cfg
➜  cp zoo_sample.cfg zoo.cfg # 根据模板文件创建配置文件
➜  vim zoo.cfg # 修改配置文件
```

- 配置文件

```bash
dataDir=/Users/itzhouq/dev/apache-zookeeper-3.6.1/data # 将该目录修改为根目录下的data文件夹
```

- 创建 data 文件夹

```bash
➜  apache-zookeeper-3.6.1 ls
LICENSE.txt         README.md           bin                 lib
NOTICE.txt          README_packaging.md conf                docs                logs
➜  apache-zookeeper-3.6.1 mkdir data # 创建data文件夹
```

- 启动服务端

```bash
./bin/zkServer.sh start
```

- 可能遇到的问题及解决

如果启动报错查看报错信息，结合根目录下的日志文件排查。我遇到以下几个问题：

- 端口号被占用的情况，杀死对应进程后再次运行。

```bash
lsof -i tcp:8080
```

- 启动脚本问题（没想到官方启动脚本还有问题）

```bash
mkdir: illegal option – e
usage: mkdir [-pv] [-m mode] directory …
-e /tmp/zookeeper echo /tmp/zookeeper/zookeeper_server.pid
-n Starting zookeeper …
zkServer.sh: line 176: -e /tmp/zookeeper
echo /tmp/zookeeper/zookeeper_server.pid: No such file or directory
FAILED TO WRITE PID
```

看网上的解答，注释掉启动脚本大概 116 行(vim 命令模式下输入 set number 可以显示行号)：

```bash
112 esac
113 ZOO_DATADIR="$($GREP "^[[:space:]]*dataDir" "$ZOOCFG" | sed -e 's/.*=//')"
114 # ZOO_DATADIR="$(echo -e "${ZOO_DATADIR}" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
115 ZOO_DATALOGDIR="$($GREP "^[[:space:]]*dataLogDir" "$ZOOCFG" | sed -e 's/.*=//')"
116
```

再启动就没报错了。



## 根据文档编写服务者和消费者

- dubbo-interface：对于公共的接口可以提取一个到一个模块
- order-service-consumer：消费者，远程调用接口
- User-service-provider：生产者，提供接口



## 示例代码

[代码地址](https://github.com/itzhouq/Demo-code-itzhouq/tree/master/spring-boot-dubbo-demo)


