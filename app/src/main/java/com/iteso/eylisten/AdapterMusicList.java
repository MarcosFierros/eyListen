package com.iteso.eylisten;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;
import java.util.List;

public class AdapterMusicList extends RecyclerView.Adapter<AdapterMusicList.MyViewHolder> {

    List<MusicList> musiclibrary;

    public AdapterMusicList(ArrayList<MusicList> musiclibrary) { this.musiclibrary = musiclibrary; }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_musiclist, viewGroup, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        MusicList list = musiclibrary.get(i);
        myViewHolder.name.setText(list.getName());
    }

    @Override
    public int getItemCount() {
        return musiclibrary.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            imageView = itemView.findViewById(R.id.item_image);
        }
    }



}
