package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FarmerCenterActivity extends AppCompatActivity {
    Menu menu;
    String mLat, mLng;
    String from;

    TextView farmerDashboardTextView;
    Button farmerDashboardButton;

    String farmerDashboardTextviewString;
    String farmerDashboardButtonString;
    int status = 0;

    SharedPreferences sharedPreferences;
    String mToken;
    String mContact;
    String mName;
    int mId;
    List<String> schemes;
    String mySchemeInString;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.test_menu, menu);

        // menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //TODO THIS IS TESTING MENU BEFORE STATUS CAN BE FETCH FROM SERVER
        if (item.getItemId() == R.id.zero) {
            changeLook(0);
        } else if (item.getItemId() == R.id.one) {
            changeLook(1);
        } else if (item.getItemId() == R.id.two) {
            changeLook(2);
        } else if (item.getItemId() == R.id.three) {
            changeLook(3);
        } else if (item.getItemId() == R.id.four) {
            changeLook(4);
        }else if (item.getItemId() == R.id.five) {
            new Logout(getApplicationContext());
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);

        schemes = new ArrayList<>();

        sharedPreferences = getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        farmerDashboardTextView = findViewById(R.id.farmer_dashboard_textview);
        farmerDashboardButton = findViewById(R.id.farmer_dashboard_button);

        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",0);
        //changeLook(0);farmerDashboardTextView
        farmerDashboardTextView.setText("Checking status...");


        //FETCH THE SCHEME LIST
        String urlTES99T = "http://192.168.43.205:8000/api/myscheme/";
        Ion.with(this)
                .load("GET",urlTES99T)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.d("TAG","arrar 0000 "+result);
                        if(result!=null){
                            for(int i =0;i<result.size();i++){
                                JsonObject schemeObj = result.get(i).getAsJsonObject();
                                String myscheme = schemeObj.get("sname").getAsString();
                                //myListOfScheme.add(myscheme);
                                schemes.add(myscheme);
                            }
                            Log.d("TAG","scheme 0000 "+schemes.get(0));

                            //mySchemeInString = String.join(",",schemes);
                            for(int i=0;i<schemes.size();i++)
                                if(i==0) mySchemeInString = schemes.get(i);
                                else mySchemeInString = mySchemeInString+","+schemes.get(i);

                            Log.d("TAG","mySchemeInString 0000 "+mySchemeInString);
                        }
                    }
                });


        /*/::::TODO - STATUS
        check user_id in the fishponds, if not present -> upload your form
        status :-
      0 = no activity, NOT YET SUBMIT ANYTHING  ;    1 = accept;2 = reject ;  3 = resubmit    ;4 = submit, but not yet react
        ::::::::*/
        //:::: TODO UNCOMMENT TUR
        // ::::TODO CHECK THE FISHPONDS DATA
        Log.d("TAG","my mID: "+mId);

        String urlTEST = "http://192.168.43.205:8000/api/findPond/"+mId;
        Ion.with(this)
                .load("POST",urlTEST)
              /*  .setHeader("Accept","application/json")
                .setHeader("Authorization","Bearer "+mToken)*/
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.d("TAG","check id: "+result);

                        if(result!=null){
                        //::::TODO THEIR IS SOMETHING IT THE FISHPONDS WITH THE MID SENT TO SERVER
                            //it will return the value of the approved
                            //String mToken = result.get("approved").getAsString();
                            boolean message = result.get("message").getAsBoolean();
                            if(message){
                                JsonObject data = result.getAsJsonObject("data");
                                String mApprove = data.get("approve").getAsString();

                                //::TODO GET THE USER DATA FOR FUTURE USE
                                sharedPreferences.edit().putString("name",data.get("name").getAsString()).apply();
                                sharedPreferences.edit().putString("fname",data.get("fname").getAsString()).apply();
                                sharedPreferences.edit().putString("address",data.get("address").getAsString()).apply();
                                sharedPreferences.edit().putString("district",data.get("district").getAsString()).apply();
                                sharedPreferences.edit().putString("location_of_pond",data.get("location_of_pond").getAsString()).apply();
                                sharedPreferences.edit().putString("tehsil",data.get("tehsil").getAsString()).apply();
                                sharedPreferences.edit().putString("image",data.get("image").getAsString()).apply();
                                sharedPreferences.edit().putString("area",data.get("area").getAsString()).apply();
                                sharedPreferences.edit().putString("epic_no",data.get("epic_no").getAsString()).apply();
                                sharedPreferences.edit().putString("name_of_scheme",data.get("name_of_scheme").getAsString()).apply();
                                //sharedPreferences.edit().putString("pondImages",data.result("pondImages").getAsString()).apply();
                                sharedPreferences.edit().putString("lat",data.get("lat").getAsString()).apply();
                                sharedPreferences.edit().putString("lng",data.get("lng").getAsString()).apply();
                                sharedPreferences.edit().putString("pondId",data.get("id").getAsString()).apply();

                                String mmA="";
                                if(mApprove.matches("0"))
                                    mmA = "4";
                                else if (mApprove.matches("1"))
                                    mmA= "1";
                                else if (mApprove.matches("2"))
                                    mmA= "2";

                                sharedPreferences.edit().putString("approve",mmA).apply();

                                switch(mApprove){
                                    case "0" :status = 4;break;
                                    case "1" :status = 1;break;
                                    case "2" :status = 2;break;
                                    case "3" :status = 3;break;
                                }
                            }else{
                                Log.d("TAG","no data");
                                status = 0;
                                sharedPreferences.edit().putString("approve","0").apply();

                           }
                        }else {
                            Log.d("TAG","Server not reachable");
                            status = 5;
                            sharedPreferences.edit().putString("approve","5").apply();

                        }
                        changeLook(status);
                    }
                });

        //::::TODO CHANGE THE STATUS DEPENDING ON THE STATUS OF THE APPROVED COLUMN


    }

    public void changeLook(int mStatus) {
        this.status = mStatus;
        switch (status) {
            case 0:
                farmerDashboardButtonString = "Open Form";
                farmerDashboardTextviewString = "Upload your data";
                break;
            case 1:
                farmerDashboardButtonString = "Download ID";
                farmerDashboardTextviewString = "Data approved";
                break;
            case 2:
                farmerDashboardButtonString = "Re-Submit";
                farmerDashboardTextviewString = "Data Rejected";
                break;
            case 3:
                farmerDashboardButtonString = "Edit";
                farmerDashboardTextviewString = "Form re-submitted";
                break;
            case 4:
                farmerDashboardButtonString = "Edit";
                farmerDashboardTextviewString = "Form submitted";
                break;
            case 5:
                farmerDashboardButtonString = "Server error";
                farmerDashboardTextviewString = "Server not reachable :(";
                farmerDashboardButton.setEnabled(false);
                break;
            default:
                break;
        }
        farmerDashboardTextView.setText(farmerDashboardTextviewString);
        farmerDashboardButton.setText(farmerDashboardButtonString);
    }

    public void dashboardButtonClick(View view) {
        if (status == 1) Toasty.success(this, "Downloading ID...", Toasty.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this,FarmerUploadDataActivity.class);
            intent.putExtra("schemes",mySchemeInString);
            startActivity(intent);
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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void departmentLabelClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact office");
        builder.setMessage("Call 77777");
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "77777")));
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
