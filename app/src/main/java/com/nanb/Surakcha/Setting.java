package com.nanb.Surakcha;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Setting extends Fragment {
    LinearLayout contact,selectmessage,term,policy,contactus;
    Switch locationserviceSwitcher;
    Main_launcer main_launcer = new Main_launcer();
    databasehelper databasehelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.setting_fragment,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((Main_launcer) getActivity()).getSupportActionBar().setTitle("Settings");
        contact = (LinearLayout) view.findViewById(R.id.contactselect);
        selectmessage = (LinearLayout) view.findViewById(R.id.selectmessage);
        locationserviceSwitcher = (Switch) view.findViewById(R.id.switcher);
        contactus = (LinearLayout) view.findViewById(R.id.contactus);
        databasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
        if(islocationServiceAvailable()){
            locationserviceSwitcher.setChecked(true);
        }else{
            locationserviceSwitcher.setChecked(false);
        }
        selectmessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment selectmessageFrag = new SelectMessage();
                t.replace(R.id.container, selectmessageFrag);
                t.commit();
            }
        });
        contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment cFrag = new ContactFragments();
                t.replace(R.id.container, cFrag);
                t.commit();
            }
        });
        locationserviceSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},main_launcer.Request_code);
                    }else{
                        startLocationService();
                        int messageid = databasehelper.getsettingdata().get(0).getMsgid();
                        Boolean success = databasehelper.updatesettingdata(messageid,1);
                    }
                } else {
                    stopLocationService();
                    int messageid = databasehelper.getsettingdata().get(0).getMsgid();
                    Boolean success = databasehelper.updatesettingdata(messageid,0);
                }
            }
        });
        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+"academyforbrilliance99@gmail.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Surakcha Location sharing");
                    startActivity(intent);
            }
        });
    }
    public void startLocationService(){
        if(!islocationServiceAvailable()){
            Intent intent = new Intent(getActivity().getApplicationContext(), locationForgroundservice.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            getActivity().startService(intent);
            //logfileCreate.appendLog("Location service start",this);
            //Toast.makeText(this,"Location service start",Toast.LENGTH_SHORT).show();
        }
    }
    public void stopLocationService(){
        if(islocationServiceAvailable()){
            Intent intent  = new Intent(getActivity().getApplicationContext(), locationForgroundservice.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            getActivity().startService(intent);
            //logfileCreate.appendLog("Location service start",this);
            Toast.makeText(getActivity().getApplicationContext().getApplicationContext(),"Location service stop",Toast.LENGTH_SHORT).show();
        }
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


}
