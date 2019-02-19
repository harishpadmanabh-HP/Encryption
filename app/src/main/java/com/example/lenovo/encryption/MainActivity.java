package com.example.lenovo.encryption;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by ADMIN on 10/10/2017.
 */

public class MainActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    SharedPreferences prefs;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int Userid;
        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Userid= prefs.getInt("userid", 0);
        if(Userid>0){
            Intent i = new Intent(getApplicationContext(),Login.class);
            startActivity(i);
            finish();
        }else{
            // Means  u are not logged in than go to your login pageview from here

            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(getApplicationContext(),Login.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);


        }}
}

