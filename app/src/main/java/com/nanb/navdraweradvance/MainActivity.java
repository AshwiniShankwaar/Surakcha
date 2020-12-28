package com.nanb.navdraweradvance;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{

    private static final int POS_CLOSE = 0;
    private static final int POS_DASEBOARD = 1;
    private static final int POS_PROFILE = 2;
    private static final int POS_SAVEDMESSAGE = 3;
    private static final int POS_SAVEDCONTACT = 4;
    private static final int POS_SETTING = 5;
    private static final int POS_SHARE = 6;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    databasehelper databasehelper;

    public static final int Request_code = 6;
    private BroadcastReceiver MyReceiver = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        MyReceiver = new MyReceiver();
        broadcastIntent();
        checkpermission();
        databasehelper = new databasehelper(getApplicationContext(),"Surakcha.db");

        locationservice();

       //startLocationService();
        slidingRootNav = (SlidingRootNav) new SlidingRootNavBuilder(this)
                .withDragDistance(180)
                .withRootViewScale(0.75f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_menu)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitle();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemfor(POS_CLOSE),
                createItemfor(POS_DASEBOARD).setChecked(true),
                createItemfor(POS_PROFILE),
                createItemfor(POS_SAVEDMESSAGE),
                createItemfor(POS_SAVEDCONTACT),
                createItemfor(POS_SETTING),
                createItemfor(POS_SHARE)
        ));

        adapter.setListener(this);
        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DASEBOARD);
    }

    private DrawerItem createItemfor(int position){
        return new SimpleItem(screenIcons[position],screenTitles[position])
                .withIconTint(color(R.color.lightpink))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.lightpink))
                .withSelectedTextTint(color(R.color.lightpink));
    }

    private int color(@ColorRes int res){
        return ContextCompat.getColor(this,res);
    }
    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.id_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for(int i = 0; i < ta.length();i++){
            int id = ta.getResourceId(i,0);
            if(id!=0){
                icons[i] = ContextCompat.getDrawable(this,id);
            }
        }
        ta.recycle();
        return icons;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private String[] loadScreenTitle() {
        return getResources().getStringArray(R.array.id_activityScreenTitle);
    }

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(position == POS_DASEBOARD){
            DashboardFragment dashboardFragment = new DashboardFragment();
            transaction.replace(R.id.container,dashboardFragment);
        }else if(position == POS_PROFILE){
           ProfileFragment profileFragment = new ProfileFragment();
            transaction.replace(R.id.container,profileFragment);
        }else if(position == POS_SAVEDCONTACT){
            SavedContact savedContact = new SavedContact();
            transaction.replace(R.id.container,savedContact);
        }else if(position == POS_SAVEDMESSAGE){
            SavedMessage savedMessage = new SavedMessage();
            transaction.replace(R.id.container,savedMessage);
        }else if(position == POS_SETTING){
            Setting setting = new Setting();
            transaction.replace(R.id.container,setting);
        }else if(position == POS_SHARE){
            Share share = new Share();
            transaction.replace(R.id.container,share);
        }

        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void checkpermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)+ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)+ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)+ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION
                },Request_code);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Request_code && grantResults.length > 0){
            if(grantResults[0 ] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
               if(!databasehelper.CheckifTableIsEmptyOrNot(databasehelper.SETTINGTABLE)){
                   databasehelper.addsettingdata(0,1);
               }
            }else{
                //logfileCreate.appendLog("Permission denied",this);
                if(!databasehelper.CheckifTableIsEmptyOrNot(databasehelper.SETTINGTABLE)){
                    databasehelper.addsettingdata(0,0);
                }
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public boolean islocationServiceAvailable(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(locationForgroundservice.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }
    public void startLocationService(){
        if(!islocationServiceAvailable()){
            Intent intent = new Intent(getApplicationContext(), locationForgroundservice.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            //logfileCreate.appendLog("Location service start",this);
            //Toast.makeText(this,"Location service start",Toast.LENGTH_SHORT).show();
        }
    }
    public void stopLocationService(){
        if(islocationServiceAvailable()){
            Intent intent  = new Intent(getApplicationContext(), locationForgroundservice.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            //logfileCreate.appendLog("Location service start",this);
            Toast.makeText(getApplicationContext().getApplicationContext(),"Location service stop",Toast.LENGTH_SHORT).show();
        }
    }
    private void locationservice() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            Toast.makeText(getApplicationContext(),"Location service is not enable",Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(this,R.style.CustomAlertDailog)
                    .setTitle("Location Service")
                    .setMessage("Let us help the application to determine the exert Location.")
                    .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Disagree",null)
                    .show();
        }
    }
}