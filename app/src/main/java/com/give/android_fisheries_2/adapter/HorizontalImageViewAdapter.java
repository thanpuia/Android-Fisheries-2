package com.give.android_fisheries_2.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.give.android_fisheries_2.R;

import java.util.ArrayList;

public class HorizontalImageViewAdapter extends RecyclerView.Adapter<HorizontalImageViewAdapter.ViewHolder>{
    ArrayList<String> realPathLakes;

    public HorizontalImageViewAdapter(ArrayList<String> realPathLake) {
        this.realPathLakes = realPathLake;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View frame = layoutInflater.inflate(R.layout.horizontal_image_view_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(frame);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeFile(realPathLakes.get(position));
        holder.pondsImageView.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return realPathLakes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView pondsImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pondsImageView = itemView.findViewById(R.id.ponds_horizontal_image_view);


        }
    }


}
