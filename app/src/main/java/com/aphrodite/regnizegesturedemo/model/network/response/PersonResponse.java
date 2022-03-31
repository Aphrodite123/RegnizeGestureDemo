package com.aphrodite.regnizegesturedemo.model.network.response;

import com.aphrodite.framework.model.network.response.BaseResponse;
import com.aphrodite.regnizegesturedemo.model.bean.PersonBean;

import java.util.List;

/**
 * Created by Aphrodite on 2020/1/9.
 */
public class PersonResponse extends BaseResponse {
    private List<PersonBean> data;

    public List<PersonBean> getData() {
        return data;
    }

    public void setData(List<PersonBean> data) {
        this.data = data;
    }
}
