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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.HorizontalImageViewAdapter;
import com.give.android_fisheries_2.adapter.RecyclerItemClickListener;
import com.give.android_fisheries_2.adapter.SchemeListAdapter;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.body.StringPart;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    //private Button selectPhotoButton;
    private Button submitButton;
    private LinearLayout linearLayoutMainForm;
    private LinearLayout uploadProgressBarLayout;
    private MaterialEditText locationOfPond;
    private MaterialEditText fathersNameEditText;
    private MaterialEditText addressEditText;
    private MaterialEditText epicOrAadhaarEditText;
    private MaterialEditText areaEditText;
   // private MaterialEditText tehsilEditText;
    private Spinner districtSpinner;
    private CheckBox checkBox;
    //private CropImageView profileImageViewButton;
    private ImageView profileImageViewButton;
    ImageView pondImageView_1;
    ImageView pondImageView_2;
    ImageView pondImageView_3;
    ImageView pondImageView_4;
    Spinner tehsilSpinner;
    ArrayList<String> tehsilArrList;

    private RecyclerView listOfSchemeRV;
    SchemeListAdapter schemeListAdapter;
    HorizontalImageViewAdapter horizontalImageViewAdapter;
    ArrayList<String> pondLists;
    List<File> fileList;
    //RecyclerView pondsImageHorizontalRecyclerView;
    private String real_path_lake;
    String web_path_str_image ;
    String web_path_str_pond1 ;
    String web_path_str_pond2 ;
    String web_path_str_pond3 ;
    String web_path_str_pond4 ;

    String local_path_str_image;
    String local_path_str_pond1;
    String local_path_str_pond2;
    String local_path_str_pond3;
    String local_path_str_pond4;

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
    File image;
    File pond1;
    File pond2;
    File pond3;
    File pond4;
    Boolean imageSelect;
    public static Boolean locationCheck;

    String DATA_UPLOAD_URL_CREATE;
    String DATA_UPLOAD_URL_EDIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_upload_data);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        DATA_UPLOAD_URL_CREATE = MainActivity.MAIN_URL+"api/fishponds/create";
        DATA_UPLOAD_URL_EDIT = MainActivity.MAIN_URL+"api/fishponds/edit";

        local_path_str_image = local_path_str_pond1 = local_path_str_pond2 = local_path_str_pond3 = local_path_str_pond4
                = web_path_str_image = web_path_str_pond1 = web_path_str_pond2 = web_path_str_pond3 = web_path_str_pond4 = "";

        imageSelect=false;
        pondLists = new ArrayList<>();
        tehsilArrList = new ArrayList<>();

        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",0);
        mApprove = Integer.parseInt(sharedPreferences.getString("approve",""));
        pondId = sharedPreferences.getString("pondId","");
        //schemes = new ArrayLreal_path_pond_1ist<>();


        //CHECK THE LAT LNG FROM THE GETLOCATION ACTIVITY

/*       lat2 = getIntent().getStringExtra("lat");
       lng2 = getIntent().getStringExtra("lng"); */
        lat2 = sharedPreferences.getString("lat","");
        lng2= sharedPreferences.getString("lng","");

        //CHECK IF LOCATION IF SELECT
        Boolean locationClick = sharedPreferences.getBoolean("location_click",false);
        if(locationClick){
            locationCheck = true;
        }else locationCheck = false;



        Log.e("TAG","My Token: "+sharedPreferences.getString("mToken","")+" approve:"+mApprove);

        /*//selectPhotoButton = findViewById(R.id.selectPhotoButton);*/
        submitButton =findViewById(R.id.submitButton);
        linearLayoutMainForm = findViewById(R.id.linearLayoutMainForm);
        uploadProgressBarLayout = findViewById(R.id.uploadProgressBarLayout);
        fathersNameEditText = findViewById(R.id.editTextDataFathersName);
        addressEditText= findViewById(R.id.editTextDataAddress);
        epicOrAadhaarEditText= findViewById(R.id.editTextDataEpicNo);
        areaEditText = findViewById(R.id.editTextDataArea);
        locationOfPond = findViewById(R.id.locationOfPondEditText);
        districtSpinner = findViewById(R.id.spinner_district);
        checkBox = findViewById(R.id.checkbox);
        listOfSchemeRV = findViewById(R.id.list_of_scheme);
        profileImageViewButton = findViewById(R.id.imageViewDateProfilePicture);
