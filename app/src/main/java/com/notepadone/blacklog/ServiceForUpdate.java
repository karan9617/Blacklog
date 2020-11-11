package com.notepadone.blacklog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.notepadone.blacklog.Adapters.TruckInfoAdapter;
import com.notepadone.blacklog.Objects.TrucksObject;
import com.notepadone.blacklog.Trucksinfo.ClientList;
import com.notepadone.blacklog.Trucksinfo.TrucksInfo;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ServiceForUpdate extends Service implements ClientList {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    private int mInterval = 20000; // in milliseconds
    private Handler mHandler;
    MqttAndroidClient client;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, "exampleServiceChannel")
                .setContentTitle("Blacklog")
                .setContentText("Your vehicles are secured.. :)")
                .setSmallIcon(R.drawable.playstore)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
        mHandler = new Handler();
        startRepeatingTask();
        try {

            String clientId = MqttClient.generateClientId();
            client = new MqttAndroidClient(getApplicationContext(), "tcp://otomator.com:1883",
                    clientId);
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                   // Toast.makeText(getApplicationContext(),"onSuccess",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getApplicationContext(),"Could not connect. Please refresh the page..",Toast.LENGTH_SHORT).show();
                }
            });
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d("tag","message>>" + new String(message.getPayload()));
                    //Toast.makeText(getApplicationContext(),new String(message.getPayload())+"",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void getTruckVitals(){
        try {
            if (client.isConnected()) {
                Log.d("tag","client.isConnected()>>" + client.isConnected());

                for(String s: ClientList.clientList){
                    client.subscribe(s,1);
                }
                //client.subscribe("BL00001", 1);
                client.setCallback(new MqttCallback() {

                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.d("tag", "message>> connection lost");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        Log.d("tag","message>>" + new String(message.getPayload()));
                        Log.d("tag","topic>>" + topic);

                        JSONObject jsonObject = new JSONObject(new String(message.getPayload()));
                        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
/*
*   closed open and closed (open only notification)
* */
                        String vehicle_no = ClientList.map.get(jsonObject.getString("did"));
                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "exampleServiceChannel")
                                .setContentTitle(topic)
                                .setContentText( vehicle_no+ " has Lid Status :"+ jsonObject.getString("lid_status"))
                                .setSmallIcon(R.drawable.playstore).setContentIntent(pendingIntent).build();
                        NotificationManager notificationManager=  (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        int cv = Integer.parseInt(topic.substring(topic.length()-1));
                        Random rn  = new Random();

                        int c = rn.nextInt();
                        if(c == 1){
                            notificationManager.notify(2,notification);

                        }
                        else{
                            notificationManager.notify(rn.nextInt(9),notification);
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
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                getTruckVitals(); //this function can change value of mInterval.
            } finally {
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
}
