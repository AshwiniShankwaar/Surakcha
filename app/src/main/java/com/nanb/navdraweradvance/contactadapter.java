package com.nanb.navdraweradvance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class contactadapter extends RecyclerView.Adapter<contactadapter.ViewHolder> {
    List<contactmodel> contactmodelList;
    ArrayList<String> contactname = new ArrayList<>();
    ArrayList<String> phonenumber = new ArrayList<>();
    Context context;
    int lastpostion = -1;

    public contactadapter(List<contactmodel> contactmodelList, Context context) {
        this.contactmodelList = contactmodelList;
        this.context = context;
    }

    @NonNull
    @Override
    public contactadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem= layoutInflater.inflate(R.layout.recycler_data_structure, parent, false);
        contactadapter.ViewHolder viewHolder = new contactadapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull contactadapter.ViewHolder holder, int position) {
        holder.name.setText(contactmodelList.get(position).getName());
        holder.phonenbr.setText(contactmodelList.get(position).getPhoneNumber());
        holder.bind(contactmodelList.get(position));

    }

    @Override
    public int getItemCount() {
        return contactmodelList.size();
    }

    public ArrayList<String> getselectedphn(){
        ArrayList<String> contact = new ArrayList<>();
        for(int i = 0; i<phonenumber.size();i++){
            contact.add(phonenumber.get(i));
        }
        return contact;
    }
    public ArrayList<String> getselectedname(){
        ArrayList<String> contact = new ArrayList<>();
        for(int i = 0; i<contactname.size();i++){
            contact.add(contactname.get(i));
        }
        return contact;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,phonenbr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            phonenbr = itemView.findViewById(R.id.message);
        }
        void bind(contactmodel contactmodel){
           if(phonenumber.contains(contactmodel.getPhoneNumber())){
               itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.purple_200));
           }else{
               itemView.setBackground(ContextCompat.getDrawable(context,R.drawable.msg_recycler_view_item_background));
           }

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(!phonenumber.contains(contactmodel.getPhoneNumber())){
                       phonenumber.add(contactmodel.getPhoneNumber());
                       contactname.add(contactmodel.getName());
                       notifyDataSetChanged();
                   }else{
                       phonenumber.remove(contactmodel.getPhoneNumber());
                       contactname.remove(contactmodel.getName());
                       notifyDataSetChanged();
                   }
               }
           });
        }
    }
}
