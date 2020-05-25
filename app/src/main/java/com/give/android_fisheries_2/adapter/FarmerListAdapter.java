package com.give.android_fisheries_2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.admin.FarmersFragment;
import com.give.android_fisheries_2.entity.FarmerEntity;
import com.squareup.picasso.Picasso;

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

        holder.nameTv.setText(mFarmerEntities.get(position).getFname()+"(Father's Name)");
        holder.addressTv.setText(mFarmerEntities.get(position).getAddress());
        holder.tehsilTv.setText(mFarmerEntities.get(position).getTehsil());
        holder.areaTv.setText(mFarmerEntities.get(position).getArea()+ Html.fromHtml("m<sup><small>2</small></sup>"));
        Picasso.get()
                .load("http://192.168.43.205:8000/public/image/"+mFarmerEntities.get(position).getImage())
                .placeholder(R.drawable.ic_email_icon)
                .error(R.mipmap.ic_fish_logo_ic)
                .into(holder.profileImagecircleImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG","veiw: "+view.getId()+"  postion:"+position);
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                //Call the alert box
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(mFarmerEntities.get(position).getName()+"'s Details");

                //SET THE CUSTOM LAYOUT
                View customLayout = layoutInflater.inflate(R.layout.custom_farmer_list_details,null);
                builder.setView(customLayout);

                //DECLARATION AND INITIALIZATION
                TextView name,fname,address,district,tehsil,area,epic,scheme;
                name = customLayout.findViewById(R.id.name_value);
                fname = customLayout.findViewById(R.id.fname_value);
                address = customLayout.findViewById(R.id.address_value);
                district = customLayout.findViewById(R.id.district_value);
                tehsil = customLayout.findViewById(R.id.tehsil_value);
                area = customLayout.findViewById(R.id.area_value);
                epic = customLayout.findViewById(R.id.epic_value);
                scheme = customLayout.findViewById(R.id.scheme_value);

                //DEFINITION
                name.setText(mFarmerEntities.get(position).getName());
                address.setText(mFarmerEntities.get(position).getAddress());
                tehsil.setText(mFarmerEntities.get(position).getTehsil());
                area.setText(mFarmerEntities.get(position).getArea());




                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setNegativeButton("Print", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImagecircleImageView = itemView.findViewById(R.id.profile_imageCirleImageView);
            nameTv = itemView.findViewById(R.id.name_tv);
            addressTv = itemView.findViewById(R.id.address_tv);
            tehsilTv = itemView.findViewById(R.id.tehsil_tv);
            areaTv = itemView.findViewById(R.id.area_tv);
            mView = itemView;
        }
    }
}
