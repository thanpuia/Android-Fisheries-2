package com.give.android_fisheries_2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.give.android_fisheries_2.R;

public class SchemeListAdapter extends RecyclerView.Adapter<SchemeListAdapter.ViewHolder> {
    String[] schemeName = {"NFDB", "RKVY", "NLUP", "Blue Revolution"};

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View frame = layoutInflater.inflate(R.layout.row_scheme,parent,false);
        ViewHolder viewHolder = new ViewHolder(frame);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        for(int i=0;i<schemeName.length;i++){
            holder.checkBox.setText(schemeName[i]);
        }

    }

    @Override
    public int getItemCount() {
        return schemeName.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.rowCheckBox);
        }
    }
}
