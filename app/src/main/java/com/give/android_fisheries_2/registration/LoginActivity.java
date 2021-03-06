package com.give.android_fisheries_2.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.admin.FarmerListActivity;
import com.give.android_fisheries_2.farmer.FarmerCenterActivity;
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
    MaterialEditText loginContact,loginPassword;
    LinearLayout loginLinearLayout;
    LinearLayout simpleProgressBarLinearLayout;
    Button loginButton;
    String LOGIN_URL;

    //DEBUG

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //"http://192.168.43.205:8000/api/login"
        LOGIN_URL = MainActivity.MAIN_URL +"api/login";

        loginContact = findViewById(R.id.loginContact);
        loginPassword = findViewById(R.id.loginPassword);
        loginLinearLayout = findViewById(R.id.loginFormLinearLayout);
        simpleProgressBarLinearLayout = findViewById(R.id.simpleProgressBarLinearLayout);
        loginButton = findViewById(R.id.loginInButton);

    }

    public void mLoginButtonClick(View view) {
        String mLoginContact ;
        String mLoginPassword ;


        //::::BOTH FIELDS ARE EMPTY
        if( loginContact.getText().toString().matches("") &&   loginPassword.getText().toString().matches("")){
            loginContact.setError("Please enter contact details!");
            loginPassword.setError("Please enter password!");
        }else{
            //::::ONE OF THE FIELD IS MISSING
            if(loginContact.getText().toString().matches("") )
                loginContact.setError("Please enter contact details!");
            else if(loginPassword.getText().toString().matches(""))
                loginPassword.setError("Please enter password!");
            else {
                mLoginPassword = loginPassword.getText().toString();
                mLoginContact = loginContact.getText().toString();
                // ::THIS SHOW THE PROGRESS BAR LAYOUT AND MAKE THE LOGIN BUTTON DISABLE SO THAT USER WILL NOT CONSTANTLY PUSH THE BUTTON ::
                simpleProgressBarLinearLayout.setVisibility(View.VISIBLE);
                loginButton.setEnabled(false);
                Ion.with(getApplicationContext())
                        .load(LOGIN_URL)
                        .setMultipartParameter("email",mLoginContact)
                        .setMultipartParameter("password",mLoginPassword)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, final JsonObject result) {  /* NOTE: Login Success   result{  "success":"true" }     Login Unsuccess  result{   "success":"false"  }  */
                                //::::HIDE THE PROGRESS BAR AND MAKE THE BUTTON ENABLE AGAIN
                                Log.e("TAG","result: "+result);

                                simpleProgressBarLinearLayout.setVisibility(View.INVISIBLE);
                                loginButton.setEnabled(true);
                                try{
                                    if(result==null){
                                        Toasty.error(getApplicationContext(),"Oh no! Internal Server Error",Toasty.LENGTH_LONG).show();
                                    }else if(result.get("success").getAsBoolean()==false){
                                        Toasty.error(getApplicationContext(),"Invalid username or password",Toasty.LENGTH_LONG).show();
                                    }
                                    else{
                                        Log.e("TAG","TESTING: "+result.get("success").getAsBoolean());
                                        Log.e("TAG","result: "+result);
                                        String mToken = result.get("token").getAsString();
                                        String mName = result.get("name").getAsString();
                                        String mContact = result.get("email").getAsString();
                                        String mRole = result.get("role").getAsString();
                                        String tempId = result.get("id").getAsString();
                                        int  mId = Integer.parseInt(tempId);
                                        //THIS GET AS BOOLEAN IS VERY IMP
                                        if(result.get("success").getAsBoolean()==true){
                                            sharedPreferences = getApplication().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
                                            sharedPreferences.edit().putBoolean("mLoginStatus",true).apply();
                                            sharedPreferences.edit().putInt("mId",mId).apply();
                                            sharedPreferences.edit().putString("mName",mName).apply();
                                            sharedPreferences.edit().putString("mToken",mToken).apply();
                                            sharedPreferences.edit().putString("mContact",mContact).apply();
                                            sharedPreferences.edit().putString("mRole",mRole).apply();
                                            if(mRole.equals("FARMER")){
                                                Intent intent = new Intent(LoginActivity.this, FarmerCenterActivity.class);
                                                intent.putExtra("status","good");
                                                startActivity(intent);
                                                finish();
                                            }else if(mRole.equals("ADMIN")){
                                                Intent intent = new Intent(LoginActivity.this, FarmerListActivity.class);
                                                intent.putExtra("status","good");
                                                startActivity(intent);
                                                finish();
                                            }

                                        }else Toasty.error(getApplicationContext(),"Incorrect Input",Toasty.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e2){
                                    Log.e("TAG","ERROR IN LOGIN: "+e2 );
                                }

                            }
                        });
            }
        }
    }

    public void notYetRegisterClick(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
        finish();
    }
}
