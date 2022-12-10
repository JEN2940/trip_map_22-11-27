package com.ecnu.tripmap.controller;

import com.ecnu.tripmap.model.vo.PostBrief;
import com.ecnu.tripmap.model.vo.PostPv;
import com.ecnu.tripmap.model.vo.PostVo;
import com.ecnu.tripmap.model.vo.UserVo;
import com.ecnu.tripmap.mysql.entity.User;
import com.ecnu.tripmap.result.Response;
import com.ecnu.tripmap.result.ResponseStatus;
import com.ecnu.tripmap.service.PostService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/post")
@Api(tags = "Post")
public class PostController {

    @Resource
    private HttpSession session;

    @Resource
    private PostService postService;

    @ApiOperation(value = "首页帖子，没有要传入的参数")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -10, message = "笔记获取失败，请联系工作人员")
    })
    @PostMapping("/list")
    public Response publish() {
        UserVo user = (UserVo) session.getAttribute("user");
//        PostVo postInfo = postService.publish(postPv,user.getUserId());
//        PostVo postInfo = postService.publish(postPv,1);
        List<PostBrief> posts = postService.postList(user.getUserId());
        if (posts == null)
            return  Response.status(ResponseStatus.PUBLISH_FAIL);
        return Response.success(posts);
    }

    @ApiOperation(value = "新增帖子，需要传入帖子")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -9, message = "笔记发布失败，请联系工作人员")
    })
    @PostMapping("/publish")
    public Response publish(@RequestBody @Valid @ApiParam(name = "postVo", value = "必须传入的有图片集，标题，帖子详情，话题列表Array，地点") PostPv postPv) {
        UserVo user = (UserVo) session.getAttribute("user");
        PostVo postInfo = postService.publish(postPv,user.getUserId());
//        PostVo postInfo = postService.publish(postPv,1);
        if (postInfo == null)
            return  Response.status(ResponseStatus.PUBLISH_FAIL);
        return Response.success(postInfo);
    }

    // TODO
    @ApiOperation(value = "查询帖子详细信息，需要传入笔记id")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -5, message = "帖子不存在")
    })
    @GetMapping("/{post_id}")
    public Response findPostInfo(@ApiParam(name = "post_id", value = "路径参数，要查找的帖子id") @PathVariable(name = "post_id") Integer post_id) {
//        UserVo user = (UserVo) session.getAttribute("user");
//        PostVo postInfo = postService.findPostInfo(post_id, user.getUserId());
        PostVo postInfo = postService.findPostInfo(post_id, 1);
        if (postInfo == null)
            return Response.status(ResponseStatus.POST_NOT_EXIST);
        return Response.success(postInfo);
    }

    // TODO
    @ApiOperation(value = "收藏一个笔记，需要传入笔记id")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -6, message = "尚未登录"),
            @ApiResponse(code = -7, message = "笔记不存在")
    })
    @PutMapping("{post_id}/collect")
    public Response collectPost(@ApiParam(name = "post_id", value = "要收藏的笔记id") @PathVariable Integer post_id) {
//        UserVo user = (UserVo) session.getAttribute("user");
//        return postService.collectPost(user.getUserId(), post_id);
        return postService.collectPost(1, post_id);
    }

    @ApiOperation(value = "取消收藏一个笔记，需要传入笔记id")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -6, message = "尚未登录"),
            @ApiResponse(code = -7, message = "笔记不存在")
    })
    @PutMapping("/cancel/{post_id}/collect")
    public Response cancelCollectPost(@ApiParam(name = "post_id",value = "要取消收藏的帖子id")@PathVariable Integer post_id){
        UserVo user=(UserVo) session.getAttribute("user");
        return postService.cancelCollectPost(user.getUserId(), post_id);
    }


    @ApiOperation(value = "点赞一个笔记，需要传入笔记id")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -6, message = "尚未登录"),
            @ApiResponse(code = -7, message = "笔记不存在")
    })
    @PutMapping("{post_id}/like")
    public Response likePost(@ApiParam(name = "post_id", value = "要点赞的笔记id") @PathVariable Integer post_id) {
        UserVo user = (UserVo) session.getAttribute("user");
        return postService.likePost(user.getUserId(), post_id);
    }


    @ApiOperation(value = "取消点赞一个笔记，需要传入笔记id")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -6, message = "尚未登录"),
            @ApiResponse(code = -7, message = "笔记不存在")
    })
    @PutMapping("/cancel/{post_id}/like")
    public Response cancelLikePost(@ApiParam(name="post_id",value = "要取消店在哪的笔记id") @PathVariable Integer post_id){
        UserVo user=(UserVo) session.getAttribute("user");
        return postService.cancelLikePost(user.getUserId(),post_id);
    }

}
