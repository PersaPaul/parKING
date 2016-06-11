package com.persasrl.paul.parking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Console;

public class parKING extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,  ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    public double lat,lng;
    public float bearing;
    public int mere=0;
    Location location;
    public Intent serviceIntent = new Intent(this, UserLocation.class);


    public static final CameraPosition cluj =
            new CameraPosition.Builder().target(new LatLng(46.7699110,23.5872390))
                    .zoom(19.0f)
                    .bearing(60)
                    .tilt(60)
                    .build();

    public static CameraPosition actual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_king);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap=googleMap;
       // mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();
        // Add a marker in Sydney and move the camera
        // mMap.addMarker(new MarkerOptions().position(cluj).title("Marker in Sydney"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(46.7772, 23.5999)));

        }

    private void enableMyLocation() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
                LocationReciever, new IntentFilter("GPSLocationUpdates"));
         startService(serviceIntent);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            //mMap.setMyLocationEnabled(true);
            startService(serviceIntent);

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if(mere==1)
            Toast.makeText(this,"Mere amu nii", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"Mnoh nu mere", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        stopService(serviceIntent);
        super.onStop();
    }

    private BroadcastReceiver LocationReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            mere=1;
            Toast.makeText(parKING.this, "Hai boss", Toast.LENGTH_SHORT).show();
            String message = intent.getStringExtra("Status");
            Bundle b = intent.getBundleExtra("Location");
            location = b.getParcelable("Location");
            actual = new CameraPosition.Builder().target(new LatLng(location.getLatitude(),location.getLongitude()))
                            .zoom(19.0f)
                            .bearing(location.getBearing())
                            .tilt(60)
                            .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(actual));
        }
    };




}
