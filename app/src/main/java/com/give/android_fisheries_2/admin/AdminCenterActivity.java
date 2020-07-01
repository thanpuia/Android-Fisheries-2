package com.give.android_fisheries_2.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.drm.DrmStore;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.FarmerListAdapter;
import com.give.android_fisheries_2.entity.FarmerEntity;
import com.give.android_fisheries_2.farmer.FarmerCenterActivity;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminCenterActivity extends AppCompatActivity {

    RecyclerView farmerRecyclerView;

    FragmentTransaction fragmentTransaction;
    Fragment mFragment;
    Menu menu;
    EditText toolbarEdittext;
    Button pondBtn, farmerBtn, smsBtn;
    int pondFarmerSms = 0;// 1= ponds; 2= farmer; 3 = sms
    String mToken;
    SharedPreferences sharedPreferences;
    ArrayList<FarmerEntity> farmerEntities;
    FarmerListAdapter farmerListAdapter;

    String districtsAndScheme[] = {"Kolasib","Champhai","Lawngtlai","Aizawl","others","PMEGY","Blue Revolution","other"};
    boolean[] checkednames= new boolean[]{false,false,false,false,false,false,false,false};

    String test[] = {"test1","test2"};
    boolean[] testboo = new boolean[]{false,false};

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);

        menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.refresh_page){
            startActivity(new Intent(this,AdminCenterActivity.class));
        }
        else if (item.getItemId() == R.id.logout){
            new Logout(getApplicationContext());
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_center);


        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(AdminCenterActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(AdminCenterActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
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

        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.m_toolbar);

        toolbarEdittext = actionBar.getCustomView().findViewById(R.id.toobarEditText);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME );
