package com.example.janusgraph.Example;

import com.example.janusgraph.config.GraphSourceConfig;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.tinkerpop.gremlin.process.traversal.Bindings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.attribute.Geoshape;
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
     * used for bindings
     */
    private static final String LABEL = "label";
    /**出边*/
    private static final String OUT_V = "outV";
    /**入边*/
    private static final String IN_V = "inV";
    /**
     *
     */
    public void createElements1() {

        log.info("creating elements方式1 使用JanusGraphTransaction对象创建");
    }


    /**
     *
     */
    public void createElements2(GraphTraversalSource g) {
        log.info("creating elements 方式2 使用 GraphTraversalSource 对象创建");
        final Bindings b = Bindings.instance();

        /************创建人******************/
        Vertex zhangming = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME,
                        b.of(SchemaProperties.NAME, "张明"),
                        b.of(SchemaProperties.NAME, "小明"),
                        b.of(SchemaProperties.NAME, "明明"),
                        b.of(SchemaProperties.NAME, "小张")
                )
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 30))
                .property(SchemaProperties.GENDER, b.of(SchemaProperties.GENDER, "男"))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "11111111111111111"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "1351111111"))
                .property(SchemaProperties.TIME, b.of(SchemaProperties.TIME, "2019年2月1号"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "0111"))
                .next();

        Vertex zhaoshi = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME,
                        b.of(SchemaProperties.NAME, "赵四"),
                        b.of(SchemaProperties.NAME, "小赵")
                )
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 29))
                .property(SchemaProperties.GENDER, b.of(SchemaProperties.GENDER, "男"))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "222222222222222222"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "13522222222"))
                .property(SchemaProperties.TIME, b.of(SchemaProperties.TIME, "2019年2月2号"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "0222"))
                .next();

        Vertex wanglei = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME,
                        b.of(SchemaProperties.NAME, "王磊"),
                        b.of(SchemaProperties.NAME, "小王")
                )
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 28))
                .property(SchemaProperties.GENDER, b.of(SchemaProperties.GENDER, "男"))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "333333333333333333"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "13533333333"))
                .property(SchemaProperties.TIME, b.of(SchemaProperties.TIME, "2019年2月3号"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "0333"))
                .next();

        Vertex lixin = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "李欣"))
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 21))
                .property(SchemaProperties.GENDER, b.of(SchemaProperties.GENDER, "女"))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "4444444444444444"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "13544444444"))
                .property(SchemaProperties.TIME, b.of(SchemaProperties.TIME, "2019年2月4号"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "0444"))
                .next();

        Vertex liyan = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "李艳"))
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 22))
                .property(SchemaProperties.GENDER, b.of(SchemaProperties.GENDER, "女"))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "55555555555555555"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "1355555555"))
                .property(SchemaProperties.TIME, b.of(SchemaProperties.TIME, "2019年2月5号"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "0555"))
                .next();


        Vertex zhangxin = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "张鑫"))
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 39))
                .property(SchemaProperties.GENDER, b.of(SchemaProperties.GENDER, "女"))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "66666666666666666"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "136666666666"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "0666"))
                .next();
        Vertex zhanghan = g.addV(b.of(LABEL, SchemaVertex.PERSON))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "张翰"))
                .property(SchemaProperties.AGE, b.of(SchemaProperties.AGE, 45))
                .property(SchemaProperties.GENDER, b.of(SchemaProperties.GENDER, "男"))
                .property(SchemaProperties.NO, b.of(SchemaProperties.NO, "7777777777777777"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "137777777777"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "0777"))
                .next();


        /******创建公司*******/
        Vertex apple = g.addV(b.of(LABEL, SchemaVertex.COMPANY))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "苹果科技公司"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "010-11111111"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "1111"))
                .property(SchemaProperties.PLACE, b.of(SchemaProperties.PLACE, Geoshape.point(37.7f, 23.9f)))
                .next();

        Vertex ibm = g.addV(b.of(LABEL, SchemaVertex.COMPANY))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "IMD公司"))
                .property(SchemaProperties.PHONE, b.of(SchemaProperties.PHONE, "010-22222222"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "1222"))
                .property(SchemaProperties.PLACE, b.of(SchemaProperties.PLACE, Geoshape.point(39.2f, 26.6f)))
                .next();

        /*****创建地点*******/
        Vertex adrs1 = g.addV(b.of(LABEL, SchemaVertex.LOCALHOST))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "洛克时代"))
                .property(SchemaProperties.ADDRESS, b.of(SchemaProperties.ADDRESS, "北京市朝阳区慧忠里13号"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "3111"))
                .property(SchemaProperties.PLACE, b.of(SchemaProperties.PLACE, Geoshape.point(26.9f, 21.1f)))
                .next();
        Vertex adrs2 = g.addV(b.of(LABEL, SchemaVertex.LOCALHOST))
                .property(SchemaProperties.NAME, b.of(SchemaProperties.NAME, "软件园"))
                .property(SchemaProperties.ADDRESS, b.of(SchemaProperties.ADDRESS, "北京市海淀区西二旗"))
                .property(SchemaProperties.ID, b.of(SchemaProperties.ID, "3222"))
                .property(SchemaProperties.PLACE, b.of(SchemaProperties.PLACE, Geoshape.point(28.3f, 39.6f)))
                .next();

        /** 创建点与点的关系 */

        g.V(b.of(OUT_V, zhangming)).as("a").V(b.of(IN_V, zhanghan)).addE(b.of(LABEL, "father")).from("a").next();
        g.V(b.of(OUT_V,zhangming)).as("a").V(b.of(IN_V,zhangxin)).addE(b.of(LABEL, "mother")).from("a").next();
        g.V(b.of(OUT_V,zhangming)).as("a").V(b.of(IN_V,zhaoshi)).addE(b.of(LABEL, "brother")).from("a").next();
        g.V(b.of(OUT_V,zhangming)).as("a").V(b.of(IN_V,apple)).addE(b.of(LABEL, "belong")).from("a").next();

        g.V(b.of(OUT_V, zhaoshi)).as("a").V(b.of(IN_V, zhanghan)).addE(b.of(LABEL, "father")).from("a").next();
        g.V(b.of(OUT_V,zhaoshi)).as("a").V(b.of(IN_V,zhangxin)).addE(b.of(LABEL, "mother")).from("a").next();
        g.V(b.of(OUT_V,zhaoshi)).as("a").V(b.of(IN_V,zhangming)).addE(b.of(LABEL, "brother")).from("a").next();
        g.V(b.of(OUT_V,zhaoshi)).as("a").V(b.of(IN_V,apple)).addE(b.of(LABEL, "belong")).from("a").next();


        g.V(b.of(OUT_V,wanglei)).as("a").V(b.of(IN_V,zhaoshi)).addE(b.of(LABEL, "brother")).from("a").next();
        g.V(b.of(OUT_V,wanglei)).as("a").V(b.of(IN_V,apple)).addE(b.of(LABEL, "belong")).from("a").next();

        g.V(b.of(OUT_V,lixin)).as("a").V(b.of(IN_V,ibm)).addE(b.of(LABEL, "belong")).from("a").next();
        g.V(b.of(OUT_V,lixin)).as("a").V(b.of(IN_V,liyan)).addE(b.of(LABEL, "brother")).from("a").next();

        g.V(b.of(OUT_V,liyan)).as("a").V(b.of(IN_V,lixin)).addE(b.of(LABEL, "brother")).from("a").next();
        g.V(b.of(OUT_V,liyan)).as("a").V(b.of(IN_V,apple)).addE(b.of(LABEL, "belong")).from("a").next();



        g.V(b.of(OUT_V,apple)).as("a").V(b.of(IN_V,adrs1)).addE(b.of(LABEL, "belong")).from("a").next();
        g.V(b.of(OUT_V,ibm)).as("a").V(b.of(IN_V,adrs2)).addE(b.of(LABEL, "belong")).from("a").next();

    }
}