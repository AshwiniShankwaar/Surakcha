package com.nanb.navdraweradvance;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class SimpleItem extends DrawerItem<SimpleItem.ViewHolder> {

    private int selectedItemIconTint;
    private int selectedItemTextTint;

    private int normalItemIconTint;
    private int normalItemTextTint;

    private Drawable icon;
    private String title;

    public SimpleItem(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }



    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.itemoption,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public void blindViewHolder(ViewHolder holder) {
        holder.title.setText(title);
        holder.icon.setImageDrawable(icon);
        holder.title.setTextColor(isChecked ? selectedItemTextTint :normalItemTextTint);
        holder.icon.setColorFilter(isChecked ? selectedItemIconTint :normalItemIconTint);
    }

    public SimpleItem withSelectedIconTint(int SelectedItemIconTint){
        this.selectedItemIconTint = SelectedItemIconTint;
        return this;
    }

    public SimpleItem withSelectedTextTint(int SelectedItemTextTint){
        this.selectedItemTextTint = SelectedItemTextTint;
        return this;
    }
    public SimpleItem withIconTint(int normalItemIconTint){
        this.normalItemIconTint = normalItemIconTint;
        return this;
    }
    public SimpleItem withTextTint(int normalItemTextTint){
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }

    static class ViewHolder extends DrawerAdapter.ViewHolder{

        private ImageView icon;
        private TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
}
