package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.SchemeListAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

public class FarmerUploadDataActivity extends AppCompatActivity {
    String[] schemes;
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

    private ImageView selectPhoto;
    private ImageView profileImageViewButton;

    private RecyclerView listOfSchemeRV;

    protected LocationManager locationManager;
    SchemeListAdapter schemeListAdapter;

    String mToken;
    String mContact;
    String mName;
    int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_upload_data);

        sharedPreferences = this.getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mToken = sharedPreferences.getString("mToken","");
        mContact = sharedPreferences.getString("mContact","");
        mName = sharedPreferences.getString("mName","");
        mId = sharedPreferences.getInt("mId",mId);


        Log.e("TAG","My Token: "+sharedPreferences.getString("token",""));
        //district = findViewById(R.id.spinner_districrt);
        progressBar = findViewById(R.id.simpleProgressBar);
        locationConfirmProgressBar = findViewById(R.id.locationConfirmProgressBar);

        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        submitButton = findViewById(R.id.submitButton);
        locationConfirmButton = findViewById(R.id.locationConfirm);
        takePhotoButton = findViewById(R.id.takePhoto);

        //progressBarLayout = findViewById(R.id.progressBarLayout);
        linearLayoutConfirmLocation = findViewById(R.id.linearLayoutConfirmLocation);
        linearLayoutMainForm = findViewById(R.id.linearLayoutMainForm);

        nameEditText = findViewById(R.id.editTextDataName);
        fathersNameEditText = findViewById(R.id.editTextDataFathersName);
        addressEditText= findViewById(R.id.editTextDataAddress);
        epicOrAadhaarEditText= findViewById(R.id.editTextDataEpicNo);
        contactEditText = findViewById(R.id.editTextDataContact);
        areaEditText = findViewById(R.id.editTextDataArea);
        tehsilEditText = findViewById(R.id.editTextTehsil);
        districtSpinner = findViewById(R.id.spinner_district);

        checkBox = findViewById(R.id.checkbox);
        listOfSchemeRV = findViewById(R.id.list_of_scheme);

        selectPhoto = findViewById(R.id.selectPhoto);
        profileImageViewButton = findViewById(R.id.imageViewDateProfilePicture);

        schemes = new String[] {"NFDB", "RKVY", "NLUP", "Blue Revolution"};
        schemeListAdapter = new SchemeListAdapter(getApplicationContext(), schemes);
        listOfSchemeRV.setAdapter(schemeListAdapter);
        listOfSchemeRV.setLayoutManager(new LinearLayoutManager(this));
    }
}
