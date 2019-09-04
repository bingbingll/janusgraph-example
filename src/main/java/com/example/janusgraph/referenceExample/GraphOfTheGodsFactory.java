package com.example.janusgraph.referenceExample;

import com.google.common.base.Preconditions;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.*;
import org.janusgraph.core.attribute.Geoshape;
import org.janusgraph.core.schema.ConsistencyModifier;
import org.janusgraph.core.schema.JanusGraphIndex;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.janusgraph.graphdb.database.StandardJanusGraph;

public class GraphOfTheGodsFactory {
    public static final String INDEX_NAME = "search";
    private static final String ERR_NO_INDEXING_BACKEND =
            "The indexing backend with name \"%s\" is not defined. Specify an existing indexing backend or " +
                    "use GraphOfTheGodsFactory.loadWithoutMixedIndex(graph,true) to load without the use of an " +
                    "indexing backend.";

    public static JanusGraph create(final String directory) {
        JanusGraphFactory.Builder config = JanusGraphFactory.build();
        config.set("storage.backend", "berkeleyje");
        config.set("storage.directory", directory);
        config.set("index." + INDEX_NAME + ".backend", "elasticsearch");

        JanusGraph graph = config.open();
        GraphOfTheGodsFactory.load(graph);
        return graph;
    }

    public static void loadWithoutMixedIndex(final JanusGraph graph, boolean uniqueNameCompositeIndex) {
        load(graph, null, uniqueNameCompositeIndex);
    }

    public static void load(final JanusGraph graph) {
        load(graph, INDEX_NAME, true);
    }

    private static boolean mixedIndexNullOrExists(StandardJanusGraph graph, String indexName) {
        return indexName == null || graph.getIndexSerializer().containsIndex(indexName);
    }

    public static void load(final JanusGraph graph, String mixedIndexName, boolean uniqueNameCompositeIndex) {
        if (graph instanceof StandardJanusGraph) {
            Preconditions.checkState(mixedIndexNullOrExists((StandardJanusGraph)graph, mixedIndexName),
                    ERR_NO_INDEXING_BACKEND, mixedIndexName);
        }

        //Create Schema
        JanusGraphManagement management = graph.openManagement();

        final PropertyKey name = management.makePropertyKey("name").dataType(String.class).make();
        JanusGraphManagement.IndexBuilder nameIndexBuilder = management.buildIndex("name", Vertex.class).addKey(name);
        if (uniqueNameCompositeIndex)
            nameIndexBuilder.unique();
        JanusGraphIndex nameIndex = nameIndexBuilder.buildCompositeIndex();
        management.setConsistency(nameIndex, ConsistencyModifier.LOCK);

        final PropertyKey age = management.makePropertyKey("age").dataType(Integer.class).make();
        if (null != mixedIndexName)
            management.buildIndex("vertices", Vertex.class).addKey(age).buildMixedIndex(mixedIndexName);
        //时间
        final PropertyKey time = management.makePropertyKey("time").dataType(Integer.class).make();
        //原因
        final PropertyKey reason = management.makePropertyKey("reason").dataType(String.class).make();
        //地点，地方，位置
        final PropertyKey place = management.makePropertyKey("place").dataType(Geoshape.class).make();
        if (null != mixedIndexName)
            management.buildIndex("edges", Edge.class).addKey(reason).addKey(place).buildMixedIndex(mixedIndexName);

        //父亲
        management.makeEdgeLabel("father").multiplicity(Multiplicity.MANY2ONE).make();
        //母亲
        management.makeEdgeLabel("mother").multiplicity(Multiplicity.MANY2ONE).make();
        //作战
        EdgeLabel battled = management.makeEdgeLabel("battled").signature(time).make();
        management.buildEdgeIndex(battled, "battlesByTime", Direction.BOTH, Order.desc, time);
        //生活生命
        management.makeEdgeLabel("lives").signature(reason).make();
        // 宠物
        management.makeEdgeLabel("pet").make();
        // 兄弟
        management.makeEdgeLabel("brother").make();


        management.makeVertexLabel("titan").make(); //
        management.makeVertexLabel("location").make(); //地点
        management.makeVertexLabel("god").make(); //神
        management.makeVertexLabel("demigod").make(); //半神
        management.makeVertexLabel("human").make(); //人
        management.makeVertexLabel("monster").make(); // 怪物

        management.commit();

        JanusGraphTransaction tx = graph.newTransaction();
        // vertices

        Vertex saturn = tx.addVertex(T.label, "titan", "name", "saturn", "age", 10000);
        Vertex sky = tx.addVertex(T.label, "location", "name", "sky");
        Vertex sea = tx.addVertex(T.label, "location", "name", "sea");
        Vertex jupiter = tx.addVertex(T.label, "god", "name", "jupiter", "age", 5000);
        Vertex neptune = tx.addVertex(T.label, "god", "name", "neptune", "age", 4500);
        Vertex hercules = tx.addVertex(T.label, "demigod", "name", "hercules", "age", 30);
        Vertex alcmene = tx.addVertex(T.label, "human", "name", "alcmene", "age", 45);
        Vertex pluto = tx.addVertex(T.label, "god", "name", "pluto", "age", 4000);
        Vertex nemean = tx.addVertex(T.label, "monster", "name", "nemean");
        Vertex hydra = tx.addVertex(T.label, "monster", "name", "hydra");
        Vertex cerberus = tx.addVertex(T.label, "monster", "name", "cerberus");
        Vertex tartarus = tx.addVertex(T.label, "location", "name", "tartarus");

        // edges

        jupiter.addEdge("father", saturn);
        jupiter.addEdge("lives", sky, "reason", "loves fresh breezes");
        jupiter.addEdge("brother", neptune);
        jupiter.addEdge("brother", pluto);

        neptune.addEdge("lives", sea).property("reason", "loves waves");
        neptune.addEdge("brother", jupiter);
        neptune.addEdge("brother", pluto);

        hercules.addEdge("father", jupiter);
        hercules.addEdge("mother", alcmene);
        hercules.addEdge("battled", nemean, "time", 1, "place", Geoshape.point(38.1f, 23.7f));
        hercules.addEdge("battled", hydra, "time", 2, "place", Geoshape.point(37.7f, 23.9f));
        hercules.addEdge("battled", cerberus, "time", 12, "place", Geoshape.point(39f, 22f));

        pluto.addEdge("brother", jupiter);
        pluto.addEdge("brother", neptune);
        pluto.addEdge("lives", tartarus, "reason", "no fear of death");
        pluto.addEdge("pet", cerberus);

        cerberus.addEdge("lives", tartarus);

        // commit the transaction to disk
        tx.commit();
    }

    /**
     * Calls {@link JanusGraphFactory#open(String)}, passing the JanusGraph configuration file path
     * which must be the sole element in the {@code args} array, then calls
     * {@link #load(JanusGraph)} on the opened graph,
     * then calls {@link JanusGraph#close()}
     * and returns.
     * <p>
     * This method may call {@link System#exit(int)} if it encounters an error, such as
     * failure to parse its arguments.  Only use this method when executing main from
     * a command line.  Use one of the other methods on this class ({@link #create(String)}
     * or {@link #load(JanusGraph)}) when calling from
     * an enclosing application.
     *
     * @param args a singleton array containing a path to a JanusGraph config properties file
     */
    public static void main(String args[]) {
        if (null == args || 1 != args.length) {
            System.err.println("Usage: GraphOfTheGodsFactory <janusgraph-config-file>");
            System.exit(1);
        }

        JanusGraph g = JanusGraphFactory.open(args[0]);
        load(g);
        g.close();
    }
}
