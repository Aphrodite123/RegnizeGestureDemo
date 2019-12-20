package com.aphrodite.regnizegesturedemo.model.bean;

import android.support.annotation.NonNull;

/**
 * Created by Aphrodite on 2019/12/6.
 */
public class GestureTypesBean implements Comparable<GestureTypesBean> {
    private int id;
    private float percent;

    public GestureTypesBean(int id, float percent) {
        this.id = id;
        this.percent = percent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public int compareTo(@NonNull GestureTypesBean o) {
        return (int) (o.percent * 100 - this.percent * 100);
    }
}
