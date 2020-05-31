package com.give.android_fisheries_2.farmer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.give.android_fisheries_2.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmerDashboardFragment extends Fragment {
    SharedPreferences sharedPreferences;
    String mToken;
    String mId;

    TextView farmerDashboardTextView;
    Button farmerDashboardButton;

    String farmerDashboardTextviewString;
    String farmerDashboardButtonString;

    int status;
    public FarmerDashboardFragment() {
        // Required empty public constructor
    }

    public FarmerDashboardFragment(int mStatus) {
        this.status = mStatus;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        mToken = sharedPreferences.getString("mToken","");

        View v = inflater.inflate(R.layout.fragment_farmer_dashboard, container, false);
        farmerDashboardTextView = v.findViewById(R.id.farmer_dashboard_textview);
        farmerDashboardButton = v.findViewById(R.id.farmer_dashboard_button);

        /*/::::TODO - STATUS
        check user_id in the fishponds, if not present -> upload your form
                                        f
        status :-
            0 = no activity, NOT YET SUBMIT ANYTHING
            1 = accept
            2 = reject
            3 = resubmit  //

            4 = submit, but not yet react
        ::::::::*/

        //:::: TODO UNCOMMENT TUR
        // ::::TODO CHECK THE FISHPONDS DATA
        /*
        Ion.with(this)
                .load("URL")
                .setHeader("Accept","application/json")
                .setHeader("Authorization","Bearer "+mToken)
                .setMultipartParameter("id",mId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {


                        if(result!=null){
                        //::::TODO THEIR IS SOMETHING IT THE FISHPONDS WITH THE MID SENT TO SERVER
                            //it will return the value of the approved
                            //String mToken = result.get("approved").getAsString();
                            String mApprove = result.get("approve").getAsString();
                            switch(mApprove){
                                case "0" :status = 0;break;
                                case "1" :status = 1;break;
                                case "2" :status = 2;break;
                                case "3" :status = 3;break;
                            }
                        }else {
                            //::::TODO NO DATA IS SENT TO FISHPONDS TABLE
                            //status : 0 = no activity, NOT YET SUBMIT ANYTHING
                            status = 4;
                        }
                    }
                });
        */
        //::::TODO CHANGE THE STATUS DEPENDING ON THE STATUS OF THE APPROVED COLUMN

            switch (status ){
                case 0:
                    farmerDashboardButtonString="Open Form";
                    farmerDashboardTextviewString="Upload your data";
                    break;
                case 1:
                    farmerDashboardButtonString="Download ID";
                    farmerDashboardTextviewString="Data approved";
                    break;
                case 2:
                    farmerDashboardButtonString="Re-Submit";
                    farmerDashboardTextviewString="Data Rejected";
                    break;
                case 3:
                    farmerDashboardButtonString="Edit";
                    farmerDashboardTextviewString="Form re-submitted";
                    break;
                case 4:
                    farmerDashboardButtonString="Edit";
                    farmerDashboardTextviewString="Form submitted";
                    break;
                default:break;
            }



//        //SET THE LOOKS
        farmerDashboardTextView.setText(farmerDashboardTextviewString);
        farmerDashboardButton.setText(farmerDashboardButtonString);


        // Inflate the layout for this fragment
        return v;
    }
}
