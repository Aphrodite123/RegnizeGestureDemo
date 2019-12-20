package com.aphrodite.regnizegesturedemo.application;

import android.content.Context;

import com.aphrodite.framework.model.network.api.RetrofitInitial;
import com.aphrodite.framework.model.network.interceptor.BaseCommonParamInterceptor;
import com.aphrodite.framework.utils.ToastUtils;
import com.aphrodite.regnizegesturedemo.application.base.BaseApplication;

/**
 * Created by Aphrodite on 2018/7/26.
 */
public class FrameApplication extends BaseApplication {
    private static FrameApplication mIpenApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    protected void initSystem() {
        this.mIpenApplication = this;

        initToast();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static FrameApplication getApplication() {
        return mIpenApplication;
    }

    private void initToast() {
        ToastUtils.init(this);
    }

    public RetrofitInitial getRetrofitInit(boolean isJson, String baseUrl, BaseCommonParamInterceptor paramInterceptor) {
        RetrofitInitial retrofitInitial = new RetrofitInitial
                .Builder()
                .with(getApplication())
                .isJson(isJson)
                .baseUrl(baseUrl)
                .commonParamInterceptor(paramInterceptor)
                .build();
        return retrofitInitial;
    }

}
