package com.ecnu.tripmap;

import com.ecnu.tripmap.model.vo.PostBrief;
import com.ecnu.tripmap.mysql.entity.Post;
import com.ecnu.tripmap.neo4j.dao.PlaceRepository;
import com.ecnu.tripmap.neo4j.dao.PostRepository;
import com.ecnu.tripmap.neo4j.dao.UserRepository;
import com.ecnu.tripmap.service.Impl.UserServiceImpl;
import com.ecnu.tripmap.utils.CopyUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootTest
class TripMapApplicationTests {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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

}
