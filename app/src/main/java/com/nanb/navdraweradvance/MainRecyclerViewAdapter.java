package com.nanb.navdraweradvance;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
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
    int selectedmessage = 0;
    public MainRecyclerViewAdapter(List<msgmodel> msgdata, Context context,int selectedmessage) {
        this.msgdata = msgdata;
        this.context = context;
        this.selectedmessage = selectedmessage;
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
        //Toast.makeText(context,String.valueOf(selectedmessage) + "pass",Toast.LENGTH_SHORT).show();
        if(msgmodel.getId() == selectedmessage){
            holder.item.setBackground(ContextCompat.getDrawable(context,R.drawable.seleceted_item));
        }else{
            holder. item.setBackground(ContextCompat.getDrawable(context,R.drawable.msg_recycler_view_item_background));
        }
    }

    @Override
    public int getItemCount() {
        return msgdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,msg;
        LinearLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            msg = itemView.findViewById(R.id.message);
            item = itemView.findViewById(R.id.item);
        }
        void bind(int postion){
            msgmodel msgmodel = msgdata.get(postion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedmessage = msgmodel.getId();
                 //Toast.makeText(context,String.valueOf(selectedmessage),Toast.LENGTH_SHORT).show();
                    databasehelper databasehelper = new databasehelper(context,"Surakcha.db");
                    int locationservice = databasehelper.getsettingdata().get(0).getLocationservice();
                    boolean update = databasehelper.updatesettingdata(msgmodel.getId(),locationservice);
                    if(update){
                        Toast.makeText(context,"message selected",Toast.LENGTH_SHORT).show();
                    }
                 notifyDataSetChanged();
                }
            });

        }
    }
    public boolean deletedata(int postion){
        msgmodel msgmodel = msgdata.get(postion);
        databasehelper databasehelper = new databasehelper(context,"Surakcha.db");
        boolean delet = databasehelper.deleteone(msgmodel);
        notifyItemRemoved(postion);
        if(delet){
            return true;
        }else{
            return false;
        }
    }

}
