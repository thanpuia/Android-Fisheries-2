package com.give.android_fisheries_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.give.android_fisheries_2.registration.LoginActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean mLoginStatus;
    String mRole;
    String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mLoginStatus = sharedPreferences.getBoolean("mLoginStatus",false);

        //IF mLoginStatus IS FALSE GO TO LOGIN PAGE
        if(!mLoginStatus)  startActivity(new Intent(this, LoginActivity.class));
        else{
            mRole = sharedPreferences.getString("mRole","noRole");

            //SHOW DIFFERENT VIEW ACCORDING TO THEIR ROLES
            switch (mRole){
                case "FARMER":
                    Log.d(TAG,"Role: Farmer");

                    break;

                case "ADMIN":
                    Log.d(TAG,"Role: Admin")  ;
                    break;

                case "SUPER_ADMIN":
                    Log.d(TAG,"Role: Super Admin") ;
                    break;

                default: Log.d(TAG,"Role: Other")   ;break;
            }

        }


    }
}
