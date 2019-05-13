package com.iteso.eylisten;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.Tools.DataHolder;
import com.iteso.eylisten.Tools.GlideApp;
import com.iteso.eylisten.beans.FirebaseList;
import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;
import com.iteso.eylisten.beans.User;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMain extends AppCompatActivity {

    private FrameLayout container;
    private MediaPlayer mediaPlayer;

    private FragmentMain fragmentMain;
    private TextView songName;
    private TextView artistName;
    private CircleImageView albumImage;
    private ImageView albumImageBig;
    private TextView songNameBig;
    private TextView artistNameBig;
    private TextView timetv;
    private TextView durationtv;
    private ImageButton replayButton;
    private ImageButton playButton;
    private ImageButton playLittleButton;
    private ImageButton playNextButton;
    private ImageButton playBackButton;
    private ImageButton shuffleButton;
    private SeekBar playBar;
    private int mLastResourceId;
    private int mLastRepeatState;
    private int mLastShuffleState;
    private boolean nextPressed;
    private boolean songFinished;
    private Equalizer.Settings eq;
    private MusicList currentList;
    private Song currentSong;
    private User currentUser;

    private DatabaseReference firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    private String[] STAR = { "*" };

    Handler mHandler;
    Runnable runnable;
    Random rand;
    static Equalizer mEqualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler=new Handler();

        nextPressed = false;
        songFinished = false;
        rand = new Random();

        mLastResourceId = R.drawable.ic_play_circle_;
        mLastRepeatState = R.drawable.ic_repeat;
        mLastShuffleState = R.drawable.ic_shuffle;

        container = findViewById(R.id.activity_main_container);
        songName = findViewById(R.id.player_main_name);
        songNameBig = findViewById(R.id.player_main_name_big);
        artistName = findViewById(R.id.player_main_artist);
        artistNameBig = findViewById(R.id.player_main_artist_big);
        albumImage = findViewById(R.id.player_main_image);
        albumImageBig = findViewById(R.id.player_main_album);
        timetv = findViewById(R.id.player_main_time);
        durationtv = findViewById(R.id.player_main_duartion);
        playButton = findViewById(R.id.player_main_play);
        playLittleButton = findViewById(R.id.activity_main_settings);
        playNextButton = findViewById(R.id.player_main_next);
        playBackButton = findViewById(R.id.player_main_back);
        replayButton = findViewById(R.id.player_main_repeat);
        shuffleButton = findViewById(R.id.player_main_shuffle);
        playBar= findViewById(R.id.player_main_progress);
        DataHolder.setLocalMusic(new ArrayList<MusicList>());
        DataHolder.setOnlineMusic(new ArrayList<MusicList>());
        DataHolder.setUserMusic(new ArrayList<MusicList>());

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    Constant.REQUEST_READ_EXTERNAL);
        }else {
            new getLocalFilesTask().execute();
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        new getUserFilesTask().execute();
        new getOnlineFilesTask().execute();

        DataHolder.setLocalAdapter(new AdapterMusicList(DataHolder.getLocalMusic(),
                getSupportFragmentManager(), this, this));
        DataHolder.setOnlineAdapter(new AdapterMusicList(DataHolder.getOnlineMusic(),
                getSupportFragmentManager(), this, this));
        DataHolder.setUserAdapter(new AdapterMusicList(DataHolder.getUserMusic(),
                getSupportFragmentManager(), this, this));

        fragmentMain = new FragmentMain();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_container, fragmentMain).commit();



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLastResourceId == R.drawable.ic_play_circle_) {
                    playButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    playLittleButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    mediaPlayer.start();
                    mLastResourceId = R.drawable.ic_pause_circle_filled_black_24dp;
                    return;
                }

                if (mLastResourceId == R.drawable.ic_pause_circle_filled_black_24dp) {
                    playButton.setImageResource(R.drawable.ic_play_circle_);
                    playLittleButton.setImageResource(R.drawable.ic_play_arrow);
                    mediaPlayer.pause();
                    mLastResourceId = R.drawable.ic_play_circle_;
                    return;
                }
            }
        });

        playLittleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLastResourceId == R.drawable.ic_play_circle_) {
                    playButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    playLittleButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    mediaPlayer.start();
                    mLastResourceId = R.drawable.ic_pause_circle_filled_black_24dp;
                    return;
                }

                if (mLastResourceId == R.drawable.ic_pause_circle_filled_black_24dp) {
                    playButton.setImageResource(R.drawable.ic_play_circle_);
                    playLittleButton.setImageResource(R.drawable.ic_play_arrow);
                    mediaPlayer.pause();
                    mLastResourceId = R.drawable.ic_play_circle_;
                    return;
                }

            }
        });

        playNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPressed = true;
                NextSong();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                songFinished=true;

                if(mLastRepeatState != R.drawable.ic_repeat_pressed) {
                    NextSong();
                }else{
                    RepeatSong();
                }
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playBar.setMax(mp.getDuration());
                updateSeekBar();
            }
        });

        runnable=new Runnable() {
            @Override
            public void run() {
                updateSeekBar();
            }
        };

        playBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrevSong();

            }
        });
        playBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timetv.setText(getFormattedTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mLastRepeatState == R.drawable.ic_repeat) {
                    replayButton.setImageResource(R.drawable.ic_repeat_pressed);
                    mLastRepeatState = R.drawable.ic_repeat_pressed;
                    return;
                }


                if (mLastRepeatState == R.drawable.ic_repeat_pressed) {
                    replayButton.setImageResource(R.drawable.ic_repeat);
                    mLastRepeatState = R.drawable.ic_repeat;
                    return;
                }
            }
        });

        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mLastShuffleState == R.drawable.ic_shuffle) {
                    shuffleButton.setImageResource(R.drawable.ic_shuffle_pressed);
                    mLastShuffleState = R.drawable.ic_shuffle_pressed;
                    return;
                }


                if (mLastShuffleState == R.drawable.ic_shuffle_pressed) {
                    shuffleButton.setImageResource(R.drawable.ic_shuffle);
                    mLastShuffleState = R.drawable.ic_shuffle;
                    return;
                }
            }
        });

        mEqualizer = new Equalizer(0,mediaPlayer.getAudioSessionId());
        mEqualizer.setEnabled(true);
        for(int i=0;i<mEqualizer.getNumberOfPresets();i++){
            Log.e("Prueba", mEqualizer.getPresetName((short)i));
            Log.e("Datos",mEqualizer.getProperties()+"");
            mEqualizer.usePreset(i+1<9?(short)(i+1):(short)0);
        }

    }

    public static void UpdateEqualizer(){
        mEqualizer.usePreset((short)Constant.getPreset());
        Log.e("Funciono","Yupi");
        Log.e("test",mEqualizer.getCurrentPreset()+"");
    }

    private void updateSeekBar() {
        if(mediaPlayer!=null) {
            playBar.setProgress(mediaPlayer.getCurrentPosition());
            mHandler.postDelayed(runnable, 100);
        }

    }

    private final void RepeatSong(){
        //song = musicList.getSongArrayList().get(0);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.reset();
        }
        try {
            getSongData(currentSong, currentList);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            playButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            playLittleButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            durationtv.setText(getFormattedTime(mediaPlayer.getDuration()));
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final void PrevSong() {
        for (int i = 0; i < currentList.getSongArrayList().size(); i++) {

            if (currentList.getSongArrayList().get(i).getSongName().equals(currentSong.getSongName())) {
                Log.e("Prueba3", currentSong.getSongName() + "");
                if (i -1 >= 0) {
                    currentSong = currentList.getSongArrayList().get(i -1);
                    break;
                } else {
                    currentSong = currentList.getSongArrayList().get(currentList.getSongArrayList().size()-1);
                    break;
                }
            }
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.reset();
        }
        try {
            getSongData(currentSong, currentList);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            playButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            playLittleButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            durationtv.setText(getFormattedTime(mediaPlayer.getDuration()));
            mediaPlayer.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final void NextSong(){

        boolean wasPlaying = false;

        if(mLastShuffleState == R.drawable.ic_shuffle_pressed && !nextPressed){
            int random= rand.nextInt(currentList.getSongArrayList().size()-1);
            random++;
            currentSong = currentList.getSongArrayList().get(random);
        }else {
            for (int i = 0; i < currentList.getSongArrayList().size(); i++) {
                Log.e("Prueba", currentList.getSongArrayList().get(i).getSongName() + "");
                Log.e("Prueba2", currentSong.getSongName() + "");

                if (currentList.getSongArrayList().get(i).getSongName().equals(currentSong.getSongName())) {
                    Log.e("Prueba3", currentSong.getSongName() + "");
                    if (i + 1 != currentList.getSongArrayList().size()) {
                        currentSong = currentList.getSongArrayList().get(i + 1);
                        break;
                    } else {
                        currentSong = currentList.getSongArrayList().get(0);
                        break;
                    }
                }
            }
        }
        //song = musicList.getSongArrayList().get(0);
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.reset();
            wasPlaying=true;
        }

        try {
            getSongData(currentSong, currentList);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();

            durationtv.setText(getFormattedTime(mediaPlayer.getDuration()));

            if(wasPlaying) {
                mediaPlayer.start();
                playButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                playLittleButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            }

            if(songFinished){
                mediaPlayer.start();
                songFinished=false;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        nextPressed=false;
        Log.e("test",mEqualizer.getCurrentPreset()+"");

    }

    private void getSongData(Song song, MusicList musicList){
        songName.setText(song.getSongName());
        songNameBig.setText(song.getSongName());
        artistName.setText(song.getArtistName());
        artistNameBig.setText(song.getArtistName());
        byte[] image = musicList.getImage();
        if (image != null) {
            albumImage.setImageBitmap(BitmapFactory.decodeByteArray(
                    image, 0, image.length));
            albumImageBig.setImageBitmap(BitmapFactory.decodeByteArray(
                    image, 0, image.length));
        } else if (musicList.getImageurl() != null) {
            GlideApp.with(this)
                    .load(musicList.getImageurl())
                    .centerCrop()
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .into(albumImage);
            GlideApp.with(this)
                    .load(musicList.getImageurl())
                    .centerCrop()
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .into(albumImageBig);
        } else{
            albumImage.setImageResource(R.drawable.logo);
            albumImageBig.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constant.REQUEST_READ_EXTERNAL:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    new getLocalFilesTask().execute();
                }
                break;
        }
    }

    public static boolean isSdPresent() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    private byte[] getAlbumArt(String uri) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(uri);
        return mmr.getEmbeddedPicture();

    }

    private String getFormattedTime(int duration) {
        return String.format("%d:" +
                        (TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))<10? "0%d": "%d"),
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    private class getLocalFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Cursor cursor;
            Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

            if (isSdPresent()) {
                cursor = getContentResolver().query(allsongsuri, STAR, selection, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {

                            String albumname = cursor.getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));

                            int albumid = cursor.getInt(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                            int song_id = cursor.getInt(cursor
                                    .getColumnIndex(MediaStore.Audio.Media._ID));

                            String songname = cursor
                                    .getString(cursor
                                            .getColumnIndex(MediaStore.Audio.Media.TITLE));

                            String artistname = cursor.getString(
                                    cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                            String fullpath = cursor.getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.DATA));

                            Song song = new Song();
                            song.setId(song_id);
                            song.setSongName(songname);
                            song.setArtistName(artistname);
                            song.setPath(fullpath);

                            int n = 0;
                            for (MusicList musicList: DataHolder.getLocalMusic()) {
                                if(!musicList.getName().equals(albumname))
                                    n += 1;
                                else
                                    musicList.getSongArrayList().add(song);
                            }

                            if (n == DataHolder.getLocalMusic().size()) {
                                MusicList musicList = new MusicList();
                                musicList.setId(albumid);
                                musicList.setName(albumname);
                                musicList.setImage(getAlbumArt(song.getPath()));
                                musicList.setEditable(false);
                                musicList.setSongArrayList(new ArrayList<Song>());
                                musicList.getSongArrayList().add(song);
                                DataHolder.getLocalMusic().add(musicList);
                            }

                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            DataHolder.getLocalAdapter().notifyDataSetChanged();
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null) {
                firebaseDatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (UserInfo profile : user.getProviderData()) {
                                if (FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                                    User newUser = new User(user.getDisplayName());
                                    firebaseDatabase.child("users")
                                            .child(profile.getUid()).setValue(newUser);
                                }
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("ERROR", databaseError.toString());
                    }
                });
                firebaseDatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User value = snapshot.getValue(User.class);
                            if(value.getName().equals(user.getDisplayName())) {
                                currentUser = value;
                            }
                        }
                        // TODO
                        if(currentUser.getTypeOfPlaylist() == 0)
                            currentList = DataHolder.getLocalMusic().get(currentUser.getCurrentPLaylist());
                        else if(currentUser.getTypeOfPlaylist() == 1)
                            currentList = DataHolder.getOnlineMusic().get(currentUser.getCurrentPLaylist());
                        else if(currentUser.getTypeOfPlaylist() == 3)
                            currentList = DataHolder.getUserMusic().get(currentUser.getCurrentPLaylist());

                        currentSong = currentList.getSongArrayList().get(currentUser.getCurrentSong());

                        songName.setText(currentSong.getSongName());
                        songNameBig.setText(currentSong.getSongName());
                        artistName.setText(currentSong.getArtistName());
                        artistNameBig.setText(currentSong.getArtistName());
                        byte[] image = currentList.getImage();
                        if (image != null) {
                            albumImage.setImageBitmap(BitmapFactory.decodeByteArray(
                                    image, 0 , image.length));
                            albumImageBig.setImageBitmap(BitmapFactory.decodeByteArray(
                                    image, 0, image.length));
                        } else if (currentList.getImageurl() != null) {
                            GlideApp.with(ActivityMain.this)
                                    .load(currentList.getImageurl())
                                    .centerCrop()
                                    .placeholder(R.drawable.logo)
                                    .into(albumImage);
                            GlideApp.with(ActivityMain.this)
                                    .load(currentList.getImageurl())
                                    .centerCrop()
                                    .placeholder(R.drawable.logo)
                                    .into(albumImageBig);
                        }


                        Uri url = Uri.parse(currentSong.getPath());

                        try {
                            mediaPlayer.setDataSource(ActivityMain.this, url);
                            mediaPlayer.prepare();
                            durationtv.setText(getFormattedTime(mediaPlayer.getDuration()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        }
    }

    private class getOnlineFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            firebaseDatabase.child("onlinePlaylist").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MusicList musicList = snapshot.getValue(MusicList.class);

                        int i = 0;
                        for(MusicList list : DataHolder.getOnlineMusic()) {
                            if(!list.getName().equals(musicList.getName()))
                                i++;
                        }
                        Log.e("FIREBASE", i + "-" + DataHolder.getOnlineMusic().size());
                        if(i == DataHolder.getOnlineMusic().size()) {
                            DataHolder.getOnlineMusic().add(musicList);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DataHolder.getOnlineAdapter().notifyDataSetChanged();
        }
    }

    private class getUserFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            firebaseDatabase.child("playlists").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MusicList musicList = snapshot.getValue(MusicList.class);
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(user != null) {
                            for (UserInfo profile : user.getProviderData()) {
                                if(musicList.getUserId().equals(profile.getUid())) {
                                    int i = 0;
                                    for(MusicList list : DataHolder.getUserMusic()) {
                                        if(!list.getName().equals(musicList.getName()))
                                            i++;
                                    }
                                    if(i == DataHolder.getUserMusic().size()) {
                                        if(musicList.getSongArrayList() == null)
                                            musicList.setSongArrayList(new ArrayList<Song>());
                                        DataHolder.getUserMusic().add(musicList);

                                    }
                                }
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            DataHolder.getUserAdapter().notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constant.REQUEST_SELECT_PIC:
                if(resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    fragmentMain.changeAlertDialogImage(uri);
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    Log.e("Funciono","Yupi");
                    currentSong = data.getParcelableExtra("selectedSong");
                    currentList = data.getParcelableExtra("selectedPlaylist");
                    Log.e("Error", currentSong.getSongName());

                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        mediaPlayer.reset();
                    }
                    Uri url = Uri.parse(currentSong.getPath());
                    try {
                        getSongData(currentSong, currentList);
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(currentSong.getPath());
                        mediaPlayer.prepare();
                        Log.e("prueba", currentSong.getSongName()+"");
                        playButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                        playLittleButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                        durationtv.setText(getFormattedTime(mediaPlayer.getDuration()));
                        mediaPlayer.start();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }

    private static int getRequestCode() {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }
}
