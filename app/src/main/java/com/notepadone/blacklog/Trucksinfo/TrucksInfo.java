package com.notepadone.blacklog.Trucksinfo;

/**
 *          Dispying all the information about the trucks
 *          LID STATUS, SPEEDS, STATUS
 * **/


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.notepadone.blacklog.Adapters.TruckInfoAdapter;
import com.notepadone.blacklog.LoginActivity;
import com.notepadone.blacklog.MainActivity2;
import com.notepadone.blacklog.Objects.ListObjectTrucks;
import com.notepadone.blacklog.Objects.TrucksObject;
import com.notepadone.blacklog.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrucksInfo extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    ImageView backarrow;
    List<TrucksObject> trucksObjectList;
    MqttAndroidClient client;

    private int mInterval = 2000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "exampleServiceChannel",
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
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
        setContentView(R.layout.activity_trucks_info);

        toolbar = findViewById(R.id.toolbar);
        backarrow = findViewById(R.id.backarrow);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trucksObjectList = new ArrayList<>();
/*
        trucksObjectList.add(new TrucksObject("Signal","BL0002345","NA","NA"));
        trucksObjectList.add(new TrucksObject("Signal","BL0002345","NA","NA"));
        trucksObjectList.add(new TrucksObject("Signal","BL0002345","NA","NA"));
        trucksObjectList.add(new TrucksObject("Signal","BL0002345","NA","NA"));
*/
        mHandler = new Handler();
        startRepeatingTask();

        listeners();
        createNotificationChannel();
     //   loadHeroList();
        try {


            String clientId = MqttClient.generateClientId();
            client = new MqttAndroidClient(TrucksInfo.this, "tcp://otoserver.xyz:1883",
                    clientId);

            IMqttToken token = client.connect();


            token.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                   //Toast.makeText(getApplicationContext(),"onSuccess",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TrucksInfo.this,new String(message.getPayload())+"",Toast.LENGTH_SHORT).show();
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
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrucksInfo.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    // connecting the mttq server fot information about trucks
    public void getTruckVitals(){
        try {
            if (client.isConnected()) {
                Log.d("tag","client.isConnected()>>" + client.isConnected());

                mInterval  =20000;
                client.subscribe("BL00001", 1);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.d("tag", "message>> connection lost");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d("tag","message>>" + new String(message.getPayload()));
                        Log.d("tag","topic>>" + topic);

                        trucksObjectList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(message.getPayload()));

                            trucksObjectList.add(new TrucksObject("Signal",jsonObject.getString("did"),jsonObject.getString("lid_status"),topic,jsonObject.getString("speed")));
                            trucksObjectList.add(new TrucksObject("Signal","BL0002345","NA","NA",jsonObject.getString("speed")));
                            trucksObjectList.add(new TrucksObject("Signal","BL0002345","NA","NA",jsonObject.getString("speed")));
                            trucksObjectList.add(new TrucksObject("Signal","BL0002345","NA","NA",jsonObject.getString("speed")));

                            TruckInfoAdapter adapter = new TruckInfoAdapter(getApplicationContext(),trucksObjectList);

                            recyclerView.setAdapter(adapter);
                        }catch (JSONException err){
                            Log.d("Error", err.toString());
                        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                getTruckVitals(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception

                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };


    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void loadHeroList() {
        //getting the progressbar
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.blacklog.otoserver.xyz/vehicle/get_vehicle_types.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("vehicle_types");
                            for (int i = 0; i < heroArray.length(); i++) {
                                JSONObject heroObject = heroArray.getJSONObject(i);
                                TrucksObject hero = new TrucksObject(heroObject.getString("vehicle_code"), heroObject.getString("vehicle_type"),
                                        heroObject.getString("vehicle_description"),heroObject.getString("vehicle_type"),"");

                                trucksObjectList.add(hero);
                            }
                            TruckInfoAdapter adapter = new TruckInfoAdapter(getApplicationContext(),trucksObjectList);

                            recyclerView.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}