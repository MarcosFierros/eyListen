package com.iteso.eylisten;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;

import java.util.ArrayList;

public class FragmentPlaylist extends Fragment {

    private ImageButton back;
    private RecyclerView songRecycler;
    private ArrayList<Song> songList;

    public FragmentPlaylist() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);

        songList = new ArrayList<>();
        songList.add(new Song("CANCION 1", "ARTISTA 1"));
        songList.add(new Song("CANCION 2", "ARTISTA 2"));
        songList.add(new Song("CANCION 3", "ARTISTA 3"));

        songRecycler = v.findViewById(R.id.fragment_playlist_recycler);
        back = v.findViewById(R.id.fragment_playlist_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMain fragmentMain = new FragmentMain();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_main_container, fragmentMain);
                fragmentTransaction.commit();
            }
        });

        songRecycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        songRecycler.setItemAnimator(new DefaultItemAnimator());
        songRecycler.setAdapter(new AdapterMusic(songList));

        return v;
    }

}
