package com.give.android_fisheries_2.farmer.form;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.give.android_fisheries_2.farmer.form.FormMainActivity.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormStep1Fragment extends Fragment {
    SharedPreferences sharedPreferences;

    TextView farmersName;
    TextView farmersContact;
    TextView welcomeTv;

  /*
    static EditText fathersName;
    static EditText address;
    static Spinner district;
    static EditText epicAadhaar;
    */

    static MaterialEditText fathersName;
    static MaterialEditText address;
    //static Spinner district;
    static MaterialSpinner district;
    static MaterialEditText epicAadhaar;

    static String local_path_str_image;
    static boolean imageSelect;
    String web_path_str_image ;

    TextView personalInfoTv;
    CircleImageView profileImageFarmer;

    public FormStep1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_step1, container, false);

        sharedPreferences = getActivity().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        fathersName = v.findViewById(R.id.fathers_name_form);
        address = v.findViewById(R.id.address_form);
        district = v.findViewById(R.id.district_spinner_form);
        epicAadhaar = v.findViewById(R.id.epic_aadhaar_form);

        farmersName = v.findViewById(R.id.farmers_name);
        farmersContact = v.findViewById(R.id.farmers_phone_number);
        welcomeTv = v.findViewById(R.id.welcome_header);

        personalInfoTv = v.findViewById(R.id.pi_header_tv);
        profileImageFarmer = v.findViewById(R.id.profile_image_farmer);

        personalInfoTv.setText(FormMainActivity.labelPersonalInformation);

        profileImageFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
                }catch (Exception e){
                    Toasty.error(getActivity(),"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
                }
            }
        });

        //SET WELCOME
        welcomeTv.setText(welcome);
        //SET NAME
        farmersName.setText(mName);
        farmersContact.setText(mContact);
        //SET THE HINT
        /*
        fathersName.setHint(FormMainActivity.hintFathersName);
        address.setHint(FormMainActivity.hintAddress);
        epicAadhaar.setHint(FormMainActivity.hintEpic);
*/
        fathersName.setFloatingLabelText(FormMainActivity.hintFathersName);
        address.setFloatingLabelText(FormMainActivity.hintAddress);
        epicAadhaar.setFloatingLabelText(FormMainActivity.hintEpic);

        //SET DISTRICT SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(adapter);
        //POPULATE SPINNER IF ALREADY SELECTED
        String mDistrict = sharedPreferences.getString("district","");
        int spinnerPosition = adapter.getPosition(mDistrict);
        district.setSelection(spinnerPosition);

        //POPULATE ALL PICTURE IF PRESENT LOCALLY
        local_path_str_image = sharedPreferences.getString("image_local","");

        if(!local_path_str_image.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_image);
            profileImageFarmer.setImageBitmap(profilePictureBitmap2);
        }
        //IF DATA PRESENT POPULATE
        approvalStatus(mApprove);
        return v;
    }

    //RETURN OF PROFILE PICTURE SELECT
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 1://PROFILE PICTURE SELECT
                    Log.d("TAG", "Esey " );
                    Uri fileUri = data.getData();
                    local_path_str_image = getRealPathFromURI(getContext(), fileUri);
                    imageSelect = true;
                    //image = new File(local_path_str_image);
                    sharedPreferences.edit().putString("image_local", local_path_str_image).apply();

                    Bitmap profilePictureBitmap = BitmapFactory.decodeFile(local_path_str_image);
                    profileImageFarmer.setImageBitmap(profilePictureBitmap);
                    break;
            }
        } catch (Exception e) {
             Log.e("TAG", "Exception in onActivityResult : " + e.getMessage());
        }
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

    public void approvalStatus(int approve){
        switch(approve){
            case 0:
            case 3:
            case 4:
            case 2:
                fathersName.setText(sharedPreferences.getString("fname",""));
                address.setText(sharedPreferences.getString("address",""));
                epicAadhaar.setText(sharedPreferences.getString("epic_no",""));
                web_path_str_image = sharedPreferences.getString("image_web","");

                //PROFILE IMAGE
                if(!web_path_str_image.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image/"+web_path_str_image).into(profileImageFarmer);
                    Log.d("TAG","Image location IF TES");
                }
                break;
            case 1:
                break;
        }
    }

}
