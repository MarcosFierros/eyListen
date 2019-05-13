package com.iteso.eylisten.Tools;

import com.iteso.eylisten.AdapterMusic;
import com.iteso.eylisten.AdapterMusicList;
import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;

public class DataHolder {

    private static ArrayList<MusicList> onlineMusic;
    private static ArrayList<MusicList> localMusic;
    private static ArrayList<MusicList> userMusic;
    private static AdapterMusicList onlineAdapter;
    private static AdapterMusicList localAdapter;
    private static AdapterMusicList userAdapter;

    public static AdapterMusicList getOnlineAdapter() {
        return onlineAdapter;
    }

    public static void setOnlineAdapter(AdapterMusicList onlineAdapter) {
        DataHolder.onlineAdapter = onlineAdapter;
    }

    public static AdapterMusicList getLocalAdapter() {
        return localAdapter;
    }

    public static void setLocalAdapter(AdapterMusicList localAdapter) {
        DataHolder.localAdapter = localAdapter;
    }

    public static AdapterMusicList getUserAdapter() {
        return userAdapter;
    }

    public static void setUserAdapter(AdapterMusicList userAdapter) {
        DataHolder.userAdapter = userAdapter;
    }

    public static void setOnlineMusic(ArrayList<MusicList> onlineMusic) {
        DataHolder.onlineMusic = onlineMusic;
    }

    public static void setLocalMusic(ArrayList<MusicList> localMusic) {
        DataHolder.localMusic = localMusic;
    }

    public static void setUserMusic(ArrayList<MusicList> userMusic) {
        DataHolder.userMusic = userMusic;
    }

    public static ArrayList<MusicList> getLocalMusic() {
        return localMusic;
    }

    public static ArrayList<MusicList> getOnlineMusic() {
        return onlineMusic;
    }

    public static ArrayList<MusicList> getUserMusic() {
        return userMusic;
    }

}
