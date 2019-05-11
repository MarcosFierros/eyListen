package com.iteso.eylisten;

import android.content.Intent;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.google.firebase.FirebaseAppLifecycleListener;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.Tools.GlideApp;
import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMain extends Fragment {

    private RecyclerView onlineRecycler;
    private RecyclerView playlistRecycler;
    private AdapterMusicList mOnlineAdapter, mPlaylistAdapter;
    private ArrayList<MusicList> musicLibrary;
    private ArrayList<MusicList> playlists;
    private ImageButton settings;
    private CircleImageView accountImage;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    public FragmentMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);


        musicLibrary = new ArrayList<>();
        playlists = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null)
            musicLibrary = bundle.getParcelableArrayList(Constant.BUNDLE_LOCALFILES);

        onlineRecycler = v.findViewById(R.id.activity_main_online_recycler);
        playlistRecycler = v.findViewById(R.id.activity_main_playlist_recycler);
        settings= v.findViewById(R.id.activity_main_settings);
        accountImage = v.findViewById(R.id.activity_main_account_pic);

        mOnlineAdapter = new AdapterMusicList(musicLibrary, getFragmentManager());
        mPlaylistAdapter = new AdapterMusicList(playlists, getFragmentManager());

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), activity_settings.class);
                startActivity(intent);
            }
        });

        onlineRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayout.HORIZONTAL,false));
        onlineRecycler.setItemAnimator(new DefaultItemAnimator());
        onlineRecycler.setAdapter(mOnlineAdapter);

        playlistRecycler.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayout.HORIZONTAL,false));
        playlistRecycler.setItemAnimator(new DefaultItemAnimator());
        playlistRecycler.setAdapter(mPlaylistAdapter);

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
}
