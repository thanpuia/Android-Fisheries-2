package com.give.android_fisheries_2.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SmsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Spinner tehsilSpinner;
    EditText messageET;
    TextView userNameTv;
    ArrayList<String> tehsilList;
    Button farmerBtn, smsBtn;

    String mToken;
    String tempMessage;
    String tempSpinner;
    String tempUserName;
    String SMS_API_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.m_toolbar_sms);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME );

        SMS_API_URL = MainActivity.MAIN_URL+"api/smsapi";
        sharedPreferences = getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        tehsilSpinner = findViewById(R.id.tehsil_spinner);
        messageET = findViewById(R.id.sms_et_message);
        userNameTv = findViewById(R.id.sms_from_tv);
        farmerBtn = findViewById(R.id.farmersBtnSMS);
        smsBtn = findViewById(R.id.smsBtnSMS);

        mToken = sharedPreferences.getString("mToken","");
        tehsilList = new ArrayList<>();
        tehsilList= stringToArrayList(sharedPreferences.getString("tehsil",""));
        bottomButton();

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, tehsilList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tehsilSpinner.setAdapter(spinnerArrayAdapter);

        //POPULATE IF ALREADY
        tempMessage = sharedPreferences.getString("sms_message","");
        tempSpinner = sharedPreferences.getString("sms_tehsil","");
        tempUserName = sharedPreferences.getString("mName","");
        messageET.setText(tempMessage);
        int spinnerPosition = spinnerArrayAdapter.getPosition(tempSpinner);
        tehsilSpinner.setSelection(spinnerPosition);
        userNameTv.setText("From: " + tempUserName);

    }

    public void smsSendClick(View view) {

        Ion.with(this)
                .load("POST", SMS_API_URL )
              //  .setHeader("Accept","application/json")
             //   .setHeader("Authorization","Bearer "+mToken)
                .setMultipartParameter("tehsil",tehsilSpinner.getSelectedItem().toString())
                .setMultipartParameter("message",messageET.getText().toString() + "\n  : "+tempUserName)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String message ="";
                        try{
                            message = result.get("status").getAsString();
                        }catch (Exception e1){}
                        try{
                            message = result.get("Code: 401").getAsString();
                        }catch (Exception e2){}
                        Log.d("TAG",""+message);

                        Toasty.info(getApplicationContext(),""+message,Toasty.LENGTH_SHORT).show();
                    }
                });
    }
    public void farmersBtnClickSMS(View view) {
        sharedPreferences.edit().putString("sms_message",messageET.getText().toString()).apply();
        sharedPreferences.edit().putString("sms_tehsil",tehsilSpinner.getSelectedItem().toString()).apply();

        startActivity(new Intent(this,FarmerListActivity.class));
        finish();
    }

    public void smsBtnClickSMS(View view) {
        sharedPreferences.edit().putString("sms_message",messageET.getText().toString()).apply();
        sharedPreferences.edit().putString("sms_tehsil",tehsilSpinner.getSelectedItem().toString()).apply();

        startActivity(new Intent(this,SmsActivity.class));
        finish();
    }

    public ArrayList<String> stringToArrayList(String string){
        String[] strArr = string.split(",");
        List<String> strList = Arrays.asList(strArr);
        ArrayList<String> strArrList = new ArrayList<String>(strList);

        return strArrList;
    }

    public void bottomButton(){

        //:::: THIS IS FOR THE BOTTOM BUTTON ACTIVE CHANGING COLOR
        Drawable farmer_inactive = getResources().getDrawable(R.drawable.ic_fish_farmer_inactive);
        Drawable sms_active = getResources().getDrawable(R.drawable.ic_fish_sms_active);

        //pondBtn.setCompoundDrawablesWithIntrinsicBounds(null,pond_inactive,null,null);
        farmerBtn.setCompoundDrawablesWithIntrinsicBounds(null,farmer_inactive,null,null);
        smsBtn.setCompoundDrawablesWithIntrinsicBounds(null,sms_active,null,null);
    }
}
