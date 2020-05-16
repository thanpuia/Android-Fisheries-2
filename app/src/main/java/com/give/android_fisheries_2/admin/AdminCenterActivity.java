package com.give.android_fisheries_2.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.ActivityInfo;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

import com.give.android_fisheries_2.R;

public class AdminCenterActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    Fragment mFragment;
    Toolbar toolbar;
    EditText toolbarEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_center);
        
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.m_toolbar);

        toolbarEdittext = actionBar.getCustomView().findViewById(R.id.toobarEditText);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME );

        mFragment = null;

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    public void pondsBtnClick(View view) {
        mFragment = new PondsFragment();
        fragmentCommit();
    }

    public void farmersBtnClick(View view) {
        mFragment = new FarmersFragment();
        fragmentCommit();
    }

    public void smsBtnClick(View view) {
        mFragment = new SmsFragment();
        fragmentCommit();
    }

    public void fragmentCommit(){
        //:: getSupportFragmentManager().beginTransaction()  <-- is should be called every time fragment is going to be replace or error will throw
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mFragmentFrameLayout,mFragment);
        fragmentTransaction.commit();
    }
}
