package com.ecnu.tripmap.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.tripmap.mysql.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlaceMapper extends BaseMapper<Post>{

}