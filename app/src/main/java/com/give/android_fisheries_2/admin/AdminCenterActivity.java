package com.give.android_fisheries_2.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.give.android_fisheries_2.R;

public class AdminCenterActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_center);

        mFragment = null;

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
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mFragmentFrameLayout,mFragment);
        fragmentTransaction.commit();
    }
}
