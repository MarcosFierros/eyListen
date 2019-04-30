package com.iteso.eylisten.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicList implements Parcelable {

    private Integer id;
    private String name;
    private String image;

    public MusicList(Integer id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public MusicList(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.image = in.readString();
    }

    public static final Creator<MusicList> CREATOR = new Creator<MusicList>() {
        @Override
        public MusicList createFromParcel(Parcel in) {
            return new MusicList(in);
        }

        @Override
        public MusicList[] newArray(int size) {
            return new MusicList[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
    }
}
