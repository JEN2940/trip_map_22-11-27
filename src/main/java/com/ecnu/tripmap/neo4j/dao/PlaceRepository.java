package com.ecnu.tripmap.neo4j.dao;

import com.ecnu.tripmap.model.vo.PlaceVo;
import com.ecnu.tripmap.neo4j.node.PlaceNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends Neo4jRepository<PlaceNode, Long> {
    @Query("MATCH (u:User) -[:STORE]-> (p:Place) WHERE u.user_id = $user_id RETURN (p)")
    List<PlaceNode> findUserStoredPlace(Integer user_id);

    @Query("MATCH (po:Post) -[:SUGGEST]-> (pa:Place) WHERE po.post_id = $post_id RETURN (pa)")
    PlaceNode findRecommendPlace(Integer post_id);

    @Query("MATCH (u:User) , (p:Place) WHERE u.user_id = $user_id AND p.place_id = $place_id CREATE (u) -[s:STORE]-> (p) RETURN id(s)")
    Integer createStoreRelationship(Integer user_id, Integer place_id);

    @Query("MATCH (pa:Place) WHERE pa.place_address = $place_address RETURN (pa)")
    PlaceNode findPlaceByAddress(String place_address);

    @Query("MATCH (po:Post) , (pa:Place) WHERE po.post_id = $post_id AND pa.place_id = $place_id CREATE (po) -[s:SUGGEST]-> (pa) RETURN id(s)")
    Integer createRecommendRelationship(Integer post_id,Integer place_id);
}
