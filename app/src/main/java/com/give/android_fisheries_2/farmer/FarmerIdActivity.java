package com.give.android_fisheries_2.farmer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.give.android_fisheries_2.MainActivity;
import com.give.android_fisheries_2.R;
import com.give.android_fisheries_2.registration.LoginActivity;
import com.give.android_fisheries_2.registration.Logout;
import com.squareup.picasso.Picasso;
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

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;

public class FarmerIdActivity extends AppCompatActivity {
    TextView idName;
    TextView idTehsil;
    TextView idDistrict;
    TextView idContact;
    TextView idFathersName;
    TextView idArea;
    TextView idScheme;

    RelativeLayout relativeLayout;
    SharedPreferences sharedPreferences;
    Menu menu;
    File myPath;
    String fileName;
    ImageView ImageViewProfilePicture;
    NotificationManagerCompat notificationManagerCompat;
    NotificationCompat.Builder builder;
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
            String idPath=saveToInternalStorage(bm);
            Log.d("TAG","Image: "+idPath);
            Toasty.success(this, "Image Downloaded!", Toasty.LENGTH_SHORT).show();
            //startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
            gotoNotification(idPath);

        }
        return true;
    }

    private void gotoNotification(String idPath) {

        //CREATE ON CLICK ACTION
       Intent intent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
    //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //File file = new File(pathFile);

       
        //startActivity(intent);
       //
        //
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        /*  Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        //Uri uri = Uri.parse("content://" + file.getAbsolutePath());
        grantUriPermission("com.saffru.colombo.cartellaclinica" +
                ".fileprovider", contentUri,
                FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(contentUri,"image/*");
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);*/
        builder = new NotificationCompat.Builder(this,"fishChannel")
                .setSmallIcon(R.drawable.ic_fish_good)
                .setContentTitle("Download Success")
                .setContentText("ID is locate in Download Folder")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(100,builder.build());
     }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name = "fishChannel";
            String description = "Chanel for fish ID";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("fishChannel",name,importance );
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_id);
        sharedPreferences = getSharedPreferences("com.example.root.sharedpreferences", Context.MODE_PRIVATE);
        createNotificationChannel();



        relativeLayout = findViewById(R.id.linear_layout_id);
        ImageViewProfilePicture = findViewById(R.id.profile_image);

        idName = findViewById(R.id.name_value);
        idTehsil = findViewById(R.id.tehsil_value);
        idArea = findViewById(R.id.area_value);
        idScheme = findViewById(R.id.scheme_value);
        idFathersName = findViewById(R.id.fathers_value);
        idContact = findViewById(R.id.contact_value);
        idDistrict = findViewById(R.id.district_value);

        idName.setText(": "+sharedPreferences.getString("mName",""));
        idTehsil.setText(": "+sharedPreferences.getString("tehsil",""));
        idArea.setText(": "+sharedPreferences.getString("area",""));
        idScheme.setText(": "+sharedPreferences.getString("name_of_scheme",""));
        idFathersName.setText(": "+sharedPreferences.getString("fname",""));
        idContact.setText(": "+sharedPreferences.getString("mContact",""));
        idDistrict.setText(": "+sharedPreferences.getString("district",""));

        String image_web = sharedPreferences.getString("image_web","");
        if(image_web.matches("")){
            //NOTHING IN IMAGE
        }else{
             Picasso.get().load(MainActivity.MAIN_URL+"public/image/"+image_web).into(ImageViewProfilePicture);

        }
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
        return directory.getAbsolutePath()+"/"+fileName;
    }// /storage/emulated/0/Download


}
