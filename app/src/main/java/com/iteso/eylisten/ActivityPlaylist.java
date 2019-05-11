package com.iteso.eylisten;

import android.graphics.BitmapFactory;
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
import android.widget.ImageView;

import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.ArrayList;

public class ActivityPlaylist extends AppCompatActivity {

    private RecyclerView songRecycler;
    private MusicList musicList;
    private ImageView playlistImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        songRecycler = findViewById(R.id.fragment_playlist_recycler);
        playlistImage = findViewById(R.id.activity_playlist_image);

        musicList = getIntent().getParcelableExtra(Constant.EXTRAS_PLAYLIST);

        byte[] image = musicList.getImage();
        if(image != null) {
            playlistImage.setImageBitmap(BitmapFactory.decodeByteArray(
                    image, 0, image.length));
        }

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
