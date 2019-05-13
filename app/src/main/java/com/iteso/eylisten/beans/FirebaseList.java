package com.iteso.eylisten.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseList implements Parcelable {

     private Integer id;
     private String name;
     private String image;
     private boolean editable;
     private HashMap<String, String> songList;

    public FirebaseList() { }

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

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public HashMap<String, String> getSongList() {
        return songList;
    }

    public void setSongList(HashMap<String, String> songList) {
        this.songList = songList;
    }

    protected FirebaseList(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        image = in.readString();
        editable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(image);
        dest.writeByte((byte) (editable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FirebaseList> CREATOR = new Creator<FirebaseList>() {
        @Override
        public FirebaseList createFromParcel(Parcel in) {
            return new FirebaseList(in);
        }

        @Override
        public FirebaseList[] newArray(int size) {
            return new FirebaseList[size];
        }
    };
}
