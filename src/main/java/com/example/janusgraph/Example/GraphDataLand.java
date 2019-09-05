package com.example.janusgraph.Example;

import com.example.janusgraph.config.GraphSourceConfig;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李兵
 * @version V1.0
 * @description TODO: 图数据添加
 * @date 2019/9/5 20:39
 */
@Service
@Log4j2
public class GraphDataLand {

    @Autowired
    GraphSourceConfig graphSourceConfig;


    /**
     *
     */
    public void createElements1() {

        log.info("creating elements方式1 使用JanusGraphTransaction对象创建");
    }

    // used for bindings
    private static final String LABEL = "label";

    private static final String NAME = "name";
    private static final String AGE = "age";

    private static final String TIME = "time";
    private static final String REASON = "reason";
    private static final String PLACE = "place";
    private static final String OUT_V = "outV";
    private static final String IN_V = "inV";


    /**
     *
     */
    public void createElements2() {
        log.info("creating elements 方式2 使用 GraphTraversalSource 对象创建");
        final Bindings b = Bindings.instance();
        GraphTraversalSource g = graphSourceConfig.getGts4();
        /************创建人******************/
        Vertex saturn = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "张明"))
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 26))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "100123456789123456"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "13512345678"))
                .next();
    }
}
