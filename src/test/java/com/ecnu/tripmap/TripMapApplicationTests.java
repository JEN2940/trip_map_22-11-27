package com.ecnu.tripmap;

import com.ecnu.tripmap.model.vo.PostBrief;
import com.ecnu.tripmap.model.vo.PostPv;
import com.ecnu.tripmap.model.vo.PostVo;
import com.ecnu.tripmap.mysql.entity.Post;
import com.ecnu.tripmap.neo4j.dao.PlaceRepository;
import com.ecnu.tripmap.neo4j.dao.PostRepository;
import com.ecnu.tripmap.neo4j.dao.UserRepository;
import com.ecnu.tripmap.service.Impl.PostServiceImpl;
import com.ecnu.tripmap.service.Impl.UserServiceImpl;
import com.ecnu.tripmap.utils.CopyUtil;
import com.ecnu.tripmap.utils.SimilarityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class TripMapApplicationTests {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SimilarityUtil similarityUtil;

    @Test
    void testSave() {
//int i = 0;
//int greater = 0;
//int equal = 0;
//int le = 0;
//        for (; i < 1000; i++) {
//            UserVo userInfo = userService.findUserInfo(i + 1);
//            List<PlaceNode> userStoredPlace = placeRepository.findUserStoredPlace(i + 1);
//            if (userInfo.getUserCollectLocationCount() > userStoredPlace.size()) {
//               le++;
//            }else if(userInfo.getUserCollectLocationCount() == userStoredPlace.size()){
//                equal++;
//            }else greater++;
//
//        }
//        System.out.println(le);
//        System.out.println(equal);
//        System.out.println(greater);
        Integer followRelationship = userRepository.createFollowRelationship(1, 1111);
        System.out.println(followRelationship);
    }


    @Test
    void copyUtilTest() {
        Post post = new Post(1, new Date(), "ilist", "desc", 0, 0, "title");
        PostBrief copy = CopyUtil.copy(post, PostBrief.class);
        System.out.println(copy);
    }

    @Test
    void password() {
        String pass = "123456";
        String encode = passwordEncoder.encode(pass);
        System.out.println(encode);
    }

    @Test
    void t(){
        List<Integer> recommend = similarityUtil.recommend(3);
        for (Integer integer : recommend) {
            System.out.println(integer);
        }
    }

    @Test
    void publishTest(){
        PostPv postPv = new PostPv();
        postPv.setPostDesc("123");
        postPv.setPostTitle("123");
        postPv.setPostImageList("123");
        postPv.setRecommendPlace("上海植物园");
        List<String> topics = new ArrayList<>();
        topics.add("上海");
        topics.add("上海旅游攻略");
        postPv.setTopicList(topics);
        PostVo postVo = postService.publish(postPv,1);
        System.out.println(postVo);
    }

    @Test
    void home_page(){
        List<PostBrief> posts = postService.postList(1);
        System.out.println(posts);
    }

}
