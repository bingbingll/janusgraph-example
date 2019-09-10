package com.example.janusgraph.Example;

import com.example.janusgraph.config.GraphSourceConfig;
import com.example.janusgraph.config.JanusGraphConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.schema.JanusGraphManagement;
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

    /**
     * used for bindings
     */
    private static final String LABEL = "label";
    /**
     * 出边
     */
    private static final String OUT_V = "outV";
    /**
     * 入边
     */
    private static final String IN_V = "inV";
    @Autowired
    GraphSourceConfig graphSourceConfig;

    @Autowired
    JanusGraphConfig janusGraphConfig;
    /**
     *
     */
    public void createElements1() {

        log.info("creating elements方式1 使用JanusGraphTransaction对象创建");
        JanusGraph graph = janusGraphConfig.graph;
        JanusGraphManagement mgt = graph.openManagement();
        /**
         * 这里不再写了 参考本工程的GraphOfTheGodsFactory.java 类的load方法。
         */

    }


    /**
     *
     */
    public void createElements2(GraphTraversalSource g) {
        log.info("creating elements 方式2 使用 GraphTraversalSource 对象创建");
        //java.lang.IllegalArgumentException: The provided key/value array length must be a multiple of two
        final Bindings b = Bindings.instance();

        /************创建人******************/
        Vertex zhangming = g.addV(SchemaVertex.PERSON)
                .property(SchemaProperties.NAME, "张明",
                        SchemaProperties.NAME, "小明",
                        SchemaProperties.NAME, "明明",
                        SchemaProperties.NAME, "小张",
                        SchemaProperties.AGE, 30,
                        SchemaProperties.GENDER, "男",
                        SchemaProperties.NO, "11111111111111111",
                        SchemaProperties.PHONE, "1351111111",
                        SchemaProperties.TIME, "2019年2月1号",
                        SchemaProperties.ID, "0111")
                .next();

        Vertex zhaoshi = g.addV(SchemaVertex.PERSON)
                .property(SchemaProperties.NAME, "赵四",
                        SchemaProperties.NAME, "小赵",
                        SchemaProperties.AGE, 29,
                        SchemaProperties.GENDER, "男",
                        SchemaProperties.NO, "222222222222222222",
                        SchemaProperties.PHONE, "13522222222",
                        SchemaProperties.TIME, "2019年2月2号",
                        SchemaProperties.ID, "0222")
                .next();

        Vertex wanglei = g.addV(SchemaVertex.PERSON)
                .property(SchemaProperties.NAME, "王磊", SchemaProperties.NAME, "小王",
                        SchemaProperties.AGE, 28,
                        SchemaProperties.GENDER, "男",
                        SchemaProperties.NO, "333333333333333333",
                        SchemaProperties.PHONE, "13533333333",
                        SchemaProperties.TIME, "2019年2月3号",
                        SchemaProperties.ID, "0333")
                .next();

        Vertex lixin = g.addV(SchemaVertex.PERSON)
                .property(SchemaProperties.NAME, "李欣"
                        , SchemaProperties.AGE, 21
                        , SchemaProperties.GENDER, "女"
                        , SchemaProperties.NO, "4444444444444444"
                        , SchemaProperties.PHONE, "13544444444"
                        , SchemaProperties.TIME, "2019年2月4号"
                        , SchemaProperties.ID, "0444")
                .next();

        Vertex liyan = g.addV(SchemaVertex.PERSON)
                .property(SchemaProperties.NAME, "李艳"
                        , SchemaProperties.AGE, 22
                        , SchemaProperties.GENDER, "女"
                        , SchemaProperties.NO, "55555555555555555"
                        , SchemaProperties.PHONE, "1355555555"
                        , SchemaProperties.TIME, "2019年2月5号"
                        , SchemaProperties.ID, "0555")
                .next();


        Vertex zhangxin = g.addV(SchemaVertex.PERSON)
                .property(SchemaProperties.NAME, "张鑫"
                        , SchemaProperties.AGE, 39
                        , SchemaProperties.GENDER, "女"
                        , SchemaProperties.NO, "66666666666666666"
                        , SchemaProperties.PHONE, "136666666666"
                        , SchemaProperties.ID, "0666")
                .next();
        Vertex zhanghan = g.addV(SchemaVertex.PERSON)
                .property(SchemaProperties.NAME, "张翰"
                        , SchemaProperties.AGE, 45
                        , SchemaProperties.GENDER, "男"
                        , SchemaProperties.NO, "7777777777777777"
                        , SchemaProperties.PHONE, "137777777777"
                        , SchemaProperties.ID, "0777")
                .next();


        /******创建公司*******/
        Vertex apple = g.addV(SchemaVertex.COMPANY)
                .property(SchemaProperties.NAME, "苹果科技公司"
                        , SchemaProperties.PHONE, "010-11111111"
                        , SchemaProperties.ID, "1111"
                        , SchemaProperties.PLACE, b.of(SchemaProperties.PLACE, getGeoFloatArray(37.7f, 23.9f)))
                .next();


        Vertex ibm = g.addV(SchemaVertex.COMPANY)
                .property(SchemaProperties.NAME, "IMD公司"
                        , SchemaProperties.PHONE, "010-22222222"
                        , SchemaProperties.ID, "1222"
                        , SchemaProperties.PLACE, b.of(SchemaProperties.PLACE, getGeoFloatArray(39.2f, 26.6f)))
                .next();

        /*****创建地点*******/
        Vertex adrs1 = g.addV(SchemaVertex.LOCALHOST)
                .property(SchemaProperties.NAME, "洛克时代"
                        , SchemaProperties.ADDRESS, "北京市朝阳区慧忠里13号"
                        , SchemaProperties.ID, "3111"
                        , SchemaProperties.PLACE, getGeoFloatArray(26.9f, 21.1f))
                .next();
        Vertex adrs2 = g.addV(SchemaVertex.LOCALHOST)
                .property(SchemaProperties.NAME, "软件园"
                        , SchemaProperties.ADDRESS, "北京市海淀区西二旗"
                        , SchemaProperties.ID, "3222"
                        , SchemaProperties.PLACE, getGeoFloatArray(28.3f, 39.6f))
                .next();

        /** 创建点与点的关系 */

        g.V(zhangming).as("a").V(zhanghan).addE("father").from("a").next();
        g.V(zhangming).as("a").V(zhangxin).addE( "mother").from("a").next();
        g.V(zhangming).as("a").V(zhaoshi).addE( "brother").from("a").next();
        g.V(zhangming).as("a").V(apple).addE( "belong").from("a").next();

        g.V(zhaoshi).as("a").V(zhanghan).addE( "father").from("a").next();
        g.V(zhaoshi).as("a").V(zhangxin).addE( "mother").from("a").next();
        g.V(zhaoshi).as("a").V(zhangming).addE( "brother").from("a").next();
        g.V(zhaoshi).as("a").V(apple).addE( "belong").from("a").next();


        g.V(wanglei).as("a").V(zhaoshi).addE( "brother").from("a").next();
        g.V(wanglei).as("a").V(apple).addE( "belong").from("a").next();

        g.V(lixin).as("a").V(ibm).addE( "belong").from("a").next();
        g.V(lixin).as("a").V(liyan).addE( "brother").from("a").next();

        g.V(liyan).as("a").V(lixin).addE( "brother").from("a").next();
        g.V(liyan).as("a").V(apple).addE( "belong").from("a").next();


        g.V(apple).as("a").V(adrs1).addE( "belong").from("a").next();
        g.V(ibm).as("a").V(adrs2).addE( "belong").from("a").next();
    }

    protected float[] getGeoFloatArray(float lat, float lon) {
        float[] fa = new float[]{lat, lon};
        return fa;
    }
}