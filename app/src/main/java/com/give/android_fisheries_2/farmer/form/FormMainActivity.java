package com.give.android_fisheries_2.farmer.form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.give.android_fisheries_2.R;

import es.dmoral.toasty.Toasty;

public class FormMainActivity extends AppCompatActivity {

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;

    String fathersName;
    String address;
    String district;
    String epicAadhaar;

    String locationOfPonds;
    String tehsil;
    String area;

    FragmentManager fragmentManager;
    int formPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_main);

        fragment1 = new FormStep1Fragment();
        fragment2 = new FormStep2Fragment();
        fragment3 = new FormStep3Fragment();
        formPage = 1;
        fragmentManager = getSupportFragmentManager();

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

            fragmentManager.beginTransaction()
                    .replace(R.id.form_main_frame_layout,fragment2)
                    .commit();
            formPage = 3;
        }else if(formPage == 3){
            //GET THE VALUE FROM STEP 2
            locationOfPonds = FormStep2Fragment.locationOfPond.getText().toString();
            tehsil          = FormStep2Fragment.tehsilofPond.getSelectedItem().toString();
            area            = FormStep2Fragment.locationOfPond.getText().toString();

            fragmentManager.beginTransaction()
                    .replace(R.id.form_main_frame_layout,fragment3)
                    .commit();
            formPage = 1;
        }


    }
}
