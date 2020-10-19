package com.notepadone.blacklog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    SliderAdapter(Context context ){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.screen2,
            R.drawable.screen1,
            R.drawable.screen3
    };
    public String[] descArray = {
            "Enter some details and add your vehicle on click.",
            "Live track your vehicle on maps & manage their trips.",
            "Get notified when fuel lid is operated & live track fuel lid."
    };
    public String[] slide_headings={
            "Create Fleet",
            "Manage Fleet",
            "Prevent Fuel theft"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout)object;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);


        ImageView  slideImageview = view.findViewById(R.id.image);
        TextView heading = view.findViewById(R.id.mainheading);
        TextView desc = view.findViewById(R.id.desc);

        slideImageview.setImageResource(slide_images[position]);
        heading.setText(slide_headings[position]);
        desc.setText(descArray[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
