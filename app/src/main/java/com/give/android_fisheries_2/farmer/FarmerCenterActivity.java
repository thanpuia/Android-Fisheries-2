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
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);

        mFragment = null;
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

            }else {
                mFragment = new FarmerDashboardFragment();
                commitFragment();
            }


        }catch (Exception e){}

        //TESTING
        changeStatus(0);

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

    public void zero2TestingBtnClick(View view) {
        status = 0;
        changeStatus(status);
    }
    public void one2TestingBtnClick(View view) {
        status = 1;
        changeStatus(status);
    }

    public void two2TestingBtnClick(View view) {
        status = 2;
        changeStatus(status);
    }

    public void three2TestingBtnClick(View view) {
        status = 3;
        changeStatus(status);
    }

    public void four2TestingBtnClick(View view) {
        status = 4;
        changeStatus(status);
    }

    public void changeStatus(int mStatus){
        this.status = mStatus;
        mFragment = new FarmerDashboardFragment(status);
        commitFragment();
    }
}
