package com.example.janusgraph.Example;

import lombok.extern.log4j.Log4j;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Multiplicity;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Parameter;
import org.springframework.stereotype.Service;

import static org.janusgraph.core.Multiplicity.MANY2ONE;

/**
 * @author 李兵
 * @version V1.0
 * @description TODO: 定义schema,这里的业务模型为，一个人，一个公司，
 * @date 2019/9/5 19:21
 */
@Service
@Log4j
public class CreateSchema {


    /**
     * 定义schema图的顶点（我喜欢叫这个为所属类型）； 人：person，公司：company，地点：localhost 三个顶点
     *
     * @param management
     */
    protected void createVertexLabels(JanusGraphManagement management) {
        management.makeVertexLabel(SchemaVertex.COMPANY).make();
        management.makeVertexLabel(SchemaVertex.LOCALHOST).make();
        management.makeVertexLabel(SchemaVertex.COMPANY).make();
    }

    /**
     * 定义schema图的边；
     *
     * Edge Label Multiplicity（边的标签多样性）值介绍如下：
     *     MULTI：在一对vertex间可以有任意多个同样label的edge。
     *     SIMPLE：在一对vertex间最多只能有一个同样label的edge。
     *     MANY2ONE：图中任意一个Vertex最多有一个出度（outgoing）edge，和不限个数的入度（incoming）edge，
     *     注意：这些对edge的限制对同一个label生效。例如：
     *                           Label: mother
     *     (大儿子)---------------------------------->|
     *     (二儿子)---------------------------------->|------------------------>(母亲)
     *     (小儿子)---------------------------------->|
     *
     *     ONE2MANY：图中任意一个Vertex最多有一个入度（incoming）edge，和不限个数的出度（outgoing）edge，
     *     注意：这些对edge的限制对同一个label生效。例如：
     *                             Label:winnerof
     *                                                           |-------------------------->(game1)
     *     (person)--------------------------->|-------------------------->(game2)
     *                                                           |-------------------------->(game3)
     *
     *     ONE2ONE：某verex中具有同样Label的edge，只能有最多一个incoming edge和最多一个outgoing edge。
     *
     * todo:默认的多样性设置为MULTI
     *
     * @param management
     */
    protected void createEdgeLabels(JanusGraphManagement management) {
        //父亲，注意这里的 Multiplicity常量，
        management.makeEdgeLabel("father").multiplicity(MANY2ONE).make();
        //母亲
        management.makeEdgeLabel("mother").multiplicity(MANY2ONE).make();
        //兄弟或平辈
        management.makeEdgeLabel("brother").make();
        //归属，及人属于哪个公司，公司中都有谁
        management.makeEdgeLabel("belong").make();
    }

    /**
     * 定义属性，及图中的属性属性，可以为边，点，进行引用。
     *
     * @param management
     */
    protected void createProperties(JanusGraphManagement management) {
        //名称
        management.makePropertyKey(SchemaProperties.NAME).dataType(String.class).make();
        //年龄
        management.makePropertyKey(SchemaProperties.AGE).dataType(Integer.class).make();
        //身份证号
        management.makePropertyKey(SchemaProperties.NO).dataType(String.class).make();
        //时间，可以为公司成立时间，人员的入职时间的这是一个笼统的定义
        management.makePropertyKey(SchemaProperties.TIME).dataType(String.class).make();
        //手机/电话
        management.makePropertyKey(SchemaProperties.PHONE).dataType(String.class).make();
        //关系型数据库的主键
        management.makePropertyKey(SchemaProperties.ID).dataType(String.class).make();
        //地方
        management.makePropertyKey(SchemaProperties.PLACE).dataType(Geoshape.class).make();
    }

    /**
     * 复合索引
     *
     * @param management
     */
    protected void createCompositeIndexes(JanusGraphManagement management) {
        /**创建顶点索引**/
        management.buildIndex("nameIndex", Vertex.class)
                .addKey(management.getPropertyKey("name"))
                .buildCompositeIndex();
        management.buildIndex("idIndex", Vertex.class)
                .addKey(management.getPropertyKey("id"))
                .buildCompositeIndex();
        management.buildIndex("noIndex", Vertex.class)
                .addKey(management.getPropertyKey("no"))
                .buildCompositeIndex();
        management.buildIndex("placeIndex", Vertex.class)
                .addKey(management.getPropertyKey("place"))
                .buildCompositeIndex();
    }


    protected boolean useMixedIndex = true;
    protected String mixedIndexConfigName = "fmbb";

    /**
     * 创建混合索引
     *
     * @param management
     */
    protected void createMixedIndexes(JanusGraphManagement management) {
        if (this.useMixedIndex) {
            management.buildIndex("noAge", Vertex.class)
                    .addKey(management.getPropertyKey("age"))
                    .addKey(management.getPropertyKey("no"))
                    .buildMixedIndex(this.mixedIndexConfigName);
            management.buildIndex("eReasonPlace", Edge.class)
                    .addKey(management.getPropertyKey("father"))
                    .addKey(management.getPropertyKey("mother"))
                    .addKey(management.getPropertyKey("brother"))
                    .addKey(management.getPropertyKey("belong"))
                    .buildMixedIndex(this.mixedIndexConfigName);
        }

    }
}
