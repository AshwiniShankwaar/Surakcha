package com.nanb.navdraweradvance;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.view.View.VISIBLE;

public class SavedMessage extends Fragment {

    ImageButton create,savedmessage;
    TextView textcount;
    Button save;
    databasehelper databasehelper;
    RecyclerView recyclerView;
    MainRecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.savedmessage_fragment,container,false);
        //getActivity().setTitle("Saved message");
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        create = view.findViewById(R.id.createmsg);

        recyclerView = (RecyclerView) view.findViewById(R.id.messagedata);
        databasehelper = new databasehelper(getActivity().getApplicationContext(),"Surakcha.db");
        List<msgmodel> data = databasehelper.getmsgdata();
        int selectedmessage = databasehelper.getsettingdata().get(0).getMsgid();
        adapter = new MainRecyclerViewAdapter(data,getActivity().getApplicationContext(),selectedmessage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);
        //Toast.makeText(getActivity().getApplicationContext(),data.toString(),Toast.LENGTH_SHORT).show();
        tochmethod();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDlertbox(view);
            }
        });

    }

    private void showsavedmesssage(View view) {
       /* Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.savedmessage_dilog);

        TextView title = dialog.findViewById(R.id.title);
        TextView msg = dialog.findViewById(R.id.message);
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
                // recreatefag();
            }
        });
        List<msgmodel> msgdata = databasehelper.getmsgdata();
        title.setText(msgdata.get(0).getTitle());
        msg.setText(msgdata.get(0).getMsg());
        dialog.show();*/
        int msgid = databasehelper.getsettingdata().get(0).getMsgid();
        List<msgmodel> msgdata = databasehelper.getsellectedmsgdata(msgid);
        new AlertDialog.Builder(getContext(),R.style.CustomAlertDailog)
                .setTitle(msgdata.get(0).getTitle())
                .setMessage(msgdata.get(0).getMsg())
                .setNegativeButton("Cancel",null)
                .show();
    }

    private void tochmethod(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                boolean result = adapter.deletedata(viewHolder.getAdapterPosition());
                if(result){
                   // adapter.notifyDataSetChanged();
                    recreatefag();
                    Toast.makeText(getActivity().getApplicationContext(),"Deleted successfully...",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Error while deleting...",Toast.LENGTH_SHORT).show();
                }

            }
        }).attachToRecyclerView(recyclerView);
    }
    private void showDlertbox(View view) {
        Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.alert_dialog);

        EditText title = dialog.findViewById(R.id.title);
        EditText msg = dialog.findViewById(R.id.message);
        save = dialog.findViewById(R.id.save);
        Button cancel = dialog.findViewById(R.id.cancel);
        textcount = dialog.findViewById(R.id.textcount);
        msg.addTextChangedListener(mTextEditorWatcher);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
               // recreatefag();
            }
        });
        save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String titledata = title.getText().toString();
                String msgdata = msg.getText().toString();
                msgmodel msgmodel = null;
                //Toast.makeText(getActivity().getApplicationContext(),titledata+" "+msgdata,Toast.LENGTH_SHORT).show();
                try{
                   if(titledata.isEmpty()){
                       Toast.makeText(getActivity().getApplicationContext(),"Please write a message Title before saving",Toast.LENGTH_SHORT).show();
                   }else if(msgdata.isEmpty()){
                       Toast.makeText(getActivity().getApplicationContext(),"Please write your message before saving",Toast.LENGTH_SHORT).show();
                   }else if(titledata.isEmpty() && msgdata.isEmpty()){
                       Toast.makeText(getActivity().getApplicationContext(),"Please write your message and message title before saving",Toast.LENGTH_SHORT).show();
                   }else{
                       msgmodel = new msgmodel(-1,titledata,msgdata);
                   }
                }catch(Exception ex){
                    Toast.makeText(getActivity().getApplicationContext(),"Error while saving message",Toast.LENGTH_SHORT).show();
                }

                databasehelpermethod(msgmodel);
                dialog.dismiss();
                adapter.notifyDataSetChanged();
               recreatefag();
            }
        });

        dialog.show();

    }

    public void recreatefag() {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        Fragment SavedMessage = new SavedMessage();
        t.replace(R.id.container, SavedMessage);
        t.commit();
    }

    private void databasehelpermethod(msgmodel msgmodel) {

        String susses = databasehelper.addOne(msgmodel);
        Toast.makeText(getActivity().getApplicationContext(),susses,Toast.LENGTH_SHORT).show();
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            textcount.setVisibility(VISIBLE);
            textcount.setText(String.valueOf(s.length()));
            if(s.length() > 0){
                save.setEnabled(true);
                if(s.length() == 80){
                    Toast.makeText(getActivity().getApplicationContext(),"Maximum words reached",Toast.LENGTH_SHORT).show();
                }
            }else{
                save.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

}
