package com.ecnu.tripmap.controller;


import com.ecnu.tripmap.mysql.entity.User;
import com.ecnu.tripmap.neo4j.dao.PlaceRepository;
import com.ecnu.tripmap.result.Response;
import com.ecnu.tripmap.result.ResponseStatus;
import com.ecnu.tripmap.service.PlaceService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/address")
@Api(tags = "address")
public class PlaceController {
    @Resource
    private HttpSession session;

    @Resource
    private PlaceService placeService;

    @Resource
    private PlaceRepository placeRepository;

    //TODO
    @ApiOperation(value = "收藏一个地点，需要传入地点id")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = -6, message = "尚未登录"),
            @ApiResponse(code = -8, message = "地点不存在")
    })
    @PutMapping("{address_id}/collect")
    public Response collectPlace(@ApiParam(name = "place_id", value = "要收藏的地点id") @PathVariable Integer address_id) {
//        User user = (User) session.getAttribute("user");
//        if (user == null)
//            return Response.status(ResponseStatus.NOT_LOGIN);
//        return placeService.collectPlace(user.getUserId(), address_id);
        return placeService.collectPlace(1, address_id);
    }

}
