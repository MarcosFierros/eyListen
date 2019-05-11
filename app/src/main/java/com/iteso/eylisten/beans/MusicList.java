package com.iteso.eylisten.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MusicList implements Parcelable {

    private Integer id;
    private String name;
    private byte[] image;
    private boolean editable;
    private ArrayList<Song> songArrayList;

    public MusicList() { }

    protected MusicList(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        image = in.createByteArray();
        editable = in.readByte() != 0;
        songArrayList = in.createTypedArrayList(Song.CREATOR);
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
        dest.writeByteArray(image);
        dest.writeByte((byte) (editable ? 1 : 0));
        dest.writeTypedList(songArrayList);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public ArrayList<Song> getSongArrayList() {
        return songArrayList;
    }

    public void setSongArrayList(ArrayList<Song> songArrayList) {
        this.songArrayList = songArrayList;
    }
}
