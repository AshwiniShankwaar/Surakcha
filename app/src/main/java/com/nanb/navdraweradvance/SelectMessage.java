package com.nanb.navdraweradvance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectMessage extends Fragment {
    databasehelper databasehelper;
    RecyclerView recyclerView;
    MainRecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.selectmessage_fragment,container,false);
        return root;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.messagedata);
        databasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
        List<msgmodel> data = databasehelper.getmsgdata();
        adapter = new MainRecyclerViewAdapter(data,getActivity().getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);
        //Toast.makeText(getActivity().getApplicationContext(),data.toString(),Toast.LENGTH_SHORT).show();
    }
}
