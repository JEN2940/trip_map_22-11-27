package com.ecnu.tripmap.service.Impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ecnu.tripmap.model.vo.PostPv;
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
import com.ecnu.tripmap.neo4j.node.PostNode;
import com.ecnu.tripmap.neo4j.node.TopicNode;
import com.ecnu.tripmap.neo4j.node.UserNode;
import com.ecnu.tripmap.result.Response;
import com.ecnu.tripmap.result.ResponseStatus;
import com.ecnu.tripmap.service.PostService;
import com.ecnu.tripmap.utils.CopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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
        copy.setRecommendPlaceId(place.getPlaceId());

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

    @Override
    public PostVo publish(PostPv postPv, Integer user_id){
        Post post = CopyUtil.copy(postPv,Post.class);
        post.setPostPublishTime(new Date());
        post.setPostCollectCount(0);
        post.setPostLikeCount(0);
        int insert = postMapper.insert(post);
        if (insert != 1){
            return null;
        }
//        post.setPostId(10000);

        PostNode postNode = CopyUtil.copy(postPv,PostNode.class);
        postRepository.save(postNode);
        Integer publishRelationShip = postRepository.createPublishRelationship(postNode.getPostId(),user_id);
        if (publishRelationShip == null)
            return null;

        List<TopicNode> topics = postPv.getTopicList();
        for (int i = 0; i < topics.size();i++){
            TopicNode topicNode = topics.get(i);
            Integer belong = topicRepository.createBelongRelationship(post.getPostId(),topicNode.getTopicId());
            if (belong == null)
                return null;
        }

        PlaceNode placeNode = placeRepository.findPlaceByAddress(postPv.getRecommendPlace());
        Integer recommend = placeRepository.createRecommendRelationship(post.getPostId(),placeNode.getPlaceId());
        if (recommend == null)
            return null;

        UserNode user = userRepository.fineUserById(user_id);

        PostVo postVo = CopyUtil.copy(post,PostVo.class);
        postVo.setUserId(user.getUserId());
        postVo.setUserNickName(user.getUserNickname());
        postVo.setUserAvatar(user.getUserAvatar());
        postVo.setLiked(false);
        postVo.setCollected(false);
        Integer isFollowed = userRepository.isFollowed(user.getUserId(),user.getUserId());
        if (isFollowed == null)
            postVo.setFollowed(false);
        else postVo.setFollowed(true);

        postVo.setRecommendPlace(placeNode.getPlaceAddress());
        postVo.setRecommendPlaceId(placeNode.getPlaceId());
        postVo.setTopicList(topics);

        return postVo;

    }
}
