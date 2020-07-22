package com.give.android_fisheries_2.farmer.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.give.android_fisheries_2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FormStep1Fragment extends Fragment {

    static EditText fathersName;
    static EditText address;
    static Spinner district;
    static EditText epicAadhaar;

    public FormStep1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_form_step1, container, false);

        fathersName = v.findViewById(R.id.fathers_name_form);
        address = v.findViewById(R.id.address_form);
        district = v.findViewById(R.id.district_spinner_form);
        epicAadhaar = v.findViewById(R.id.epic_aadhaar_form);

        //SET DISTRIC SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(),R.array.districts,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        district.setAdapter(adapter);

        return v;
    }
}
