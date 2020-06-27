package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.HorizontalImageViewAdapter;
import com.give.android_fisheries_2.adapter.RecyclerItemClickListener;
import com.give.android_fisheries_2.adapter.SchemeListAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;

public class FarmerUploadDataActivity extends AppCompatActivity {

    String[] schemes;
    boolean[] schemesChecked=new boolean[]{false,false,false,false};
    SharedPreferences sharedPreferences;
    private TextView location;
    private Button selectPhotoButton;
    private Button submitButton;
    private LinearLayout linearLayoutMainForm;
    private LinearLayout uploadProgressBarLayout;
    private MaterialEditText nameEditText;
    private MaterialEditText fathersNameEditText;
    private MaterialEditText addressEditText;
    private MaterialEditText epicOrAadhaarEditText;
    private MaterialEditText contactEditText;
    private MaterialEditText areaEditText;
    private MaterialEditText tehsilEditText;
    private Spinner districtSpinner;
    private CheckBox checkBox;
    //private CropImageView profileImageViewButton;
    private ImageView profileImageViewButton;
    private RecyclerView listOfSchemeRV;
    SchemeListAdapter schemeListAdapter;
    HorizontalImageViewAdapter horizontalImageViewAdapter;
    ArrayList<String> pondLists;
    List<File> fileList;
    RecyclerView pondsImageHorizontalRecyclerView;
    private String real_path_lake;
    String real_path_profileImage ;
    private String TAG = "TAG";
    String mToken;
    String mContact;
    String mName;
    String lat2 ="";
    String lng2 ="";
    String latLng="";
    int position;
    int mId;
    int mApprove;
    String pondId;
    Uri mamaUri;
    File mySamePropic;
    Boolean imageSelect;
    //List<String> myListOfScheme ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_upload_data);

        imageSelect=false;
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        pondLists = new ArrayList<>();

        //schemes = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String schemesStr = bundle.getString("schemes");

        schemes = schemesStr.split(",");
        Log.d("TAG","d2222"+schemes[0]);
        //CHECK THE LAT LNG FROM THE GETLOCATION ACTIVITY

        lat2 = getIntent().getStringExtra("lat");
        lng2 = getIntent().getStringExtra("lng");

      //  location.setText(lat2+", "+lng2);
        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",0);
        mApprove = Integer.parseInt(sharedPreferences.getString("approve",""));
        pondId = sharedPreferences.getString("pondId","");

        Log.e("TAG","My Token: "+sharedPreferences.getString("mToken","")+" approve:"+mApprove);

        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        submitButton =findViewById(R.id.submitButton);
        linearLayoutMainForm = findViewById(R.id.linearLayoutMainForm);
        uploadProgressBarLayout = findViewById(R.id.uploadProgressBarLayout);
        fathersNameEditText = findViewById(R.id.editTextDataFathersName);
        addressEditText= findViewById(R.id.editTextDataAddress);
        epicOrAadhaarEditText= findViewById(R.id.editTextDataEpicNo);
        areaEditText = findViewById(R.id.editTextDataArea);
        tehsilEditText = findViewById(R.id.editTextTehsil);
        districtSpinner = findViewById(R.id.spinner_district);
        checkBox = findViewById(R.id.checkbox);
        listOfSchemeRV = findViewById(R.id.list_of_scheme);
        profileImageViewButton = findViewById(R.id.imageViewDateProfilePicture);
        pondsImageHorizontalRecyclerView = findViewById(R.id.ponds_image_view_recycler_view);
        location = findViewById(R.id.pondsLocation);

