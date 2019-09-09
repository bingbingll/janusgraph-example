package com.example.janusgraph.config;

import lombok.extern.log4j.Log4j2;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.MessageSerializer;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.driver.ser.AbstractGraphSONMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerGremlinV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.Serializers;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.net.URLDecoder;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

/**
 * @author 李兵
 * @version V1.0
 * @description TODO: 获取图对象配置
 * @date 2019/8/30 20:05
 */
@Configuration
@Log4j2
public class GraphSourceConfig {

    /**
     * 基于官网介绍编写1
     * TODO:需要修改 remote-graph.properties 配置文件的gremlin.remote.driver.clusterFile=值为绝对路径
     * @return
     * @throws Exception
     */
    public GraphTraversalSource getGts1() throws Exception {
        Graph graph = EmptyGraph.instance();
        //不这样写获取读不到文件路径
        String path = getClass().getClassLoader().getResource("conf/remote-graph.properties").getPath();
        String decode = URLDecoder.decode(path.substring(1), "utf-8");
        GraphTraversalSource g = graph.traversal().withRemote(decode);
        return g;
    }

    /**
     * 基于官网介绍编写2
     * TODO:需要修改 remote-graph.properties 配置文件的gremlin.remote.driver.clusterFile=值为绝对路径
     * @return
     * @throws Exception
     */
    public GraphTraversalSource getGts2() throws Exception {
        String path1 = Thread.currentThread()
                .getContextClassLoader()
                .getResource("conf/remote-graph.properties").getPath();
        GraphTraversalSource g = traversal().withRemote(java.net.URLDecoder.decode(path1.substring(1), "utf-8"));
        return g;
    }


    /**
     * 基于官网介绍编写3
     *
     */
    public GraphTraversalSource getGts3()  {
        GraphTraversalSource g = traversal()
                .withRemote(
                        DriverRemoteConnection.using("172.16.2.137", 8182, "g")
                );
        return g;
    }



    /**
     * 池的写法
     * @return
     */
    public Cluster getCluster() {
        //TODO:配置地址-> http://tinkerpop.apache.org/javadocs/3.4.1/core/org/apache/tinkerpop/gremlin/driver/Cluster.Builder.html
        return Cluster.build()
                .serializer(Serializers.GRAPHSON_V2D0)
                .maxConnectionPoolSize(20)
                .maxInProcessPerConnection(15)
                .maxWaitForConnection(3000)
                .reconnectInterval(10)
                //可配置多个,是你的janusgraph 的地址
                .addContactPoint("172.16.2.137")
                .port(8182)
                .create();
    }
    public Client getClient() {
        Cluster cluster = getCluster();
        return cluster.connect();
    }

    /**
     * 基于官网介绍编写4
     * @return
     */
    public GraphTraversalSource getGts4() {
        GraphTraversalSource g = traversal().
                withRemote(DriverRemoteConnection.
                        using(getClient(), "g")
                );
        return g;
    }




    public void close(GraphTraversalSource g, Client client) {
        try {
            g.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
