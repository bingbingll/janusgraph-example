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
>-- 官网文档：[文档](https://docs.janusgraph.org/latest/index.html)  
>-- 源码网址: [GitHub](https://github.com/JanusGraph/janusgraph)  
>-- 搭建形式：[官网](https://docs.janusgraph.org/latest/cassandra.html)  
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
当使用熟练了可以进行集群，集群也很简单参靠我的集群搭建[链接]()。这里需要你看下版本[兼容](https://docs.janusgraph.org/latest/version-compat.html)：

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
4. 下载Elasticsearch，这里使用Janus graph自带的。出于安全原因，Elasticsearch必须在非root帐户下运行，所以需要手动添加用户进行启动脚本如下：
	1.  root 用户创建用户命令：`groupadd es;`、`useradd es -g es -p es;`、`chown -R es:es elasticsearch` 这一句要在janusgraph-0.4.0-hadoop2目录执行。

## 服务运行 ##

# 实战 #

## Java代码编写 ##
<font face="黑体" color=red size=4> 项目下载后需要你修改 项目的conf/gremlin.remote.driver.clusterFile属性的路径值</font>