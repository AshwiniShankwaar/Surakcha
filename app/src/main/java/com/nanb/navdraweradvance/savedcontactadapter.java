package com.nanb.navdraweradvance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class savedcontactadapter extends RecyclerView.Adapter<savedcontactadapter.ViewHolder> {
    List<contactmodel> contactmodelList;
    Context context;
    int lastpostion = -1;

    public savedcontactadapter(List<contactmodel> contactmodelList, Context context) {
        this.contactmodelList = contactmodelList;
        this.context = context;
    }

    @NonNull
    @Override
    public savedcontactadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem= layoutInflater.inflate(R.layout.recycler_data_structure, parent, false);
        savedcontactadapter.ViewHolder viewHolder = new savedcontactadapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull savedcontactadapter.ViewHolder holder, int position) {
        //Toast.makeText(context,String.valueOf(contactmodelList.size()), Toast.LENGTH_SHORT).show();
        if(holder.getAdapterPosition() > lastpostion){
            Animation animation = AnimationUtils.loadAnimation(context,R.anim.slideinrow);
            holder.itemView.startAnimation(animation);
            holder.name.setText(contactmodelList.get(position).getName());
            holder.phonenbr.setText(contactmodelList.get(position).getPhoneNumber());
            lastpostion = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return contactmodelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,phonenbr;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
            phonenbr = itemView.findViewById(R.id.message);
        }
    }
    public boolean deletedata(int postion){
        contactmodel contactmodel = contactmodelList.get(postion);
        databasehelper databasehelper = new databasehelper(context,"Surakcha.db");
        boolean delet = databasehelper.deletecontactone(contactmodel);
        if(delet){
            return true;
        }else{
            return false;
        }
    }
}
