package com.nanb.Surakcha;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView dp;
    TextView name,email,phonenbr,pradd,cradd;
    Button update;
    databasehelper databasehelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((Main_launcer) getActivity()).getSupportActionBar().setTitle("Profile");
        dp = view.findViewById(R.id.profiledp);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phonenbr = view.findViewById(R.id.phone);
        pradd = view.findViewById(R.id.perAddress);
        cradd = view.findViewById(R.id.currAddress);
        update = view.findViewById(R.id.updateprofile);
        databasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
        File dpfile = new File(getActivity().getFilesDir().getPath()+"/profiledp.jpg");
        if(dpfile.exists()){
            Picasso.get().load(dpfile).into(dp);
        }

        if(databasehelper.CheckifTableIsEmptyOrNot(databasehelper.PROFILETABLE)){
            setprofiledata();
        }else{
           // Toast.makeText(getActivity().getApplicationContext(),"no data",Toast.LENGTH_SHORT).show();
            Log.d("profiledata","no Data");
        }
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment editfag = new editprofile();
                t.replace(R.id.container, editfag);
                t.commit();
            }
        });


    }
    public void setprofiledata() {
        databasehelper.getprofiledata();
        List<profilemodel> profilemodel =  databasehelper.getprofiledata();;
        // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(profilemodel.size()),Toast.LENGTH_SHORT).show();
        name.setText(profilemodel.get(0).getName());
        email.setText(profilemodel.get(0).getEmail());
        phonenbr.setText(profilemodel.get(0).getPhonenumber());
        pradd.setText(profilemodel.get(0).getPermanentaddress());
        cradd.setText(profilemodel.get(0).getCurrentaddress());

    }
}
