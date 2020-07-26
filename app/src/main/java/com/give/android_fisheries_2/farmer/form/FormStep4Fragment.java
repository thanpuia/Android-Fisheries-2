package com.give.android_fisheries_2.farmer.form;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.farmer.FarmerUploadDataActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import static com.give.android_fisheries_2.farmer.form.FormMainActivity.formMainButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormStep4Fragment extends Fragment {
    SharedPreferences sharedPreferences;
    private MapView mMapView;
    private GoogleMap googleMap;
    Location myLocation;
    LatLng userLocationSet;
    boolean mapMove = false;
    Button savedLocationButton;

    TextView saveLocation;


    public FormStep4Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_form_step4, container, false);
        sharedPreferences = v.getContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        mMapView = v.findViewById(R.id.mapViewNew);
        saveLocation = v.findViewById(R.id.save_location_header_tv);

        saveLocation.setText(FormMainActivity.labelSaveLocation);

        //savedLocationButton = v.findViewById(R.id.locationsavedClick);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        }catch (Exception e){}
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mGoogleMap) {
                googleMap = mGoogleMap;

                //INITAIL CAMERA POSTITION MIZORAM
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(23.725173, 92.716037))      // Sets the center of the map TO MIZORAM
                        .zoom(8)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setMyLocationEnabled(true);
                googleMap.setPadding(0,180,0,0);
                formMainButton.setVisibility(View.VISIBLE);

                if(googleMap!=null){
                    googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                        @Override
                        public void onMyLocationClick(@NonNull Location location) {
                            myLocation = location;
                            LatLng mLastKnown = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnown.latitude,mLastKnown.longitude), 13));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(new LatLng(mLastKnown.latitude,mLastKnown.longitude))      // Sets the center of the map to location user
                                    .zoom(17)                   // Sets the zoom
                                    //   .bearing(90)                // Sets the orientation of the camera to east
                                    //     .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                                    .build();                   // Creates a CameraPosition from the builder
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                    });
                }

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        LatLng midLatLng = googleMap.getCameraPosition().target;
                        //Toast.makeText(getApplicationContext(),"Location:"+midLatLng,Toast.LENGTH_SHORT).show();
                        userLocationSet = new LatLng(midLatLng.latitude,midLatLng.longitude);
                        mapMove = true;

                        //SET LOCATION EVERY MOVE
                        if(userLocationSet.latitude==0.0f){
                            sharedPreferences.edit().putString("lat","0").apply();
                            sharedPreferences.edit().putString("lng","0").apply();
                        }else{
                            String lat = String.valueOf(userLocationSet.latitude);
                            String lng = String.valueOf(userLocationSet.longitude);
                            sharedPreferences.edit().putString("lat",lat).apply();
                            sharedPreferences.edit().putString("lng",lng).apply();
                        }
                    }
                });
            }
        });

//        savedLocationButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try{
//                    if(userLocationSet.latitude==0.0f) Toast.makeText(getActivity(),"Please move the map!",Toast.LENGTH_SHORT).show();
//                    else {
//                        FarmerUploadDataActivity.locationCheck = true;
//                        Toast.makeText(getActivity(), "Location:" + userLocationSet, Toast.LENGTH_SHORT).show();
//                        String lat = String.valueOf(userLocationSet.latitude);
//                        String lng = String.valueOf(userLocationSet.longitude);
//                        sharedPreferences.edit().putString("lat",lat).apply();
//                        sharedPreferences.edit().putString("lng",lng).apply();
//                        sharedPreferences.edit().putBoolean("location_click",true).apply();
//
//                       // startActivity(new Intent(this,FarmerUploadDataActivity.class));
//                      //  finish();
//                        //   sharedPreferences.edit().putString("mContact",mContact).apply();
//
//
//                    }
//                }catch (Exception e){}
//            }
//        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
