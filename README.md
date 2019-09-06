# janusgraph-dome
这是一个基于Java-maven创建的的Janus graph 0.4.0版本示例程序，及遇到的问题。这里我们不做过多的介绍，因为用到的组件比较多，你想要了解那种就去它的官网进行文档阅读。本项目主要介绍Java操作。

## 介绍 ##
&emsp;&emsp;JanusGraph（以下都是官网抄的，我们只需要知道支持那种组件就行）是一个开源的分布式图数据库。它具有很好的扩展性，通过多机集群可支持存储和查询数百亿的顶点和边的图数据。JanusGraph是一个事务数据库，支持大量用户高并发地执行复杂的实时图遍历。  
&emsp;&emsp;JanusGraph旨在支持图形处理，以至于它们需要超出单个机器所能提供的存储和计算能力。实时遍历和分析查询的缩放图数据处理是JanusGraph的基本优势。。

**它提供了如下特性**：    

- 支持数据和用户增长的弹性和线性扩展;  
- 通过数据分发和复制来提过性能和容错;  
- 支持多数据中心的高可用和热备份;   
- 支持ACID 和最终一致性;  
- 支持多种后端存储：  
	1. **Apache Cassandr**
	2. **Apache HBase**  
	3. **Google Cloud Bigtable**  
	4. **Oracle BerkeleyDB(仅供测试环境)**  
- 支持全局的图数据分析，报表以及和如下大数据平台的ETL集成：  
	1. **Apache Spark**        
	2. **Apache Giraph**        
	3. **Apache Hadoop**
- 支持geo, 数值范围以及通过如下工具进行全文检索：
	1. **ElasticSearch**
	2. **Apache Solr**
	3. **Apache Lucene**
- 支持与Apache TinkerPop图栈进行原生集成：  
	1. **Gremlin 图查询语言** 
	2. **Gremlin 图服务器** 
	3. **Gremlin 应用**
- 在Apache 2 license下开源可以通过如下工具来可视化存储在JanusGraph中的图数据：
	1. **Cytoscape** 
	2. **Apache TinkerPop的Gephi插件** 
	3. **Graphexp KeyLines by Cambridge Intelligence** 
	4. **Linkurious**

