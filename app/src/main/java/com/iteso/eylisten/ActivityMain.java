package com.iteso.eylisten;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {

    private RecyclerView onlineRecycler;
    private RecyclerView playlistRecycler;
    private AdapterMusicList mOnlineAdapter, mPlaylistAdapter;
    private ArrayList<MusicList> musicLibrary;
    private ArrayList<MusicList> playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLists();

        onlineRecycler = findViewById(R.id.activity_main_online_recycler);
        playlistRecycler = findViewById(R.id.activity_main_playlist_recycler);
        mOnlineAdapter = new AdapterMusicList(musicLibrary);
        mPlaylistAdapter = new AdapterMusicList(playlists);

        onlineRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false));
        onlineRecycler.setItemAnimator(new DefaultItemAnimator());
        onlineRecycler.setAdapter(mOnlineAdapter);

        playlistRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false));
        playlistRecycler.setItemAnimator(new DefaultItemAnimator());
        playlistRecycler.setAdapter(mPlaylistAdapter);

    }

    private void initLists() {

        musicLibrary = new ArrayList<>();
        playlists = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            playlists.add(new MusicList(i, "PLAYLIST " + i, i+""));
            musicLibrary.add(new MusicList(i, "ALBUM " + i, i+""));
        }


    }

}
