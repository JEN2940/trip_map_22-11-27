package com.ecnu.tripmap.model.vo;


import com.ecnu.tripmap.neo4j.node.TopicNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPv {
    private String postImageList;

    private String postDesc;

    private String postTitle;

    private String recommendPlace;

    private List<TopicNode> topicList;
}