**用到的链接**
>-- 官网地址：[官网](https://janusgraph.org/)  
>-- 官网文档：[文档](https://docs.janusgraph.org/)
>-- 源码网址: [GitHub](https://github.com/JanusGraph/janusgraph)  
>-- 搭建形式：[官网](https://docs.janusgraph.org/basics/deployment/)  
>-- Java-API：[官网](https://javadoc.io/doc/org.janusgraph/janusgraph-core/0.4.0)
>
>-- 数据存储:
>>[Apache Cassandra](https://docs.janusgraph.org/latest/cassandra.html)   
>>[Apache HBaseracle](https://docs.janusgraph.org/latest/hbase.html)    
>>[Berkeley DB Java版](https://docs.janusgraph.org/latest/bdb.html)  
>
>--指数，加速并启用更复杂的查询：  
>>[Elasticsearch](https://docs.janusgraph.org/latest/elasticsearch.html)    
>>[Apache Solr](https://docs.janusgraph.org/latest/solr.html)   
>>[Apache Lucene](https://docs.janusgraph.org/latest/lucene.html)   
>
>--查询语言：  
>>[tinkerpop-Gremlin](http://tinkerpop.apache.org/docs/3.4.1/reference/#tail-step)

## 基础环境及组件 ##
Janus graph可以在Linux系统或window系统下运行，两种方式运行.bat/.sh 两种文件即可。
本项目基于centos7系统，Cassandra 单机，Elasticsearch 单机。我们这里为的是学习，组件越多问题越多，越不好排查，
当使用熟练了可以进行集群，集群也很简单参考集[链接](https://github.com/bingbingll/janusgraph-dome/blob/master/集群搭建.md)。
这里需要你看下版本[兼容](https://docs.janusgraph.org/changelog/)：

1. centos7.x 
	1. 注意：先关闭防火墙-[防火墙参考](https://www.cnblogs.com/yyxq/p/10551274.html)
	2. Java Oracle-Java-1.8或openJava-1.8
	3. 安装必要软件
		1.  安装jdk: `yum install java -y`
		2.  安装zip: `yum install -y unzip zip`
	4. 服务器最好是2台以上；我这里是172.16.2.137、172.16.2.138。 
3. 下载Cassandra 上传至172.16.2.138 并解压
	1. 启动: 后台启动->`bin/cassandra -R`  前台启动-> `./cassandra`
	2. 停止：`pgrep -f CassandraDaemon`  `kill -9 [进程号]`
4. 下载Elasticsearch。出于安全原因，Elasticsearch必须在非root帐户下运行，所以需要手动添加用户进行启动脚本如下：
	1. root 用户创建用户命令：`groupadd es;`、`useradd es -g es -p es;`、`chown -R es:es elasticsearch` 这一句要在janusgraph-0.4.0-hadoop2目录执行。
	2. centos需要修改以下参数；root 用户下 修改配置文件  `vi /etc/security/limits.conf` 最后一行增加 `* soft nofile 65536 * hard nofile 131072`  ，保存退出后，在修改 `vi /etc/sysctl.conf` 最后一行追加 `vm.max_map_count=655360` 然后保存退出，执行 `sysctl -p`
	3. 后台启动： ./elastic -d
	4. 停止：ps -ef | grep elastic kill -9 [进程号]

## 服务运行 ##
登录centos系统，进入janusgraph-0.4.0-hadoop2/conf/gremlin-server 文件夹目录修改文件如下：

1. 服务式启动（Java程序调用必须启动）
	- gremlin-server.yaml
		- `host: 172.16.2.13` janusgraph所在的服务器IP，这样就可以在其他电脑上访问到了
		- `channelizer: org.apache.tinkerpop.gremlin.server.channel.WsAndHttpChannelizer` 支持websocket和http
 	- janusgraph-cql-es-server.properties
		- `storage.hostname=172.16.2.138` 数据库所在的服务器IP，多个IP用,号隔开
		- `storage.cql.keyspace=janusgraphtest` 自定义库名称
		- `index.search.backend=elasticsearch` es无需改动
		- `index.search.hostname=127.0.0.1` 使用的是自带的无需改动，若是有别的es改位其IP即可，多个IP用,号隔开  
	- 上面两个文件改完后保存，然后cd到/janusgraph-0.4.0-hadoop2/bin目录 输入 `nohup ./gremlin-server.sh conf/gremlin-server/gremlin-server.yaml` 进行后台启动。



2. 控制台式启动（主要用于控制台查看学习用）
	- 启动 Gremlin Console：./gremlin.sh  启动控制台命令
	- 开启一个图数据库实例：gremlin> graph = JanusGraphFactory.open('conf/janusgraph-cql-es.properties')；注意这里的配置文件内容要和conf/gremlin-server/janusgraph-cql-es-server.properties的内容一致
	- 获取管理对象：gremlin> mgmt = graph.openManagement()
	- 查看所有的顶点标签： gremlin> labels = mgmt.getVertexLabels()
        - ==>user  
          ==>statusUpdate
    - 查看所有的边：mgmt.getRelationTypes(EdgeLabel.class)
        - ==>posts  
          ==>follows  
          ==>works  
          ==>follow  
          ==>mother
    - 根据主键查看属性:mgmt.getPropertyKey('marcelocf.janusgraph.userName')
        - ==>marcelocf.janusgraph.userName
    - 获取图遍历句柄：gremlin> g = graph.traversal()

## Java代码编写 ##
<font face="黑体" color=red size=4> 项目下载后需要你修改 项目的conf/gremlin.remote.driver.clusterFile属性的路径值</font>

### schema 介绍
&emsp;&emsp;首先这里需要先了解一下Janus graph中schema的概念，若是schema概念没弄明白后续跟没法学习应用了。
若您的英语很好可以先在官网文档-[链接](https://docs.janusgraph.org/basics/schema/)-中对于schema的阐述进行了解（反正我只是看个大概脑子稀里糊涂的）。
还可以参考[链接](https://www.cnblogs.com/jiyuqi/p/7127178.html?utm_source=itdadao&utm_medium=referral) 介绍。  
  
&emsp;&emsp;官网文档说到**The schema type - i.e. edge label, property key,or vertex label - is assigned to elements in the graph - i.e. edge, properties or vertices respectively - when they are first created. The assigned schema type cannot be changed for a particular element. This ensures a stable type system that is easy to reason about.** 什么意思呢？根据我的理解，schema（模型）！用于描述定义一个图中的数据是什么样子的，数据依据此定义进行数据制作从而完成一个图。因此创建schema（模型）时需要先定义图中顶点（vertex）、边（edge）和属性（property）然后才能组成一个schema。在图逻辑中，vertex描述为每个单独的实体（相当于实体，抽象出来的一个类别，例如人，公司，地点等等），edge作为连接2个以上的vertex的桥梁，描述vertex与vertex之间的关系，而property依附在vertex或edge上，填充实体或边的属性。换句话说：我们在关系型数据库中定义一个实体表时；要给这个表设定一个表名，列名及列的类型，值是否为空，是否为主键等。这和我们定义一个schema是一个概念！这样说同学你是否豁然开朗呢？

&emsp;&emsp;在引用官网的一句话**Each JanusGraph graph has a schema comprised of the edge labels, property keys, and vertex labels used therein. A JanusGraph schema can either be explicitly or implicitly defined. Users are encouraged to explicitly define the graph schema during application development. An explicitly defined schema is an important component of a robust graph application and greatly improves collaborative software development. Note, that a JanusGraph schema can be evolved over time without any interruption of normal database operations. Extending the schema does not slow down query answering and does not require database downtime.** 每一个图都有一个schema，schema中的数据是由**边标签、属性键、顶点标签**组成，在创建schema时可以显示地定义（就是通过配置文件或Java定义变量）schema（图）的三个元素，也可以隐式定义，鼓励用户在应用程序开发期间显式定义图形模式。隐式定义方式通过g对象创建、通过http服务式创建等。可以参考本工程的com.example.janusgraph.referenceExample目录下的GraphOfTheGodsFactory.java和RemoteGraphApp.java 或 RemoteGraphApp.java 继承的JanusGraphApp.class 进行深刻理解，然后根据你所在的业务场景进行选择。

### 创建schema
可以根据[schema 介绍](https://github.com/bingbingll/janusgraph-example#schema-介绍)这个节点的几个类进行编写，这里我选择JanusGraphApp.class 编写形式。为什么呢？原因为操作 JanusGraph有两套 API，
分别是 Graph Structure（结构） 和 Graph Process（处理）。 建议只用 graph Structure来做图的模型定义及数据库管理相关操作。
图的数据操作，包括创建、更新、删除及遍历都用 g（ Graph Process）来操作。如果想用 API 来大批量地操作数据，可以跳过 JanusGraph，直接写入后端存储。
创建schema参考本工程的[CreateSchema.java](https://github.com/bingbingll/janusgraph-example/blob/master/src/main/java/com/example/janusgraph/Example/CreateSchema.java);
图数据写入参考本工程的[GraphDataLand.java](https://github.com/bingbingll/janusgraph-example/blob/master/src/main/java/com/example/janusgraph/Example/GraphDataLand.java)
### 加载数据
索引创建