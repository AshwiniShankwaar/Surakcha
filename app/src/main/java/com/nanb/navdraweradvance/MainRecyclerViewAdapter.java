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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>{
    List<msgmodel> msgdata;
    Context context;
    int lastpostion = -1;
    public MainRecyclerViewAdapter(List<msgmodel> msgdata, Context context) {
        this.msgdata = msgdata;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View listItem= layoutInflater.inflate(R.layout.recycler_data_structure, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       /*if(holder.getAdapterPosition() > lastpostion){
           Animation animation = AnimationUtils.loadAnimation(context,R.anim.slideinrow);
           holder.itemView.startAnimation(animation);

           lastpostion = holder.getAdapterPosition();
       }*/
        //databasehelper databasehelper = new databasehelper(context,"Surakcha.db");
        msgmodel msgmodel = msgdata.get(position);
        holder.title.setText(msgmodel.getTitle());
        holder.msg.setText(msgmodel.getMsg());
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return msgdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,msg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            msg = itemView.findViewById(R.id.message);
        }
        void bind(int postion){
            msgmodel msgmodel = msgdata.get(postion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.purple_200));
                    databasehelper databasehelper = new databasehelper(context,"Surakcha.db");
                   int locationservice = databasehelper.getsettingdata().get(0).getLocationservice();
                    boolean update = databasehelper.updatesettingdata(msgmodel.getId(),locationservice);
                    if(update){
                        Toast.makeText(context,"message selected",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public boolean deletedata(int postion){
        msgmodel msgmodel = msgdata.get(postion);
        databasehelper databasehelper = new databasehelper(context,"Surakcha.db");
        boolean delet = databasehelper.deleteone(msgmodel);
        if(delet){
            return true;
        }else{
            return false;
        }
    }
}