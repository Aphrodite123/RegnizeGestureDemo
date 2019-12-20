package com.aphrodite.regnizegesturedemo.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aphrodite on 2019/12/6.
 */
public class RectangleBean implements Parcelable {
    private int top;
    private int left;
    private int width;
    private int height;

    public RectangleBean() {
    }

    protected RectangleBean(Parcel in) {
        top = in.readInt();
        left = in.readInt();
        width = in.readInt();
        height = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(top);
        dest.writeInt(left);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RectangleBean> CREATOR = new Creator<RectangleBean>() {
        @Override
        public RectangleBean createFromParcel(Parcel in) {
            return new RectangleBean(in);
        }

        @Override
        public RectangleBean[] newArray(int size) {
            return new RectangleBean[size];
        }
    };

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
