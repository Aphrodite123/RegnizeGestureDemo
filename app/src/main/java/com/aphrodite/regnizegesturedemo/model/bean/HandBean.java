package com.aphrodite.regnizegesturedemo.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aphrodite on 2019/12/6.
 */
public class HandBean implements Parcelable {
    private RectangleBean hand_rectangle;
    private GestureDetailBean gesture;

    public HandBean() {
    }

    protected HandBean(Parcel in) {
        hand_rectangle = in.readParcelable(RectangleBean.class.getClassLoader());
        gesture = in.readParcelable(GestureDetailBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(hand_rectangle, flags);
        dest.writeParcelable(gesture, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HandBean> CREATOR = new Creator<HandBean>() {
        @Override
        public HandBean createFromParcel(Parcel in) {
            return new HandBean(in);
        }

        @Override
        public HandBean[] newArray(int size) {
            return new HandBean[size];
        }
    };

    public RectangleBean getHand_rectangle() {
        return hand_rectangle;
    }

    public void setHand_rectangle(RectangleBean hand_rectangle) {
        this.hand_rectangle = hand_rectangle;
    }

    public GestureDetailBean getGesture() {
        return gesture;
    }

    public void setGesture(GestureDetailBean gesture) {
        this.gesture = gesture;
    }
}
