# ZooKeeper-分布式过程协同技术
demo项目

## 一、Zookeeper安装与使用

### 1、下载与安装
进入一下网址：https://archive.apache.org/dist/zookeeper/，获取最新版本的Zookeeper压缩包，zookeeper-3.4.9.tar.gz，解压可以看到可执行文件、文档、源码、jar包等文件。
Linux系统安装：
1.下载压缩包到指定目录
Cd命令切换到指定命令，使用以下命令下载。
Wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz 
2.解压压缩包
tar -xvzf zookeeper-3.4.5.tar.gz

### 2、启动Zookeeper服务端和客户端
#### (1).启动服务端
(1)假设你已经下载并解压了ZooKeeper发行包，进入shell，变更目录 （cd）到项目根目录下，重命名配置文件：mv conf/zoo_sample.cfg conf/zoo.cfg
(2)虽然是可选的，最好还是把data目录移出/tmp目录，以防止 ZooKeeper填满了根分区（root partition）。可以在zoo.cfg文件中修改这 个目录的位置。dataDir=/root/data/zookeeper
(3)最后，为了启动服务器，执行以下命令：bin/zkServer.sh start
(4)这个服务器命令使得ZooKeeper服务器在后台中运行。如果在前台 中运行以便查看服务器的输出，可以通过以下命令运行：
bin/zkServer.sh start-foreground
#### (2).启动客户端
在另一个shell中进入项目根目录，运行 以下命令：bin/zkCli.sh

    . . . . . .
     2012-12-06 12:07:23,545 [myid:] - INFO [main:ZooKeeper@438] -① Initiating client connection, connectString=localhost:2181 sessionTimeout=30000 watcher=org.apache.zookeeper. ZooKeeperMain$MyWatcher@2c641e9a Welcome to ZooKeeper! 
    2012-12-06 12:07:23,702 [myid:] - INFO [main-SendThread② (localhost:2181):ClientCnxn$SendThread@966] - Opening socket connection to server localhost/127.0.0.1:2181. Will not attempt to authenticate using SASL (Unable to locate a login configuration) JLine support is enabled 
    2012-12-06 12:07:23,717 [myid:] - INFO [main-SendThread③ (localhost:2181):ClientCnxn$SendThread@849] - Socket connection established to localhost/127.0.0.1:2181, initiating session [zk: localhost:2181(CONNECTING) 0] 
    2012-12-06 12:07:23,987 [myid:] - INFO [main-SendThread④ (localhost:2181):ClientCnxn$SendThread@1207] - Session establishment complete on server localhost/127.0.0.1:2181, sessionid = 0x13b6fe376cd0000, negotiated timeout = 30000 WATCHER:: WatchedEvent state:SyncConnected type:None path:null⑤ 

①客户端启动程序来建立一个会话。 

②客户端尝试连接到localhost/127.0.0.1：2181。

③客户端连接成功，服务器开始初始化这个新会话。 

④会话初始化成功完成。 

⑤服务器向客户端发送一个SyncConnected事件。

### 3、简单命令
#### (1).列出根目录所有znode
命令： ls /
![](file/comand1.png)
#### (2).创建znode
命令：create /workers ""

含义：创建名为“workers”的znode，并存储空字符串数据。

#### (3).删除znode
命令：delete /workers

#### (4).退出命令（quit）

#### (5).ZooKeeper服务器（bin/zkServer.sh stop）
