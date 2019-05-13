package com.iteso.eylisten;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.iteso.eylisten.Tools.GlideApp;
import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

public class AdapterMusicList extends RecyclerView.Adapter<AdapterMusicList.MyViewHolder> implements Parcelable {

    List<MusicList> musiclibrary;
    Context context;
    FragmentManager fragmentManager;
    Activity activity;
    int index;

    public AdapterMusicList(ArrayList<MusicList> musiclibrary, FragmentManager fragmentManager, Activity activity, Context context) {
        this.musiclibrary = musiclibrary;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.activity = activity;
    }


    protected AdapterMusicList(Parcel in) {
        musiclibrary = in.createTypedArrayList(MusicList.CREATOR);
        index = in.readInt();
    }

    public static final Creator<AdapterMusicList> CREATOR = new Creator<AdapterMusicList>() {
        @Override
        public AdapterMusicList createFromParcel(Parcel in) {
            return new AdapterMusicList(in);
        }

        @Override
        public AdapterMusicList[] newArray(int size) {
            return new AdapterMusicList[size];
        }
    };

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
        } else if (list.getImageurl() != null) {
            GlideApp.with(context)
                    .load(list.getImageurl())
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(myViewHolder.imageView);
        }
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityPlaylist.class);
                intent.putExtra(Constant.EXTRAS_PLAYLIST, list);
                ((ActivityMain) activity).startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musiclibrary.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(musiclibrary);
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
                    Intent intent = new Intent(itemView.getContext(), ActivityPlaylist.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }



}
