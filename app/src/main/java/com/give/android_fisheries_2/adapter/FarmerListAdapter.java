package com.give.android_fisheries_2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.entity.FarmerEntity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmerListAdapter extends RecyclerView.Adapter<FarmerListAdapter.ViewHolder> {
    ArrayList<FarmerEntity> mFarmerEntities;

    public FarmerListAdapter(ArrayList<FarmerEntity> farmerEntities) {
    this.mFarmerEntities = farmerEntities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View frame = layoutInflater.inflate(R.layout.farmer_list_layout,parent,false);
        FarmerListAdapter.ViewHolder viewHolder = new FarmerListAdapter.ViewHolder(frame);

        return viewHolder;

//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View frame = layoutInflater.inflate(R.layout.row_scheme,parent,false);
//        SchemeListAdapter.ViewHolder viewHolder = new SchemeListAdapter.ViewHolder(frame);
//
//        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameTv.setText(mFarmerEntities.get(position).getName());
        holder.addressTv.setText(mFarmerEntities.get(position).getAddress());
        holder.tehsilTv.setText(mFarmerEntities.get(position).getTehsil());
        holder.areaTv.setText(mFarmerEntities.get(position).getArea());

    }

    @Override
    public int getItemCount() {
        return mFarmerEntities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImagecircleImageView;
        TextView nameTv;
        TextView addressTv;
        TextView tehsilTv;
        TextView areaTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImagecircleImageView = itemView.findViewById(R.id.profile_imageCirleImageView);
            nameTv = itemView.findViewById(R.id.name_tv);
            addressTv = itemView.findViewById(R.id.address_tv);
            tehsilTv = itemView.findViewById(R.id.tehsil_tv);
            areaTv = itemView.findViewById(R.id.area_tv);
        }
    }
}
