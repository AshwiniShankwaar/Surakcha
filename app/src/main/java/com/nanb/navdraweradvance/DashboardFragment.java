package com.nanb.navdraweradvance;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    String locationdata = "",TAG="Location data",latitudedata="",longitudedata="",AddressData="";
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;
    TextView lat,lon,add,textdata;
    Button send;
    ImageView map,locationicon;
    CardView locationcard;
    databasehelper databasehelper;

    String messagedata = "";
    Animation top,bottom;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.dashboard_fragment,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((Main_launcer) getActivity()).getSupportActionBar().setTitle("Dashboard");
        locationicon = view.findViewById(R.id.locationicon);
        locationcard = view.findViewById(R.id.location);
        textdata = view.findViewById(R.id.textdata);

        top = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.topanimation);
        bottom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.bottomanimation);

        locationicon.setAnimation(top);
        locationcard.setAnimation(bottom);
        textdata.setAnimation(top);
        
        lat = view.findViewById(R.id.latitude);
        lon = view.findViewById(R.id.longitude);
        add = view.findViewById(R.id.address);
        send = view.findViewById(R.id.send);
        map = view.findViewById(R.id.map);
        send.setAnimation(bottom);
        databasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
        sendmethod();
        mapmethod();
        startLocationService();
        File file = new File(getActivity().getFilesDir(),"SurakchaLocation.txt");
        if(file.exists()){
            setdata();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
               try{
                   File file = new File(getActivity().getFilesDir(), "SurakchaLocation.txt");
                   if(file.exists()){
                       setdata();
                   }
               }catch(Exception e){
                   e.printStackTrace();
               }
            }
        }, delay);
    }

    public boolean islocationServiceAvailable(){
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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
        if(databasehelper.CheckifTableIsEmptyOrNot(databasehelper.SETTINGTABLE)){
            int locationservice = databasehelper.getsettingdata().get(0).getLocationservice();
            if(locationservice == 1){
                if(!islocationServiceAvailable()){
                    Intent intent = new Intent(getActivity().getApplicationContext(), locationForgroundservice.class);
                    intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
                    getActivity().startService(intent);
                    //logfileCreate.appendLog("Location service start",this);
                    //Toast.makeText(this,"Location service start",Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            if(!islocationServiceAvailable()){
                Intent intent = new Intent(getActivity().getApplicationContext(), locationForgroundservice.class);
                intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
                getActivity().startService(intent);
                //logfileCreate.appendLog("Location service start",this);
                //Toast.makeText(this,"Location service start",Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void mapmethod() {
        map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment mapFragment = new mapFragment();
                t.replace(R.id.container, mapFragment);
                t.commit();
            }
        });
    }

    private void sendmethod() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<contactmodel> data = databasehelper.getsavedcontactdata();
               if(data.size() <= 0){
                   Toast.makeText(getActivity().getApplicationContext(),"Select contact to send message.",Toast.LENGTH_SHORT).show();
               }else{
                   String Locationhyperlink =  "https://www.google.com/maps/search/?api=1&query="+latitudedata+","+longitudedata;
                   int messageid = databasehelper.getsettingdata().get(0).getMsgid();
                   //Toast.makeText(getActivity().getApplicationContext(),databasehelper.getselectedmessage(messageid),Toast.LENGTH_SHORT).show();
                    messagedata = databasehelper.getselectedmessage(messageid);
                   //Toast.makeText(getActivity().getApplicationContext(),messagedata,Toast.LENGTH_SHORT).show();
                   for(contactmodel phnnumber:data){
                       SmsManager smsManager = SmsManager.getDefault();
                       ArrayList<String> parts = smsManager.divideMessage(messagedata+"\n\nLatitude: "+latitudedata+"\nLongitude: "+longitudedata+"\nAddress: "+AddressData+"\nGoogle map Link: "+Locationhyperlink);
                       smsManager.sendMultipartTextMessage(phnnumber.getPhoneNumber(),null,parts,null,null);
                   }
               }
            }
        });
    }

    private void setdata() {
        loadlocationdata();
        getLatituteandlongitute();
        lat.setText("Latitude: "+latitudedata);
        lon.setText("Longitude: "+longitudedata);
        add.setText("Location: "+AddressData);
    }
    private void getLatituteandlongitute() {
        Log.d(TAG, "getLatituteandlongitute: " + locationdata);
        int lastpartoflatitude = locationdata.indexOf(",");
        int lastpartoflongitude = locationdata.indexOf(", Location: ");
        AddressData = locationdata.substring(lastpartoflongitude+11);
        latitudedata = locationdata.substring(10,lastpartoflatitude);
        longitudedata = locationdata.substring(lastpartoflatitude+13,lastpartoflongitude);
        Log.d(TAG, String.valueOf(lastpartoflatitude) +" "+ String.valueOf(lastpartoflongitude) + " "+ latitudedata+" "+longitudedata);
    }
    private void loadlocationdata() {
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new FileReader(new File(getContext().getFilesDir(),"SurakchaLocation.txt")));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = null;
        try {
            line = bufReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (line != null) { locationdata = line;
            try {
                line = bufReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
