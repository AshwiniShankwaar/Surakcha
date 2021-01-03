package com.nanb.Surakcha;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class editprofile extends Fragment {
    CircleImageView dp;
    public EditText name, email, phonenbr, pradd, cradd;
    Button update,cancel;
    public static final int PICK_IMAGE = 1;
    String nameStr,phonestr,Permanentaddress,currentAddress,emailStr;
    databasehelper databasehelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.editprofile_fragment, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((Main_launcer) getActivity()).getSupportActionBar().setTitle("Edit profile");
        implementationmethod(view);
        databasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
       File dpfile = new File(getActivity().getFilesDir().getPath()+"/profiledp.jpg");
       if(dpfile.exists()){
           Picasso.get().load(dpfile).into(dp);
       }
      if(databasehelper.CheckifTableIsEmptyOrNot(databasehelper.PROFILETABLE)){
          setprofiledata();
      }else{
          //Toast.makeText(getActivity().getApplicationContext(),"no data",Toast.LENGTH_SHORT).show();
          Log.d("profiledata","no Data");
      }
        dpmethod();
       savemethod();
       cancelmethod();
    }

    private void cancelmethod() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment profileFrag = new ProfileFragment();
                t.replace(R.id.container, profileFrag);
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

    private void savemethod() {
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString();
                emailStr = email.getText().toString();
                phonestr = phonenbr.getText().toString();
                Permanentaddress = pradd.getText().toString();
                currentAddress = cradd.getText().toString();

                if(nameStr.isEmpty() || emailStr.isEmpty() || phonestr.isEmpty() || Permanentaddress.isEmpty() || currentAddress.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"Make sure every feild is filled", Toast.LENGTH_SHORT).show();
                }else{

                    //Add code to upload profile data to the server and fetch tyhe data from the server to save in the database

                    String success = databasehelper.addprofile(nameStr,emailStr,phonestr,Permanentaddress,currentAddress);
                    Toast.makeText(getActivity().getApplicationContext(),success, Toast.LENGTH_SHORT).show();
                    if(success == "Saved"){
                        sendtoprofilefrag();
                    }
                }
            }
        });
    }

    private void sendtoprofilefrag() {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        Fragment profilefag = new ProfileFragment();
        t.replace(R.id.container, profilefag);
        t.commit();
    }

    private void dpmethod() {
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && data !=null ) {
            Uri selectedImage = data.getData();
            String real_path = getRealPathFromURI(getActivity().getApplicationContext(),selectedImage);
           // Log.d("path",real_path);
            CropImage.activity(selectedImage)
                    .start(getContext(),this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                String imagesourcepath = resultUri.getPath();
                File source = new File(imagesourcepath);

                String Destinationpath = getContext().getFilesDir().getAbsolutePath();
                File destination = new File(Destinationpath,"profiledp.jpg");
                try {
                    copyFile(source,destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Picasso.get().load(destination).into(dp);
                //Toast.makeText(getActivity().getApplicationContext(),imagesourcepath +" "+Destinationpath,Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void implementationmethod(View view) {
        dp = view.findViewById(R.id.profiledp);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phonenbr = view.findViewById(R.id.phone);
        pradd = view.findViewById(R.id.perAddress);
        cradd = view.findViewById(R.id.currAddress);
        update = view.findViewById(R.id.updateprofile);
        cancel = view.findViewById(R.id.cancel);

    }

    public String getRealPathFromURI(Context context,Uri contentUri) {
        String[] proj = {
                MediaStore.Audio.Media.DATA
        };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
//        Toast.makeText(personalChat.this, Long.toString(cursor.getLong(sizeIndex)),Toast.LENGTH_SHORT).show();
        return cursor.getString(column_index);
    }
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
