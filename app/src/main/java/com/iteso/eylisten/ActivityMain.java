package com.iteso.eylisten;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.iteso.eylisten.beans.MusicList;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {

    private FrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.activity_main_container);

        FragmentMain fragmentMain = new FragmentMain();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_container, fragmentMain);
        fragmentTransaction.commit();

    }

}
