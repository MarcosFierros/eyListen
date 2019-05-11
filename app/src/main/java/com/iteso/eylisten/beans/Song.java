package com.iteso.eylisten.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private int id;
    private String songName;
    private String artistName;
    private String path;

    public Song() {}

    protected Song(Parcel in) {
        id = in.readInt();
        songName = in.readString();
        artistName = in.readString();
        path = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(songName);
        dest.writeString(artistName);
        dest.writeString(path);
    }
}
