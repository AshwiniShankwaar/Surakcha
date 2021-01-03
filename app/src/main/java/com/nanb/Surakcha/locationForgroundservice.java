package com.nanb.Surakcha;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;

public class locationForgroundservice extends Service {
    private String Adrs = "";

    //LogfileCreate logfileCreate = new LogfileCreate();
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
           // Toast.makeText(getApplicationContext(),"Pass1",Toast.LENGTH_SHORT).show();
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();

               String fullAdrs =  getCompleteAddressString(latitude,longitude);
                Adrs = "latitude: "+latitude + ", longitude: "+longitude + ", Location: "+fullAdrs;
                //logfileCreate.appendLog(Adrs,getApplicationContext());
                try {
                    FileOutputStream fos = null;
                    String fileName = "SurakchaLocation.txt";
                    fos = openFileOutput(fileName, MODE_PRIVATE);
                    fos.write(Adrs.getBytes());
                    fos.close();
                    // Add code to upload location data in the firebase database

                    uploaddatatotheserver();
                  //  logfileCreate.appendLog("File created" + getFilesDir()+"/"+fileName,getApplicationContext());
                    //display file saved message
                    //Toast.makeText(getBaseContext(), "File saved successfully!"+ " "+getFilesDir(),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void uploaddatatotheserver() {

    }

    private String getCompleteAddressString(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Address", strReturnedAddress.toString());
                //logfileCreate.appendLog("Address: "+ strReturnedAddress,getApplicationContext());
            } else {
                //logfileCreate.appendLog("Address: Cannot get Address",getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //logfileCreate.appendLog("Address: Cannot get Address",getApplicationContext());
        }
        //Toast.makeText(getApplicationContext(),strAdd,Toast.LENGTH_SHORT).show();
        return strAdd;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    private void startLocationService() {
        //Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();
        String channel_id = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Surakcha Location service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running...");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channel_id) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(channel_id, "Location service", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
                //Toast.makeText(getApplicationContext(),"notification created",Toast.LENGTH_SHORT).show();
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID,builder.build());
        //Toast.makeText(getApplicationContext(),"forground service started",Toast.LENGTH_SHORT).show();
    }

    private void stoplocationservice(){
        //Toast.makeText(getApplicationContext(),"Stoped",Toast.LENGTH_SHORT).show();
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(getApplicationContext(),"Pass1",Toast.LENGTH_SHORT).show();
        if(intent != null){
            String action = intent.getAction();
          //  Toast.makeText(getApplicationContext(),action,Toast.LENGTH_SHORT).show();
            if(action != null){
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE)){
                    startLocationService();
                    //Toast.makeText(getApplicationContext(),"start service",Toast.LENGTH_SHORT).show();
                }else if(action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)){
                    stoplocationservice();
                    //Toast.makeText(getApplicationContext(),"stop service",Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onStartCommand(intent,flags, startId);
    }
}
