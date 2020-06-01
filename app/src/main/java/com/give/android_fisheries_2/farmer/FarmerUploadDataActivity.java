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
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;

public class FarmerUploadDataActivity extends AppCompatActivity {

    String[] schemes;
    boolean[] schemesChecked=new boolean[]{false,false,false,false};
    SharedPreferences sharedPreferences;
    private TextView location;
    private ProgressBar progressBar;
    private ProgressBar locationConfirmProgressBar;
    private Button selectPhotoButton;
    private Button submitButton;
    private Button locationConfirmButton;
    private Button takePhotoButton;
    private LinearLayout progressBarLayout;
    private LinearLayout linearLayoutConfirmLocation;
    private LinearLayout linearLayoutMainForm;
    private MaterialEditText nameEditText;
    private MaterialEditText fathersNameEditText;
    private MaterialEditText addressEditText;
    private MaterialEditText epicOrAadhaarEditText;
    private MaterialEditText contactEditText;
    private MaterialEditText areaEditText;
    private MaterialEditText tehsilEditText;
    private Spinner districtSpinner;
    private CheckBox checkBox;
    private CropImageView profileImageViewButton;
    //private ImageView profileImageViewButton;
    private RecyclerView listOfSchemeRV;
    SchemeListAdapter schemeListAdapter;
    HorizontalImageViewAdapter horizontalImageViewAdapter;
    ArrayList<String> pondLists;
    List<File> fileList;
    RecyclerView pondsImageHorizontalRecyclerView;
    private String real_path_lake;
    private String real_path_profileImage;
    private String TAG = "TAG";
    String mToken;
    String mContact;
    String mName;
    String lat2 ="";
    String lng2 ="";
    String latLng="";
    int position;
    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_upload_data);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        pondLists = new ArrayList<>();

        //CHECK THE LAT LNG FROM THE GETLOCATION ACTIVITY
        lat2 = getIntent().getStringExtra("lat");
        lng2 = getIntent().getStringExtra("lng");

        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",mId);

        Log.e("TAG","My Token: "+sharedPreferences.getString("mToken",""));

        progressBar = findViewById(R.id.simpleProgressBar);
        locationConfirmProgressBar = findViewById(R.id.locationConfirmProgressBar);
        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        submitButton =findViewById(R.id.submitButton);
        locationConfirmButton = findViewById(R.id.locationConfirm);
        linearLayoutConfirmLocation = findViewById(R.id.linearLayoutConfirmLocation);
        linearLayoutMainForm = findViewById(R.id.linearLayoutMainForm);
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

        location.setText(lat2+ ", "+lng2);

        //takePhotoButton.setEnabled(false);

        /*mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(getActivity())
                .addOnConnectionFailedListener(getActivity())
                .addApi(LocationServices.API).build();*/

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        //:::: TODO THIS SHOULD BE TAKEN FROM THE SERVER BEFORE PRODUCTION
        schemes = new String[] {"NFDB", "RKVY", "NLUP", "Blue Revolution"};
        schemeListAdapter = new SchemeListAdapter(getApplicationContext(), schemes);
        listOfSchemeRV.setAdapter(schemeListAdapter);
        listOfSchemeRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        profileImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
            */

            }
        });
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

    //TODO::::ONACTIVITY RESULT THAR  BECAUSE CROP IMAGE HIAN  HEMI FUNCTION HI A CHHOM
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //cropImageView.getCroppedImageAsync();
                profileImageViewButton.setImageUriAsync(resultUri);

//                real_path_profileImage = getRealPathFromURI(this,resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if(requestCode==2){
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
        }
       
    }
/*

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 1://PROFILE PICTURE SELECT
                */
