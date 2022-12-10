package com.ecnu.tripmap.service;

import com.ecnu.tripmap.result.Response;

public interface PlaceService {
    Response collectPlace(Integer user_id, Integer place_id);
    Response cancelCollectPlace(Integer user_id,Integer place_id);
}
