package com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.fangvo.myapplication2.app.R;

public class PriceListItem implements PriceListItemInterface {
    public final String         str1;
    public final Double         str2;
    public final Long         str3;

    public PriceListItem(String text1, Double text2, Long text3) {
        this.str1 = text1;
        this.str2 = text2;
        this.str3 = text3;
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

        //NumberFormat formatter = NumberFormat.getInstance(Locale.ENGLISH);

        TextView text1 = (TextView) view.findViewById(R.id.list_content1);
        TextView text2 = (TextView) view.findViewById(R.id.list_content2);
        TextView text3 = (TextView) view.findViewById(R.id.list_content3);
        text1.setText(str1);
        text2.setText(str2.toString());
        text3.setText(str3.toString());

        return view;
    }

}