/*
   //     pondsImageHorizontalRecyclerView = findViewById(R.id.ponds_image_view_recycler_view);
*/
        location = findViewById(R.id.pondsLocation);
        tehsilSpinner = findViewById(R.id.spinner_tehsil_farmer_upload);

        pondImageView_1 = findViewById(R.id.pond_image_1);
        pondImageView_2 = findViewById(R.id.pond_image_2);
        pondImageView_3 = findViewById(R.id.pond_image_3);
        pondImageView_4 = findViewById(R.id.pond_image_4);

        Bitmap profilePictureBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_person_black_24dp);
        profileImageViewButton.setImageBitmap(profilePictureBitmap);


        String schemesStr = sharedPreferences.getString("schemes","");

        schemes = schemesStr.split(",");
        Log.d("TAG","d2222"+schemes[0]);

        location.setText(lat2+", "+lng2);

        //TODO :: POPULATE THE FARMER DATA IF ALREADY PRESENT
        approvalStatus(mApprove);

        //SET DISTRIC SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        //POPULATE SPINNER IF ALREADY SELECTED
        String mDistrict = sharedPreferences.getString("district","");
        int spinnerPosition = adapter.getPosition(mDistrict);
        districtSpinner.setSelection(spinnerPosition);

        //SET TEHSIL SPINNER
        tehsilArrList= stringToArrayList(sharedPreferences.getString("all_tehsil","")) ;

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, tehsilArrList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tehsilSpinner.setAdapter(spinnerArrayAdapter);

        //POPULATE TEHSIL IF ALREADY SELECtED
        String mTehsil = sharedPreferences.getString("tehsil","");
        int spinnerPositionTehsil = spinnerArrayAdapter.getPosition(mTehsil);
        tehsilSpinner.setSelection(spinnerPositionTehsil);

        //:::: TODO THIS SHOULD BE TAKEN FROM THE SERVER BEFORE PRODUCTION
