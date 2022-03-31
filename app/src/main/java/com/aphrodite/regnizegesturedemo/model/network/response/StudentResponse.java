package com.aphrodite.regnizegesturedemo.model.network.response;

import com.aphrodite.framework.model.network.response.BaseResponse;
import com.aphrodite.regnizegesturedemo.model.bean.StudentBean;

import java.util.List;

/**
 * Created by Aphrodite on 2020/1/10.
 */
public class StudentResponse extends BaseResponse {
    private List<StudentBean> data;

    public List<StudentBean> getData() {
        return data;
    }

    public void setData(List<StudentBean> data) {
        this.data = data;
    }
}
