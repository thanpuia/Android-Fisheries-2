package com.give.android_fisheries_2.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;

import com.give.android_fisheries_2.admin.FishPondMapActivity;
import com.give.android_fisheries_2.entity.FarmerEntity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmerListAdapter extends RecyclerView.Adapter<FarmerListAdapter.ViewHolder> {
    ArrayList<FarmerEntity> mFarmerEntities;
    Context mContext;

    public FarmerListAdapter(ArrayList<FarmerEntity> farmerEntities, Context context) {
    this.mFarmerEntities = farmerEntities;
    this.mContext = context;
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
//        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.nameTv.setText(mFarmerEntities.get(position).getName() );
        holder.addressTv.setText(mFarmerEntities.get(position).getAddress());
        holder.tehsilTv.setText(mFarmerEntities.get(position).getTehsil());
        holder.areaTv.setText(mFarmerEntities.get(position).getArea()+ " ha");
        Picasso.get()
                .load(MainActivity.MAIN_URL+"public/image/"+mFarmerEntities.get(position).getImage())
                .placeholder(R.drawable.ic_fish_good)
                .error(R.mipmap.ic_fish_logo_ic)
                .into(holder.profileImagecircleImageView);
 
        if(mFarmerEntities.get(position).getApprove().matches("0")){
            //no reaction. pending
            holder.farmerApproveStatusTv.setText("Pending");
            holder.farmerApproveStatusTv.setTextColor(Color.parseColor("#b0ae27"));
        }else if(mFarmerEntities.get(position).getApprove().matches("1")){
            //accept
            holder.farmerApproveStatusTv.setText("Approved");
            holder.farmerApproveStatusTv.setTextColor(Color.parseColor("#3e9e41"));
        }else if (mFarmerEntities.get(position).getApprove().matches("2")){
            //reject
            holder.farmerApproveStatusTv.setText("Rejected");
            holder.farmerApproveStatusTv.setTextColor(Color.parseColor("#ba4545"));
        }


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
        TextView farmerApproveStatusTv;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImagecircleImageView = itemView.findViewById(R.id.profile_imageCirleImageView);
            nameTv = itemView.findViewById(R.id.name_tv);
            addressTv = itemView.findViewById(R.id.address_tv);
            tehsilTv = itemView.findViewById(R.id.tehsil_tv);
            areaTv = itemView.findViewById(R.id.area_tv);
            farmerApproveStatusTv = itemView.findViewById(R.id.farmer_approve_status);
            mView = itemView;
        }
    }
}