/*
        //  schemes = new String[] {"NFDB", "RKVY", "NLUP", "Blue Revolution"};
*/
        //POPULATE THE CHECK BOX
        ArrayList<Integer> mCheckedItem = new ArrayList<>();
        String mScheme = sharedPreferences.getString("name_of_scheme","");
        List<String> items = Arrays.asList(mScheme.split("\\s*,\\s*"));
        Log.d("TAG","Selectd list:"+ items);

        for(int i=0;i< items.size();i++){
            for (int j=0;j<schemes.length;j++){
                if(schemes[j].matches(items.get(i)))
                    mCheckedItem.add(j);
            }
        }

       //POPULATE ALL PICTURE IF PRESENT LOCALLY
        local_path_str_image = sharedPreferences.getString("image_local","");
        local_path_str_pond1 = sharedPreferences.getString("pond1_local","");
        local_path_str_pond2 = sharedPreferences.getString("pond2_local","");
        local_path_str_pond3 = sharedPreferences.getString("pond3_local","");
        local_path_str_pond4 = sharedPreferences.getString("pond4_local","");

        if(!local_path_str_image.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_image);
            profileImageViewButton.setImageBitmap(profilePictureBitmap2);
        }
        if(!local_path_str_pond1.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond1);
            pondImageView_1.setImageBitmap(profilePictureBitmap2);
        }
        if(!local_path_str_pond2.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond2);
            pondImageView_2.setImageBitmap(profilePictureBitmap2);
        }
        if(!local_path_str_pond3.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond3);
            pondImageView_3.setImageBitmap(profilePictureBitmap2);
        }
        if(!local_path_str_pond4.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond4);
            pondImageView_4.setImageBitmap(profilePictureBitmap2);
        }

        schemeListAdapter = new SchemeListAdapter(getApplicationContext(), schemes, mCheckedItem);
        listOfSchemeRV.setAdapter(schemeListAdapter);
        listOfSchemeRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

      /*  selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);
            }
        });
*/
        //RecyclerItemClickListener.class IS USER DEFINE CLASS
        /*pondsImageHorizontalRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), pondsImageHorizontalRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
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
        }));*/
    }

    //RETURN OF EVERY PICTURE SELECT
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 1://PROFILE PICTURE SELECT

                    Uri fileUri = data.getData();
                    local_path_str_image = getRealPathFromURI(this, fileUri);
                    imageSelect = true;
                    image = new File(local_path_str_image);
                    sharedPreferences.edit().putString("image_local",local_path_str_image).apply();

                    Bitmap profilePictureBitmap = BitmapFactory.decodeFile(local_path_str_image);
                    profileImageViewButton.setImageBitmap(profilePictureBitmap);
                    break;
              /*  case 2://LAKE PICTURE SELECT
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
*/
                case 11:
                    Uri fileUri_11 = data.getData();
                    local_path_str_pond1 = getRealPathFromURI(this, fileUri_11);
                    pond1 = new File(local_path_str_pond1);
                    sharedPreferences.edit().putString("pond1_local",local_path_str_pond1).apply();

                    Bitmap profilePictureBitmap_11 = BitmapFactory.decodeFile(local_path_str_pond1);
                    pondImageView_1.setImageBitmap(profilePictureBitmap_11);
                    break;

                case 12:
                    Uri fileUri_12 = data.getData();
                    local_path_str_pond2 = getRealPathFromURI(this, fileUri_12);
                    pond2 = new File(local_path_str_pond2);
                    sharedPreferences.edit().putString("pond2_local",local_path_str_pond2).apply();

                    Bitmap profilePictureBitmap_12 = BitmapFactory.decodeFile(local_path_str_pond2);
                    pondImageView_2.setImageBitmap(profilePictureBitmap_12);
                    break;

                case 13:
                    Uri fileUri_13 = data.getData();
                    local_path_str_pond3 = getRealPathFromURI(this, fileUri_13);
                    pond3 = new File(local_path_str_pond3);
                    sharedPreferences.edit().putString("pond3_local",local_path_str_pond3).apply();

                    Bitmap profilePictureBitmap_13 = BitmapFactory.decodeFile(local_path_str_pond3);
                    pondImageView_3.setImageBitmap(profilePictureBitmap_13);
                    break;

                case 14:
                    Uri fileUri_14 = data.getData();
                    local_path_str_pond4 = getRealPathFromURI(this, fileUri_14);
                    pond4 = new File(local_path_str_pond4);
                    sharedPreferences.edit().putString("pond4_local",local_path_str_pond4).apply();

                    Bitmap profilePictureBitmap_14 = BitmapFactory.decodeFile(local_path_str_pond4);
                    pondImageView_4.setImageBitmap(profilePictureBitmap_14);
                    break;

            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    public void submitClick(View view) {
        submitButton.setVisibility(GONE);
        uploadProgressBarLayout.setVisibility(View.VISIBLE);
        String lat = sharedPreferences.getString("lat","");

        if( fathersNameEditText.getText().toString().matches("") || addressEditText.getText().toString().matches("") ||
               areaEditText.getText().toString().matches("") ||
                epicOrAadhaarEditText.getText().toString().matches("")|| locationOfPond.getText().toString().matches("")){
            Toasty.error(this,"All fields are madatory",Toasty.LENGTH_SHORT).show();
            startActivity(new Intent(this,FarmerUploadDataActivity.class));
        }else if (lat.matches("")){
            Toasty.error(this,"Get Location",Toasty.LENGTH_SHORT).show();
            submitButton.setVisibility(View.VISIBLE);
            uploadProgressBarLayout.setVisibility(GONE);
        }//THIS IS NEW VALIDATION FOR HANDLING PROPIC
        else if(local_path_str_image.matches("") && web_path_str_image.matches("")){
            Toasty.error(this,"Select you image",Toasty.LENGTH_SHORT).show();
            submitButton.setVisibility(View.VISIBLE);
            uploadProgressBarLayout.setVisibility(GONE);
        }
        else
            apiFirst();

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
        String location_of_pond= locationOfPond.getText().toString();
        String tehsil = tehsilSpinner.getSelectedItem().toString();
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
        sharedPreferences.edit().putString("area",area).apply();
        sharedPreferences.edit().putString("epic_no",epic_no).apply();
        sharedPreferences.edit().putString("name_of_scheme",name_of_scheme).apply();
        sharedPreferences.edit().putString("lat",lat).apply();
        sharedPreferences.edit().putString("lng",lng).apply();

        Log.d("TAG","Approve status: "+mApprove);
        if(mApprove==0){
            method = "POST";
           // createOrEditUrl =  "http://192.168.43.205:8000/api/fishponds/create";
            createOrEditUrl = DATA_UPLOAD_URL_CREATE;
        }else if(mApprove ==1 ||mApprove ==2 ||mApprove ==3 ||mApprove ==4 ){
            method="POST";
           // createOrEditUrl = "http://192.168.43.205:8000/api/fishponds/edit/"+pondId;
            createOrEditUrl = DATA_UPLOAD_URL_EDIT + pondId;
        }else{
            method="";
            createOrEditUrl="";
        }

        Builders.Any.B builder = Ion.with(this).load(method,createOrEditUrl)
                .setHeader("Accept","application/json")
                .setHeader("Authorization","Bearer "+mToken);

        List<Part> parts = new ArrayList<>();
        //ADD PARTS
        if(mApprove ==1 ||mApprove ==2 ||mApprove ==3 ||mApprove ==4) parts.add(new StringPart("_method","PUT"));

        parts.add(new StringPart("district",district));
        parts.add(new StringPart("name",mName));
        parts.add(new StringPart("contact",mContact));
        parts.add(new StringPart("fname",fname));
        parts.add(new StringPart("address",address));
        parts.add(new StringPart("location_of_pond",location_of_pond));
        parts.add(new StringPart("tehsil",tehsil));
        parts.add(new StringPart("area",area));
        parts.add(new StringPart("epic_no",epic_no));
        parts.add(new StringPart("name_of_scheme",name_of_scheme));
        parts.add(new StringPart("lat",lat));
        parts.add(new StringPart("lng",lng));
        parts.add(new StringPart("user_id", String.valueOf(mId)));

        if(!local_path_str_image.matches("")) {
            parts.add(new FilePart("image", new File(local_path_str_image)));
        }
        if(!local_path_str_pond1.matches(""))
            parts.add(new FilePart("pondImage_one",new File(local_path_str_pond1)));
        if(!local_path_str_pond2.matches(""))
            parts.add(new FilePart("pondImage_two",new File(local_path_str_pond2)));
        if(!local_path_str_pond3.matches(""))
            parts.add(new FilePart("pondImage_three",new File(local_path_str_pond3)));
        if(!local_path_str_pond4.matches(""))
            parts.add(new FilePart("pondImage_four",new File(local_path_str_pond4)));

        builder.addMultipartParts(parts);
        builder.asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                Log.d("TAG","FILE "+pond1);
                Log.d("TAG","URL 1: "+result);
                //::::::FOR NOW ONLY BECAUSE SECOND API IS NOT CALLED::::::
                submitButton.setVisibility(View.VISIBLE);
                uploadProgressBarLayout.setVisibility(GONE);
                if(result!=null) Toasty.success(getApplicationContext(),"Upload Successfully!",Toasty.LENGTH_SHORT).show();
                else Toasty.error(getApplicationContext(),"Sorry, server out of reach",Toasty.LENGTH_SHORT).show();
               finish();
                startActivity(new Intent(getApplicationContext(),FarmerCenterActivity.class));
                //::::::    UPTO THIS   ::::::
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
     /*   pondsImageHorizontalRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","view "+view);
            }
        });*/
    }

    public void getLocationClick(View view) {

        try{
            String fname = fathersNameEditText.getText().toString();
            String address= addressEditText.getText().toString();
            String district= districtSpinner.getSelectedItem().toString();;
            String location_of_pond= locationOfPond.getText().toString();
            String tehsil = tehsilSpinner.getSelectedItem().toString();
            String area= areaEditText.getText().toString();
            String epic_no= epicOrAadhaarEditText.getText().toString();

            String name_of_scheme= TextUtils.join(",", SchemeListAdapter.schemeChecked);

            sharedPreferences.edit().putString("fname",fname).apply();
            sharedPreferences.edit().putString("address",address).apply();
            sharedPreferences.edit().putString("district",district).apply();
            sharedPreferences.edit().putString("location_of_pond",location_of_pond).apply();
            sharedPreferences.edit().putString("tehsil",tehsil).apply();
            //sharedPreferences.edit().putString("image",result.get("image").getAsString()).apply();
            sharedPreferences.edit().putString("area",area).apply();
            sharedPreferences.edit().putString("epic_no",epic_no).apply();
            sharedPreferences.edit().putString("name_of_scheme",name_of_scheme).apply();

            startActivity(new Intent(this,GetLocationInMapActivity.class));

        }catch (Exception e){
            Toasty.error(this,"Location Permission is not given",Toasty.LENGTH_SHORT).show();
        }
    }

    public void profilePictureSelectClick(View view) {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
        }catch (Exception e){
            Toasty.error(this,"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
        }
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
             //   tehsilEditText.setText(sharedPreferences.getString("tehsil",""));
                locationOfPond.setText(sharedPreferences.getString("location_of_pond",""));
                location.setText(lat2+ ", "+lng2);
                //populate spinner
                //populate checkbox
                //populateimage
                location.setText("Lat: "+sharedPreferences.getString("lat","")+", \nLng: "+sharedPreferences.getString("lng",""));

                web_path_str_image = sharedPreferences.getString("image_web","");
                web_path_str_pond1 = sharedPreferences.getString("pond1_web","");
                web_path_str_pond2 = sharedPreferences.getString("pond2_web","");
                web_path_str_pond3 = sharedPreferences.getString("pond3_web","");
                web_path_str_pond4 = sharedPreferences.getString("pond4_web","");

              //  if(!myImage.matches(""))
               //     imageSelect = true; //IMAGE IS ALREADY SELECTED
//                image = new File(myImage);
//                pond1 = new File(pondImage_one);
//                pond2 = new File(pondImage_two);

//                pond3 = new File(pondImage_three);
//                pond4 = new File(pondImage_four);

                //PROFILE IMAGE
                if(!web_path_str_image.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image/"+web_path_str_image).into(profileImageViewButton);
                    Log.d("TAG","Image location IF TES");
                }else
                    Log.d("TAG","else");
                //POND IMAGE ONE
                if(!web_path_str_pond1.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image1/"+web_path_str_pond1).into(pondImageView_1);
                    Log.d("TAG","Image location IF TES");
                }else
                    Log.d("TAG","else");
                //POND IMAGE TWO
                if(!web_path_str_pond2.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image2/"+web_path_str_pond2).into(pondImageView_2);
                    Log.d("TAG","Image location IF TES");
                }else
                    Log.d("TAG","else");
                //POND IMAGE THREE
                if(!web_path_str_pond3.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image3/"+web_path_str_pond3).into(pondImageView_3);
                    Log.d("TAG","Image location IF TES");
                }else
                    Log.d("TAG","else");
                //POND IMAGE FOUR
                if(!web_path_str_pond4.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image4/"+web_path_str_pond4).into(pondImageView_4);
                    Log.d("TAG","Image location IF TES");
                }else
                    Log.d("TAG","else");
                break;
            case 1:
                break;
        }
    }


    public void photo1Click(View view) {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "pond_1"),11);
        }catch (Exception e){
            Toasty.error(this,"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
        }

    }

    public void photo2Click(View view) {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "pond_2"),12);
        }catch (Exception e){
            Toasty.error(this,"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
        }
    }

    public void photo3Click(View view) {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "pond_3"),13);
        }catch (Exception e){
            Toasty.error(this,"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
        }
    }

    public void photo4Click(View view) {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "pond_4"),14);
        }catch (Exception e){
            Toasty.error(this,"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
        }
    }

    public ArrayList<String> stringToArrayList(String string){
        String[] strArr = string.split(",");
        List<String> strList = Arrays.asList(strArr);
        ArrayList<String> strArrList = new ArrayList<String>(strList);

        return strArrList;
    }
//    @Override
//    public void onBackPressed() {
//        finish();
//        startActivity(new Intent(this,FarmerCenterActivity.class));
//    }
}

