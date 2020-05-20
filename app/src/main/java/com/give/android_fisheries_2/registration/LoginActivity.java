package com.give.android_fisheries_2.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.MalformedURLException;
import java.net.URL;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferences;
    MaterialEditText loginEmail,loginPassword;
    LinearLayout loginLinearLayout;
    ProgressBar loginProgressBar;

   private String URLs=String.valueOf(R.string.IP_ADDRESS);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginContact);
        loginPassword = findViewById(R.id.loginPassword);
        loginLinearLayout = findViewById(R.id.loginFormLinearLayout);
        loginProgressBar = findViewById(R.id.simpleProgressBarLogin);


    }

    public void mLoginButtonClick(View view) {

        String mLoginContact = loginEmail.getText().toString();
        String mLoginPassword = loginPassword.getText().toString();

        loginProgressBar.setVisibility(View.VISIBLE);
        loginLinearLayout.setVisibility(View.INVISIBLE);
        Ion.with(getApplicationContext())
                .load("http://test-env.eba-pnm2djie.ap-south-1.elasticbeanstalk.com/api/login")
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        loginLinearLayout.setVisibility(View.INVISIBLE);
                        loginProgressBar.setVisibility(View.VISIBLE);
                    }
                })
                .setMultipartParameter("contact",mLoginContact)
                .setMultipartParameter("password",mLoginPassword)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, final JsonObject result) {  /* NOTE: Login Success   result{  "success":"true" }     Login Unsuccess  result{   "success":"false"  }  */
                        Log.e("TAG","TESTING: "+result.get("success").getAsBoolean());
                        Log.e("TAG","result: "+result);

                        boolean success = result.get("success").getAsBoolean();
                        String mToken = result.get("token").getAsString();
                        String mName = result.get("name").getAsString();
                        String mContact = result.get("contact").getAsString();
                   //     String mRole = result.get("role").getAsString();

                        int mId = result.get("id").getAsInt();


                        Log.e("TAG","TESTING: "+result.get("success"));

                        loginLinearLayout.setVisibility(View.VISIBLE);
                        loginProgressBar.setVisibility(View.INVISIBLE);

                        Log.e("TAG","result:"+result);
                        //THIS GET AS BOOLEAN IS VERY IMP
                        if(result.get("success").getAsBoolean()==true){
                            loginLinearLayout.setVisibility(View.VISIBLE);
                            loginProgressBar.setVisibility(View.INVISIBLE);

                            sharedPreferences = getApplication().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putBoolean("mLoginStatus",true).apply();
                            sharedPreferences.edit().putInt("mId",mId).apply();
                            sharedPreferences.edit().putString("mName",mName).apply();
                            sharedPreferences.edit().putString("mToken",mToken).apply();
                            sharedPreferences.edit().putString("mContact",mContact).apply();
                          //  sharedPreferences.edit().putString("mRole",mRole).apply();


                            Log.e("TAG","LOGIN");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("status","good");
                            startActivity(intent);
                            finish();
                        }else{
                            loginLinearLayout.setVisibility(View.VISIBLE);
                            loginProgressBar.setVisibility(View.INVISIBLE);
                            Toasty.error(getApplicationContext(),"Incorrect Input",Toasty.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void notYetRegisterClick(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
        finish();
    }
}
