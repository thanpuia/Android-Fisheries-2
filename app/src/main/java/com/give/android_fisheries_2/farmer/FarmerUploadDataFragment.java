package com.give.android_fisheries_2.farmer;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmerUploadDataFragment extends Fragment {

    GoogleApiClient mGoogleApiClient;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String real_path_lake;
    private static String real_path_profileImage;

    private double lat,lng;
    private String TAG = "TAG";
    private LatLng formPicture;
    private LatLng fromLastKnowLocation;

    //  private String URL1="http://10.180.243.6:8000/api/fishponds/create/";

    //  private String URL2="";

    int LOCATION_CONFIRM_NO_CYCLES = 7;

    String[] schemes;
    boolean[] schemesChecked=new boolean[]{false,false,false,false};

    SharedPreferences sharedPreferences;
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

    private ImageView profileImageViewButton;

    private RecyclerView listOfSchemeRV;

    protected LocationManager locationManager;
    SchemeListAdapter schemeListAdapter;

    String mToken;
    String mContact;
    String mName;
    int mId;
    TextView location;
    String lat2 ="";
    String lng2 ="";
    String latLng="";

    List<File>  fileList;
    ArrayList<String> pondLists;
    RecyclerView pondsImageHorizontalRecyclerView;
    HorizontalImageViewAdapter horizontalImageViewAdapter;

    public FarmerUploadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_farmer_upload_data, container, false);
        pondLists = new ArrayList<>();
        try{
            Log.d("TAG","uplaod Fragmet  "+this.getArguments().getString("lat"));
             lat2 = getArguments().getString("lat");
             lng2 = getArguments().getString("lng");
             latLng = "Location: "+lat2+","+lng2;
            location = view.findViewById(R.id.pondsLocation);
            location.setText(latLng);
        }catch (Exception e){

        }

        sharedPreferences = getActivity().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",mId);

        Log.e("TAG","My Token: "+sharedPreferences.getString("mToken",""));
        //district = findViewById(R.id.spinner_districrt);
        progressBar = view.findViewById(R.id.simpleProgressBar);
        locationConfirmProgressBar = view.findViewById(R.id.locationConfirmProgressBar);

        selectPhotoButton = view.findViewById(R.id.selectPhotoButton);
        submitButton = view.findViewById(R.id.submitButton);
        locationConfirmButton = view.findViewById(R.id.locationConfirm);
        takePhotoButton = view.findViewById(R.id.takePhoto);

        //progressBarLayout = view.findViewById(R.id.progressBarLayout);
        linearLayoutConfirmLocation = view.findViewById(R.id.linearLayoutConfirmLocation);
        linearLayoutMainForm = view.findViewById(R.id.linearLayoutMainForm);

        nameEditText = view.findViewById(R.id.editTextDataName);
        fathersNameEditText = view.findViewById(R.id.editTextDataFathersName);
        addressEditText= view.findViewById(R.id.editTextDataAddress);
        epicOrAadhaarEditText= view.findViewById(R.id.editTextDataEpicNo);
        areaEditText = view.findViewById(R.id.editTextDataArea);
        tehsilEditText = view.findViewById(R.id.editTextTehsil);
        districtSpinner = view.findViewById(R.id.spinner_district);

        checkBox = view.findViewById(R.id.checkbox);
        listOfSchemeRV = view.findViewById(R.id.list_of_scheme);

        profileImageViewButton = view.findViewById(R.id.imageViewDateProfilePicture);
        pondsImageHorizontalRecyclerView = view.findViewById(R.id.ponds_image_view_recycler_view);

        //takePhotoButton.setEnabled(false);

        /*mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(getActivity())
                .addOnConnectionFailedListener(getActivity())
                .addApi(LocationServices.API).build();*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        schemes = new String[] {"NFDB", "RKVY", "NLUP", "Blue Revolution"};
        schemeListAdapter = new SchemeListAdapter(getActivity(), schemes);
        listOfSchemeRV.setAdapter(schemeListAdapter);
        listOfSchemeRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
            }
        });

        profileImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);
            }
        });

        //RecyclerItemClickListener.class IS USER DEFINE CLASS
        pondsImageHorizontalRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), pondsImageHorizontalRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                Log.d("TAG","pos "+position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;
    }
//
//    public void selectPhoto(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
//    }
//
//    public void profilePictureButtonClick(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"),2);
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 1://LAKE PICTURE SELECT
                    if (resultCode == Activity.RESULT_OK) {
                        //testing multi img start
                        ClipData clipData = data.getClipData();
                        if(clipData!=null){
                            int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                            Uri imageUri;
                            for(int i = 0; i < count; i++){

                                ClipData.Item item = clipData.getItemAt(i);
                                //imageUri = data.getClipData().getItemAt(i).getUri();
                                imageUri = item.getUri();

                                real_path_lake = getRealPathFromURI(getActivity(), imageUri);                        //Log.e(TAG, "data: ") ;

                                pondLists.add(real_path_lake);
                                // fileList.add(new File(real_path_lake));
                                Log.d("TAG","image: "+real_path_lake);
                            }

                        }else{
                            Uri uri = data.getData();
                            String mRealPathLake = getRealPathFromURI(getActivity(),uri);
                            pondLists.add(mRealPathLake);
                        }
                        horizontalImageViewAdapter = new HorizontalImageViewAdapter(pondLists);
                        pondsImageHorizontalRecyclerView.setAdapter(horizontalImageViewAdapter);
                        pondsImageHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true));

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
                        selectPhoto.setImageBitmap(bitmap);*/
                        //End

                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                        Toast.makeText(getActivity(),"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
                    }
                    break;
                case 2://PROFILE PICTURE SELECT
                    Uri fileUri = data.getData();
                    real_path_profileImage = getRealPathFromURI(getActivity(),fileUri);
                    Bitmap profilePictureBitmap = BitmapFactory.decodeFile(real_path_profileImage);
                    profileImageViewButton.setImageBitmap(profilePictureBitmap);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    public void submitClick(View view) {
        submitButton.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);
        apiFirst();
        //     apiSecond();
        // Log.e("TAG",schemeCheckLists+"{}{}");
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
/*        String fname = "TESTIGN";
        String address= "TESTIGN";
        String district= "TESTIGN";
        String location_of_pond= "TESTIGN";
        String tehsil = "TESTIGN";
        String area= "TESTIGN";
        String epic_no= "TESTIGN";*/

        Log.d("TAG",""+fname+address+district+location_of_pond+tehsil+area+epic_no+name_of_scheme);
        String lat= "77";
        String lng="77";
        try{
            Ion.with(getActivity())
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
                            //::::::            UPTO THIS::::::
                        }
                    });
        }catch (Exception e){
            Log.e("TAG","ERROR IN URL1:"+e);

        }
    }

    public void apiSecond(){

        // if(lat==0.0 ||lng==0.0){
        if(false){
            Toast.makeText(getActivity(),"PICTURE HAS NO COORDINATES!",Toast.LENGTH_LONG).show ();
        }else{
            try{
                Ion.with(getActivity())
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
//                        .setMultipartFile("pondImages[]","multipart/form-data",new File(real_path_lake))
                        //::::TODO UPLOAD THE PONDS IMAGE HERE
                       // .setMultipartFile("pondImages[]","multipart/form-data",fileList)

                        //json ang nilo in MULTIPART ANGIN HANDLE MAI RAWH SE SERV
                        // ER AH. A HMA A DIK SA ANG KHAN. JSON BODY LEH MULTIPART A AWM KOP THEI SI LO

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {           ///sdcard/DCIM/Camera/IMG_20200217_123440.jpg
                                submitButton.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(GONE);
                                Log.e(TAG,"result: "+result);

                                Toasty.success(getActivity(),"Upload Success!!",Toasty.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                                //  finish();
                            }
                        });


            }catch (Exception e){
                Toast.makeText(getActivity(),"Some error in data:"+e,Toast.LENGTH_LONG).show ();

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

}
