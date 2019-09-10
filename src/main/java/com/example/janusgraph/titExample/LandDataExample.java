package com.example.janusgraph.titExample;

import com.github.javafaker.Faker;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 *
 */
@Component
@Deprecated
public class LandDataExample {

    private final Faker faker;
    private final Date oneMonthAgo;

    /**
     * 初始化
     */
    public LandDataExample() {
        this.faker=new Faker();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        this.oneMonthAgo = cal.getTime();
    }


    /**
     * 循环添加
     * @param count
     * @return
     */
    public Vertex[] generateUsers(int count,JanusGraph janusGraph){
        Vertex[] users = new Vertex[count];

        for(int i=0; i < count; i++){
            users[i] = addUser("testUser" + i,janusGraph);
        }

        return users;
    }

    /**
     * 循环添加
     * @param user
     * @param count
     * @return
     */
    public Vertex[] generateStatusUpdates(Vertex user, int count,JanusGraph janusGraph){
        Vertex[] updates = new Vertex[count];
        for(int i=0; i < count; i++) {
            updates[i] = addStatusUpdatew(user, getContent(),janusGraph);
        }
        return updates;
    }

    public void commit(JanusGraph janusGraph){
        janusGraph.tx().commit();
    }

    /**
     * Commit the current transaction and close the graph.
     */
    private void close(JanusGraph janusGraph){
        commit(janusGraph);
        janusGraph.close();
    }

    /**
     * 添加1个顶点
     * @param userName
     * @return
     */
    private Vertex addUser(String userName,JanusGraph janusGraph){
        Vertex user = janusGraph.addVertex(InitSchemaExample.USER);
        user.property(InitSchemaExample.USER_NAME, userName);
        return user;
    }

    /**
     * 添加1个点
     * @param user
     * @param statusUpdateContent
     * @return
     */
    private Vertex addStatusUpdatew(Vertex user, String statusUpdateContent,JanusGraph janusGraph) {
        Vertex statusUpdate = janusGraph.addVertex(InitSchemaExample.STATUS_UPDATE);
        statusUpdate.property(InitSchemaExample.CONTENT, statusUpdateContent);
        user.addEdge(InitSchemaExample.POSTS, statusUpdate, InitSchemaExample.CREATED_AT, getTimestamp());
        return statusUpdate;
    }

    /**
     * 添加边
     * @param forUser
     * @param users
     * @param count
     * @return
     */
    public Vertex[] generateFollows(Vertex forUser, Vertex[] users, int count){
        Vertex[] followedUsers = new Vertex[count];

        for(int i = 0; i < count; i++) {
            followedUsers[i] = users[faker.number().numberBetween(0, users.length - 1)];
            Edge follows = forUser.addEdge(InitSchemaExample.FOLLOWS, followedUsers[i], InitSchemaExample.CREATED_AT, getTimestamp());
        }
        return followedUsers;
    }




    /**
     * Return a timestamp between 1 month ago and now.
     * @return
     */
    private Long getTimestamp(){
        return faker.date().between(oneMonthAgo, new Date()).getTime();
    }

    private String getContent(){
        return faker.chuckNorris().fact();
    }


}
