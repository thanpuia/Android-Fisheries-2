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
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
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
    TextView officeAddress;
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
    String helpline_contact;
    String helpline_name;

    List<String> tehsil;


    String[] tehsilStrArr;
    String tehsilInString;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu_farmer, menu);

        // menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //TODO THIS IS TESTING MENU BEFORE STATUS CAN BE FETCH FROM SERVER

        if (item.getItemId() == R.id.refresh_page) {
                finish();
                startActivity(getIntent());
        }else if (item.getItemId() == R.id.log_out) {
            new Logout(getApplicationContext());
            finish();
            startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);
        tehsil = new ArrayList<>();
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
               //v Toast.makeText(FarmerCenterActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                //Toast.makeText(FarmerCenterActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CALL_PHONE).check();

        schemes = new ArrayList<>();

        sharedPreferences = getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        farmerDashboardTextView = findViewById(R.id.farmer_dashboard_textview);
        farmerDashboardButton = findViewById(R.id.farmer_dashboard_button);
        officeAddress = findViewById(R.id.farmer_dashboard_address);

        officeAddress.setText("Department of Fisheries \n New Capital Complex, Khatla, Aizawl, Mizoram");
        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",0);
        //changeLook(0);farmerDashboardTextView
        farmerDashboardTextView.setText("Checking status...");

        //DOWNLOAD HELPLINE NUMBER
        String urlHelpline = "http://192.168.43.205:8000/api/helpline/";
        Ion.with(this)
                .load(urlHelpline)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                         if(result!=null){
                             helpline_contact = result.get("contact").toString();
                             helpline_name = result.get("name").toString();
                             officeAddress.setText("Department of Fisheries \n New Capital Complex, Khatla, Aizawl, Mizoram\nPh: "+ helpline_contact);
                         }else{
                             helpline_contact = "";
                             helpline_name = "";
                         }
                    }
                });

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
                                schemes.add(myscheme);
                            }
                            for(int i=0;i<schemes.size();i++)
                                if(i==0) mySchemeInString = schemes.get(i);
                                else mySchemeInString = mySchemeInString+","+schemes.get(i);
                        }
                        sharedPreferences.edit().putString("schemes",mySchemeInString).apply();
                    }
                });
        String myTehsilURL = "http://192.168.43.205:8000/api/mytehsil";

        //DOWNLOAD TEHSIL
        Ion.with(this)
                .load("GET",myTehsilURL)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.d("TAG","tehsil "+result);
                        if(result!=null){
                            for(int i =0;i<result.size();i++){
                                JsonObject schemeObj = result.get(i).getAsJsonObject();
                                String myTehsil = schemeObj.get("tname").getAsString();
                                tehsil.add(myTehsil);
                            }
                            for(int i=0;i<tehsil.size();i++)
                                if(i==0) tehsilInString = tehsil.get(i);
                                else tehsilInString = tehsilInString+","+tehsil.get(i);
                        }
                        sharedPreferences.edit().putString("all_tehsil",tehsilInString).apply();
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

        //DOWNLOAD USER POND
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
                                try{
                                    //::TODO GET THE USER DATA FOR FUTURE USE
                                    sharedPreferences.edit().putString("name",data.get("name").getAsString()).apply();
                                    sharedPreferences.edit().putString("fname",data.get("fname").getAsString()).apply();
                                    sharedPreferences.edit().putString("address",data.get("address").getAsString()).apply();
                                    sharedPreferences.edit().putString("district",data.get("district").getAsString()).apply();
                                    sharedPreferences.edit().putString("location_of_pond",data.get("location_of_pond").getAsString()).apply();
                                    sharedPreferences.edit().putString("tehsil",data.get("tehsil").getAsString()).apply();

                                    sharedPreferences.edit().putString("area",data.get("area").getAsString()).apply();
                                    sharedPreferences.edit().putString("epic_no",data.get("epic_no").getAsString()).apply();
                                    sharedPreferences.edit().putString("name_of_scheme",data.get("name_of_scheme").getAsString()).apply();
                                    //sharedPreferences.edit().putString("image",data.get("image").getAsString()).apply();
                                    sharedPreferences.edit().putString("lat",data.get("lat").getAsString()).apply();

                                    sharedPreferences.edit().putString("lng",data.get("lng").getAsString()).apply();
                                    sharedPreferences.edit().putString("pondId",data.get("id").getAsString()).apply();
                                }catch(Exception e2){
                                    Log.d("TAG","Farmer Ponds data download error: "+e);
                                }

                                try{
                                    if(data.get("image").getAsString()!=null)
                                        sharedPreferences.edit().putString("image_web",data.get("image").getAsString()).apply();
                                }catch (Exception e1){ }

                                try{
                                    if(data.get("pondImage_one").getAsString()!=null)
                                        sharedPreferences.edit().putString("pond1_web",data.get("pondImage_one").getAsString()).apply();
                                }catch (Exception e1){ }

                                try{
                                    if(data.get("pondImage_two").getAsString()!=null)
                                        sharedPreferences.edit().putString("pond2_web",data.get("pondImage_two").getAsString()).apply();
                                }catch (Exception e1){ }

                                try{
                                    if(data.get("pondImage_three").getAsString()!=null)
                                        sharedPreferences.edit().putString("pond3_web",data.get("pondImage_three").getAsString()).apply();
                                }catch (Exception e1){ }

                                try{
                                    if(data.get("pondImage_four").getAsString()!=null)
                                        sharedPreferences.edit().putString("pond4_web",data.get("pondImage_four").getAsString()).apply();
                                }catch (Exception e1){ }


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
        if (status == 1){
            startActivity(new Intent(this,FarmerIdActivity.class));
        }
        else {
            Intent intent = new Intent(this,FarmerUploadDataActivity.class);
            //finish();
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
        builder.setTitle("Contact "+helpline_name);
        builder.setMessage("Call "+helpline_contact);
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
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + helpline_contact)));
            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
