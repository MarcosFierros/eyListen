package com.iteso.eylisten;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;

import java.util.List;

public class AdapterMusic extends RecyclerView.Adapter<AdapterMusic.MyViewHolder> implements Parcelable {

    final MusicList musicList;
    Activity activity;

    public AdapterMusic(MusicList musicList, Activity activity) {
        this.musicList = musicList;
        this.activity = activity;
    }


    protected AdapterMusic(Parcel in) {
        musicList = in.readParcelable(MusicList.class.getClassLoader());
    }

    public static final Creator<AdapterMusic> CREATOR = new Creator<AdapterMusic>() {
        @Override
        public AdapterMusic createFromParcel(Parcel in) {
            return new AdapterMusic(in);
        }

        @Override
        public AdapterMusic[] newArray(int size) {
            return new AdapterMusic[size];
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_music, viewGroup, false);
        return new AdapterMusic.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Song song = musicList.getSongArrayList().get(i);
        myViewHolder.song.setText(song.getSongName());
        myViewHolder.artist.setText(song.getArtistName());

        myViewHolder.song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedSong", song);
                intent.putExtra("selectedPlaylist", musicList);
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();

            }

        });
    }

    @Override
    public int getItemCount() {
        return musicList.getSongArrayList().size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(musicList, flags);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView song, artist;
        RelativeLayout backgroundDelete;
        RelativeLayout backgroundAdd;
        public RelativeLayout viewForeground;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            song = itemView.findViewById(R.id.item_music_song);
            artist = itemView.findViewById(R.id.item_music_artist);
            backgroundDelete = itemView.findViewById(R.id.item_music_back_delete);
            backgroundAdd = itemView.findViewById(R.id.item_music_back_add);
            viewForeground = itemView.findViewById(R.id.item_music_foreground);
        }


        public void changeBackgroundAt(boolean b){
            if(b) {
                backgroundDelete.setVisibility(View.VISIBLE);
                backgroundAdd.setVisibility(View.INVISIBLE);
            } else {
                backgroundDelete.setVisibility(View.INVISIBLE);
                backgroundAdd.setVisibility(View.VISIBLE);
            }
        }

    }
}
