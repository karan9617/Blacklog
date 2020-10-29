package com.notepadone.blacklog;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.notepadone.blacklog.Trucksinfo.TrucksInfo;

import org.w3c.dom.Text;

import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class EndDrawerToggle implements DrawerLayout.DrawerListener {

    private DrawerLayout drawerLayout;
    private DrawerArrowDrawable arrowDrawable;
    private AppCompatImageButton toggleButton;
    private String openDrawerContentDesc;
    String diff= "";
    private String closeDrawerContentDesc;
    ImageView imageView, backarrow;

    public EndDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar,
                           int openDrawerContentDescRes, int closeDrawerContentDescRes,String diff) {

        this.drawerLayout = drawerLayout;
        this.openDrawerContentDesc = activity.getString(openDrawerContentDescRes);
        this.closeDrawerContentDesc = activity.getString(closeDrawerContentDescRes);

        this.diff = diff;
        arrowDrawable = new DrawerArrowDrawable(toolbar.getContext());
        arrowDrawable.setDirection(DrawerArrowDrawable.ARROW_DIRECTION_END);

        backarrow = new ImageView(activity);
        backarrow.setImageResource(R.drawable.ic_baseline_arrow_back_ios_24);

        imageView = new ImageView(activity);
        imageView.setImageResource(R.drawable.cross_icon);

        TextView textView = new TextView(activity);
        textView.setTextColor(Color.parseColor("#D4AF37"));
        textView.setTextSize(22);
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,20,0,0);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        //textView.setLayoutParams(params);

        if(diff.equalsIgnoreCase("maps")){
            textView.setText("Maps");
        }
        else{
            textView.setText("Trucks Lists");
        }

        toggleButton = new AppCompatImageButton(activity, null, R.attr.toolbarNavigationButtonStyle);

        toolbar.addView(toggleButton, new Toolbar.LayoutParams(GravityCompat.END));


        toolbar.addView(textView, params);
        toolbar.addView(backarrow, new Toolbar.LayoutParams(GravityCompat.START));

        //toolbar.addView(imageView, new Toolbar.LayoutParams(GravityCompat.END));

        toggleButton.setImageDrawable(arrowDrawable);
        toggleButton.setOnClickListener(new OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                toggle();
                                            }
                                        }
        );

        backarrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TrucksInfo.class);
                activity.startActivity(intent);
            }
        });
    }

    public void syncState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            setPosition(1f);
        }
        else {
            setPosition(0f);
        }
    }

    public void toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.setBackgroundColor(Color.parseColor("83322828"));
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    public void setPosition(float position) {
        if (position == 1f) {
            arrowDrawable.setVerticalMirror(true);
            toggleButton.setContentDescription(closeDrawerContentDesc);
        }
        else if (position == 0f) {
            arrowDrawable.setVerticalMirror(false);
            toggleButton.setContentDescription(openDrawerContentDesc);
        }
        arrowDrawable.setProgress(position);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        setPosition(Math.min(1f, Math.max(0, slideOffset)));
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        setPosition(1f);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setPosition(0f);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}