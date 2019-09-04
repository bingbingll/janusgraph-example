package com.example.janusgraph.config;

import lombok.extern.log4j.Log4j2;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author 李兵
 * @version V1.0
 * @description TODO:
 * @date 2019/9/3 17:09
 */
@Configuration
@Log4j2
public class GetGraphAndMgt {
    public final JanusGraph graph;

    public final JanusGraphManagement mgt;

    private static final String CONFIG_FILE = "conf/janusgraph-cql-es-server.properties";

    /**
     * Initialize the graph and the graph management interface.
     * 使用无参构造
     */
    public GetGraphAndMgt() {
        try {
//            this.dropOldKeyspace();
        } catch (Exception ex) {
            log.info("Cannot drop keyspace janusgraph");
        }


        String path = Thread.currentThread()
                .getContextClassLoader()
                .getResource(CONFIG_FILE).getPath().substring(1);

        String decode = "";
        try {
            decode = URLDecoder.decode(path);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取配置文件路径错误原因："+e.getLocalizedMessage());
        }
        log.info("Connecting graph");
        graph = JanusGraphFactory.open(decode);
        log.info("Getting management");
        mgt = graph.openManagement();
    }

//    private void dropOldKeyspace() {
//        TTransport tr = new TFramedTransport(new TSocket("localhost", 9160));
//        TProtocol proto = new TBinaryProtocol(tr);
//        Cassandra.Client client = new Cassandra.Client(proto);
//        tr.open();
//
//        client.system_drop_keyspace(JANUSGRAPH);
//        LOGGER.info("DROPPED keyspace janusgraph");
//        tr.close();
//    }
}
