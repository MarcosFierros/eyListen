package com.iteso.eylisten;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;
import java.util.List;

public class AdapterMusicList extends RecyclerView.Adapter<AdapterMusicList.MyViewHolder> {

    List<MusicList> musiclibrary;
    FragmentManager fragmentManager;
    int index;

    public AdapterMusicList(ArrayList<MusicList> musiclibrary, FragmentManager fragmentManager) {
        this.musiclibrary = musiclibrary;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_musiclist, viewGroup, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final MusicList list = musiclibrary.get(i);
        myViewHolder.name.setText(list.getName());
        byte[] image = list.getImage();
        if(image != null) {
            myViewHolder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(
                    image, 0 , image.length));
        }
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityPlaylist.class);
                intent.putExtra(Constant.EXTRAS_PLAYLIST, list);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musiclibrary.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView imageView;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            imageView = itemView.findViewById(R.id.item_image);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    FragmentPlaylist fragmentPlaylist = new FragmentPlaylist();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.activity_main_container, fragmentPlaylist);
//                    fragmentTransaction.commit();
                    Intent intent = new Intent(itemView.getContext(), ActivityPlaylist.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }



}
