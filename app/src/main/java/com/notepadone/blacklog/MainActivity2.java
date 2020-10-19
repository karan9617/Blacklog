package com.notepadone.blacklog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
    Button next,publish;
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

        next = findViewById(R.id.next);
        publish = findViewById(R.id.publish);
        viewPager = findViewById(R.id.sliderViewPager);
        dots = findViewById(R.id.dots);
        next.setVisibility(View.INVISIBLE);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);


        addDotIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
        Intent intent = new Intent(MainActivity2.this, TrucksInfo.class);
        startActivity(intent);

        listeners();

        try {


        String clientId = MqttClient.generateClientId();
         client = new MqttAndroidClient(MainActivity2.this, "tcp://otoserver.xyz:1883",
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
                    Toast.makeText(MainActivity2.this,new String(message.getPayload())+"",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
//1883

           /* String topic1 = "testtopic/karanvish";
            String payload1 = "the payload";
            byte[] encodedPayload = new byte[0];
            try {
                encodedPayload = payload1.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                client.publish(topic1, message);
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
            }


            */
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    public void subscribe(View v){

    }
    public void publishMethod(){
        String topic = "BL00001";
        String payload = "the payload";


        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            client.publish(topic, message);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    try {
                        Toast.makeText(getApplicationContext(), token.getMessage() + " ", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
    public void listeners(){
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishMethod();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (client.isConnected()) {
                        Log.d("tag","client.isConnected()>>" + client.isConnected());

                        client.subscribe("BL00005", 1);
                        client.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable cause) {
                                Log.d("tag","message>> connection lost");
                            }
//location: speed bhi daalna
                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                Log.d("tag","message>>" + new String(message.getPayload()));
                                Log.d("tag","topic>>" + topic);
                                //parseMqttMessage(new String(message.getPayload()));

                            }

                            @Override
                            public void deliveryComplete(IMqttDeliveryToken token) {

                            }
                        });
                    }
                } catch (Exception e) {
                    Log.d("tag","Error :" + e);
                }

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