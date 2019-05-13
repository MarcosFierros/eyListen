package com.iteso.eylisten;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.Tools.DataHolder;
import com.iteso.eylisten.Tools.GlideApp;
import com.iteso.eylisten.Tools.RecyclerItemTouchHelper;
import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;
import com.jgabrielfreitas.core.BlurImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ActivityPlaylist extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView songRecycler;
    static CollapsingToolbarLayout collapsingToolbarLayout;
    static private AdapterMusic adapterMusic;
    private static MusicList musicList;
    private static ImageView playlistImage;
    private EditPlaylistDialog editPlaylistDialog;
    static Uri selectedImageUri;
    private static Song selectedSong;

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
        } else if (musicList.getImageurl() != null) {
            GlideApp.with(this)
                    .load(musicList.getImageurl())
                    .centerCrop()
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .into(playlistImage);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(musicList.getName());
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ToolbarTheme);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ToolbarTheme);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorTextDark));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random rand = new Random();
                Song song = musicList.getSongArrayList().get(
                        rand.nextInt(musicList.getSongArrayList().size()));

                Intent intent = new Intent();
                intent.putExtra("selectedSong", song);
                intent.putExtra("selectedPlaylist", musicList);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        adapterMusic = new AdapterMusic(musicList, this);
        songRecycler.setHasFixedSize(true);
        songRecycler.setLayoutManager(new LinearLayoutManager(this));
        songRecycler.setItemAnimator(new DefaultItemAnimator());
        songRecycler.setAdapter(adapterMusic);

        int swipeDirs;
        if(musicList.isEditable())
            swipeDirs = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        else
            swipeDirs = ItemTouchHelper.RIGHT;

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, swipeDirs, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(songRecycler);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(musicList.isEditable()){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_activity_playlist, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                editPlaylistDialog = new EditPlaylistDialog();
                editPlaylistDialog.show(getSupportFragmentManager(), "editPlaylist");
                return true;
            case R.id.action_delete:
                DeletePlaylistDialog deletePlaylistDialog = new DeletePlaylistDialog();
                deletePlaylistDialog.show(getSupportFragmentManager(), "deletePlaylist");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constant.REQUEST_SELECT_PIC:
                if(resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    editPlaylistDialog.setPlaylistImage(uri);
                }
                break;
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        selectedSong =  musicList.getSongArrayList().get(position);

        if(direction == ItemTouchHelper.LEFT) {

            DeleteSongDialog deleteSongDialog = new DeleteSongDialog();
            deleteSongDialog.show(getSupportFragmentManager(), "deleteSong");

        } else if(direction == ItemTouchHelper.RIGHT) {

            AddSongDialog addSongDialog = new AddSongDialog();
            addSongDialog.show(getSupportFragmentManager(), "addSong");
        }

    }

    public static class DeletePlaylistDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage("Are you sure you want to delete this playlist?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int i = 0;
                            for(MusicList m: DataHolder.getUserMusic()) {
                                if(m.getName().equals(musicList.getName())) {
                                    DataHolder.getUserMusic().remove(i);
                                    break;
                                }
                                i++;
                            }
                            DataHolder.getUserAdapter().notifyDataSetChanged();

                            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                            firebaseDatabase.child("playlists")
                                    .child(musicList.getUid()).setValue(null);
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }

    }

    public static class EditPlaylistDialog extends DialogFragment {

        private ImageView addPlaylistImage;
        private EditText addPlaylistName;

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View v = inflater.inflate(R.layout.alert_addplaylist_layout, null);
            addPlaylistImage = v.findViewById(R.id.alert_addplaylist_image);
            addPlaylistName = v.findViewById(R.id.alert_addplaylist_name);

            byte[] image = musicList.getImage();
            if(image != null) {
                addPlaylistImage.setImageBitmap(BitmapFactory.decodeByteArray(
                        image, 0, image.length));
            } else if (musicList.getImageurl() != null) {
                GlideApp.with(this)
                        .load(musicList.getImageurl())
                        .centerCrop()
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                        .into(addPlaylistImage);
            }
            addPlaylistName.setText(musicList.getName());

            addPlaylistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                2000);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        if(intent.resolveActivity(getActivity().getPackageManager()) != null)
                            getActivity().startActivityForResult(intent, Constant.REQUEST_SELECT_PIC);
                    }
                }
            });

            builder.setView(v)
                    .setTitle("Edit Playlist")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            if (selectedImageUri != null) {
                                musicList.setImageurl(selectedImageUri.toString());
                                GlideApp.with(getContext())
                                        .load(selectedImageUri)
                                        .centerCrop()
                                        .placeholder(R.drawable.logo)
                                        .into(playlistImage);

                            }
                            musicList.setName(addPlaylistName.getText().toString());
                            collapsingToolbarLayout.setTitle(musicList.getName());
                            DataHolder.getUserMusic().set(musicList.getId(), musicList);
                            DataHolder.getUserAdapter().notifyDataSetChanged();
                            selectedImageUri = null;

                            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                            firebaseDatabase.child("playlists")
                                    .child(musicList.getUid()).setValue(musicList);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }

        public void setPlaylistImage(Uri uri) {
            selectedImageUri = uri;
            GlideApp.with(this)
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(addPlaylistImage);
        }
    }

    public static class DeleteSongDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


            builder.setMessage("Are you sure you want to delete this song?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            musicList.getSongArrayList().remove(selectedSong);
                            DataHolder.getUserMusic().set(musicList.getId(), musicList);
                            DataHolder.getUserAdapter().notifyDataSetChanged();
                            adapterMusic.notifyDataSetChanged();

                            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                            firebaseDatabase.child("playlists")
                                    .child(musicList.getUid()).setValue(musicList);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            adapterMusic.notifyDataSetChanged();
                        }
                    });
            return builder.create();
        }

    }

    public static class AddSongDialog extends DialogFragment {

        private Spinner playlistSpinner;
        private int playlistSelected;

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            View v = inflater.inflate(R.layout.aler_addsong_dialog, null);
            playlistSpinner = v.findViewById(R.id.alert_addsong_spinner);

            final ArrayAdapter<MusicList> adapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    DataHolder.getUserMusic());

            playlistSpinner.setAdapter(adapter);
            playlistSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    playlistSelected = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    playlistSelected = 0;
                }
            });
            builder.setView(v).setTitle("Select Playlist")
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                            DataHolder.getUserMusic().get(playlistSelected)
                                   .getSongArrayList().add(selectedSong);
                            DataHolder.getUserAdapter().notifyDataSetChanged();
                            adapterMusic.notifyDataSetChanged();

                            MusicList list = DataHolder.getUserMusic().get(playlistSelected);
                            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                            firebaseDatabase.child("playlists")
                                    .child(list.getUid()).setValue(list);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            adapterMusic.notifyDataSetChanged();
                        }
                    });
            return builder.create();
        }

    }


}
