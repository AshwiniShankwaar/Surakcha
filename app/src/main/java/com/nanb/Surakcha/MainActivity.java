package com.nanb.Surakcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    TextView appname,apptag;
    LinearLayout comapnyname;
    ImageView logo;
    Animation top,bottom,side;
    private static int SPLASH_SCREEN = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        top = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.topanimation);
        bottom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottomanimation);
        side = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splasesideanimation);
        appname = findViewById(R.id.Appname);
        apptag = findViewById(R.id.AppTag);
        comapnyname = findViewById(R.id.comapny);
        logo = findViewById(R.id.logo);


        logo.setAnimation(top);
        appname.setAnimation(side);
        apptag.setAnimation(side);
        comapnyname.setAnimation(bottom);


        new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        Intent i = new Intent(getApplicationContext(),Main_launcer.class);
                        startActivity(i);
                        finish();
                    }
                },SPLASH_SCREEN);
    }
}