package com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.fangvo.myapplication2.app.R;

public class SellListItem implements ListItemInterface {
    public String name;
    public String date;
    public Double sum;
    public  Integer ID;

    public SellListItem(Integer text4,String text1, String text2, Double text3) {
        this.name = text1;
        this.date = text2;
        this.sum = text3;
        this.ID = text4;
    }

    @Override
    public int getViewType() {
        return ListAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.sell_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }


        TextView text1 = (TextView) view.findViewById(R.id.list_content1);
        TextView text2 = (TextView) view.findViewById(R.id.list_content2);
        TextView text3 = (TextView) view.findViewById(R.id.list_content3);
        text1.setText(name);
        text2.setText(date);
        text3.setText(String.format("%.2f", sum));

        return view;
    }

}
