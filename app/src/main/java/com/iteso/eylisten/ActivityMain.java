package com.iteso.eylisten;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityMain extends AppCompatActivity {

    private FrameLayout container;
    private MediaPlayer mediaPlayer;

    private TextView songName;
    private TextView artistName;
    private CircleImageView albumImage;
    private ImageView albumImageBig;
    private TextView songNameBig;
    private TextView artistNameBig;
    private TextView timetv;
    private TextView durationtv;

    private ArrayList<MusicList> localmusic;

    private String[] STAR = { "*" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.activity_main_container);
        songName = findViewById(R.id.player_main_name);
        songNameBig = findViewById(R.id.player_main_name_big);
        artistName = findViewById(R.id.player_main_artist);
        artistNameBig = findViewById(R.id.player_main_artist_big);
        albumImage = findViewById(R.id.player_main_image);
        albumImageBig = findViewById(R.id.player_main_album);
        timetv = findViewById(R.id.player_main_time);
        durationtv = findViewById(R.id.player_main_duartion);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
                    Constant.REQUEST_READ_EXTERNAL);
        }else {
            readLocalFiles();
        }

        // TODO
        MusicList lastplaylist = localmusic.get(0);
        Song lastSong = lastplaylist.getSongArrayList().get(0);

        songName.setText(lastSong.getSongName());
        songNameBig.setText(lastSong.getSongName());
        artistName.setText(lastSong.getArtistName());
        artistNameBig.setText(lastSong.getArtistName());
        byte[] image = lastplaylist.getImage();
        if (image != null) {
            albumImage.setImageBitmap(BitmapFactory.decodeByteArray(
                    image, 0 , image.length));
            albumImageBig.setImageBitmap(BitmapFactory.decodeByteArray(
                    image, 0, image.length));
        }

        Uri url = Uri.parse(lastSong.getPath());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), url);
            mediaPlayer.prepare();
            durationtv.setText(getFormattedTime(mediaPlayer.getDuration()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mediaPlayer.start();


        final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        /*StorageReference audioRef = firebaseStorage.getReferenceFromUrl(
                "gs://heylisten-62e4d.appspot.com/The Now Now/" +
                        "1. Humility (feat. George Benson).mp3");

        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                final String url = uri.toString();

                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(url, new HashMap<String, String>());
                songName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                artistName.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                byte[] image = mmr.getEmbeddedPicture();
                albumImage.setImageBitmap(
                        BitmapFactory.decodeByteArray(image, 0, image.length));

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                    //mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //mediaPlayer.start();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constant.REQUEST_READ_EXTERNAL:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    readLocalFiles();
                }
                break;
        }
    }

    private void readLocalFiles() {

        Cursor cursor;
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        localmusic = new ArrayList<>();

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

                        Log.e("CHECK ERROR", songname);
                        int n = 0;
                        for (MusicList musicList: localmusic) {
                            if(!musicList.getName().equals(albumname))
                                n += 1;
                            else
                                musicList.getSongArrayList().add(song);
                        }

                        if (n == localmusic.size()) {
                            MusicList musicList = new MusicList();
                            musicList.setId(albumid);
                            musicList.setName(albumname);
                            musicList.setImage(getAlbumArt(song.getPath()));
                            musicList.setEditable(false);
                            musicList.setSongArrayList(new ArrayList<Song>());
                            musicList.getSongArrayList().add(song);
                            localmusic.add(musicList);
                        }



                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }

        FragmentMain fragmentMain = new FragmentMain();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constant.BUNDLE_LOCALFILES, localmusic);
        fragmentMain.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_container, fragmentMain);
        fragmentTransaction.commit();

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
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

}
