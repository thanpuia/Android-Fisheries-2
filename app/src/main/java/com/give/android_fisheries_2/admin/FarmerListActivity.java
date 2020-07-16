package com.give.android_fisheries_2.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.FarmerListAdapter;
import com.give.android_fisheries_2.adapter.RecyclerItemClickListener;
import com.give.android_fisheries_2.adapter.SchemeListAdapter;
import com.give.android_fisheries_2.entity.FarmerEntity;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FarmerListActivity extends AppCompatActivity {
    Menu menu;
    EditText toolbarEdittext;
    static RecyclerView farmerRecyclerView;
    static ProgressBar progressBarFarmerList;
    static ArrayList<FarmerEntity> farmerEntities;
    static FarmerListAdapter farmerListAdapter;
    SharedPreferences sharedPreferences;
    String mToken;
    Button farmerBtn, smsBtn;
    // Button pondBtn;
    SchemeListAdapter schemeListAdapter;
    List<String> schemes;
    List<String> tehsil;

    String[] schemesStrArr;
    String mySchemeInString;

    String[] tehsilStrArr;
    String tehsilInString;
    String schemeSelect;
    RecyclerView recyclerViewScheme;
    String districtStr;

    ArrayList<String> checkScheme;
    String MY_SCHEME_URL ;
    String MY_TEHSIL_URL ;
    String MY_SEARCH_BY_NAME_URL;
    String MY_POND_LIST_URL;
    String MY_POND_SEARCH;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);

        // menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.refresh_page) {
            finish();
            startActivity(getIntent());
        } else if (item.getItemId() == R.id.filter_list) {
            filtersBtnClick();
        } else if (item.getItemId() == R.id.log_out) {
            new Logout(getApplicationContext());
            finish();
            startActivity(new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK));

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_list);
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.m_toolbar);
        toolbarEdittext = actionBar.getCustomView().findViewById(R.id.toobarEditText);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        //  pondBtn = findViewById(R.id.pondsBtn);
        farmerBtn = findViewById(R.id.farmersBtn);
        smsBtn = findViewById(R.id.smsBtn);
        schemes = new ArrayList<>();
        tehsil = new ArrayList<>();

        bottomButton();

        sharedPreferences = getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mToken = sharedPreferences.getString("mToken", "");
        progressBarFarmerList = findViewById(R.id.simpleProgressBarFarmerList);
        farmerRecyclerView = findViewById(R.id.farmerRecyclerList);
        farmerRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        farmerEntities = new ArrayList<>();

        progressBarFarmerList.setVisibility(View.VISIBLE);
        farmerRecyclerView.setVisibility(View.INVISIBLE);
        checkScheme = new ArrayList<>();
       /* String myschemeURL = "http://192.168.43.205:8000/api/myscheme";
        String myTehsilURL = "http://192.168.43.205:8000/api/mytehsil";
        final String mySearchByName = "http://192.168.43.205:8000/api/fishpond/searchbyname/";*/
       //SINGLE URL
         MY_SCHEME_URL = MainActivity.MAIN_URL +"api/myscheme";
         MY_TEHSIL_URL = MainActivity.MAIN_URL +"api/mytehsil";
         MY_SEARCH_BY_NAME_URL = MainActivity.MAIN_URL +"api/fishpond/searchbyname/";
         MY_POND_LIST_URL = MainActivity.MAIN_URL +"api/fishponds/pondlist";
         MY_POND_SEARCH = MainActivity.MAIN_URL +"api/fishponds/search";

        //HANDLE THE SEARCH BAR
        toolbarEdittext.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Ion.with(getApplicationContext())
                            .load(MY_SEARCH_BY_NAME_URL+toolbarEdittext.getText().toString())
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    Log.d("TAG","Search result: "+result);
                                    if(result!=null){
                                        Boolean message = result.get("message").getAsBoolean();
                                        if(message){
                                            try{
                                                farmerEntities.clear();
                                                Log.d("TAG"," list AL: "+result);
                                                JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                for(int i=0;i<jsonArray.length();i++){
                                                    JSONObject singleRow =  jsonArray.getJSONObject(i);
                                                    String name = singleRow.getString("name");
                                                    String contact = singleRow.getString("contact");
                                                    String district = singleRow.getString("district");
                                                    String fname = singleRow.getString("fname");
                                                    String address = singleRow.getString("address");
                                                    String location_of_pond = singleRow.getString("location_of_pond");
                                                    String tehsil = singleRow.getString("tehsil");
                                                    String area = singleRow.getString("area");
                                                    String epicOrAadhaar = singleRow.getString("epic_no");
                                                    String nameOfScheme = singleRow.getString("name_of_scheme");
                                                    String image = singleRow.getString("image");
                                                    Double lat = singleRow.getDouble("lat");
                                                    Double lng = singleRow.getDouble("lng");
                                                    String pond1 = singleRow.getString("pondImage_one");
                                                    String pond2 = singleRow.getString("pondImage_two");
                                                    String pond3 = singleRow.getString("pondImage_three");
                                                    String pond4 = singleRow.getString("pondImage_four");


                                                    FarmerEntity mFarmerEntity= new FarmerEntity(name,contact,fname,address,district,location_of_pond,tehsil,area,epicOrAadhaar,nameOfScheme,image,lat,lng,pond1,pond2,pond3,pond4);
                                                    farmerEntities.add(mFarmerEntity);
                                                }
                                                listAdapterAdder(getApplicationContext(),farmerEntities);
                                                Toasty.success(getApplicationContext(),"Search Found!",Toasty.LENGTH_SHORT).show();

                                            }catch (Exception f){
                                                Log.d("TAG","Error in farmer data manipulate: "+f);
                                            }

                                        }else {
                                            Toasty.error(getApplicationContext(),"Not Found!",Toasty.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toasty.error(getApplicationContext(),"Server error",Toasty.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    return true;
                }
                return false;
            }
        });

        Ion.with(this)
                .load("GET", MY_SCHEME_URL)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.d("TAG", "arrar 0000 " + result);
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject schemeObj = result.get(i).getAsJsonObject();
                                String myscheme = schemeObj.get("sname").getAsString();
                                schemes.add(myscheme);
                            }
                            for (int i = 0; i < schemes.size(); i++)
                                if (i == 0) mySchemeInString = schemes.get(i);
                                else mySchemeInString = mySchemeInString + "," + schemes.get(i);
                        }
                        sharedPreferences.edit().putString("schemes", mySchemeInString).apply();
                    }
                });
        //DOWNLOAD TEHSIL
        Ion.with(this)
                .load("GET", MY_TEHSIL_URL)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        Log.d("TAG", "tehsil " + result);
                        if (result != null) {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject schemeObj = result.get(i).getAsJsonObject();
                                String myTehsil = schemeObj.get("tname").getAsString();
                                tehsil.add(myTehsil);
                            }
                            for (int i = 0; i < tehsil.size(); i++)
                                if (i == 0) tehsilInString = tehsil.get(i);
                                else tehsilInString = tehsilInString + "," + tehsil.get(i);
                        }
                        sharedPreferences.edit().putString("tehsil", tehsilInString).apply();
                    }
                });
        Ion.with(this)
                .load(MY_POND_LIST_URL)
                .setHeader("Accept", "application/json")
                //  .setHeader("Content-Type","application/x-www-form-urlencoded")
                .setHeader("Authorization", "Bearer " + mToken)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.d("TAG", " list AL: " + result);
                            JSONObject jsonObject = new JSONObject(String.valueOf(result));
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject singleRow = jsonArray.getJSONObject(i);
                                String name = singleRow.getString("name");
                                String contact = singleRow.getString("contact");
                                String fname = singleRow.getString("fname");
                                String address = singleRow.getString("address");
                                String district = singleRow.getString("district");
                                String location_of_pond = singleRow.getString("location_of_pond");
                                String tehsil = singleRow.getString("tehsil");
                                String area = singleRow.getString("area");
                                String epicOrAadhaar = singleRow.getString("epic_no");
                                String nameOfScheme = singleRow.getString("name_of_scheme");
                                String image = singleRow.getString("image");
                                Double lat = singleRow.getDouble("lat");
                                Double lng = singleRow.getDouble("lng");
                                String pond1 = singleRow.getString("pondImage_one");
                                String pond2 = singleRow.getString("pondImage_two");
                                String pond3 = singleRow.getString("pondImage_three");
                                String pond4 = singleRow.getString("pondImage_four");

                                FarmerEntity mFarmerEntity= new FarmerEntity(name,contact,fname,address,district,location_of_pond,tehsil,area,epicOrAadhaar,nameOfScheme,image,lat,lng,pond1,pond2,pond3,pond4);
                                farmerEntities.add(mFarmerEntity);
                            }
                            farmerListAdapter = new FarmerListAdapter(farmerEntities, getApplicationContext());
                            farmerRecyclerView.setAdapter(farmerListAdapter);

                            //ONCLICK LIST
                            farmerRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), farmerRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, final int position) {
                                    LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                                    //Call the alert box
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setTitle(farmerEntities.get(position).getName() + "'s Details");

                                    //SET THE CUSTOM LAYOUT
                                    View customLayout = layoutInflater.inflate(R.layout.custom_farmer_list_details, null);
                                    builder.setView(customLayout);

                                    //DECLARATION AND INITIALIZATION
                                    final TextView name, contact, fname, address, district, tehsil, area, epic, scheme;
                                    Button messageFarmerButton;
                                    messageFarmerButton = customLayout.findViewById(R.id.message_farmer_button);
                                    name = customLayout.findViewById(R.id.name_value);
                                    contact = customLayout.findViewById(R.id.contact_value);
                                    fname = customLayout.findViewById(R.id.fname_value);
                                    address = customLayout.findViewById(R.id.address_value);
                                    district = customLayout.findViewById(R.id.district_value);
                                    tehsil = customLayout.findViewById(R.id.tehsil_value);
                                    area = customLayout.findViewById(R.id.area_value);
                                    epic = customLayout.findViewById(R.id.epic_value);
                                    scheme = customLayout.findViewById(R.id.scheme_value);

                                    messageFarmerButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent (getApplicationContext(),SmsActivity.class);
                                            intent.putExtra("sms",true);
                                            intent.putExtra("name",farmerEntities.get(position).getName());
                                            intent.putExtra("contact",farmerEntities.get(position).getContact());
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                    //DEFINITION
                                    name.setText(farmerEntities.get(position).getName());
                                    contact.setText(farmerEntities.get(position).getContact());
                                    fname.setText(farmerEntities.get(position).getFname());
                                    district.setText(farmerEntities.get(position).getDistrict());
                                    address.setText(farmerEntities.get(position).getAddress());
                                    tehsil.setText(farmerEntities.get(position).getTehsil());
                                    area.setText(farmerEntities.get(position).getArea());
                                    epic.setText(farmerEntities.get(position).getEpicOrAadhaar());
                                    scheme.setText(farmerEntities.get(position).getNameOfScheme());


                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    builder.setNegativeButton("Map", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startActivity(new Intent(getApplicationContext(), FishPondMapActivity.class)
                                                    .putExtra("lat", farmerEntities.get(position).getLat())
                                                    .putExtra("lng", farmerEntities.get(position).getLng())
                                                    .putExtra("name", farmerEntities.get(position).getName())
                                                    .putExtra("district", farmerEntities.get(position).getDistrict())
                                                    .putExtra("area", farmerEntities.get(position).getArea())
                                                    .putExtra("scheme", farmerEntities.get(position).getNameOfScheme())
                                                    .putExtra("tehsil", farmerEntities.get(position).getTehsil())

                                                    .putExtra("pond1",farmerEntities.get(position).getPond1())
                                                    .putExtra("pond2",farmerEntities.get(position).getPond2())
                                                    .putExtra("pond3",farmerEntities.get(position).getPond3())
                                                    .putExtra("pond4",farmerEntities.get(position).getPond4()));
                                        }
                                    });

                                    builder.setNeutralButton("Call", new DialogInterface.OnClickListener() {
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
                                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +farmerEntities.get(position).getContact())));

                                        }
                                    });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    Log.d("TAG","FARMER NAMe: "+farmerEntities.get(position).getName());
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }
                            }));
                            progressBarFarmerList.setVisibility(View.INVISIBLE);
                            farmerRecyclerView.setVisibility(View.VISIBLE);
                        }catch (Exception f){
                            Log.d("TAG","Error in farmer data manipulate: "+f);
                        }
                    }
                });
    }

