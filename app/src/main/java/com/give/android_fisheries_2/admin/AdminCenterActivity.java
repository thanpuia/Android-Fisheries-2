package com.give.android_fisheries_2.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.drm.DrmStore;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.give.android_fisheries_2.R;

public class AdminCenterActivity extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    Fragment mFragment;
    Menu menu;
    EditText toolbarEdittext;

    String districtsAndScheme[] = {"Kolasib","Champhai","Lawngtlai","Aizawl","others","PMEGY","Blue Revolution","other"};
    boolean[] checkednames= new boolean[]{false,false,false,false,false,false,false,false};

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.header_menu,menu);

        menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

//        if(item.getItemId() == R.id.english)
//           // setLanguage("English");
//        else if (item.getItemId() == R.id.spanish)
//         //   setLanguage("Spanish");
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_center);
        
        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.m_toolbar);

        toolbarEdittext = actionBar.getCustomView().findViewById(R.id.toobarEditText);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME );

        mFragment = null;


        toolbarEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                Log.d("TAG","keyEvent:"+keyEvent+" actionID:"+actionId);
                if(actionId==6){
                    //TODO:: actionId 6 is the id of pressing done key in android
                    Toast.makeText(getApplicationContext(),""+toolbarEdittext.getText(),Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
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

    public void filtersBtnClick(View view) {
       // Log.d("TAG","FILTER CLCK");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select District and Scheme");
        builder.setMultiChoiceItems(districtsAndScheme, checkednames, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                checkednames[which] = isChecked;
            }
        });

        builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TAG",""+i);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
