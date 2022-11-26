package com.ecnu.tripmap.mysql.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("post")
public class Post {
    @TableId
    private Integer postId;

    @TableField(value = "post_publish_time", fill = FieldFill.INSERT)
    private Date postPublishTime;

    @TableField("post_image_list")
    private String postImageList;

    @TableField("post_desc")
    private String postDesc;

    @TableField("post_collect_count")
    private Integer postCollectCount = 0;

    @TableField("post_like_count")
    private Integer postLikeCount = 0;

    @TableField("post_title")
    private String postTitle;
}