//    public void pondBtnClick1(View view) {
//        startActivity(new Intent(this,FishPondMapActivity.class));
//        finish();
//    }

    public void farmersBtnClick1(View view) {
        startActivity(new Intent(this,FarmerListActivity.class));
        finish();
    }

    public void smsBtnClick1(View view) {
        startActivity(new Intent(this,SmsActivity.class));
        finish();
    }

    public void bottomButton(){

        //:::: THIS IS FOR THE BOTTOM BUTTON ACTIVE CHANGING COLOR
        Drawable pond_inactive = getResources().getDrawable(R.drawable.ic_fish_ponds_inactive);
        Drawable farmer_active = getResources().getDrawable(R.drawable.ic_fish_farmer_active);
        Drawable sms_inactive = getResources().getDrawable(R.drawable.ic_fish_sms_inactive);

        //pondBtn.setCompoundDrawablesWithIntrinsicBounds(null,pond_inactive,null,null);
        farmerBtn.setCompoundDrawablesWithIntrinsicBounds(null,farmer_active,null,null);
        smsBtn.setCompoundDrawablesWithIntrinsicBounds(null,sms_inactive,null,null);
    }


    public void filtersBtnClick() {
        // Log.d("TAG","FILTER CLCK");
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Select Filters");

        //SET THE CUSTOM LAYOUT
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog,null);
        builder.setView(customLayout);

        CheckBox aizawlCB,kolasibCB,lawngtlaiCB,lungleiCB,mamitCB,siahaCB,serchhipCB,champhaiCB,hnahthialCB,khawzawlCB,saitualCB;
        final String[] district = {"Aizawl","Kolasib","Lawngtlai","Lunglei","Mamit","Siaha","Serchhip","Champhai","Hnahthial","Khawzawl","Saitual"};

        aizawlCB = customLayout.findViewById(R.id.Aizawl);    kolasibCB = customLayout.findViewById(R.id.Khawzawl);    lawngtlaiCB = customLayout.findViewById(R.id.Lawngtlai);
        lungleiCB = customLayout.findViewById(R.id.Lunglei);    mamitCB = customLayout.findViewById(R.id.Mamit);    siahaCB = customLayout.findViewById(R.id.Siaha);
        serchhipCB = customLayout.findViewById(R.id.Serchhip);    champhaiCB = customLayout.findViewById(R.id.Champhai);    hnahthialCB = customLayout.findViewById(R.id.Hnahthial);
        khawzawlCB = customLayout.findViewById(R.id.Khawzawl);    saitualCB = customLayout.findViewById(R.id.Saitual);
        recyclerViewScheme = customLayout.findViewById(R.id.list_of_scheme);
       // rkvyCB = customLayout.findViewById(R.id.rkvy);    nlupCB = customLayout.findViewById(R.id.nlup);    blueRevolutionCB = customLayout.findViewById(R.id.blue_revolution);
          //otherSchemeCB = customLayout.findViewById(R.id.others);nfdbCB = customLayout.findViewById(R.id.nfdb);

        final CheckBox[] districtCheckBoxes = new CheckBox[]{aizawlCB,kolasibCB,lawngtlaiCB,lungleiCB,mamitCB,siahaCB,serchhipCB,champhaiCB,hnahthialCB, khawzawlCB,saitualCB};
        final ArrayList<String> districtCheckedList = new ArrayList<>();
       // final CheckBox[] schemeCheckBoxex = new CheckBox[]{nfdbCB,rkvyCB,nlupCB,blueRevolutionCB,otherSchemeCB};
      //  final boolean[] schemeChecked = new boolean[]{false,false,false,false,false};

        //dynamic checkbox scheme


        //String schemesStr = sharedPreferences.getString("schemes","");

        schemesStrArr = mySchemeInString.split(",");
        ArrayList<Integer> myArraylist = new ArrayList<>();
        schemeListAdapter = new SchemeListAdapter(getApplicationContext(), schemesStrArr,myArraylist);
        recyclerViewScheme.setAdapter(schemeListAdapter);
        recyclerViewScheme.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // ::THE CHECKBOX IS MAKER FOR FURTHER USES
                for(int i=0;i<districtCheckBoxes.length;i++)
                    if(districtCheckBoxes[i].isChecked()){
                       districtCheckedList.add(district[i]);
                    }
                districtStr = TextUtils.join(",", districtCheckedList);
               schemeSelect = TextUtils.join(",", SchemeListAdapter.schemeChecked);
