package com.give.android_fisheries_2.farmer.form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.give.android_fisheries_2.R;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class FormMainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;

    //STEP 0
    String name;
    String contact;

    //STEP 1
    String fathersName;
    String address;
    String district;
    String epicAadhaar;

    //STEP 2
    String locationOfPonds;
    String tehsil;
    String area;

    //STEP 3
    String local_path_str_image;
    String local_path_str_pond1;
    String local_path_str_pond2;
    String local_path_str_pond3;
    String local_path_str_pond4;

    //STEP 4
    String lat;
    String lng;


    FragmentManager fragmentManager;
    int formPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("mName","");
        contact = sharedPreferences.getString("mContact","");

        fragment1 = new FormStep1Fragment();
        fragment2 = new FormStep2Fragment();
        fragment3 = new FormStep3Fragment();
        fragment4 = new FormStep4Fragment();

        formPage = 1;
        fragmentManager = getSupportFragmentManager();

//        fragmentManager.beginTransaction()
//                .replace(R.id.form_main_frame_layout,fragment1)
//                .commit();
    }

    public void nextClick(View view) {
        if(formPage ==1){

            fragmentManager.beginTransaction()
                    .replace(R.id.form_main_frame_layout,fragment1)
                    .commit();
            formPage = 2;
        }else if(formPage == 2){
            //GET THE VALUE FROM STEP 1
            fathersName = FormStep1Fragment.fathersName.getText().toString();
            address     = FormStep1Fragment.address.getText().toString();
            district    = FormStep1Fragment.district.getSelectedItem().toString();
            epicAadhaar = FormStep1Fragment.epicAadhaar.getText().toString();
            Log.d("TAG","Father: "+FormStep1Fragment.fathersName.getText());

            //SAVED TO SHARED PREFERENCE
            sharedPreferences.edit().putString("fname",fathersName).apply();
            sharedPreferences.edit().putString("address",address).apply();
            sharedPreferences.edit().putString("district",district).apply();
            sharedPreferences.edit().putString("epic_no",epicAadhaar).apply();

            fragmentManager.beginTransaction()
                    .replace(R.id.form_main_frame_layout,fragment2)
                    .commit();
            formPage = 3;
        }else if(formPage == 3){
            //GET THE VALUE FROM STEP 2
            locationOfPonds = FormStep2Fragment.locationOfPond.getText().toString();
            tehsil          = FormStep2Fragment.tehsilofPond.getSelectedItem().toString();
            area            = FormStep2Fragment.locationOfPond.getText().toString();

            //SAVED THE SHARED PREFERENCE
            sharedPreferences.edit().putString("location_of_pond",locationOfPonds).apply();
            sharedPreferences.edit().putString("tehsil",tehsil).apply();
            sharedPreferences.edit().putString("area",area).apply();

            fragmentManager.beginTransaction()
                    .replace(R.id.form_main_frame_layout,fragment3)
                    .commit();
            formPage = 4;
        }else if (formPage == 4){

            local_path_str_pond1 = sharedPreferences.getString("pond1_local","");
            local_path_str_pond2 = sharedPreferences.getString("pond2_local","");
            local_path_str_pond3 = sharedPreferences.getString("pond3_local","");
            local_path_str_pond4 = sharedPreferences.getString("pond4_local","");

            fragmentManager.beginTransaction()
                    .replace(R.id.form_main_frame_layout,fragment4)
                    .commit();
            formPage = 5;
        }
    }

    public void submitEverythingFormStep4(View view) {
        Log.d("TAG","sfsdfsf");
        lat = sharedPreferences.getString("lat","");
        lng = sharedPreferences.getString("lng","");

        Log.d("TAG",""+name+contact+fathersName+address+tehsil+area+locationOfPonds+local_path_str_pond2+local_path_str_pond1+lat   );

    }
}
