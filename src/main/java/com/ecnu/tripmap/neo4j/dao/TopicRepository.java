package com.ecnu.tripmap.neo4j.dao;

import com.ecnu.tripmap.neo4j.node.PostNode;
import com.ecnu.tripmap.neo4j.node.TopicNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends Neo4jRepository<PostNode, Long> {

    @Query("MATCH (p:Post) -[BELONG]-> (t:Topic) WHERE p.post_id = $post_id RETURN (t)")
    List<TopicNode> findPostTopics(Integer post_id);

    @Query(" (p:Post), (t:Topic) WHERE p.post_id = $post_id AND t.topic_id = $topic_id CREATE (p) -[b:BELONG]-> (t) RETURN id(b)")
    Integer createBelongRelationship(Integer post_id, Integer topic_id);
}
