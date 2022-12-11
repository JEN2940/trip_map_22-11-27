package com.ecnu.tripmap.service.Impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ecnu.tripmap.model.vo.PlaceBiref;
import com.ecnu.tripmap.mysql.entity.Place;
import com.ecnu.tripmap.mysql.entity.Post;
import com.ecnu.tripmap.mysql.entity.User;
import com.ecnu.tripmap.mysql.mapper.PlaceMapper;
import com.ecnu.tripmap.mysql.mapper.UserMapper;
import com.ecnu.tripmap.neo4j.dao.PlaceRepository;
import com.ecnu.tripmap.result.Response;
import com.ecnu.tripmap.result.ResponseStatus;
import com.ecnu.tripmap.service.PlaceService;
import com.ecnu.tripmap.utils.CopyUtil;
import com.ecnu.tripmap.utils.SimilarityUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class PlaceServiceImpl implements PlaceService {

    @Resource
    private PlaceRepository placeRepository;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PlaceMapper placeMapper;

    @Resource SimilarityUtil similarityUtil;

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

    public List<PlaceBiref> recommendPlaces(Integer user_id){
        List<Integer> placesId = similarityUtil.recommend(user_id);
        List<PlaceBiref> places = new ArrayList<>();
        for (int i = 0;i < 10; i++){
            Integer placeID = placesId.get(i);
            Place one = new LambdaQueryChainWrapper<>(placeMapper)
                    .eq(Place::getPlaceId, placeID)
                    .one();
            PlaceBiref copy = CopyUtil.copy(one,PlaceBiref.class);
            places.add(copy);
        }
        return places;
    }
}