/*    Uri fileUri = data.getData();
                    real_path_profileImage = getRealPathFromURI(this,fileUri);
                    Bitmap profilePictureBitmap = BitmapFactory.decodeFile(real_path_profileImage);
                    profileImageViewButton.setImageBitmap(profilePictureBitmap);
                *//*

                    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                        CropImage.ActivityResult result = CropImage.getActivityResult(data);
                        if (resultCode == RESULT_OK) {
                            Uri resultUri = result.getUri();
                            //cropImageView.getCroppedImageAsync();
                            profileImageViewButton.setImageUriAsync(resultUri);
                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = result.getError();
                        }
                    }

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
*/
/*
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        //data gives you the image uri. Try to convert that to bitmap
                        Uri file_uri = data.getData(); // parse to Uri if your videoURI is string
                        real_path_lake = getRealPathFromURI(getActivity(), file_uri);                        //Log.e(TAG, "data: ") ;
                        //path = getRealPathFromURI(data.getData());
                        Log.e(TAG, "Path: "+real_path_lake) ;
                        ExifInterface exif = new ExifInterface(String.valueOf(real_path_lake));
                        float[] latLong = new float[2];
                        boolean hasLatLong = exif.getLatLong(latLong);
                        if (hasLatLong) {
                            System.out.println("Latitude: " + latLong[0]);
                            System.out.println("Longitude: " + latLong[1]);
                            Log.e(TAG,"lat: "+ latLong[0]);
                            lat=latLong[0];
                            lng=latLong[1];

                            formPicture = new LatLng(lat,lng);
                            if(lat==0.0 ||lng==0.0){
                                Toast.makeText(getActivity(),"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
                            }
                        }else formPicture = new LatLng(0.0,0.0);

                        //Display the photo start
                        Bitmap bitmap = BitmapFactory.decodeFile(real_path_lake);
                        selectPhoto.setImageBitmap(bitmap);*//*

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
*/

    public void submitClick(View view) {
        submitButton.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        apiFirst();
        //     apiSecond();
    }

    /* Get the real path from the URI */
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
//    public void getLastKnowLocation(View view) {
//        locationConfirmButton.setEnabled(false);
//
//        locationConfirmProgressBar.setVisibility(View.VISIBLE);
//        EasyCountDownTextview countDownTextview = new EasyCountDownTextview(this);//= (EasyCountDownTextview) findViewById(R.id.easyCountDownTextview);
//        countDownTextview.setTime(0, 0, 0, LOCATION_CONFIRM_NO_CYCLES);
//        countDownTextview.setOnTick(new CountDownInterface() {
//            @Override public void onTick(long time) {
//
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return;
//                }
//
//                Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                Double lat = location.getLatitude(); Double lng = location.getLongitude();
//                time = Math.round(time/1000);
//                int countTime = (int) (LOCATION_CONFIRM_NO_CYCLES - time)* 10;
//
//                if(time==0)
//                    countTime = 100;
//                Toast.makeText(getApplicationContext(),"Complete: "+countTime+"%" ,Toast.LENGTH_SHORT).show();
//
//                locationConfirmProgressBar.setProgress(countTime);
//                // Toast.makeText(getApplicationContext(),"lat:"+lat+"  lng:"+lng +"\nClick:"+time+" time(s)" ,Toast.LENGTH_SHORT).show();
//
//
//
//            }
//            @Override
//            public void onFinish() {
//
//                locationConfirmButton.setEnabled(true);
//                takePhotoButton.setEnabled(true);
//                linearLayoutMainForm.setVisibility(View.VISIBLE);
//                linearLayoutConfirmLocation.setVisibility(GONE);
//
//                fromLastKnowLocation = new LatLng(lat,lng);
//
//            }
//        });
//    }
    public void takePhotoClick(View view) {
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
        try{
            Ion.with(this)
                    .load("POST","http://192.168.43.205:8000/api/fishponds/create")
                    .setHeader("Accept","application/json")
                    .setHeader("Authorization","Bearer "+mToken)
                    .setMultipartParameter("fname",fname)
                    .setMultipartParameter("address",address)
                    .setMultipartParameter("district",district)
                    .setMultipartParameter("location_of_pond",location_of_pond)
                    .setMultipartParameter("tehsil",tehsil)
                    .setMultipartParameter("area",area)
                    .setMultipartParameter("epic_no",epic_no)
                    .setMultipartParameter("name_of_scheme",name_of_scheme)
                    .setMultipartParameter("lat",lat)
                    .setMultipartParameter("lng",lng)
                    .setMultipartFile("image","multipart/form-data",new File(real_path_profileImage))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            Log.d("TAG","FILE "+real_path_profileImage);
                            Log.d("TAG","URL 1: "+result);

                            //::::::FOR NOW ONLY BECAUSE SECOND API IS NOT CALLED::::::
                            submitButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(GONE);
                            //::::::    UPTO THIS   ::::::
                        }
                    });
        }catch (Exception e){
            Log.e("TAG","ERROR IN URL1:"+e);
        }
    }

    public void apiSecond(){
        // if(lat==0.0 ||lng==0.0){
        if(false){
            Toast.makeText(this,"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
        }else{
            try{
                String[] sad ={"sdf","sf"};
                Ion.with(this)
                        .load("PUT","http://test-env.eba-pnm2djie.ap-south-1.elasticbeanstalk.com/api/fishponds/uploadpond/"+mId)
                        .uploadProgressHandler(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
//                                progressBar.setVisibility(View.VISIBLE);
//                                progressBarLayout.setVisibility(View.VISIBLE);
//
//                                //lakeName.setVisibility(View.INVISIBLE);
//                              //  district.setVisibility(View.INVISIBLE);
//                                selectPhotoButton.setVisibility(View.INVISIBLE);
//                                submitButton.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setHeader("Authorization","Bearer "+mToken)
                        .setHeader("Content-Type","multipart/form-data")

                        .setMultipartParameter("_method", "PUT")
                       .setMultipartFile("pondImages[]","multipart/form-data",new File(real_path_lake))
                        //::::TODO UPLOAD THE PONDS IMAGE HERE
                       //  .setMultipartFile("pondImages[]","multipart/form-data",fileList)

                        //json ang nilo in MULTIPART ANGIN HANDLE MAI RAWH SE SERV
                        // ER AH. A HMA A DIK SA ANG KHAN. JSON BODY LEH MULTIPART A AWM KOP THEI SI LO

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {           ///sdcard/DCIM/Camera/IMG_20200217_123440.jpg
                                submitButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(GONE);
                                Log.e(TAG,"result: "+result);

                                Toasty.success(getApplicationContext(),"Upload Success!!",Toasty.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                //  finish();
                            }
                        });


            }catch (Exception e){
                Toast.makeText(this,"Some error in data:"+e,Toast.LENGTH_LONG).show ();

            }
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
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

}
