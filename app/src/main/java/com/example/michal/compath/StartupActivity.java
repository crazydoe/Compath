package com.example.michal.compath;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;

public class StartupActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private int GPS_PERM_CODE = 100;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERM_CODE);
        }else {
            //ActiveAndroid.initialize(getApplicationContext());
            startMainNaviActicity();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == GPS_PERM_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMainNaviActicity();
            }else {
                finish();
                System.exit(0);
            }
        }else {
            finish();
            System.exit(0);
        }
    }


    private void startMainNaviActicity() {

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);

        preferences.edit().putInt("seconds_refresh_time", R.string.default_refresh_seconds)
                .putInt("meters_refresh_distance", R.string.default_refresh_meters).commit();
        preferences.edit().putBoolean("theme_dark", true)
                .putBoolean("voice_commands", false).commit();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}

