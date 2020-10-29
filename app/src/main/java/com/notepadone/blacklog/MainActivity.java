package com.notepadone.blacklog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.security.NetworkSecurityPolicy;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.notepadone.blacklog.Trucksinfo.TrucksInfo;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE;

        SharedPreferences sh = getSharedPreferences("MySharedPref",MODE_PRIVATE);

        String s = sh.getString("username","");
        if(s.length() == 0){
            logo = findViewById(R.id.logoblacklog);

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    ObjectAnimator fadeIn5523= ObjectAnimator.ofFloat(logo, "alpha", 1f, 0f);
                    fadeIn5523.setDuration(2300);
                    fadeIn5523.start();

                }
            }, 2500);


            final Handler handler2 = new Handler(Looper.getMainLooper());
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);

                }
            }, 4000);
        }
        else{
            Intent intent = new Intent(MainActivity.this, TrucksInfo.class);
            startActivity(intent);
        }

    }
}