package com.give.android_fisheries_2.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.FarmerListAdapter;
import com.give.android_fisheries_2.entity.FarmerEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmersFragment extends Fragment {

    static RecyclerView farmerRecyclerView;
    static ProgressBar progressBarFarmerList;
    static ArrayList<FarmerEntity> farmerEntities;
    static FarmerListAdapter farmerListAdapter;
    SharedPreferences sharedPreferences;
    String mToken;
    public FarmersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_farmers, container, false);
        sharedPreferences = view.getContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        mToken = sharedPreferences.getString("mToken","");
        progressBarFarmerList = view.findViewById(R.id.simpleProgressBarFarmerList);
        farmerRecyclerView = view.findViewById(R.id.farmerRecyclerList);
        farmerRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        farmerEntities = new ArrayList<>();
//        String[] names = {"name1","name2","name3"};
//        String[] address = {"address1","address2","address3"};
//        String[] tehsils = {"tehsil1","tehsil2","tehsil3"};
//        String[] areas = {"area1","area2","area3"};
//        farmerEntities = new ArrayList<>();
//        //TODO :: GENERATING FAKE DATA
//        for(int i=0;i< names.length;i++){
//            FarmerEntity mFarmer = new FarmerEntity(names[i],address[i],tehsils[i],areas[i]);
//            farmerEntities.add(mFarmer);
//        }
//        farmerListAdapter = new FarmerListAdapter(farmerEntities, getContext());
//
//        farmerRecyclerView.setAdapter(farmerListAdapter);

        progressBarFarmerList.setVisibility(View.VISIBLE);
        farmerRecyclerView.setVisibility(View.INVISIBLE);


        Ion.with(getContext())
                .load("http://192.168.43.205:8000/api/fishponds/pondlist")
                .setHeader("Accept","application/json")
                //  .setHeader("Content-Type","application/x-www-form-urlencoded")
                .setHeader("Authorization","Bearer "+mToken)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try{
                            Log.d("TAG"," list AL: "+result);
                            JSONObject jsonObject = new JSONObject(String.valueOf(result));
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject singleRow =  jsonArray.getJSONObject(i);
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

                                FarmerEntity mFarmerEntity= new FarmerEntity("name",fname,address,district,location_of_pond,tehsil,area,epicOrAadhaar,nameOfScheme,image,lat,lng);
                                farmerEntities.add(mFarmerEntity);
                            }
                            farmerListAdapter = new FarmerListAdapter(farmerEntities, getContext());
                            farmerRecyclerView.setAdapter(farmerListAdapter);
                            progressBarFarmerList.setVisibility(View.INVISIBLE);
                            farmerRecyclerView.setVisibility(View.VISIBLE);
                        }catch (Exception f){
                            Log.d("TAG","Error in farmer data manipulate: "+f);
                        }
                    }
                });
        //Log.d("TAG"," list AL: "+mToken);
        return view;
    }

    //TODO:::: MULTIPLE DISTRICT SEARCH DUH DAWN TA ILA ENG TIN NGE KA THUN ANG
    public static void search(final Context c, String mToken,String district){
        farmerEntities.clear();
        Ion.with(c)
                .load("http://192.168.43.205:8000/api/fishponds/search")
                .setHeader("Accept","application/json")
                //  .setHeader("Content-Type","application/x-www-form-urlencoded")
                .setHeader("Authorization","Bearer "+mToken)
                .setMultipartParameter("district",district)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try{
                            Log.d("TAG"," list AL: "+result);
                            JSONObject jsonObject = new JSONObject(String.valueOf(result));
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject singleRow =  jsonArray.getJSONObject(i);
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

                                FarmerEntity mFarmerEntity= new FarmerEntity("name",fname,address,district,location_of_pond,tehsil,area,epicOrAadhaar,nameOfScheme,image,lat,lng);
                                farmerEntities.add(mFarmerEntity);
                            }
                            farmerListAdapter = new FarmerListAdapter(farmerEntities, c);
                            farmerRecyclerView.setAdapter(farmerListAdapter);
                            progressBarFarmerList.setVisibility(View.INVISIBLE);
                            farmerRecyclerView.setVisibility(View.VISIBLE);
                        }catch (Exception f){
                            Log.d("TAG","Error in farmer data manipulate: "+f);
                        }
                    }
                });
    }


}
