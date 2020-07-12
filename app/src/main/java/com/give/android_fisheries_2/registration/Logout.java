package com.give.android_fisheries_2.registration;

import android.content.Context;
import android.content.Intent;
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
        sharedPreferences.edit().putString("mRole",null).apply();
        sharedPreferences.edit().putString("image",null).apply();


        sharedPreferences.edit().putString("fname","").apply();
        sharedPreferences.edit().putString("address","").apply();
        sharedPreferences.edit().putString("district","").apply();
        sharedPreferences.edit().putString("location_of_pond","").apply();
        sharedPreferences.edit().putString("tehsil","").apply();
        sharedPreferences.edit().putString("all_tehsil","").apply();

        //sharedPreferences.edit().putString("image","").apply();
        sharedPreferences.edit().putString("area","").apply();
        sharedPreferences.edit().putString("epic_no","").apply();
        sharedPreferences.edit().putString("name_of_scheme","").apply();
        //sharedPreferences.edit().putString("pondImages","").apply();
        sharedPreferences.edit().putString("lat","").apply();
        sharedPreferences.edit().putString("lng","").apply();
        //  sharedPreferences.edit().putString("mRole","").apply();
        sharedPreferences.edit().putString("approve","").apply();
        sharedPreferences.edit().putString("image","").apply();
        sharedPreferences.edit().putString("pond1","").apply();
        sharedPreferences.edit().putString("pond2","").apply();
        sharedPreferences.edit().putString("pond3","").apply();
        sharedPreferences.edit().putString("pond4","").apply();



        Toast.makeText(c,"LOGOUT!",Toast.LENGTH_SHORT).show();

    }
}
