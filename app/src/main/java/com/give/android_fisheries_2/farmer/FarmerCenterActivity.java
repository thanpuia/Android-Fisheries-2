package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import es.dmoral.toasty.Toasty;

public class FarmerCenterActivity extends AppCompatActivity {
    Menu menu;
    String mLat, mLng;
    String from;
    SharedPreferences sharedPreferences;
    String mToken;
    String mId;

    TextView farmerDashboardTextView;
    Button farmerDashboardButton;

    String farmerDashboardTextviewString;
    String farmerDashboardButtonString;

    int status = 0;

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
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_center);
        sharedPreferences = getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        mToken = sharedPreferences.getString("mToken", "");
        farmerDashboardTextView = findViewById(R.id.farmer_dashboard_textview);
        farmerDashboardButton = findViewById(R.id.farmer_dashboard_button);

        changeLook(0);

        /*/::::TODO - STATUS
        check user_id in the fishponds, if not present -> upload your form
                                        f
        status :-
            0 = no activity, NOT YET SUBMIT ANYTHING
            1 = accept
            2 = reject
            3 = resubmit  //

            4 = submit, but not yet react
        ::::::::*/

        //:::: TODO UNCOMMENT TUR
        // ::::TODO CHECK THE FISHPONDS DATA
        /*
        Ion.with(this)
                .load("URL")
                .setHeader("Accept","application/json")
                .setHeader("Authorization","Bearer "+mToken)
                .setMultipartParameter("id",mId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if(result!=null){
                        //::::TODO THEIR IS SOMETHING IT THE FISHPONDS WITH THE MID SENT TO SERVER
                            //it will return the value of the approved
                            //String mToken = result.get("approved").getAsString();
                            String mApprove = result.get("approve").getAsString();
                            switch(mApprove){
                                case "0" :status = 0;break;
                                case "1" :status = 1;break;
                                case "2" :status = 2;break;
                                case "3" :status = 3;break;
                            }
                        }else {
                            //::::TODO NO DATA IS SENT TO FISHPONDS TABLE
                            //status : 0 = no activity, NOT YET SUBMIT ANYTHING
                            status = 4;
                        }
                    }
                });
        */
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
            default:
                break;
        }


//        //SET THE LOOKS
        farmerDashboardTextView.setText(farmerDashboardTextviewString);
        farmerDashboardButton.setText(farmerDashboardButtonString);

        //TESTING
    }

//    public void twoBtnClick(View view) {
//        mFragment = new FarmerUploadDataFragment();
//        commitFragment();
//    }


    //FARMERS DATA UPLOAD XML ATANG
    public void selectPhoto(View view) {
    }

//
//    public void changeStatus(int mStatus){
//        this.status = mStatus;
//        mFragment = new FarmerDashboardFragment(status);
//        commitFragment();
//    }

    public void dashboardButtonClick(View view) {
        if (status == 1) Toasty.success(this, "Downloading ID...", Toasty.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(this, FarmerUploadDataActivity.class));
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
