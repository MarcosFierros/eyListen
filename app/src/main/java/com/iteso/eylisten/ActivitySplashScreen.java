package com.iteso.eylisten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.iteso.eylisten.Tools.Constant;
import com.iteso.eylisten.beans.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                /*User user = loadPrefrerences();
                Intent mainIntent;
                if(user.isLooged())
                    mainIntent = new Intent().setClass(ActivitySplashScreen.this, ActivityMain.class);
                else
                    mainIntent = new Intent().setClass(ActivitySplashScreen.this, ActivityLogin.class);
                startActivity(mainIntent);

                finish();*/
                Intent intent;
                if(AccessToken.getCurrentAccessToken() != null)
                    intent = new Intent(getApplicationContext(), ActivityMain.class);
                 else
                    intent = new Intent(getApplicationContext(), ActivityLogin.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 1000);
        /*
        try {
            PackageInfo info =
                    getPackageManager().getPackageInfo("com.iteso.eyListen",
                            PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/

    }

    /*public User loadPrefrerences () {
        SharedPreferences sharedPreferences =
                getSharedPreferences(Constant.USER_PREFERENCES, MODE_PRIVATE);
        User user = new User();
        user.setName(sharedPreferences.getString("USER", null));
        user.setPassword(sharedPreferences.getString("PWD", null));
        user.setLooged(sharedPreferences.getBoolean("LOGGED", false));
        sharedPreferences = null;
        return user;
    }*/

}
