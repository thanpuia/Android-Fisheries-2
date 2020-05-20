package com.give.android_fisheries_2.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Logout {

    SharedPreferences sharedPreferences;


    public Logout(Context c) {
        sharedPreferences = c.getApplicationContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("mLoginStatus",false).apply();
        sharedPreferences.edit().putInt("mId",0).apply();
        sharedPreferences.edit().putString("mName",null).apply();
        sharedPreferences.edit().putString("mToken",null).apply();
        sharedPreferences.edit().putString("mContact",null).apply();
      //  sharedPreferences.edit().putString("mRole",null).apply();

        Toast.makeText(c,"LOGOUT!",Toast.LENGTH_SHORT).show();
    }
}
