package com.notepadone.blacklog;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.notepadone.blacklog.Trucksinfo.TrucksInfo;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String lat,longitude;
    ImageView backarrow;
    Toolbar toolbar;
    private EndDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getActionBar().hide();


        }
        catch (NullPointerException e){}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        lat = i.getStringExtra("lat");
        longitude  = i.getStringExtra("long");

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        drawerToggle = new EndDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open_nav_drawer,
                R.string.close_nav_drawer,"maps");


        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //adjust with the longitude and latitude
        if(lat.equalsIgnoreCase("-1") && longitude.equalsIgnoreCase("-1")){
            LatLng sydney = new LatLng(Double.parseDouble(lat),Double.parseDouble(longitude));
            mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            Toast.makeText(getApplicationContext(),"Location not available",Toast.LENGTH_LONG).show();
        }
        else{
            LatLng sydney = new LatLng(Double.parseDouble(lat),Double.parseDouble(longitude));
            mMap.addMarker(new MarkerOptions().position(sydney).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }
}