package com.give.android_fisheries_2.farmer.form;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.SchemeListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormStep2Fragment extends Fragment {
    ArrayList<String> tehsilArrList;
    static EditText locationOfPond;
    static Spinner tehsilofPond;
    static EditText areaOfPond;
    SharedPreferences sharedPreferences;
    String[] schemes;
    SchemeListAdapter schemeListAdapter;
    RecyclerView listOfSchemeRVPond;

    public FormStep2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_form_step2, container, false);
        sharedPreferences = v.getContext().getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        locationOfPond = v.findViewById(R.id.location_of_pond_form);
        tehsilofPond = v.findViewById(R.id.tehsil_spinner_form);
        areaOfPond = v.findViewById(R.id.area_form);
        listOfSchemeRVPond = v.findViewById(R.id.list_of_scheme_rv_form);

        //SET TEHSIL SPINNER
        tehsilArrList= stringToArrayList(sharedPreferences.getString("all_tehsil","")) ;

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_spinner_item, tehsilArrList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tehsilofPond.setAdapter(spinnerArrayAdapter);

        //POPULATE THE CHECK BOX

        String schemesStr = sharedPreferences.getString("schemes","");

        schemes = schemesStr.split(",");
        ArrayList<Integer> mCheckedItem = new ArrayList<>();
        String mScheme = sharedPreferences.getString("name_of_scheme","");
        List<String> items = Arrays.asList(mScheme.split("\\s*,\\s*"));
        Log.d("TAG","Selectd list:"+ items);

        for(int i=0;i< items.size();i++){
            for (int j=0;j<schemes.length;j++){
                if(schemes[j].matches(items.get(i)))
                    mCheckedItem.add(j);
            }
        }
        schemeListAdapter = new SchemeListAdapter(getContext(), schemes, mCheckedItem);
        listOfSchemeRVPond.setAdapter(schemeListAdapter);
        listOfSchemeRVPond.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return v;
    }

    public ArrayList<String> stringToArrayList(String string){
        String[] strArr = string.split(",");
        List<String> strList = Arrays.asList(strArr);
        ArrayList<String> strArrList = new ArrayList<String>(strList);

        return strArrList;
    }
}
