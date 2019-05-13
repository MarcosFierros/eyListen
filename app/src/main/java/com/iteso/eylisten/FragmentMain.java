package com.iteso.eylisten;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.auth.policy.Resource;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.Tools.DataHolder;
import com.iteso.eylisten.Tools.GlideApp;
import com.iteso.eylisten.beans.MusicList;
import com.iteso.eylisten.beans.Song;
import com.iteso.eylisten.beans.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMain extends Fragment {

    private RecyclerView onlineRecycler;
    private RecyclerView playlistRecycler;
    private RecyclerView localRecycler;
    private ImageButton settings, addPlaylist;
    private CircleImageView accountImage;
    private AddPlaylistDialog addPlaylistDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    static Uri selectedImageuri;

    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        localRecycler = v.findViewById(R.id.activity_main_local_recycler);
        onlineRecycler = v.findViewById(R.id.activity_main_online_recycler);
        playlistRecycler = v.findViewById(R.id.activity_main_playlist_recycler);
        settings = v.findViewById(R.id.activity_main_settings);
        addPlaylist = v.findViewById(R.id.activity_main_add);
        accountImage = v.findViewById(R.id.activity_main_account_pic);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_settings.class);
                startActivity(intent);
            }
        });
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlaylistDialog = new AddPlaylistDialog();
                addPlaylistDialog.show(getFragmentManager(), "addPlaylist");
            }
        });


        localRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayout.HORIZONTAL, false));
        localRecycler.setItemAnimator(new DefaultItemAnimator());
        localRecycler.setAdapter(DataHolder.getLocalAdapter());

        onlineRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayout.HORIZONTAL,false));
        onlineRecycler.setItemAnimator(new DefaultItemAnimator());
        onlineRecycler.setAdapter(DataHolder.getOnlineAdapter());

        playlistRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayout.HORIZONTAL,false));
        playlistRecycler.setItemAnimator(new DefaultItemAnimator());
        playlistRecycler.setAdapter(DataHolder.getUserAdapter());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fetchUserInfo();
            }
        };

        return v;
    }

    private void fetchUserInfo(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String photourl = "";
        if(user != null) {
            for(UserInfo profile: user.getProviderData()) {
                if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())){
                    photourl = "https://graph.facebook.com/" + profile.getUid() + "/picture?height=500";
                }
            }

            GlideApp.with(getContext())
                    .load(Uri.parse(photourl))
                    .centerCrop()
                    .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .into(accountImage);
        }

    }

    public void changeAlertDialogImage(Uri uri) {
        addPlaylistDialog.setPlaylistImage(uri);
        selectedImageuri = uri;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    public static class AddPlaylistDialog extends DialogFragment {

        private ImageView playlistImage;
        private EditText playlistName;

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View v = inflater.inflate(R.layout.alert_addplaylist_layout, null);
            playlistImage = v.findViewById(R.id.alert_addplaylist_image);
            playlistName = v.findViewById(R.id.alert_addplaylist_name);

            playlistImage.setOnClickListener(new View.OnClickListener() {
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
                    .setTitle("Create Playlist")
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            if (selectedImageuri == null) {
                                Resources res = getContext().getResources();
                                selectedImageuri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                                        "://" + res.getResourcePackageName(R.drawable.logo)
                                        + '/' + res.getResourceTypeName(R.drawable.logo)
                                        + '/' + res.getResourceEntryName(R.drawable.logo));
                            }

                            MusicList musicList = new MusicList();
                            musicList.setId(DataHolder.getUserMusic().size());
                            musicList.setName(playlistName.getText().toString());
                            musicList.setEditable(true);
                            musicList.setImage(null);
                            musicList.setImageurl(selectedImageuri.toString());
                            musicList.setSongArrayList(new ArrayList<Song>());

                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user != null) {
                                for (UserInfo profile : user.getProviderData()) {
                                    musicList.setUserId(profile.getUid());
                                }
                            }

                            DataHolder.getUserMusic().add(musicList);
                            DataHolder.getUserAdapter().notifyDataSetChanged();
                            selectedImageuri = null;


                            DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
                            String playlistId = firebaseDatabase.child("playlists").push().getKey();
                            musicList.setUid(playlistId);
                            firebaseDatabase.child("playlists").child(playlistId).setValue(musicList);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            return builder.create();
        }

        public void setPlaylistImage(Uri uri) {
            GlideApp.with(getContext())
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.logo)
                    .into(playlistImage);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
