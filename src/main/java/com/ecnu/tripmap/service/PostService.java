package com.ecnu.tripmap.service;

import com.ecnu.tripmap.model.vo.PostVo;
import com.ecnu.tripmap.result.Response;

public interface PostService {

    Response collectPost(Integer user_id, Integer post_id);

    Response likePost(Integer user_id, Integer post_id);

    PostVo findPostInfo(Integer post_id, Integer user_id);

}
