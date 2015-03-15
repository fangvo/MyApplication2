package com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter;

import android.view.LayoutInflater;
import android.view.View;

public interface PriceListItemInterface {
    public int getViewType();
    public View getView(LayoutInflater inflater, View convertView);
}
