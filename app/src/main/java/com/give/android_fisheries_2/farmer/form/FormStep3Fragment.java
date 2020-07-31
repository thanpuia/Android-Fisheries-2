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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import es.dmoral.toasty.Toasty;

import static com.give.android_fisheries_2.farmer.form.FormMainActivity.mApprove;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormStep3Fragment extends Fragment {
    SharedPreferences sharedPreferences;

    FrameLayout pond1FrameLayout;
    FrameLayout pond2FrameLayout;
    FrameLayout pond3FrameLayout;
    FrameLayout pond4FrameLayout;

    String local_path_str_image;
    String local_path_str_pond1;
    String local_path_str_pond2;
    String local_path_str_pond3;
    String local_path_str_pond4;

    private String real_path_lake;

    String web_path_str_image;
    String web_path_str_pond1;
    String web_path_str_pond2;
    String web_path_str_pond3;
    String web_path_str_pond4;

    File image;
    File pond1;
    File pond2;
    File pond3;
    File pond4;

    Boolean imageSelect;

    ImageView pond1_iv;
    ImageView pond2_iv;
    ImageView pond3_iv;
    ImageView pond4_iv;

    TextView pond1_tv;
    TextView pond2_tv;
    TextView pond3_tv;
    TextView pond4_tv;

    TextView labelUploadPondsPicture;

    public FormStep3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_step3, container, false);
        sharedPreferences = getActivity().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        local_path_str_image = local_path_str_pond1 = local_path_str_pond2 = local_path_str_pond3 = local_path_str_pond4
                = web_path_str_image = web_path_str_pond1 = web_path_str_pond2 = web_path_str_pond3 = web_path_str_pond4 = "";

        imageSelect=false;

        pond1FrameLayout = v.findViewById(R.id.pond1_framelayout);
        pond2FrameLayout = v.findViewById(R.id.pond2_framelayout);
        pond3FrameLayout = v.findViewById(R.id.pond3_framelayout);
        pond4FrameLayout = v.findViewById(R.id.pond4_framelayout);

        pond1_iv = v.findViewById(R.id.pond1_iv);
        pond2_iv = v.findViewById(R.id.pond2_iv);
        pond3_iv = v.findViewById(R.id.pond3_iv);
        pond4_iv = v.findViewById(R.id.pond4_iv);

        pond1_tv = v.findViewById(R.id.pond1_tv);
        pond2_tv = v.findViewById(R.id.pond2_tv);
        pond3_tv = v.findViewById(R.id.pond3_tv);
        pond4_tv = v.findViewById(R.id.pond4_tv);

        labelUploadPondsPicture = v. findViewById(R.id.upp_header_tv);

        labelUploadPondsPicture.setText(FormMainActivity.labelUploadPondsPicture);

        //POPULATE ALL PICTURE IF PRESENT LOCALLY
        local_path_str_image = sharedPreferences.getString("image_local","");
        local_path_str_pond1 = sharedPreferences.getString("pond1_local","");
        local_path_str_pond2 = sharedPreferences.getString("pond2_local","");
        local_path_str_pond3 = sharedPreferences.getString("pond3_local","");
        local_path_str_pond4 = sharedPreferences.getString("pond4_local","");

        if(!local_path_str_image.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_image);
           // profileImageViewButton.setImageBitmap(profilePictureBitmap2);
        }
        
        if(!local_path_str_pond1.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond1);
            pond1_iv.setImageBitmap(profilePictureBitmap2);
            pond1_tv.setText("Edit");
            pond1_tv.setTextColor(getContext().getResources().getColor(R.color.white1));
            pond1_tv.setBackgroundResource(R.drawable.rounded_textview);
        }
        if(!local_path_str_pond2.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond2);
            pond2_iv.setImageBitmap(profilePictureBitmap2);
            pond2_tv.setText("Edit");
            pond2_tv.setTextColor(getContext().getResources().getColor(R.color.white1));
            pond2_tv.setBackgroundResource(R.drawable.rounded_textview);

        }
        if(!local_path_str_pond3.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond3);
            pond3_iv.setImageBitmap(profilePictureBitmap2);
            pond3_tv.setText("Edit");
            pond3_tv.setTextColor(getContext().getResources().getColor(R.color.white1));
            pond3_tv.setBackgroundResource(R.drawable.rounded_textview);

        }
        if(!local_path_str_pond4.equals("")){
            Bitmap profilePictureBitmap2 = BitmapFactory.decodeFile(local_path_str_pond4);
            pond4_iv.setImageBitmap(profilePictureBitmap2);
            pond4_tv.setText("Edit");
            pond4_tv.setTextColor(getContext().getResources().getColor(R.color.white1));
            pond4_tv.setBackgroundResource(R.drawable.rounded_textview);

        }

        //PONDS PICTURE ONCLICK
        pond1FrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "pond_1"), 11);
                } catch (Exception e) {
                    Toasty.error(getActivity(), "Storage Permission is not given", Toasty.LENGTH_SHORT).show();
                }
            }
        });

        pond2FrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "pond_2"),12);
                }catch (Exception e){
                    Toasty.error(getActivity(),"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
                }
            }
        });

        pond3FrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "pond_3"),13);
                }catch (Exception e){
                    Toasty.error(getActivity(),"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
                }
            }
        });

        pond4FrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "pond_4"),14);
                }catch (Exception e){
                    Toasty.error(getActivity(),"Storage Permission is not given",Toasty.LENGTH_SHORT).show();
                }
            }
        });

        approvalStatus(mApprove);

        return v;
    }
    //RETURN OF EVERY PICTURE SELECT

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case 1://PROFILE PICTURE SELECT
                    Uri fileUri = data.getData();
                    local_path_str_image = getRealPathFromURI(getContext(), fileUri);
                    imageSelect = true;
                    image = new File(local_path_str_image);
                    sharedPreferences.edit().putString("image_local", local_path_str_image).apply();

                    Bitmap profilePictureBitmap = BitmapFactory.decodeFile(local_path_str_image);
                    //pond1_iv.setImageBitmap(profilePictureBitmap);
                    break;
                case 11:
                    pond1_tv.setText("Edit");
                    pond1_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond1_tv.setBackgroundResource(R.drawable.rounded_textview);

                    Uri fileUri_11 = data.getData();
                    local_path_str_pond1 = getRealPathFromURI(getContext(), fileUri_11);
                    pond1 = new File(local_path_str_pond1);
                    sharedPreferences.edit().putString("pond1_local", local_path_str_pond1).apply();

                    Bitmap profilePictureBitmap_11 = BitmapFactory.decodeFile(local_path_str_pond1);
                    pond1_iv.setImageBitmap(profilePictureBitmap_11);
                    break;

                case 12:
                    pond2_tv.setText("Edit");
                    pond2_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond2_tv.setBackgroundResource(R.drawable.rounded_textview);

                    Uri fileUri_12 = data.getData();
                    local_path_str_pond2 = getRealPathFromURI(getContext(), fileUri_12);
                    pond2 = new File(local_path_str_pond2);
                    sharedPreferences.edit().putString("pond2_local", local_path_str_pond2).apply();

                    Bitmap profilePictureBitmap_12 = BitmapFactory.decodeFile(local_path_str_pond2);
                    pond2_iv.setImageBitmap(profilePictureBitmap_12);
                    break;

                case 13:
                    pond3_tv.setText("Edit");
                    pond3_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond3_tv.setBackgroundResource(R.drawable.rounded_textview);


                    Uri fileUri_13 = data.getData();
                    local_path_str_pond3 = getRealPathFromURI(getContext(), fileUri_13);
                    pond3 = new File(local_path_str_pond3);
                    sharedPreferences.edit().putString("pond3_local", local_path_str_pond3).apply();

                    Bitmap profilePictureBitmap_13 = BitmapFactory.decodeFile(local_path_str_pond3);
                    pond3_iv.setImageBitmap(profilePictureBitmap_13);
                    break;

                case 14:
                    pond4_tv.setText("Edit");
                    pond4_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond4_tv.setBackgroundResource(R.drawable.rounded_textview);

                    Uri fileUri_14 = data.getData();
                    local_path_str_pond4 = getRealPathFromURI(getContext(), fileUri_14);
                    pond4 = new File(local_path_str_pond4);
                    sharedPreferences.edit().putString("pond4_local", local_path_str_pond4).apply();

                    Bitmap profilePictureBitmap_14 = BitmapFactory.decodeFile(local_path_str_pond4);
                    pond4_iv.setImageBitmap(profilePictureBitmap_14);
                    break;

            }
        } catch (Exception e) {
            // Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
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

                web_path_str_pond1 = sharedPreferences.getString("pond1_web","");
                web_path_str_pond2 = sharedPreferences.getString("pond2_web","");
                web_path_str_pond3 = sharedPreferences.getString("pond3_web","");
                web_path_str_pond4 = sharedPreferences.getString("pond4_web","");

                //POND IMAGE ONE
                if(!web_path_str_pond1.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image1/"+web_path_str_pond1).into(pond1_iv);
                    Log.d("TAG","Image location IF TES");
                    pond1_tv.setText("Edit");
                    pond1_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond1_tv.setBackgroundResource(R.drawable.rounded_textview);


                }else
                    Log.d("TAG","else");
                //POND IMAGE TWO
                if(!web_path_str_pond2.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image2/"+web_path_str_pond2).into(pond2_iv);
                    Log.d("TAG","Image location IF TES");
                    pond2_tv.setText("Edit");
                    pond2_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond2_tv.setBackgroundResource(R.drawable.rounded_textview);

                }else
                    Log.d("TAG","else");
                //POND IMAGE THREE
                if(!web_path_str_pond3.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image3/"+web_path_str_pond3).into(pond3_iv);
                    Log.d("TAG","Image location IF TES");
                    pond3_tv.setText("Edit");
                    pond3_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond3_tv.setBackgroundResource(R.drawable.rounded_textview);


                }else
                    Log.d("TAG","else");
                //POND IMAGE FOUR
                if(!web_path_str_pond4.equals("")){
                    Picasso.get().load(MainActivity.MAIN_URL+"public/image4/"+web_path_str_pond4).into(pond4_iv);
                    Log.d("TAG","Image location IF TES");
                    pond4_tv.setText("Edit");
                    pond4_tv.setTextColor(getContext().getResources().getColor(R.color.white2));
                    pond4_tv.setBackgroundResource(R.drawable.rounded_textview);

                }else
                    Log.d("TAG","else");
                break;
            case 1:
                break;
        }
    }

}
