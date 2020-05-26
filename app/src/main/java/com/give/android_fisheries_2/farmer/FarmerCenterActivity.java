package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.give.android_fisheries_2.R;

public class FarmerCenterActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);

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
}
