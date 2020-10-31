package com.notepadone.blacklog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.notepadone.blacklog.Trucksinfo.TrucksInfo;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class MainActivity2 extends AppCompatActivity {

    SliderAdapter sliderAdapter;
    ViewPager viewPager;
    LinearLayout dots;
    private TextView[] mDots;
    Button next;
    MqttAndroidClient client;
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
        setContentView(R.layout.activity_main2);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String usernameString = sharedPref.getString("usernameShared","");
        if(usernameString.length() == 0) {
            next = findViewById(R.id.next);
            viewPager = findViewById(R.id.sliderViewPager);
            dots = findViewById(R.id.dots);
            next.setVisibility(View.INVISIBLE);
            sliderAdapter = new SliderAdapter(this);
            viewPager.setAdapter(sliderAdapter);

            setupMqttServer();
            addDotIndicator(0);
            viewPager.addOnPageChangeListener(viewListener);
            listeners();
        }
        else
        {
            Intent intent = new Intent(MainActivity2.this, TrucksInfo.class);
            startActivity(intent);
        }
    }

    public void setupMqttServer(){
        try {


            String clientId = MqttClient.generateClientId();
            client = new MqttAndroidClient(MainActivity2.this, "tcp://otomator.com:1883",
                    clientId);

            IMqttToken token = client.connect();


            token.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(getApplicationContext(),"onSuccess",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
                }
            });
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //Toast.makeText(TrucksInfo.this,new String(message.getPayload())+"",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void listeners(){

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void addDotIndicator(int position){
        mDots = new TextView[3];
        dots.removeAllViews();
        for(int i =0;i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            dots.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorwhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if(position == 2){
                next.setVisibility(View.VISIBLE);
            }else {
                next.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageSelected(int position) {

            addDotIndicator(position);
            if(position == 3){
                next.setVisibility(View.VISIBLE);
            }else {
                next.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    public void onBackPressed() {
        finish();

    }
}