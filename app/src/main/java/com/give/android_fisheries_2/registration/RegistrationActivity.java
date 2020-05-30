package com.give.android_fisheries_2.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.StringJoiner;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    private MaterialEditText name, password, contact, passwordConfirm;
    private Button registerNowButton;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    RelativeLayout registrationProgressBarRelativeLayout;
    String mName, mContact, mPassword, mPasswordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.userName);
        password = findViewById(R.id.userPassword);
        passwordConfirm = findViewById(R.id.userPasswordConfirm);
        contact = findViewById(R.id.userPhone);
        registerNowButton = findViewById(R.id.registerNowButton);
        progressBar = findViewById(R.id.simpleProgressBarRegistration);
        registrationProgressBarRelativeLayout = findViewById(R.id.registration_progress_bar_relative_layout);
    }

    public void registerNowClick(View view) {

        mName = name.getText().toString();
        mContact = contact.getText().toString();
        mPassword = password.getText().toString();
        mPasswordConfirm = passwordConfirm.getText().toString();

        if(mName.matches("") && mPassword.matches("") && mContact.matches("")&& mPasswordConfirm.matches("")){
            name.setError("Please enter your name!");
            contact.setError("Please enter contact details!");
            password.setError("Please enter password!");
            passwordConfirm.setError("Please enter confirm password!");
        }

        if(mName.matches(""))   name.setError("Please enter contact details!");
        else if(mContact.matches("")) contact.setError("Please enter contact details!");
        else if(mPassword.matches(""))  password.setError("Please enter password!");
        else if(mPasswordConfirm.matches(""))   passwordConfirm.setError("Please enter confirm password!");
        else if(mContact.length() != 10) Toasty.error(this,"Phone number should be 10 digits",Toasty.LENGTH_SHORT).show();
        else if(!mPassword.matches(mPasswordConfirm)) Toasty.error(this,"Password not matching! ",Toasty.LENGTH_SHORT).show();
        else{
            //:::: SHOW THE PROGRESS BAR AND DISABLE THE REGISTRATION BUTTON ::::
            registrationProgressBarRelativeLayout.setVisibility(View.VISIBLE);
            registerNowButton.setEnabled(false);
            try{
                Ion.with(getApplicationContext())
                        .load("http://192.168.43.205:8000/api/register")
                        .setMultipartParameter("name",mName)
                        .setMultipartParameter("contact",mContact)
                        .setMultipartParameter("password",mPassword)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                registrationProgressBarRelativeLayout.setVisibility(View.INVISIBLE);
                                registerNowButton.setEnabled(true);
                                Log.e("TAG","RESULT::"+result);
                                if(result!=null){
                                    JsonElement tokenAsJson = result.get("token");
                                    Log.e("TAG","TOKEN::"+result.get("token"));
                                    if(tokenAsJson!=null){
                                        Toasty.success(getApplicationContext(),"Register Successfully!",Toasty.LENGTH_SHORT).show();
                                        //STORED IN THE SHARED PREFERENCE FOR LATER REFERENCE
                                        sharedPreferences = getApplication().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
                                        sharedPreferences.edit().putString("mToken",result.get("token").toString()).apply();
                                        sharedPreferences.edit().putString("mName",mName).apply();
                                        sharedPreferences.edit().putString("mContact",mContact).apply();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        registerNowButton.setVisibility(View.VISIBLE);

                                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                    }else Toasty.error(getApplicationContext(),"Phone number already taken or password should be more than 8 char",Toasty.LENGTH_SHORT).show();
                                }else Toasty.error(getApplicationContext(),"Phone number already taken or password should be more than 8 char",Toasty.LENGTH_SHORT).show();
                            }
                        });
            }catch (Exception e){}
        }
    }

    public void loginInTextClick(View view) {
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();

    }

}
