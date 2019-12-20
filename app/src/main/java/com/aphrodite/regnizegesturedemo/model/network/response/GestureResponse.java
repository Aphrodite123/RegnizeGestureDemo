package com.aphrodite.regnizegesturedemo.model.network.response;

import com.aphrodite.framework.model.network.response.BaseResponse;
import com.aphrodite.regnizegesturedemo.model.bean.HandBean;

import java.util.List;

/**
 * Created by Aphrodite on 2019/12/6.
 */
public class GestureResponse extends BaseResponse {
    private String request_id;
    private int time_used;
    private List<HandBean> hands;
    private String image_id;

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<HandBean> getHands() {
        return hands;
    }

    public void setHands(List<HandBean> hands) {
        this.hands = hands;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
