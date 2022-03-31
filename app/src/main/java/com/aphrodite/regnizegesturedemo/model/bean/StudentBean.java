package com.aphrodite.regnizegesturedemo.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aphrodite on 2020/1/10.
 */
public class StudentBean implements Parcelable {
    private int number;
    private String name;
    private String sex;
    private String job;
    private String address;
    private String profession;

    public StudentBean() {
    }

    protected StudentBean(Parcel in) {
        number = in.readInt();
        name = in.readString();
        sex = in.readString();
        job = in.readString();
        address = in.readString();
        profession = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(name);
        dest.writeString(sex);
        dest.writeString(job);
        dest.writeString(address);
        dest.writeString(profession);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StudentBean> CREATOR = new Creator<StudentBean>() {
        @Override
        public StudentBean createFromParcel(Parcel in) {
            return new StudentBean(in);
        }

        @Override
        public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public String toString() {
        return "StudentBean{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", job='" + job + '\'' +
                ", address='" + address + '\'' +
                ", profession='" + profession + '\'' +
                '}';
    }
}
