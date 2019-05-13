package com.iteso.eylisten.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String name;
    private int typeOfPlaylist;
    private int currentPLaylist;
    private int currentSong;

    public User() { };

    public User(String name) {
        this.name = name;
        this.typeOfPlaylist = 0;
        this.currentPLaylist = 0;
        this.currentSong = 0;
    }

    protected User(Parcel in) {
        name = in.readString();
        typeOfPlaylist = in.readInt();
        currentPLaylist = in.readInt();
        currentSong = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeOfPlaylist() {
        return typeOfPlaylist;
    }

    public void setTypeOfPlaylist(int typeOfPlaylist) {
        this.typeOfPlaylist = typeOfPlaylist;
    }

    public int getCurrentPLaylist() {
        return currentPLaylist;
    }

    public void setCurrentPLaylist(int currentPLaylist) {
        this.currentPLaylist = currentPLaylist;
    }

    public int getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(int currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(typeOfPlaylist);
        dest.writeInt(currentPLaylist);
        dest.writeInt(currentSong);
    }
}
