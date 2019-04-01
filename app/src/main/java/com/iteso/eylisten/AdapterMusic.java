package com.iteso.eylisten;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iteso.eylisten.beans.Song;

import java.util.List;

public class AdapterMusic extends RecyclerView.Adapter<AdapterMusic.MyViewHolder> {

    List<Song> songList;

    public AdapterMusic(List<Song> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_music, viewGroup, false);
        return new AdapterMusic.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Song song = songList.get(i);
        myViewHolder.song.setText(song.getSongName());
        myViewHolder.artist.setText(song.getArtistName());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView song, artist;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            song = itemView.findViewById(R.id.item_music_song);
            artist = itemView.findViewById(R.id.item_music_artist);
        }

    }
}
