package com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.fangvo.myapplication2.app.R;

public class PriceListItem implements PriceListItemInterface {
    public String name;
    public Double chena;
    public Long kolvo;

    public PriceListItem(String text1, Double text2, Long text3) {
        this.name = text1;
        this.chena = text2;
        this.kolvo = text3;
    }

    @Override
    public int getViewType() {
        return PriceListAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.price_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }


        TextView text1 = (TextView) view.findViewById(R.id.list_content1);
        TextView text2 = (TextView) view.findViewById(R.id.list_content2);
        TextView text3 = (TextView) view.findViewById(R.id.list_content3);
        text1.setText(name);
        text2.setText(String.format("%.2f",chena));
        text3.setText(kolvo.toString());

        return view;
    }

}
