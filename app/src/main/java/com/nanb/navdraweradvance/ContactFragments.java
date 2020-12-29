package com.nanb.navdraweradvance;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class ContactFragments extends Fragment {

    Button save,cancel;
    RecyclerView recyclerView;
    String TAG = "Contact:",id="",name="",pnumber="";
    ArrayList<String> numbers = new ArrayList<String>();
    ArrayList<String> dublicatnumberid = new ArrayList<String>();
    ArrayList<String> ids = new ArrayList<String>();
    ArrayList<String> displaynames = new ArrayList<String>();
    List<contactmodel> contactdata = new ArrayList<>();
    contactadapter customadapter;
    databasehelper contactdatabasehelper;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.contact_fragment,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Select contact");
        save = view.findViewById(R.id.save);
        recyclerView = view.findViewById(R.id.contactlist);
        searchView = view.findViewById(R.id.searchview);
        get();
        contactdatabasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
        customadapter = new contactadapter(contactdata,getActivity().getApplicationContext());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(customadapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<contactmodel> filtered = new ArrayList<>();
                for(contactmodel contactmodel: contactdata){
                    if(contactmodel.getName().contains(newText) || contactmodel.getName().toLowerCase().contains(newText) || contactmodel.getPhoneNumber().contains(newText)){
                        filtered.add(contactmodel);
                    }
                }
                customadapter = new contactadapter(filtered,getActivity().getApplicationContext());
                recyclerView.setAdapter(customadapter);
                return true;
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String success = "";
                //Toast.makeText(getActivity().getApplicationContext(),String.valueOf(customadapter.getselected().size()),Toast.LENGTH_SHORT).show();
                if(customadapter.getselectedphn().size()>0){
                    if(customadapter.getselectedname().size() == customadapter.getselectedphn().size()){
                        for(int i=0;i<customadapter.getselectedphn().size();i++){
                            //Toast.makeText(getActivity().getApplicationContext(),customadapter.getselectedname().get(i)+"\n"+customadapter.getselectedphn().get(i),Toast.LENGTH_SHORT).show();
                            success = contactdatabasehelper.addcontactOne(customadapter.getselectedname().get(i),customadapter.getselectedphn().get(i));
                            if(success.equals("Saved")){
                                sendtohome();
                            }
                        }
                    }
                    Toast.makeText(getActivity().getApplicationContext(),success,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"no data",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendtohome() {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        Fragment dashboard = new DashboardFragment();
        t.replace(R.id.container, dashboard);
        t.commit();
    }

    public void get(){
        Cursor cursor  = getActivity().getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext()){
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
            pnumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            if(pnumber.length()<10){
                Log.d("contact data","invalide");
                dublicatnumberid.add(id);
            }else {
                //String sdata = pnumber.substring(0, 3);
                //boolean data = sdata.equals("+91");
                // boolean spacedata = spdata.equals(" ");
                if (numbers.contains(pnumber)) {
                    Log.d("contact data", "dublicate");
                    dublicatnumberid.add(id);
                } else {
                    String sdata = pnumber.substring(0, 3);
                    boolean data = sdata.equals("+91");

                    if(data){
                        String firstpart = pnumber.substring(3,8);
                        String lastpart = pnumber.substring(8);

                        if(numbers.contains(sdata.concat(" ").concat(firstpart).concat(" ").concat(lastpart))){
                            Log.d("contact data", "dublicate");
                            dublicatnumberid.add(id);
                        }else{
                            numbers.add(pnumber);
                            ids.add(id);
                            displaynames.add(name);
                        }
                    }else{
                        String firstpart = pnumber.substring(0,5);
                        String lastpart = pnumber.substring(5);

                        if(numbers.contains(firstpart.concat(" ").concat(lastpart))){
                            Log.d("contact data", "dublicate");
                            dublicatnumberid.add(id);
                        }else{
                            numbers.add(pnumber);
                            ids.add(id);
                            displaynames.add(name);
                        }
                    }
                }
            }
        }
        //Toast.makeText(getApplicationContext(),String.valueOf(numbers.size())+" "+String.valueOf(dublicatnumberid.size()),Toast.LENGTH_SHORT).show();

        for(int i = 0;i<ids.size();i++){
            //Toast.makeText(getApplicationContext(),numbers.get(i),Toast.LENGTH_SHORT).show();
            contactmodel contactmodel = new contactmodel(displaynames.get(i),numbers.get(i),ids.get(i));
            //Toast.makeText(getActivity().getApplicationContext(),numbers.get(i), Toast.LENGTH_SHORT).show();
            contactdata.add(contactmodel);
        }


    }
}
