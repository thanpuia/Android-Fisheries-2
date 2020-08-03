package com.give.android_fisheries_2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.give.android_fisheries_2.R;

import java.util.ArrayList;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.ViewHolder> {
    String[] documentTitle;


    public DocumentListAdapter(Context applicationContext, String[] mdocu) {
       documentTitle = mdocu;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View frame = layoutInflater.inflate(R.layout.document_list_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(frame);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textViewTitle.setText(documentTitle[position]);

    }

    @Override
    public int getItemCount() {
        return documentTitle.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.documentsTopicsTextView);
            cardView = itemView.findViewById(R.id.documentsCardView);


            //checkBox.
        }
    }
}
