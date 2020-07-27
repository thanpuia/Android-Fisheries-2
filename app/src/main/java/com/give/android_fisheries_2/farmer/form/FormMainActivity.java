package com.give.android_fisheries_2.farmer.form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.SchemeListAdapter;
import com.give.android_fisheries_2.farmer.FarmerCenterActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.async.http.body.StringPart;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static android.view.View.GONE;

public class FormMainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;

    ImageView imageViewStep;
    static Button formMainButton;
    //ProgressBar progressBarForm;
    LinearLayout prgressBarLinearLayout;

    //VARIABLE FOR UPLOADING DATA
    static int mApprove;
    String method;
    String createOrEditUrl;
    static String mToken;
    static String mContact;
    static String mName;
    int mId;

    String DATA_UPLOAD_URL_CREATE;
    String DATA_UPLOAD_URL_EDIT;

    //Error Label and Hints strings
    String errorFathersName;
    String errorAddress;
    String errorSelectDistrict;
    String errorEpic;
    String errorLocationOfPond;
    String errorTehsil;
    String errorArea;
    String errorPondsPicture;

    static String labelPersonalInformation;
    static String labelLakeInformation;
    static String labelUploadPondsPicture;
    static String labelSaveLocation;

    static String hintFathersName ;
    static String hintAddress;
    static String hintDistrict;
    static String hintEpic;
    static String hintLocationOfPond;
    static String hintTehsil;
    static String hintArea;
    static String hintPondsPicture;

    static String welcome;

    String language;

    //STEP 0

    String pondId;

    //STEP 1
    String fathersName;
    String address;
    String district;
    String epicAadhaar;

    //STEP 2
    String locationOfPonds;
    String tehsil;
    String area;
    String scheme;

    //STEP 3
    String local_path_str_image;
    String local_path_str_pond1;
    String local_path_str_pond2;
    String local_path_str_pond3;
    String local_path_str_pond4;

    String web_path_str_image ;
    String web_path_str_pond1 ;
    String web_path_str_pond2 ;
    String web_path_str_pond3 ;
    String web_path_str_pond4 ;

    //STEP 4
    String lat;
    String lng;


    FragmentManager fragmentManager;
    int formPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_main);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mApprove = Integer.parseInt(sharedPreferences.getString("approve",""));

        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",0);
        pondId = sharedPreferences.getString("pondId","");
        local_path_str_image = sharedPreferences.getString("image_local","");
        web_path_str_image  = sharedPreferences.getString("image_web","");

        DATA_UPLOAD_URL_CREATE = MainActivity.MAIN_URL+"api/fishponds/create";
        DATA_UPLOAD_URL_EDIT = MainActivity.MAIN_URL+"api/fishponds/edit/";

        fragment1 = new FormStep1Fragment();
        fragment2 = new FormStep2Fragment();
        fragment3 = new FormStep3Fragment();
        fragment4 = new FormStep4Fragment();

        imageViewStep = findViewById(R.id.imageview_step);
        formMainButton = findViewById(R.id.form_main_button);
        //progressBarForm = findViewById(R.id.prgressBarForm);
        prgressBarLinearLayout = findViewById(R.id.progressBarLinearLayout);

        formPage = 1;
        fragmentManager = getSupportFragmentManager();

        //LANGUAGE
        language = sharedPreferences.getString("language","");
        if(language.matches("English")){
            errorFathersName = getString(R.string.errorFathersNameEnglish);
            errorAddress = getString(R.string.errorAddressEnglish);
            errorSelectDistrict = getString(R.string.errorSelectDistrictEnglish);
            errorEpic = getString(R.string.errorEpicEnglish);
            errorLocationOfPond = getString(R.string.errorLocationOfPondEnglish);
            errorTehsil = getString(R.string.errorTehsilEnglish);
            errorArea = getString(R.string.errorAreaEnglish);
            errorPondsPicture = getString(R.string.errorPondsPictureEnglish);

            labelPersonalInformation = getString(R.string.labelPersonalInfoEnglish);
            labelLakeInformation = getString(R.string.labelLakeInfoEnglish);
            labelUploadPondsPicture = getString(R.string.labelUploadPondsPictureEnglish);
            labelSaveLocation = getString(R.string.labelSaveLocationEnglish);

              hintFathersName = getString(R.string.hintFathersNameEnglish);
              hintAddress = getString(R.string.hintAddressEnglish);
              hintDistrict = getString(R.string.hintAddressEnglish);
              hintEpic = getString(R.string.hintEpicEnglish);
              hintLocationOfPond = getString(R.string.hintLocationOfPondEnglish);
              hintTehsil  = getString(R.string.hintTehsilEnglish);
              hintArea = getString(R.string.hintAreaEnglish);
              hintPondsPicture = getString(R.string.hintPondsPictureEnglish);

              welcome = getString(R.string.welcomeEnglish);

        }else{
            errorFathersName = getString(R.string.errorFathersNameMizo);
            errorAddress = getString(R.string.errorAddressMizo);
            errorSelectDistrict = getString(R.string.errorSelectDistrictMizo);
            errorEpic = getString(R.string.errorEpicMizo);
            errorLocationOfPond = getString(R.string.errorLocationOfPondMizo);
            errorTehsil = getString(R.string.errorTehsilMizo);
            errorArea = getString(R.string.errorAreaMizo);
            errorPondsPicture = getString(R.string.errorPondsPictureMizo);

            labelPersonalInformation = getString(R.string.labelPersonalInfoMizo);
            labelLakeInformation = getString(R.string.labelLakeInfoMizo);
            labelUploadPondsPicture = getString(R.string.labelUploadPondsPictureMizo);
            labelSaveLocation = getString(R.string.labelSaveLocationMizo);

            hintFathersName = getString(R.string.hintFathersNameMizo);
            hintAddress = getString(R.string.hintAddressMizo);
            hintDistrict = getString(R.string.hintAddressMizo);
            hintEpic = getString(R.string.hintEpicMizo);
            hintLocationOfPond = getString(R.string.hintLocationOfPondMizo);
            hintTehsil  = getString(R.string.hintTehsilMizo);
            hintArea = getString(R.string.hintAreaMizo);
            hintPondsPicture = getString(R.string.hintPondsPictureMizo);

            welcome = getString(R.string.welcomeMizo);
        }

        fragmentManager.beginTransaction()
                .replace(R.id.form_main_frame_layout,fragment1)
                .commit();
        formPage = 1;
        imageViewStep.setImageResource(R.drawable.dot3);
    }

    public void nextClick(View view) {

        if(formPage == 1){

            //GET THE VALUE FROM STEP 1
            fathersName = FormStep1Fragment.fathersName.getText().toString();
            address     = FormStep1Fragment.address.getText().toString();
            district    = FormStep1Fragment.district.getSelectedItem().toString();
            epicAadhaar = FormStep1Fragment.epicAadhaar.getText().toString();
            Log.d("TAG","Father: "+FormStep1Fragment.fathersName.getText());

            //CHECK IF ALL ARE FILLED
            if(fathersName.matches("")){
                FormStep1Fragment.fathersName.setError(errorFathersName);
            }else if(address.matches("")){
                FormStep1Fragment.address.setError(errorAddress);
            }else if(district.matches("Select District...")){
                Toasty.error(this,errorSelectDistrict,Toasty.LENGTH_SHORT).show();
            }else if(epicAadhaar.matches("")){
                FormStep1Fragment.epicAadhaar.setError(errorEpic);
            }else if(web_path_str_image.matches("")&&local_path_str_image.matches("")) {
                Toasty.error(this,"Select Profile Picture",Toasty.LENGTH_SHORT).show();

            }else {
                    //SAVED AND GOTO NEXT IF ONLY ALL ARE FILLED
                    //SAVED TO SHARED PREFERENCE
                    sharedPreferences.edit().putString("fname",fathersName).apply();
                    sharedPreferences.edit().putString("address",address).apply();
                    sharedPreferences.edit().putString("district",district).apply();
                    sharedPreferences.edit().putString("epic_no",epicAadhaar).apply();

                    fragmentManager.beginTransaction()
                            .replace(R.id.form_main_frame_layout,fragment2)
                            .commit();

                    imageViewStep.setImageResource(R.drawable.dot2);
                    formPage = 2;

            }

        }else if(formPage == 2){

            //GET THE VALUE FROM STEP 2
            locationOfPonds = FormStep2Fragment.locationOfPond.getText().toString();
            tehsil          = FormStep2Fragment.tehsilofPond.getSelectedItem().toString();
            area            = FormStep2Fragment.areaOfPond.getText().toString();
            scheme          = TextUtils.join(",", SchemeListAdapter.schemeChecked);

            //CHECK IF ALL ARE FILLED
            if(locationOfPonds.matches("")){
                FormStep2Fragment.locationOfPond.setError(errorLocationOfPond);
            }else if (tehsil.matches("Select Tehsil...")){
                Toasty.error(this,errorTehsil,Toasty.LENGTH_SHORT).show();
            }else if(area.matches("")){
                FormStep2Fragment.areaOfPond.setError(errorArea);
            }else {
                //SAVED AND GOTO NEXT IF ONLY ALL ARE FILLED
                //SAVED TO SHARED PREFERENCE
                sharedPreferences.edit().putString("location_of_pond",locationOfPonds).apply();
                sharedPreferences.edit().putString("tehsil",tehsil).apply();
                sharedPreferences.edit().putString("area",area).apply();
                sharedPreferences.edit().putString("name_of_scheme",scheme).apply();

                imageViewStep.setImageResource(R.drawable.dot1);
                fragmentManager.beginTransaction()
                        .replace(R.id.form_main_frame_layout,fragment3)
                        .commit();
                formPage = 3;
            }

        }else if (formPage == 3){

            local_path_str_pond1 = sharedPreferences.getString("pond1_local","");
            local_path_str_pond2 = sharedPreferences.getString("pond2_local","");
            local_path_str_pond3 = sharedPreferences.getString("pond3_local","");
            local_path_str_pond4 = sharedPreferences.getString("pond4_local","");

            web_path_str_pond1 = sharedPreferences.getString("pond1_web","");
            web_path_str_pond2 = sharedPreferences.getString("pond2_web","");
            web_path_str_pond3 = sharedPreferences.getString("pond3_web","");
            web_path_str_pond4 = sharedPreferences.getString("pond4_web","");

            if(local_path_str_pond1.matches("")&&local_path_str_pond2.matches("")&&
                    local_path_str_pond3.matches("")&&local_path_str_pond4.matches("")&&
            web_path_str_pond1.matches("")&&web_path_str_pond2.matches("")&&
                    web_path_str_pond3.matches("")&&web_path_str_pond4.matches("")){
                Toasty.error(this,errorPondsPicture,Toasty.LENGTH_SHORT).show();
            }else{
                imageViewStep.setImageResource(R.drawable.dotlast);
                fragmentManager.beginTransaction()
                        .replace(R.id.form_main_frame_layout,fragment4)
                        .commit();
                formPage = 4;
                formMainButton.setText("SUBMIT");
                formMainButton.setVisibility(View.GONE);

            }


        }else if(formPage == 4){
            lat = sharedPreferences.getString("lat","");
            lng = sharedPreferences.getString("lng","");
            if(lng.matches("0")){
                Log.d("TAG","MAP not move");
            }else{
                Log.d("TAG",""+mName+mContact+fathersName+address+district+epicAadhaar+locationOfPonds+tehsil+area+scheme+local_path_str_pond1+lat+lng);
                //hide and show
                formMainButton.setVisibility(View.INVISIBLE);
                prgressBarLinearLayout.setVisibility(View.VISIBLE);
                //SENT DATA TO SERVER
                if(mApprove==0){
                    method = "POST";
                    // createOrEditUrl =  "http://192.168.43.205:8000/api/fishponds/create";
                    createOrEditUrl = DATA_UPLOAD_URL_CREATE;
                    Log.d("TAG","create");
                }else if(mApprove ==1 ||mApprove ==2 ||mApprove ==3 ||mApprove ==4 ){
                    method="POST";
                    // createOrEditUrl = "http://192.168.43.205:8000/api/fishponds/edit/"+pondId;
                    createOrEditUrl = DATA_UPLOAD_URL_EDIT + pondId;
                    Log.d("TAG","edit : "+createOrEditUrl);
                }else{
                    method="";
                    createOrEditUrl="";
                    Log.d("TAG","none");
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
                parts.add(new StringPart("fname",fathersName));
                parts.add(new StringPart("address",address));
                parts.add(new StringPart("location_of_pond",locationOfPonds));
                parts.add(new StringPart("tehsil",tehsil));
                parts.add(new StringPart("area",area));
                parts.add(new StringPart("epic_no",epicAadhaar));
                parts.add(new StringPart("name_of_scheme",scheme));
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
                      //  Log.d("TAG","FILE "+pond1);
                        Log.d("TAG","URL 1: "+result);
                        //::::::FOR NOW ONLY BECAUSE SECOND API IS NOT CALLED::::::
                        formMainButton.setVisibility(View.VISIBLE);
                        prgressBarLinearLayout.setVisibility(GONE);
                        if(result!=null) Toasty.success(getApplicationContext(),"Upload Successfully!",Toasty.LENGTH_SHORT).show();
                        else Toasty.error(getApplicationContext(),"Sorry, server out of reach",Toasty.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), FarmerCenterActivity.class));
                        //::::::    UPTO THIS   ::::::
                    }
                });
            }
        }
    }



}
