package com.example.janusgraph;

import com.example.janusgraph.Example.CreateSchema;
import com.example.janusgraph.Example.GraphDataLand;
import com.example.janusgraph.config.GraphSourceConfig;
import com.example.janusgraph.config.JanusGraphConfig;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 李兵
 * @version V1.0
 * @description TODO:
 * @date 2019/9/6 16:15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamplTest {
    @Autowired
    CreateSchema schema;
    @Autowired
    GraphDataLand land;
    @Autowired
    GraphSourceConfig graphSourceConfig;
    @Autowired
    JanusGraphConfig janusGraphConfig;


    /**
     * 使用Graph Structure（结构）进行创建和定义数据模型
     */
    @Test
    public void testSchema(){

        try {
            schema.createProperties(janusGraphConfig.mgt);
            schema.createVertexLabels(janusGraphConfig.mgt);
            schema.createEdgeLabels(janusGraphConfig.mgt);
            schema.createCompositeIndexes(janusGraphConfig.mgt);
            schema.createMixedIndexes(janusGraphConfig.mgt);
        } catch (Exception e) {
            e.printStackTrace();
            janusGraphConfig.rollback();
        }finally {
            janusGraphConfig.close();
        }
    }

    /**
     * 使用Graph Process（处理）创建数据并插入到schema数据模型中
     */
    @Test
    public void testLand(){
        Client client = graphSourceConfig.getClient();
        GraphTraversalSource g = graphSourceConfig.getGts4(client);
        land.createElements2(g);
        graphSourceConfig.close(g,client);
    }

}
