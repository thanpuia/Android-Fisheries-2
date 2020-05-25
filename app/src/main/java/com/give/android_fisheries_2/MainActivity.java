package com.give.android_fisheries_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.give.android_fisheries_2.admin.AdminCenterActivity;
import com.give.android_fisheries_2.farmer.FarmerUploadDataActivity;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean mLoginStatus;
    String mRole;
    String TAG = "tag";
    Menu menu;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.header_menu,menu);

        menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.english){
          //  new Logout(getApplicationContext());

        }
        else if (item.getItemId() == R.id.logout){
            new Logout(getApplicationContext());
            startActivity(new Intent(this,LoginActivity.class));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mLoginStatus = sharedPreferences.getBoolean("mLoginStatus",false);

        //TODO :: START :: THIS IS FOR TESTING PURPOSE
      //  mLoginStatus = true;
        //END

        //IF mLoginStatus IS FALSE GO TO LOGIN PAGE
        if(!mLoginStatus)  startActivity(new Intent(this, LoginActivity.class));
        else{
            mRole = sharedPreferences.getString("mRole","noRole");

            //TODO ::::TESTING , SINCE THERE IS NO ROLE IN THE DATABASE. THE ROLE IS CREATED FOR USER FOR TESTING PURPOSE

            //TODO :://ROLE IS ASSAIGN FOR TESTING PURPOSE ONLY. START. THIS IS FOR TESTING PURPOSE ONLY
            String testContact = sharedPreferences.getString("mContact","000");
            if (testContact.equals("9436153649"))
                mRole = "FARMER";
            else if (testContact.equals("7810911046"))
                mRole = "ADMIN";
           // mRole = "FARMER";
           // mRole = "ADMIN";
            //mRole = "SUPER_ADMIN";
            //END

            //SHOW DIFFERENT VIEW ACCORDING TO THEIR ROLES
            switch (mRole){
                case "FARMER":
                    Log.d(TAG,"Role: Farmer");
                    startActivity(new Intent(this, FarmerUploadDataActivity.class));
                    break;

                case "ADMIN":
                    Log.d(TAG,"Role: Admin")  ;
                    startActivity(new Intent(this, AdminCenterActivity.class));

                    break;

                case "SUPER_ADMIN":
                    Log.d(TAG,"Role: Super Admin") ;
                    break;

                default: Log.d(TAG,"Role: Other")   ;break;
            }
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
