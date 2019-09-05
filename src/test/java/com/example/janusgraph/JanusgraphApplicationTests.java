package com.example.janusgraph;

import com.example.janusgraph.config.GraphSourceConfig;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JanusgraphApplicationTests {
    GraphSourceConfig config=new GraphSourceConfig();
    @Test
    public void testAdd() throws Exception {
        GraphTraversalSource g = config.getGts1();
        Vertex next = g.addV("测试1")
                .property("name", "测试1")
                .property("age", "25")
                .property("no", "11111111")
                .next();
    }

    @Test
    public void testAdd2() throws Exception {
        GraphTraversalSource g = config.getGts2();
        try {
            g.addV("测试2")
                    .property("name", "测试2", "no", "2222222", "addr", "北京市海淀区")
                    .next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g.close();
        }
    }

    @Test
    public void testAdd31() {
        GraphTraversalSource g = null;
        Client client = config.getClient();
        try {
            client.submit("g.addV('测试3').property('name','测试3','no','3333333','addr','北京市海淀区')");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            GraphSourceConfig.close(g, client);
        }
    }

    @Test
    public void testAdd4() throws Exception {
        GraphTraversalSource g = config.getGts4();
        try {
            g.addV("测试444").property("name","测试444","no","44444444");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g.close();
        }
    }


    @Test
    public void testGetVal() throws Exception {
        GraphTraversalSource g = config.getGts1();
        GraphTraversal<Vertex, Map<Object, Object>> vmgt = g.V().valueMap(true);
        System.out.println(vmgt.hasNext());
        while (vmgt.hasNext()) {
            System.out.println(vmgt.next().toString());
        }
    }

    @Autowired
    LandData loader;
    @Autowired
    GraphSourceConfig gsc;
    @Test
    public void landdata(){
        JanusGraph janusGraph = gsc.getJanusGraph2();
        Vertex[] vertices = loader.generateUsers(10, janusGraph);
        loader.commit(janusGraph);

        for(Vertex user: vertices) {
            System.out.println("User: "+user.value(InitSchemaExample.USER_NAME).toString()+" -comments");
            for(Vertex update: loader.generateStatusUpdates(user, 10,janusGraph)) {
                System.out.println("--->"+update.value(InitSchemaExample.CONTENT).toString());
            }
            loader.commit(janusGraph);

            System.out.println("User {} follows:"+user.value(InitSchemaExample.USER_NAME).toString());
            Vertex[] vertices1 = loader.generateFollows(user, vertices, 5);
            for(Vertex followedUser: vertices1) {
                System.out.println("->"+followedUser.value(InitSchemaExample.USER_NAME).toString());
            }
            loader.commit(janusGraph);
        }

    }
}