//                for(int j=0;j<schemeCheckBoxex.length;j++)
//                    if(schemeCheckBoxex[j].isChecked()) {
//                        schemeChecked[j]=true;
//                        Log.d("TAG","check2:"+schemeCheckBoxex[j]);
//
//                    }
//                for (int i=0;i<districtChecked.length;i++){
//                    if(districtChecked[i]){
//
//                        String testDistrict = String.valueOf(districtCheckBoxes[i].getText());
//                        Log.d("TAG",""+String.valueOf(districtCheckBoxes[i].getText()));
//                    }
//                }
                if(districtStr.matches("")){
                    districtStr = "Aizawl,Kolasib,Lawngtlai,Lunglei,Mamit,Siaha,Serchhip,Champhai,Hnahthial,Khawzawl,Saitual";
                }
                if(schemeSelect.matches("")){
                    schemeSelect = mySchemeInString;
                }
                Log.d("TAG","district select:"+districtStr);
                Log.d("TAG","Scheme select:"+schemeSelect);
                search(getApplicationContext(),mToken,districtStr,schemeSelect);


            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void search(final Context c, String mToken,String district,String scheme){
        farmerEntities.clear();
        Ion.with(c)
                .load(MY_POND_SEARCH)
                .setHeader("Accept","application/json")
                //  .setHeader("Content-Type","application/x-www-form-urlencoded")
                .setHeader("Authorization","Bearer "+mToken)
                .setMultipartParameter("district",district)
                .setMultipartParameter("scheme",scheme)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try{
                            farmerEntities.clear();
                            Log.d("TAG"," list AL: "+result);
                            JSONObject jsonObject = new JSONObject(String.valueOf(result));
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject singleRow =  jsonArray.getJSONObject(i);
                                String name = singleRow.getString("name");
                                String contact = singleRow.getString("contact");
                                String district = singleRow.getString("district");
                                String fname = singleRow.getString("fname");
                                String address = singleRow.getString("address");
                                String location_of_pond = singleRow.getString("location_of_pond");
                                String tehsil = singleRow.getString("tehsil");
                                String area = singleRow.getString("area");
                                String epicOrAadhaar = singleRow.getString("epic_no");
                                String nameOfScheme = singleRow.getString("name_of_scheme");
                                String image = singleRow.getString("image");
                                Double lat = singleRow.getDouble("lat");
                                Double lng = singleRow.getDouble("lng");
                                String pond1 = singleRow.getString("pondImage_one");
                                String pond2 = singleRow.getString("pondImage_two");
                                String pond3 = singleRow.getString("pondImage_three");
                                String pond4 = singleRow.getString("pondImage_four");

                                FarmerEntity mFarmerEntity= new FarmerEntity(name,contact,fname,address,district,location_of_pond,tehsil,area,epicOrAadhaar,nameOfScheme,image,lat,lng,pond1,pond2,pond3,pond4);
                                farmerEntities.add(mFarmerEntity);
                            }
                          listAdapterAdder(c,farmerEntities);
                        }catch (Exception f){
                            Log.d("TAG","Error in farmer data manipulate: "+f);
                        }
                    }
                });
    }

    boolean doubleBackToExitPressedOnce = false;

    public void listAdapterAdder(Context c, ArrayList<FarmerEntity> mFarmer){
        farmerListAdapter = new FarmerListAdapter(mFarmer, c);
        farmerRecyclerView.setAdapter(farmerListAdapter);
        progressBarFarmerList.setVisibility(View.INVISIBLE);
        farmerRecyclerView.setVisibility(View.VISIBLE);
        farmerListAdapter.notifyDataSetChanged();
    }
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

}
