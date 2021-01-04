package com.nanb.Surakcha;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class SavedContact extends Fragment {

    databasehelper databasehelper;
    RecyclerView recyclerView;
    savedcontactadapter savedcontactadapter;
    ImageButton create;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.savedcontact_fragment,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((Main_launcer) getActivity()).getSupportActionBar().setTitle("Saved Contacts");
        create = view.findViewById(R.id.createmsg);
        recyclerView = (RecyclerView) view.findViewById(R.id.messagedata);
        databasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
        List<contactmodel> data = databasehelper.getsavedcontactdata();
        //Toast.makeText(getActivity().getApplicationContext(), String.valueOf(data.size()), Toast.LENGTH_SHORT).show();
        savedcontactadapter = new savedcontactadapter(data,getActivity().getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(savedcontactadapter);
        tochmethod();
        ceatebutton();
    }

    private void ceatebutton() {
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction t = getFragmentManager().beginTransaction();
                Fragment cFrag = new ContactFragments();
                t.replace(R.id.container, cFrag);
                t.commit();
            }
        });
    }

    private void tochmethod(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                boolean result = savedcontactadapter.deletedata(viewHolder.getAdapterPosition());
                if(result){
                    // adapter.notifyDataSetChanged();
                    recreatefag();
                    Toast.makeText(getActivity().getApplicationContext(),"Deleted successfully...",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Error while deleting...",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.red))
                        .addActionIcon(R.drawable.delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);
    }
    public void recreatefag() {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        Fragment SavedMessage = new SavedContact();
        t.replace(R.id.container, SavedMessage);
        t.commit();
    }
}
