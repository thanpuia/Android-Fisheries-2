package com.give.android_fisheries_2.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.FarmerListAdapter;
import com.give.android_fisheries_2.entity.FarmerEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmersFragment extends Fragment {

    RecyclerView farmerRecyclerView;
    ArrayList<FarmerEntity> farmerEntities;
    FarmerListAdapter farmerListAdapter;
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

        farmerRecyclerView = view.findViewById(R.id.farmerRecyclerList);
        farmerRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        String[] names = {"name1","name2","name3"};
        String[] address = {"address1","address2","address3"};
        String[] tehsils = {"tehsil1","tehsil2","tehsil3"};
        String[] areas = {"area1","area2","area3"};
        farmerEntities = new ArrayList<>();
        //TODO :: GENERATING FAKE DATA
        for(int i=0;i< names.length;i++){
            FarmerEntity mFarmer = new FarmerEntity(names[i],address[i],tehsils[i],areas[i]);
            farmerEntities.add(mFarmer);
        }
        farmerListAdapter = new FarmerListAdapter(farmerEntities, getContext());

        farmerRecyclerView.setAdapter(farmerListAdapter);
        Ion.with(getContext())
                .load("http://test-env.eba-pnm2djie.ap-south-1.elasticbeanstalk.com/api/fishponds/pondlist")
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
                                String image = singleRow.getString("image");

                                FarmerEntity mFarmerEntity= new FarmerEntity(fname,address,district,image);
                                farmerEntities.add(mFarmerEntity);

                            }
                            farmerListAdapter = new FarmerListAdapter(farmerEntities, getContext());

                            farmerRecyclerView.setAdapter(farmerListAdapter);


                        }catch (Exception f){}

                    }
                });



        //Log.d("TAG"," list AL: "+mToken);

        return view;
    }


}
