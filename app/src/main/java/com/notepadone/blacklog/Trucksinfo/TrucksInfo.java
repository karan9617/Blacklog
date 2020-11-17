package com.notepadone.blacklog.Trucksinfo;

/**
 *          Dispying all the information about the trucks
 *          LID STATUS, SPEEDS, STATUS
 *
 *
 * **/


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.notepadone.blacklog.Adapters.TruckInfoAdapter;
import com.notepadone.blacklog.EndDrawerToggle;
import com.notepadone.blacklog.LoginActivity;
import com.notepadone.blacklog.MainActivity2;
import com.notepadone.blacklog.Objects.ListObjectTrucks;
import com.notepadone.blacklog.Objects.TrucksObject;
import com.notepadone.blacklog.R;
import com.notepadone.blacklog.ServiceForUpdate;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class TrucksInfo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ClientList {

    RecyclerView recyclerView;
    Toolbar toolbar;
    //ImageView backarrow;
    ProgressBar progress_circular;
    List<TrucksObject> trucksObjectList;
    MqttAndroidClient client;
    private EndDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;


    NavigationView navigationView;
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
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        progress_circular = findViewById(R.id.progress_circular);
        progress_circular.setVisibility(View.VISIBLE);
        Intent serviceIntent = new Intent(TrucksInfo.this, ServiceForUpdate.class);
        serviceIntent.putExtra("inputExtra", "input");


        //ContextCompat.startForegroundService(TrucksInfo.this, serviceIntent);

        toolbar = findViewById(R.id.toolbar);

      //  backarrow = findViewById(R.id.backarrow);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        trucksObjectList = new ArrayList<>();
        getInitialTrucksInfo();
       // mHandler = new Handler();
       // startRepeatingTask();


            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    progress_circular.setVisibility(View.INVISIBLE);
                }
            }, 2000);

        //listeners();
        //createNotificationChannel();

        drawerToggle = new EndDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open_nav_drawer,
                R.string.close_nav_drawer,"list");
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        try {
            String clientId = MqttClient.generateClientId();
            client = new MqttAndroidClient(TrucksInfo.this, "tcp://otomator.com:1883", clientId);
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //Toast.makeText(getApplicationContext(),"onSuccess",Toast.LENGTH_SHORT).show();
                    if(!ServiceForUpdate.isRunning) {


                        Intent serviceIntent = new Intent(TrucksInfo.this, ServiceForUpdate.class);
                        ContextCompat.startForegroundService(TrucksInfo.this, serviceIntent);
                    }
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                   // Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
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
    // connecting the mttq server fot information about trucks


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
             //   getTruckVitals(); //this function can change value of mInterval.
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    public void getInitialTrucksInfo(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://api.blacklog.in/vehicle/get_vehicle.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                          //  Toast.makeText(getApplicationContext(),"success:"+new String(response),Toast.LENGTH_LONG).show();
                            Log.e("LOG_VOLLEYResponse", response.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response));
                                JSONArray jsonArrayForVehicles = jsonObject.getJSONArray("vehicles");
                                //now looping through all the elements of the json array
                                for (int i = 0; i < jsonArrayForVehicles.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject vehicleObject = jsonArrayForVehicles.getJSONObject(i);

                                    //creating a hero object and giving them the values from json object
                                    long time = Long.parseLong(vehicleObject.getString("lid_status_timestamp"));
                                    double t = (time)/3600;
                                    ClientList.map.put(vehicleObject.getString("device_id"),vehicleObject.getString("vehicle_no"));
                                    ClientList.clientList.add(vehicleObject.getString("device_id"));
                                    trucksObjectList.add(new TrucksObject(vehicleObject.getString("vehicle_longitude"),vehicleObject.getString("vehicle_latitude"),vehicleObject.getString("lid_status_timestamp"),vehicleObject.getString("device_id"),vehicleObject.getString("vehicle_no"),vehicleObject.getString("blacklog_model"),vehicleObject.getString("lid_status"),vehicleObject.getString("lid_status"),"NA"));
                                }
                                TruckInfoAdapter adapter = new TruckInfoAdapter(getApplicationContext(),trucksObjectList);
                                //recyclerView.getRecycledViewPool().clear();
                                //adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.e("LOG_VOLLEYResponseError", error.toString());

                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("user_id","1234567890cwcQd6vAcg");
                    params.put("vehicle_no","");
                    return params;
                }

            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }
    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }



    @Override
    public void onBackPressed() {

        this.finishAffinity();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_logout:
                SharedPreferences sh = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                sh.edit().clear().commit();
                Intent intent = new Intent(TrucksInfo.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}