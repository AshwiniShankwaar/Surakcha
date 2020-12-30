package com.nanb.Surakcha;

import android.view.ViewGroup;

public abstract class DrawerItem<T extends DrawerAdapter.ViewHolder> {
    protected boolean isChecked;
    public abstract T createViewHolder(ViewGroup parent);
    public abstract void blindViewHolder(T holder);
    public DrawerItem<T>setChecked(boolean isChecked){
        this.isChecked = isChecked;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isSelectable(){
        return true;
    }
}
