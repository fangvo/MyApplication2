package com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter;

import android.view.LayoutInflater;
import android.view.View;

public interface ListItemInterface {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
