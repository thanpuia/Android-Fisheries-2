package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;
import com.tapadoo.alerter.Alerter;
import com.tapadoo.alerter.OnHideAlertListener;
import com.tapadoo.alerter.OnShowAlertListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class FarmerIdActivity extends AppCompatActivity {
    TextView idName;
    TextView idTehsil;
    RelativeLayout relativeLayout;
    SharedPreferences sharedPreferences;
    Menu menu;
    File myPath;
    String fileName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu_download, menu);

        // menu.getItem(0).setIcon(ContextCompat.getDrawable(this,R.drawable.ic_dehaze_black_24dp));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        //TODO THIS IS TESTING MENU BEFORE STATUS CAN BE FETCH FROM SERVER

        if (item.getItemId() == R.id.download_id) {
            Bitmap bm = null;
            relativeLayout.setDrawingCacheEnabled(true);
            relativeLayout.buildDrawingCache();
            bm = relativeLayout.getDrawingCache();
            String sdf=saveToInternalStorage(bm);
            Log.d("TAG","Image: "+sdf);
            Toasty.success(this, "Image Downloaded!", Toasty.LENGTH_SHORT).show();

            Alerter.create(this)
                    .setTitle("Download ID")
                    .setText("ID file in Download Folder")
                    .setIcon(
                            R.mipmap.ic_launcher)
                    .setBackgroundColorRes(
                            R.color.colorPrimaryDark)
                    .setDuration(3000)
                    .setOnClickListener(
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v)
                                {

                                    startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

                                }
                            })

                    .setOnShowListener(
                            new OnShowAlertListener() {

                                @Override
                                public void onShow()
                                {
                                    // do something when
                                    // Alerter message shows
                                }
                            })

                    .setOnHideListener(
                            new OnHideAlertListener() {

                                @Override
                                public void onHide()
                                {
                                    // do something when
                                    // Alerter message hides
                                }
                            })
                    .show();

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_id);
        sharedPreferences = getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);

        relativeLayout = findViewById(R.id.linear_layout_id);
        idName = findViewById(R.id.name_value);
        idTehsil = findViewById(R.id.tehsil_value);

        idName.setText(sharedPreferences.getString("mName",""));
        idTehsil.setText(sharedPreferences.getString("tehsil",""));

    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        //File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // Create imageDir
        //GET DATE
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        fileName = formattedDate+"-fish-id.jpeg";
        myPath= new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


}
