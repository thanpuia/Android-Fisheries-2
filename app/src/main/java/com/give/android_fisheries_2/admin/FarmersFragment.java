package com.give.android_fisheries_2.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.FarmerListAdapter;
import com.give.android_fisheries_2.entity.FarmerEntity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmersFragment extends Fragment {

    RecyclerView farmerRecyclerView;
    ArrayList<FarmerEntity> farmerEntities;
    FarmerListAdapter farmerListAdapter;

    public FarmersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_farmers, container, false);
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

        farmerListAdapter = new FarmerListAdapter(farmerEntities);

        farmerRecyclerView.setAdapter(farmerListAdapter);

        return view;
    }
}