        Bitmap profilePictureBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_person_black_24dp);
        profileImageViewButton.setImageBitmap(profilePictureBitmap);

        //TODO :: POPULATE THE FARMER DATA IF ALREADY PRESENT
        approvalStatus(mApprove);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        //POPULATE SPINNER IF ALREADY SELECTED
        String mDistrict = sharedPreferences.getString("district","");
        int spinnerPosition = adapter.getPosition(mDistrict);
        districtSpinner.setSelection(spinnerPosition);

        //:::: TODO THIS SHOULD BE TAKEN FROM THE SERVER BEFORE PRODUCTION
        //  schemes = new String[] {"NFDB", "RKVY", "NLUP", "Blue Revolution"};

        //POPULATE THE CHECK BOX
        ArrayList<Integer> mCheckedItem = new ArrayList<>();
        String mScheme = sharedPreferences.getString("name_of_scheme","");
        List<String> items = Arrays.asList(mScheme.split("\\s*,\\s*"));
        Log.d("TAG","list:"+schemes);
        Log.d("TAG","Selectd list:"+ items);
        for(int i=0;i< items.size();i++){
            for (int j=0;j<schemes.length;j++){
                if(schemes[j].matches(items.get(i))){
                    mCheckedItem.add(j);
                }
            }
        }

        schemeListAdapter = new SchemeListAdapter(getApplicationContext(), schemes,mCheckedItem);
        listOfSchemeRV.setAdapter(schemeListAdapter);
        listOfSchemeRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);
            }
        });

        //RecyclerItemClickListener.class IS USER DEFINE CLASS
        pondsImageHorizontalRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), pondsImageHorizontalRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Log.d("TAG","pos "+position);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pondLists.remove(position);
                        pondsImageHorizontalRecyclerView.removeViewAt(position);
                        horizontalImageViewAdapter.notifyItemRemoved(position);
                        horizontalImageViewAdapter.notifyItemRangeChanged(position,pondLists.size());
                        horizontalImageViewAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 1://PROFILE PICTURE SELECT

                    Uri fileUri = data.getData();
                    real_path_profileImage = getRealPathFromURI(this, fileUri);
                    imageSelect = true;
                    //test for file could be final
                    mySamePropic = new File(real_path_profileImage);

                    Bitmap profilePictureBitmap = BitmapFactory.decodeFile(real_path_profileImage);
                    profileImageViewButton.setImageBitmap(profilePictureBitmap);
                    break;

                case 2://LAKE PICTURE SELECT
                    if (resultCode == Activity.RESULT_OK) {
                        ClipData clipData = data.getClipData();
                        if(clipData!=null){
                            int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                            Uri imageUri;
                            for(int i = 0; i < count; i++){
                                ClipData.Item item = clipData.getItemAt(i);
                                imageUri = item.getUri();
                                real_path_lake = getRealPathFromURI(this, imageUri);
                                pondLists.add(real_path_lake);
                                Log.d("TAG","image: "+real_path_lake);
                            }
                        }else{
                            Uri uri = data.getData();
                            String mRealPathLake = getRealPathFromURI(this,uri);
                            pondLists.add(mRealPathLake);
                        }
                        horizontalImageViewAdapter = new HorizontalImageViewAdapter(pondLists);
                        pondsImageHorizontalRecyclerView.setAdapter(horizontalImageViewAdapter);
                        pondsImageHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    public void submitClick(View view) {
        submitButton.setVisibility(GONE);
        uploadProgressBarLayout.setVisibility(View.VISIBLE);
        apiFirst();
      //  apiSecond();
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();
        cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    public void apiFirst(){
        String fname = fathersNameEditText.getText().toString();
        String address= addressEditText.getText().toString();
        String district= districtSpinner.getSelectedItem().toString();;
        String location_of_pond= "location of pond";//HEI HI ENG KAN CIAG LO
        String tehsil = tehsilEditText.getText().toString();
        String area= areaEditText.getText().toString();
        String epic_no= epicOrAadhaarEditText.getText().toString();
        String name_of_scheme= TextUtils.join(",", SchemeListAdapter.schemeChecked);
        String lat = lat2;
        String lng = lng2;
        Log.d("TAG",""+fname+address+district+location_of_pond+tehsil+area+epic_no+name_of_scheme);
        String createOrEditUrl;
        String method;

        //Put the new edit
        sharedPreferences.edit().putString("fname",fname).apply();
        sharedPreferences.edit().putString("address",address).apply();
        sharedPreferences.edit().putString("district",district).apply();
        sharedPreferences.edit().putString("location_of_pond",location_of_pond).apply();
        sharedPreferences.edit().putString("tehsil",tehsil).apply();
        //sharedPreferences.edit().putString("image",result.get("image").getAsString()).apply();
        sharedPreferences.edit().putString("area",area).apply();
        sharedPreferences.edit().putString("epic_no",epic_no).apply();
        sharedPreferences.edit().putString("name_of_scheme",name_of_scheme).apply();
        //sharedPreferences.edit().putString("pondImages",data.result("pondImages").getAsString()).apply();
        sharedPreferences.edit().putString("lat",lat).apply();
        sharedPreferences.edit().putString("lng",lng).apply();

        Log.d("TAG"," "+mApprove);
        if(mApprove==0){
            method = "POST";
            createOrEditUrl =  "http://192.168.43.205:8000/api/fishponds/create";
        }else if(mApprove ==1 ||mApprove ==2 ||mApprove ==3 ||mApprove ==4 ){
            method="POST";
            createOrEditUrl = "http://192.168.43.205:8000/api/fishponds/edit/"+pondId;
        }else{
            method="";
            createOrEditUrl="";
        }

        //TODO if image is not selected then, it throws error , thats why i did, two things, if image is select version and image not select version
        if(imageSelect){
            try{
                Ion.with(this)
                        .load(method,createOrEditUrl )
                        .setHeader("Accept","application/json")
                        .setHeader("Authorization","Bearer "+mToken)
                        .setMultipartParameter("_method","PUT")
                        .setMultipartParameter("district",district)
                        .setMultipartParameter("name",mName)
                        .setMultipartParameter("fname",fname)
                        .setMultipartParameter("address",address)
                        .setMultipartParameter("location_of_pond",location_of_pond)
                        .setMultipartParameter("tehsil",tehsil)
                        .setMultipartParameter("area",area)
                        .setMultipartParameter("epic_no",epic_no)
                        .setMultipartParameter("name_of_scheme",name_of_scheme)
                        .setMultipartFile("image","multipart/form-data",new File(real_path_profileImage))
                        .setMultipartParameter("lat",lat)
                        .setMultipartParameter("lng",lng)
                        .setMultipartParameter("user_id", String.valueOf(mId))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Log.d("TAG","FILE "+real_path_profileImage);
                                Log.d("TAG","URL 1: "+result);
                                //::::::FOR NOW ONLY BECAUSE SECOND API IS NOT CALLED::::::
                                submitButton.setVisibility(View.VISIBLE);
                                uploadProgressBarLayout.setVisibility(GONE);
                                if(result!=null) Toasty.success(getApplicationContext(),"Upload Successfully!",Toasty.LENGTH_SHORT).show();
                                else Toasty.error(getApplicationContext(),"Sorry, server out of reach",Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),FarmerCenterActivity.class));
                                finish();
                                //::::::    UPTO THIS   ::::::
                            }
                        });
            }catch (Exception e){
                Log.e("TAG","ERROR IN URL1:"+e);
            }
        }else{
            try{
                Ion.with(this)
                        .load(method,createOrEditUrl )
                        .setHeader("Accept","application/json")
                        .setHeader("Authorization","Bearer "+mToken)
                        .setMultipartParameter("_method","PUT")
                        .setMultipartParameter("district",district)
                        .setMultipartParameter("name",mName)
                        .setMultipartParameter("fname",fname)
                        .setMultipartParameter("address",address)
                        .setMultipartParameter("location_of_pond",location_of_pond)
                        .setMultipartParameter("tehsil",tehsil)
                        .setMultipartParameter("area",area)
                        .setMultipartParameter("epic_no",epic_no)
                        .setMultipartParameter("name_of_scheme",name_of_scheme)
                        //.setMultipartParameter("lat",lat)
                      //  .setMultipartParameter("lng",lng)
                        .setMultipartParameter("user_id", String.valueOf(mId))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Log.d("TAG","FILE "+real_path_profileImage);
                                Log.d("TAG","URL 1: "+result);
                                //::::::FOR NOW ONLY BECAUSE SECOND API IS NOT CALLED::::::
                                submitButton.setVisibility(View.VISIBLE);
                                uploadProgressBarLayout.setVisibility(GONE);
                                if(result!=null) Toasty.success(getApplicationContext(),"Upload Successfully!",Toasty.LENGTH_SHORT).show();
                                else Toasty.error(getApplicationContext(),"Sorry, server out of reach",Toasty.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),FarmerCenterActivity.class));
                                finish();
                                //::::::    UPTO THIS   ::::::
                            }
                        });
            }catch (Exception e){
                Log.e("TAG","ERROR IN URL1:"+e);
            }
        }

    }
    public void apiSecond(){

            try{
                List<Part> files = new ArrayList<>();
                for (int i = 0;i<pondLists.size();i++){
                    Log.d("TAG","Array of files created");
                    files.add(new FilePart("pondImages["+i+"+",new File(pondLists.get(i))));
                }
                Ion.with(this)
                        .load("PUT","http://192.168.225.57:8000/api/fishponds/uploadpond/"+mId)
                        .setHeader("Authorization","Bearer "+mToken)
                        .setHeader("Content-Type","multipart/form-data")

                        .setMultipartParameter("_method", "PUT")
                        //.setMultipartFile("pondImages[]","multipart/form-data",new File(real_path_lake))
                        //::::TODO UPLOAD THE PONDS IMAGE HERE
                         .addMultipartParts(files)
                        //json ang nilo in MULTIPART ANGIN HANDLE MAI RAWH SE SERV
                        // ER AH. A HMA A DIK SA ANG KHAN. JSON BODY LEH MULTIPART A AWM KOP THEI SI LO

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {           ///sdcard/DCIM/Camera/IMG_20200217_123440.jpg
                                submitButton.setVisibility(View.VISIBLE);
                                Log.e(TAG,"result: "+result);
                                Toasty.success(getApplicationContext(),"Upload Success!!",Toasty.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                //  finish();
                            }
                        });
            }catch (Exception e){
                Toast.makeText(this,"Some error in api second call:"+e,Toast.LENGTH_LONG).show ();
            }
    }

    @Override
    public void onResume() {
        super.onResume();
        pondsImageHorizontalRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","view "+view);
            }
        });
    }

    public void getLocationClick(View view) {
        startActivity(new Intent(this,GetLocationInMapActivity.class));
    }

    public void profilePictureSelectClick(View view) {
        Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }

    public void approvalStatus(int approve){
        switch(approve){
            case 0:
            case 3:
            case 4:
            case 2:
                fathersNameEditText.setText(sharedPreferences.getString("fname",""));
                addressEditText.setText(sharedPreferences.getString("address",""));
                epicOrAadhaarEditText.setText(sharedPreferences.getString("epic_no",""));
                areaEditText.setText(sharedPreferences.getString("area",""));
                tehsilEditText.setText(sharedPreferences.getString("tehsil",""));
                location.setText(lat2+ ", "+lng2);
                //populate spinner
                //populate checkbox
                //populateimage
                location.setText(sharedPreferences.getString("lat","")+", "+sharedPreferences.getString("lng",""));

                String myImage = sharedPreferences.getString("image","");
                Log.d("TAG","Image location "+myImage);
                if(!myImage.equals("")){
                    Picasso.get().load("http://192.168.43.205:8000/public/image/"+myImage).into(profileImageViewButton);
                    Log.d("TAG","Image location IF TES");
                    //Picasso.get().load("http://192.168.43.205:8000/public/image/"+myImage).into(getTarget("http://192.168.43.205:8000/public/image/777"));


                }else
                    Log.d("TAG","else");

                break;
            case 1:
                break;
        }
    }

    private Target getTarget(final String url){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mySamePropic = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/sssss");
                        try{
                       //     mySamePropic.createNewFile();
                            mySamePropic.mkdirs();
                            FileOutputStream outputStream= new FileOutputStream(new File(mySamePropic, new Date().toString()+".jpg"));
                            bitmap.compress(Bitmap.CompressFormat.JPEG,80,outputStream);
                            outputStream.flush();
                            outputStream.close();

                        }catch (IOException e){
                            Log.e("TAG",e.getLocalizedMessage());
                        }
                    }
                }).start();
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };
        return target;
    }
}
