package com.iteso.eylisten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.beans.User;


import java.util.Timer;
import java.util.TimerTask;

public class ActivitySplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                User user = loadPrefrerences();
                Intent mainIntent;
                if(user.isLooged())
                    mainIntent = new Intent().setClass(ActivitySplashScreen.this, ActivityMain.class);
                else
                    mainIntent = new Intent().setClass(ActivitySplashScreen.this, ActivityLogin.class);
                startActivity(mainIntent);

                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 2000);

    }

    public User loadPrefrerences () {
        SharedPreferences sharedPreferences =
                getSharedPreferences(Constant.USER_PREFERENCES, MODE_PRIVATE);
        User user = new User();
        user.setName(sharedPreferences.getString("USER", null));
        user.setPassword(sharedPreferences.getString("PWD", null));
        user.setLooged(sharedPreferences.getBoolean("LOGGED", false));
        sharedPreferences = null;
        return user;
    }

}
