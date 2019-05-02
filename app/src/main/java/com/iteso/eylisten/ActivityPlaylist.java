package com.iteso.eylisten;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;

import java.util.ArrayList;

public class ActivityPlaylist extends AppCompatActivity {

    private RecyclerView songRecycler;
    private ArrayList<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        songList = new ArrayList<>();
        songList.add(new Song("CANCION 1", "ARTISTA 1"));
        songList.add(new Song("CANCION 2", "ARTISTA 2"));
        songList.add(new Song("CANCION 3", "ARTISTA 3"));
        songList.add(new Song("CANCION 4", "ARTISTA 4"));
        songList.add(new Song("CANCION 5", "ARTISTA 5"));
        songList.add(new Song("CANCION 6", "ARTISTA 6"));
        songList.add(new Song("CANCION 7", "ARTISTA 7"));
        songList.add(new Song("CANCION 8", "ARTISTA 8"));
        songList.add(new Song("CANCION 9", "ARTISTA 9"));

        MusicList musicList = new MusicList(0, "Playlist 1", "0",false);
        musicList.setSongArrayList(songList);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(musicList.getName());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ToolbarTheme);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ToolbarTheme);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorTextDark));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        songRecycler = findViewById(R.id.fragment_playlist_recycler);
        songRecycler.setLayoutManager(new LinearLayoutManager(this));
        songRecycler.setItemAnimator(new DefaultItemAnimator());
        songRecycler.setAdapter(new AdapterMusic(musicList.getSongArrayList()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
