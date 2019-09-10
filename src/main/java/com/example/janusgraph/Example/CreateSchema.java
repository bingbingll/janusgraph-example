package com.example.janusgraph.Example;

import lombok.extern.log4j.Log4j2;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.Cardinality;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.springframework.stereotype.Service;

import static org.janusgraph.core.Multiplicity.MANY2ONE;

/**
 * @author 李兵
 * @version V1.0
 * @description TODO: 定义schema,这里的业务模型为，一个人，一个公司，
 * @date 2019/9/5 19:21
 */
@Service
@Log4j2
public class CreateSchema {


    protected boolean useMixedIndex = true;
    protected String mixedIndexConfigName = "search";

    /**
     * 定义schema图的顶点（我喜欢叫这个为所属类型）； 人：person，公司：company，地点：localhost 三个顶点
     *
     * @param management
     */
    public void createVertexLabels(JanusGraphManagement management) {
        management.makeVertexLabel(SchemaVertex.PERSON).make();
        management.makeVertexLabel(SchemaVertex.LOCALHOST).make();
        management.makeVertexLabel(SchemaVertex.COMPANY).make();
    }

    /**
     * 定义属性，及图中的属性属性，可以为边，点，进行引用。
     * 使用基数（基数）来定义与任何给定顶点上的键相关联的值的允许基数。
     * SINGLE：对于此类密钥，每个元素最多允许一个值。换句话说，键→值映射对于图中的所有元素都是唯一的。
     * 属性键birthDate是具有SINGLE基数的示例，因为每个人只有一个出生日期。
     * LIST：允许每个元素的任意数量的值用于此类键。换句话说，密钥与允许重复值的值列表相关联。
     * 假设我们将传感器建模为图形中的顶点，则属性键sensorReading是LIST基数的示例，允许记录大量（可能重复的）传感器读数。
     * SET：允许多个值，但每个元素没有重复值用于此类键。换句话说，密钥与一组值相关联。
     * 如果我们想要捕获个人的所有姓名（包括昵称，婚前姓名等），则属性键名称具有SET基数。
     * <p>
     * 默认基数设置为SINGLE。请注意，边和属性上使用的属性键具有基数SINGLE。不支持为边或属性上的单个键附加多个值。
     *
     * @param management
     */
    public void createProperties(JanusGraphManagement management) {
        //名称,可以由多个名称
        management.makePropertyKey(SchemaProperties.NAME).dataType(String.class).cardinality(Cardinality.SET).make();
        //年龄,
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
        management.makePropertyKey(SchemaProperties.ADDRESS).dataType(String.class).make();
        //公司坐标
        management.makePropertyKey(SchemaProperties.PLACE).dataType(Geoshape.class).make();
    }

    /**
     * 定义schema图的边；
     * <p>
     * MULTI: 允许任何顶点对之间的同一标签的多个边。换句话说，该图是关于这种边缘标签的多图。边缘多重性没有约束。
     * SIMPLE:在任何一对顶点之间最多允许此类标签的一个边缘。换句话说，该图是关于标签的简单图。确保边缘对于给定标签和顶点对是唯一的
     * MANY2ONE:在图形中的任何顶点上最多允许此标签的一个输出，但不对入射边缘施加约束。
     * 边缘标签母亲是MANY2ONE多样性的一个例子，因为每个人最多只有一个母亲，但母亲可以有多个孩子。
     * ONE2MANY:在图形中的任何顶点上最多允许此类标签的一个输入边缘，但不对输出边缘施加约束。
     * 边缘标签winnerOf是具有ONE2MANY多样性的示例，因为每个比赛最多只能赢得一个人，但是一个人可以赢得多个比赛.
     * ONE2ONE:在图中的任何顶点上最多允许此标签的一个输入边和一个输出边。边缘标签结婚是一个具有ONE2ONE多样性的例子，因为一个人与另一个人结婚。
     * todo:默认设置为 MULTI。
     * todo:具体参考这位同学写的链接：https://www.cnblogs.com/jiyuqi/p/7127178.html?utm_source=itdadao&utm_medium=referral
     *
     * @param management
     */
    public void createEdgeLabels(JanusGraphManagement management) {
        //父亲，注意这里的 Multiplicity常量，
        management.makeEdgeLabel("father").multiplicity(MANY2ONE).make();
        //母亲
        management.makeEdgeLabel("mother").multiplicity(MANY2ONE).make();
        //兄弟或平辈
        management.makeEdgeLabel("brother").make();
        //归属，人属于哪个公司，公司中都有谁 公司地点在哪里
        management.makeEdgeLabel("belong").make();
    }

    /**
     * 复合索引
     *
     * @param management
     */
    public void createCompositeIndexes(JanusGraphManagement management) {
        /**创建顶点 单个索引**/
        management.buildIndex("byNameIndex", Vertex.class)
                .addKey(management.getPropertyKey("name"))
                .buildCompositeIndex();
        management.buildIndex("byAgeIndex", Vertex.class)
                .addKey(management.getPropertyKey("age"))
                .buildCompositeIndex();
        management.buildIndex("byIdIndex", Vertex.class)
                .addKey(management.getPropertyKey("id"))
                .buildCompositeIndex();
        management.buildIndex("byNoIndex", Vertex.class)
                .addKey(management.getPropertyKey("no"))
                .buildCompositeIndex();
        management.buildIndex("byAddressIndex", Vertex.class)
                .addKey(management.getPropertyKey("address"))
                .buildCompositeIndex();
        management.buildIndex("byPlaceIndex", Vertex.class)
                .addKey(management.getPropertyKey("place"))
                .buildCompositeIndex();
    }

    /**
     * 创建混合索引
     *
     * @param management
     */
    public void createMixedIndexes(JanusGraphManagement management) {
        if (this.useMixedIndex) {
            management.buildIndex("nameAndAgeAndNoAndId", Vertex.class)
                    .addKey(management.getPropertyKey("name"))
                    .addKey(management.getPropertyKey("age"))
                    .addKey(management.getPropertyKey("no"))
                    .addKey(management.getPropertyKey("id"))
                    .buildMixedIndex(this.mixedIndexConfigName);

            management.buildIndex("byNANIP", Edge.class)
                    .addKey(management.getPropertyKey("name"))
                    .addKey(management.getPropertyKey("age"))
                    .addKey(management.getPropertyKey("no"))
                    .addKey(management.getPropertyKey("id"))
                    .addKey(management.getPropertyKey("place"))
                    .buildMixedIndex(this.mixedIndexConfigName);
        }

    }
}
