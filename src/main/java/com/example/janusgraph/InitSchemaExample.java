package com.example.janusgraph;

import com.example.janusgraph.config.GetGraphAndMgt;
import com.example.janusgraph.config.GraphTraversalSourceConfig;
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
public class InitSchemaExample {
    @Autowired
    GraphTraversalSourceConfig graphSource;
    /**
     * The index backend is identified by a key in the configuration; in our example we called it
     * <pre>search</pre>.
     * <br/>
     * Saving it in a static variable so we can reuse.
     */
    private static final String BACKING_INDEX = "search";

    private static final String USER = "user";
    private static final String USER_NAME = "marcelocf.janusgraph.userName";

    private static final String STATUS_UPDATE = "statusUpdate";
    private static final String CONTENT = "marcelocf.janusgraph.content";

    private static final String CREATED_AT = "marcelocf.janusgraph.createdAt";

    private static final String POSTS = "posts";
    private static final String FOLLOWS = "follows";


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
        GetGraphAndMgt getGraphAndMgt = new GetGraphAndMgt();
        JanusGraphManagement mgt = getGraphAndMgt.mgt;
        JanusGraph graph = getGraphAndMgt.graph;
        InitSchemaExample jgExample = new InitSchemaExample();
        jgExample.createSchema(mgt);
        jgExample.createStatusUpdateSchema(mgt);
        jgExample.createEdgeSchema(mgt);
        jgExample.close(mgt, graph);
    }
}
