package com.iteso.eylisten;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;

public class FragmentMain extends Fragment {

    private RecyclerView onlineRecycler;
    private RecyclerView playlistRecycler;
    private AdapterMusicList mOnlineAdapter, mPlaylistAdapter;
    private ArrayList<MusicList> musicLibrary;
    private ArrayList<MusicList> playlists;
    private ImageButton settings;


    public FragmentMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        musicLibrary = new ArrayList<>();
        playlists = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            playlists.add(new MusicList(i, "PLAYLIST " + i, i+""));
            musicLibrary.add(new MusicList(i, "ALBUM " + i, i+""));
        }

        onlineRecycler = v.findViewById(R.id.activity_main_online_recycler);
        playlistRecycler = v.findViewById(R.id.activity_main_playlist_recycler);
        settings= v.findViewById(R.id.activity_main_settings);
        mOnlineAdapter = new AdapterMusicList(musicLibrary, getFragmentManager());
        mPlaylistAdapter = new AdapterMusicList(playlists, getFragmentManager());

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_settings.class);
                startActivity(intent);
            }
        });

        onlineRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayout.HORIZONTAL,false));
        onlineRecycler.setItemAnimator(new DefaultItemAnimator());
        onlineRecycler.setAdapter(mOnlineAdapter);

        playlistRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayout.HORIZONTAL,false));
        playlistRecycler.setItemAnimator(new DefaultItemAnimator());
        playlistRecycler.setAdapter(mPlaylistAdapter);

        return v;
    }



}
