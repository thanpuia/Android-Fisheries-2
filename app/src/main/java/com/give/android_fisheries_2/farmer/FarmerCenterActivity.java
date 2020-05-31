package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;

import es.dmoral.toasty.Toasty;

public class FarmerCenterActivity extends AppCompatActivity {
    FragmentTransaction fragmentTransaction;
    Fragment mFragment;
    Menu menu;
    String mLat,mLng;
    String from;
    int status;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.test_menu,menu);

       // menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //TODO THIS IS TESTING MENU BEFORE STATUS CAN BE FETCH FROM SERVER
        if(item.getItemId() == R.id.zero){
            status = 0;
            changeStatus(status);

        } else if (item.getItemId() == R.id.one){
            status = 1;
            changeStatus(status);
        }else if (item.getItemId() == R.id.two){
            status = 2;
            changeStatus(status);
        }else if (item.getItemId() == R.id.three){
            status = 3;
            changeStatus(status);
        }else if (item.getItemId() == R.id.four){
            status = 4;
            changeStatus(status);
        }
        return true;
    }

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



//    public void twoBtnClick(View view) {
//        mFragment = new FarmerUploadDataFragment();
//        commitFragment();
//    }


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


    public void changeStatus(int mStatus){
        this.status = mStatus;
        mFragment = new FarmerDashboardFragment(status);
        commitFragment();
    }

    public void dashboardButtonClick(View view) {
        if(status == 1) Toasty.success(this,"Downloading ID...",Toasty.LENGTH_SHORT).show();
        else {
            mFragment = new FarmerUploadDataFragment();
            commitFragment();
        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
