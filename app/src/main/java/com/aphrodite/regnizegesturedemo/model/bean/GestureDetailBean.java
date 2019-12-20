package com.aphrodite.regnizegesturedemo.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aphrodite on 2019/12/6.
 */
public class GestureDetailBean implements Parcelable {
    private float beg;
    private float big_v;
    private float double_finger_up;
    private float fist;
    private float hand_open;
    private float heart_a;
    private float heart_b;
    private float heart_c;
    private float heart_d;
    private float index_finger_up;
    private float namaste;
    private float ok;
    private float palm_up;
    private float phonecall;
    private float rock;
    private float thanks;
    private float thumb_down;
    private float thumb_up;
    private float unknown;
    private float victory;

    public GestureDetailBean() {
    }

    protected GestureDetailBean(Parcel in) {
        beg = in.readFloat();
        big_v = in.readFloat();
        double_finger_up = in.readFloat();
        fist = in.readFloat();
        hand_open = in.readFloat();
        heart_a = in.readFloat();
        heart_b = in.readFloat();
        heart_c = in.readFloat();
        heart_d = in.readFloat();
        index_finger_up = in.readFloat();
        namaste = in.readFloat();
        ok = in.readFloat();
        palm_up = in.readFloat();
        phonecall = in.readFloat();
        rock = in.readFloat();
        thanks = in.readFloat();
        thumb_down = in.readFloat();
        thumb_up = in.readFloat();
        unknown = in.readFloat();
        victory = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(beg);
        dest.writeFloat(big_v);
        dest.writeFloat(double_finger_up);
        dest.writeFloat(fist);
        dest.writeFloat(hand_open);
        dest.writeFloat(heart_a);
        dest.writeFloat(heart_b);
        dest.writeFloat(heart_c);
        dest.writeFloat(heart_d);
        dest.writeFloat(index_finger_up);
        dest.writeFloat(namaste);
        dest.writeFloat(ok);
        dest.writeFloat(palm_up);
        dest.writeFloat(phonecall);
        dest.writeFloat(rock);
        dest.writeFloat(thanks);
        dest.writeFloat(thumb_down);
        dest.writeFloat(thumb_up);
        dest.writeFloat(unknown);
        dest.writeFloat(victory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GestureDetailBean> CREATOR = new Creator<GestureDetailBean>() {
        @Override
        public GestureDetailBean createFromParcel(Parcel in) {
            return new GestureDetailBean(in);
        }

        @Override
        public GestureDetailBean[] newArray(int size) {
            return new GestureDetailBean[size];
        }
    };

    public float getBeg() {
        return beg;
    }

    public void setBeg(float beg) {
        this.beg = beg;
    }

    public float getBig_v() {
        return big_v;
    }

    public void setBig_v(float big_v) {
        this.big_v = big_v;
    }

    public float getDouble_finger_up() {
        return double_finger_up;
    }

    public void setDouble_finger_up(float double_finger_up) {
        this.double_finger_up = double_finger_up;
    }

    public float getFist() {
        return fist;
    }

    public void setFist(float fist) {
        this.fist = fist;
    }

    public float getHand_open() {
        return hand_open;
    }

    public void setHand_open(float hand_open) {
        this.hand_open = hand_open;
    }

    public float getHeart_a() {
        return heart_a;
    }

    public void setHeart_a(float heart_a) {
        this.heart_a = heart_a;
    }

    public float getHeart_b() {
        return heart_b;
    }

    public void setHeart_b(float heart_b) {
        this.heart_b = heart_b;
    }

    public float getHeart_c() {
        return heart_c;
    }

    public void setHeart_c(float heart_c) {
        this.heart_c = heart_c;
    }

    public float getHeart_d() {
        return heart_d;
    }

    public void setHeart_d(float heart_d) {
        this.heart_d = heart_d;
    }

    public float getIndex_finger_up() {
        return index_finger_up;
    }

    public void setIndex_finger_up(float index_finger_up) {
        this.index_finger_up = index_finger_up;
    }

    public float getNamaste() {
        return namaste;
    }

    public void setNamaste(float namaste) {
        this.namaste = namaste;
    }

    public float getOk() {
        return ok;
    }

    public void setOk(float ok) {
        this.ok = ok;
    }

    public float getPalm_up() {
        return palm_up;
    }

    public void setPalm_up(float palm_up) {
        this.palm_up = palm_up;
    }

    public float getPhonecall() {
        return phonecall;
    }

    public void setPhonecall(float phonecall) {
        this.phonecall = phonecall;
    }

    public float getRock() {
        return rock;
    }

    public void setRock(float rock) {
        this.rock = rock;
    }

    public float getThanks() {
        return thanks;
    }

    public void setThanks(float thanks) {
        this.thanks = thanks;
    }

    public float getThumb_down() {
        return thumb_down;
    }

    public void setThumb_down(float thumb_down) {
        this.thumb_down = thumb_down;
    }

    public float getThumb_up() {
        return thumb_up;
    }

    public void setThumb_up(float thumb_up) {
        this.thumb_up = thumb_up;
    }

    public float getUnknown() {
        return unknown;
    }

    public void setUnknown(float unknown) {
        this.unknown = unknown;
    }

    public float getVictory() {
        return victory;
    }

    public void setVictory(float victory) {
        this.victory = victory;
    }

    @Override
    public String toString() {
        return "GestureDetailBean{" +
                "beg=" + beg +
                ", big_v=" + big_v +
                ", double_finger_up=" + double_finger_up +
                ", fist=" + fist +
                ", hand_open=" + hand_open +
                ", heart_a=" + heart_a +
                ", heart_b=" + heart_b +
                ", heart_c=" + heart_c +
                ", heart_d=" + heart_d +
                ", index_finger_up=" + index_finger_up +
                ", namaste=" + namaste +
                ", ok=" + ok +
                ", palm_up=" + palm_up +
                ", phonecall=" + phonecall +
                ", rock=" + rock +
                ", thanks=" + thanks +
                ", thumb_down=" + thumb_down +
                ", thumb_up=" + thumb_up +
                ", unknown=" + unknown +
                ", victory=" + victory +
                '}';
    }
}
