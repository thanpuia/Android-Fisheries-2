package com.give.android_fisheries_2.admin;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.adapter.RecyclerItemClickListener;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.give.android_fisheries_2.adapter.HorizontalImageViewAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class FishPondMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    Menu menu;
    EditText toolbarEdittext;
    Button  farmerBtn, smsBtn;
    RecyclerView pondsImageHorizontalRecyclerView;
    ImageView pondmapImage;

            ArrayList<String> pondLists;
   // Button pondBtn;
    double lat;
    double lng;
    String pond1,pond2,pond3,pond4,area,scheme,tehsil,district;

    String name;
    HorizontalImageViewAdapter horizontalImageViewAdapter;
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.menu = menu;

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);

        menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.refresh_page){
            startActivity(new Intent(this,FarmerListActivity.class));
        }
        else if (item.getItemId() == R.id.logout){
            new Logout(getApplicationContext());
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish_pond_map);
        // final ActionBar actionBar = getSupportActionBar();
        //   getSupportActionBar().setCustomView(R.layout.m_toolbar);
        //        toolbarEdittext = actionBar.getCustomView().findViewById(R.id.toobarEditText);
        //   actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME );


        // RECYCLER WIGET IF KEEP AND INITIALIZE. CREATE THE ADAPTER


        //pondBtn = findViewById(R.id.pondsBtn);
        farmerBtn = findViewById(R.id.farmersBtn);
        smsBtn = findViewById(R.id.smsBtn);
        pondsImageHorizontalRecyclerView = findViewById(R.id.ponds_image_view_rv);
        pondLists = new ArrayList<>();

        pondmapImage = findViewById(R.id.pond_image_on_map);

        bottomButton();

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat",0.0);
        lng = intent.getDoubleExtra("lng",0.0);
        pond1 =intent.getStringExtra("pond1");
        pond2 =intent.getStringExtra("pond2");
        pond3 =intent.getStringExtra("pond3");
        pond4 =intent.getStringExtra("pond4");
        name = intent.getStringExtra("name");
        district = intent.getStringExtra("district");
        scheme = intent.getStringExtra("scheme");
        tehsil = intent.getStringExtra("tehsil");
        area = intent.getStringExtra("area");


        pondLists.add(pond1);
        pondLists.add(pond2);
        pondLists.add(pond3);
        pondLists.add(pond4);

            //IMAGES OF THE PONDS
        horizontalImageViewAdapter = new HorizontalImageViewAdapter(pondLists,getApplicationContext());
        pondsImageHorizontalRecyclerView.setAdapter(horizontalImageViewAdapter);
        pondsImageHorizontalRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fish_map);
        mapFragment.getMapAsync(this);


        pondsImageHorizontalRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), pondsImageHorizontalRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
               // Toasty.info(getApplicationContext(),"DFSDF"+position,Toasty.LENGTH_SHORT).show();
                showMyDialog(position);


            }

            @Override
            public void onLongItemClick(View view, int position) { }
        }));

    }

    private void showMyDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FishPondMapActivity.this);
      //  builder.setTitle("Select Filters");

        int realPosition = pos+1;
        //SET THE CUSTOM LAYOUT
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog_fishpond_map,null);
        final PhotoView photoViewPondMapImage = customLayout.findViewById(R.id.map_pond_image);

        Picasso.get().load("http://192.168.43.205:8000/public/image"+realPosition +"/"+pondLists.get(pos)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                photoViewPondMapImage.setImageBitmap(bitmap);

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        builder.setView(customLayout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //    TextView editText = customLayout.findViewById(R.id.editText);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(lat, lng);
        String detailsPonds = tehsil +", "+district+", "+ area +" m2";
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .snippet(detailsPonds)
                .title(name));
        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        googleMap.moveCamera(center);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);
    }

    public void smsBtnClick2(View view) {
       startActivity(new Intent(this,SmsActivity.class));
        finish();
    }

    public void farmersBtnClick2(View view) {
        startActivity(new Intent(this,FarmerListActivity.class));
        finish();
    }

//    public void pondBtnClick2(View view) {
//        startActivity(new Intent(this,FishPondMapActivity.class));
//        finish();
//    }

    public void bottomButton(){

        //:::: THIS IS FOR THE BOTTOM BUTTON ACTIVE CHANGING COLOR
        Drawable pond_active = getResources().getDrawable(R.drawable.ic_fish_ponds_active);
        Drawable farmer_inactive = getResources().getDrawable(R.drawable.ic_fish_farmer_inactive);
        Drawable sms_inactive = getResources().getDrawable(R.drawable.ic_fish_sms_inactive);

       // pondBtn.setCompoundDrawablesWithIntrinsicBounds(null,pond_active,null,null);
        farmerBtn.setCompoundDrawablesWithIntrinsicBounds(null,farmer_inactive,null,null);
        smsBtn.setCompoundDrawablesWithIntrinsicBounds(null,sms_inactive,null,null);
    }

}
