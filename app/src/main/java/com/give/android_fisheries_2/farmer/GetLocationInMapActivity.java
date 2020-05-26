package com.give.android_fisheries_2.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.GetCurrentLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class GetLocationInMapActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback, OnMapReadyCallback {
    LatLng userLocationSet;
    Location myLocation;

    FusedLocationProviderClient fusedLocationProviderClient;
    boolean mapMove = false;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location_in_map);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        myLocation = location;

        Log.d("TAG"," Location "+myLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapMoving);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng mLastKnown = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnown.latitude,mLastKnown.longitude), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLastKnown.latitude,mLastKnown.longitude))      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
             //   .bearing(90)                // Sets the orientation of the camera to east
           //     .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng midLatLng = googleMap.getCameraPosition().target;
                //Toast.makeText(getApplicationContext(),"Location:"+midLatLng,Toast.LENGTH_SHORT).show();
                userLocationSet = new LatLng(midLatLng.latitude,midLatLng.longitude);
                mapMove = true;
            }
        });
    }

    public void imageLockClick(View view) {
        if(userLocationSet.latitude==0.0f) Toast.makeText(getApplicationContext(),"Please move the map!",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(getApplicationContext(),"Location:"+ userLocationSet,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,FarmerCenterActivity.class);
            intent.putExtra("lat",userLocationSet.latitude);
            intent.putExtra("lng",userLocationSet.longitude);
            intent.putExtra("from","getLocation");
            startActivity(intent);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
