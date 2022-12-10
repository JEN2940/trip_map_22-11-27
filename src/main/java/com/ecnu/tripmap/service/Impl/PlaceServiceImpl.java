package com.ecnu.tripmap.service.Impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ecnu.tripmap.mysql.entity.User;
import com.ecnu.tripmap.mysql.mapper.UserMapper;
import com.ecnu.tripmap.neo4j.dao.PlaceRepository;
import com.ecnu.tripmap.result.Response;
import com.ecnu.tripmap.result.ResponseStatus;
import com.ecnu.tripmap.service.PlaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class PlaceServiceImpl implements PlaceService {

    @Resource
    private PlaceRepository placeRepository;

    @Resource
    private UserMapper userMapper;

    public Response collectPlace(Integer user_id, Integer place_id){
        Integer collectRelationship = placeRepository.createStoreRelationship(user_id,place_id);
        if (collectRelationship == null)
            return Response.status(ResponseStatus.PLACE_NOT_EXIST);
        User user = new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getUserId, user_id)
                .one();
        user.setUserCollectLocationCount(user.getUserCollectLocationCount() + 1);
        userMapper.updateById(user);
        return Response.success(collectRelationship);
    }

    public Response cancelCollectPlace(Integer user_id,Integer place_id){
        //删除收藏关系
        placeRepository.cancelStoreRelationship(user_id,place_id);
        User user = new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getUserId, user_id)
                .one();
        user.setUserCollectLocationCount(user.getUserCollectLocationCount() - 1);
        userMapper.updateById(user);
        return Response.success();

    }
}
