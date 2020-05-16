package com.give.android_fisheries_2.adapter;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.give.android_fisheries_2.R;

public class SchemeListAdapter extends RecyclerView.Adapter<SchemeListAdapter.ViewHolder> {
    String[] schemeName;
    Context mContext;
    public SchemeListAdapter(Context applicationContext, String[] schemes) {
        schemeName  = schemes;
        mContext = applicationContext;
    }

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

        //::CHECKBOX IS POPULATE DYNAMICALLY . THE LINEAR LAYOUT IS USED TO POPULATE THE CHECKBOX ARRAY
        CheckBox cb = new CheckBox(mContext);
        cb.setText(schemeName[position]);
        //cb.setTextSize(27);
        // cb.setTextColor(Color.rgb(150, 190, 200));
        //cb.setTypeface(Typeface.MONOSPACE);
        //cb.setButtonDrawable(R.drawable.checkboxselector);
        holder.checkBox.addView(cb);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("TAG","CompoundButton-" +compoundButton.getText()+"  "+ b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return schemeName.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.rowCheckBox);


            //checkBox.
        }
    }
}
