package com.example.janusgraph.titExample;

import com.example.janusgraph.config.JanusGraphConfig;
import com.example.janusgraph.config.GraphSourceConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.EdgeLabel;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.VertexLabel;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.core.schema.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 初始化一个schema，并设定顶点，边，属性
 */
@Log4j2
@Deprecated
public class InitSchemaExample {
    @Autowired
    GraphSourceConfig graphSource;
    /**
     * The index backend is identified by a key in the configuration; in our example we called it
     * <pre>search</pre>.
     * <br/>
     * Saving it in a static variable so we can reuse.
     */
    public static final String BACKING_INDEX = "search";

    public static final String USER = "user";
    public static final String USER_NAME = "marcelocf.janusgraph.userName";

    public static final String STATUS_UPDATE = "statusUpdate";
    public static final String CONTENT = "marcelocf.janusgraph.content";

    public static final String CREATED_AT = "marcelocf.janusgraph.createdAt";

    public static final String POSTS = "posts";
    public static final String FOLLOWS = "follows";


    /**
     * Create the user schema - vertex label, property and index.
     */
    private void createSchema(JanusGraphManagement mgt) {

        VertexLabel user = mgt.makeVertexLabel(USER).make();
        PropertyKey userName = mgt.makePropertyKey(USER_NAME).dataType(String.class).make();

        mgt.buildIndex(indexName(USER, USER_NAME), Vertex.class).
                addKey(userName, Mapping.STRING.asParameter()).
                indexOnly(user).
                buildMixedIndex(BACKING_INDEX);
    }

    /**
     * Create the statusUpdate schema - vertex label, property and full-text index.
     */
    private void createStatusUpdateSchema(JanusGraphManagement mgt) {
        log.info("Create {} schema", STATUS_UPDATE);
        VertexLabel statusUpdate = mgt.makeVertexLabel(STATUS_UPDATE).make();
        PropertyKey content = mgt.makePropertyKey(CONTENT).dataType(String.class).make();

        mgt.buildIndex(indexName(STATUS_UPDATE, CONTENT), Vertex.class).
                addKey(content, Mapping.TEXTSTRING.asParameter()).
                indexOnly(statusUpdate).
                buildMixedIndex(BACKING_INDEX);
    }

    /**
     * Create both <i>posts</i> and <i>follows</i> edges and related index.
     * <br/>
     * <p>
     * Because the property and index for both follows and posts is the same we create them at the same point here.
     */
    private void createEdgeSchema(JanusGraphManagement mgt) {
        log.info("create edges schema");
        EdgeLabel posts = mgt.makeEdgeLabel(POSTS).make();
        EdgeLabel follows = mgt.makeEdgeLabel(FOLLOWS).make();
        PropertyKey createdAt = mgt.makePropertyKey(CREATED_AT).dataType(Long.class).make();

        mgt.buildIndex(indexName(POSTS, CREATED_AT), Edge.class).
                addKey(createdAt).
                indexOnly(posts).
                buildMixedIndex(BACKING_INDEX);

        mgt.buildIndex(indexName(FOLLOWS, CREATED_AT), Edge.class).
                addKey(createdAt).
                indexOnly(follows).
                buildMixedIndex(BACKING_INDEX);
    }

    /**
     * We are using this to create predictable names for our indexes. You could name it however you want, but doing
     * like this will make it possible to reindex stuff in the future... if we want (we do want, btw)
     *
     * @param label       edge or vertex label
     * @param propertyKey property key
     * @return
     */
    public static String indexName(String label, String propertyKey) {
        return label + ":by:" + propertyKey;
    }


    /**
     * Commit the current transaction and close the graph.
     */
    private void close(JanusGraphManagement mgt, JanusGraph graph) {
        mgt.commit();
        graph.tx().commit();
        graph.close();
    }


    public static void main(String[] args) {
        JanusGraphConfig getGraphAndMgt = new JanusGraphConfig();
        JanusGraphManagement mgt = getGraphAndMgt.mgt;
        JanusGraph graph = getGraphAndMgt.graph;
        InitSchemaExample jgExample = new InitSchemaExample();
        jgExample.createSchema(mgt);
        jgExample.createStatusUpdateSchema(mgt);
        jgExample.createEdgeSchema(mgt);
        jgExample.close(mgt, graph);
    }
}
