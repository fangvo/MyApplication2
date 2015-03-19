package com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.fangvo.myapplication2.app.R;

public class PriceListHeader implements ListItemInterface {
    private final String         name;

    public PriceListHeader(String name) {
        this.name = name;
    }

    @Override
    public int getViewType() {
        return ListAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.prise_list_headers, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.separator);
        text.setText(name);

        return view;
    }

}