//        farmerRecyclerView = findViewById(R.id.farmerRecyclerList);
//        farmerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        farmerEntities = new ArrayList<>();
        pondBtn = findViewById(R.id.pondsBtn);
        farmerBtn = findViewById(R.id.farmersBtn);
        smsBtn = findViewById(R.id.smsBtn);
        sharedPreferences = this.getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mToken = sharedPreferences.getString("mToken","");
        mFragment = null;

        toolbarEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                Log.d("TAG","keyEvent:"+keyEvent+" actionID:"+actionId);
                if(actionId==6){
                    //TODO:: actionId 6 is the id of pressing done key in android
                    Toast.makeText(getApplicationContext(),""+toolbarEdittext.getText(),Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    public void pondsBtnClick(View view) {
        mFragment = new PondsFragment();
        pondFarmerSms = 1;
        fragmentCommit();
    }

    public void farmersBtnClick(View view) {
        mFragment = new FarmersFragment();
        pondFarmerSms = 2;
        fragmentCommit();
    }

    public void smsBtnClick(View view) {
        mFragment = new SmsFragment();
        pondFarmerSms = 3;
        fragmentCommit();
    }

    public void fragmentCommit(){

        //:::: THIS IS FOR THE BOTTOM BUTTON ACTIVE CHANGING COLOR
        Drawable pond_active = getResources().getDrawable(R.drawable.ic_fish_ponds_active);
        Drawable pond_inactive = getResources().getDrawable(R.drawable.ic_fish_ponds_inactive);
        Drawable farmer_active = getResources().getDrawable(R.drawable.ic_fish_farmer_active);
        Drawable farmer_inactive = getResources().getDrawable(R.drawable.ic_fish_farmer_inactive);
        Drawable sms_active = getResources().getDrawable(R.drawable.ic_fish_sms_active);
        Drawable sms_inactive = getResources().getDrawable(R.drawable.ic_fish_sms_inactive);

        switch (pondFarmerSms){
            case 1:
                pondBtn.setCompoundDrawablesWithIntrinsicBounds(null,pond_active,null,null);
                farmerBtn.setCompoundDrawablesWithIntrinsicBounds(null,farmer_inactive,null,null);
                smsBtn.setCompoundDrawablesWithIntrinsicBounds(null,sms_inactive,null,null);
                break;
            case 2:
                pondBtn.setCompoundDrawablesWithIntrinsicBounds(null,pond_inactive,null,null);
                farmerBtn.setCompoundDrawablesWithIntrinsicBounds(null,farmer_active,null,null);
                smsBtn.setCompoundDrawablesWithIntrinsicBounds(null,sms_inactive,null,null);
                break;
            case 3:
                pondBtn.setCompoundDrawablesWithIntrinsicBounds(null,pond_inactive,null,null);
                farmerBtn.setCompoundDrawablesWithIntrinsicBounds(null,farmer_inactive,null,null);
                smsBtn.setCompoundDrawablesWithIntrinsicBounds(null,sms_active,null,null);
                break;
        }
        //:: getSupportFragmentManager().beginTransaction()  <-- is should be called every time fragment is going to be replace or error will throw
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mFragmentFrameLayout,mFragment);
        fragmentTransaction.commit();
    }

    public void filtersBtnClick(View view) {
       // Log.d("TAG","FILTER CLCK");
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select District and Scheme");
        builder.setMultiChoiceItems(districtsAndScheme, checkednames, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                checkednames[which] = isChecked;
            }
        });

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j =0;j<checkednames.length;j++){
                    if(checkednames[j]){
                        Log.d("TAG","SELECT ITEM: "+ districtsAndScheme[j]);
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Filters");

        //SET THE CUSTOM LAYOUT
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
        builder.setView(customLayout);

        CheckBox allDistrictCB,aizawlCB,kolasibCB,lawngtlaiCB,lungleiCB,mamitCB,siahaCB,serchhipCB,champhaiCB,hnahthialCB,khawzawlCB,saitualCB,nfdbCB,nlupCB,rkvyCB,blueRevolutionCB,otherSchemeCB;
        aizawlCB = customLayout.findViewById(R.id.Aizawl);    kolasibCB = customLayout.findViewById(R.id.Khawzawl);    lawngtlaiCB = customLayout.findViewById(R.id.Lawngtlai);
        lungleiCB = customLayout.findViewById(R.id.Lunglei);    mamitCB = customLayout.findViewById(R.id.Mamit);    siahaCB = customLayout.findViewById(R.id.Siaha);
        serchhipCB = customLayout.findViewById(R.id.Serchhip);    champhaiCB = customLayout.findViewById(R.id.Champhai);    hnahthialCB = customLayout.findViewById(R.id.Hnahthial);
        khawzawlCB = customLayout.findViewById(R.id.Khawzawl);    saitualCB = customLayout.findViewById(R.id.Saitual);    nfdbCB = customLayout.findViewById(R.id.nfdb);
        rkvyCB = customLayout.findViewById(R.id.rkvy);    nlupCB = customLayout.findViewById(R.id.nlup);    blueRevolutionCB = customLayout.findViewById(R.id.blue_revolution);
        allDistrictCB = customLayout.findViewById(R.id.all);    otherSchemeCB = customLayout.findViewById(R.id.others);

        final CheckBox[] districtCheckBoxes = new CheckBox[]{allDistrictCB,aizawlCB,kolasibCB,lawngtlaiCB,lungleiCB,mamitCB,siahaCB,serchhipCB,champhaiCB,hnahthialCB, khawzawlCB,saitualCB};
        final boolean[] districtChecked = new boolean[]{false,false,false,false,false,false,false,false,false,false,false,false};
        final CheckBox[] schemeCheckBoxex = new CheckBox[]{nfdbCB,rkvyCB,nlupCB,blueRevolutionCB,otherSchemeCB};
        final boolean[] schemeChecked = new boolean[]{false,false,false,false,false};

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // ::THE CHECKBOX IS MAKER FOR FURTHER USES
                for(int i=0;i<districtCheckBoxes.length;i++)
                    if(districtCheckBoxes[i].isChecked()){
                        districtChecked[i]=true;
                        Log.d("TAG","check:"+districtCheckBoxes[i]);
                    }

                for(int j=0;j<schemeCheckBoxex.length;j++)
                    if(schemeCheckBoxex[j].isChecked()) {
                        schemeChecked[j]=true;
                        Log.d("TAG","check:"+schemeCheckBoxex[j]);

                    }
                for (int i=0;i<districtChecked.length;i++){
                    if(districtChecked[i]){

                        String testDistrict = String.valueOf(districtCheckBoxes[i].getText());
                        Log.d("TAG",""+String.valueOf(districtCheckBoxes[i].getText()));
                       FarmersFragment.search(getApplicationContext(),mToken,testDistrict);

                    }
                }


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
