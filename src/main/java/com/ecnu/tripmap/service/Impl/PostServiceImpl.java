package com.ecnu.tripmap.service.Impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ecnu.tripmap.model.vo.PostVo;
import com.ecnu.tripmap.model.vo.UserVo;
import com.ecnu.tripmap.mysql.entity.Post;
import com.ecnu.tripmap.mysql.entity.User;
import com.ecnu.tripmap.mysql.mapper.PostMapper;
import com.ecnu.tripmap.mysql.mapper.UserMapper;
import com.ecnu.tripmap.neo4j.dao.PlaceRepository;
import com.ecnu.tripmap.neo4j.dao.PostRepository;
import com.ecnu.tripmap.neo4j.dao.TopicRepository;
import com.ecnu.tripmap.neo4j.dao.UserRepository;
import com.ecnu.tripmap.neo4j.node.PlaceNode;
import com.ecnu.tripmap.neo4j.node.TopicNode;
import com.ecnu.tripmap.neo4j.node.UserNode;
import com.ecnu.tripmap.result.Response;
import com.ecnu.tripmap.result.ResponseStatus;
import com.ecnu.tripmap.service.PostService;
import com.ecnu.tripmap.utils.CopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostRepository postRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private PlaceRepository placeRepository;

    @Resource
    private TopicRepository topicRepository;

    @Resource
    private UserMapper userMapper;

    @Override
    public Response collectPost(Integer user_id, Integer post_id) {
        Integer collectRelationship = postRepository.createCollectRelationship(user_id, post_id);
        if (collectRelationship == null)
            return Response.status(ResponseStatus.POST_NOT_EXIST);
        Post post = new LambdaQueryChainWrapper<>(postMapper)
                .eq(Post::getPostId, post_id)
                .one();
        post.setPostCollectCount(post.getPostCollectCount() + 1);
        postMapper.updateById(post);
        User user = new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getUserId, user_id)
                .one();
        user.setUserCollectPostCount(user.getUserCollectPostCount() + 1);
        userMapper.updateById(user);
        return Response.success(collectRelationship);
    }

    @Override
    public Response likePost(Integer user_id, Integer post_id) {
        Integer likeRelationship = postRepository.createLikeRelationship(user_id,post_id);
        if (likeRelationship == null)
            return Response.status(ResponseStatus.POST_NOT_EXIST);
        Post post = new LambdaQueryChainWrapper<>(postMapper)
                .eq(Post::getPostId, post_id)
                .one();
        post.setPostLikeCount(post.getPostLikeCount() + 1);
        postMapper.updateById(post);
        return Response.success(likeRelationship);
    }

    @Override
    public PostVo findPostInfo(Integer post_id, Integer user_id){
        Post post = new LambdaQueryChainWrapper<>(postMapper)
                .eq(Post::getPostId,post_id)
                .one();
        if (post == null)
            return null;
        PostVo copy = CopyUtil.copy(post,PostVo.class);

        UserNode publisher = userRepository.findPublisher(post_id);
        copy.setUserId(publisher.getUserId());
        copy.setUserAvatar(publisher.getUserAvatar());
        copy.setUserNickName(publisher.getUserNickname());

        PlaceNode place = placeRepository.findRecommendPlace(post_id);
        copy.setRecommendPlace(place.getPlaceAddress());

        //是否关注，点赞，收藏
        Integer isLiked = postRepository.isLiked(user_id, post_id);
        if (isLiked == null)
            copy.setLiked(false);
        else copy.setLiked(true);

        Integer isCollected = postRepository.isCollected(user_id,post_id);
        if (isCollected == null)
            copy.setCollected(false);
        else copy.setCollected(true);

        Integer isFollowed = userRepository.isFollowed(user_id,publisher.getUserId());
        if (isFollowed == null)
            copy.setFollowed(false);
        else copy.setFollowed(true);

        //话题列表
        List<TopicNode> topics = topicRepository.findPostTopics(post_id);
        copy.setTopicList(topics);


        return copy;
    }
}
