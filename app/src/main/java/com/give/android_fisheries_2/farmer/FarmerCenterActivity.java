package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.give.android_fisheries_2.R;

public class FarmerCenterActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    Fragment mFragment;

    String mLat,mLng;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);

        try{
            Bundle extras = getIntent().getExtras();
            if(extras!=null){

                    //:::::TODO  SENDING DATA TO FRAGMENT FROM ACTIVITY
                    String lat = extras.getString("lat");
                    String lng = extras.getString("lng");
                    Log.d("TAG","FARMERCenter: "+lat);

                    Bundle bundle = new Bundle();
                    bundle.putString("lat",lat);
                    bundle.putString("lng",lng);

                    mFragment = new FarmerUploadDataFragment();
                    mFragment.setArguments(bundle);
                    commitFragment();

            }


        }catch (Exception e){}

        mFragment = null;

    }

    public void oneBtnClick(View view) {
    }

    public void twoBtnClick(View view) {
        mFragment = new FarmerUploadDataFragment();
        commitFragment();
    }

    public void threeBtnClick(View view) {
    }
    public void commitFragment(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.farmer_frame_layout,mFragment);
        fragmentTransaction.commit();
    }

    public void getLocationClick(View view) {
        startActivity(new Intent(this,GetLocationInMapActivity.class));
    }


    //FARMERS DATA UPLOAD XML ATANG
    public void selectPhoto(View view) {
    }
}
