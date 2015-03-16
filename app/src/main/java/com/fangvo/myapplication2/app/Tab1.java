package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;
import java.util.Map;


public class Tab1 extends Activity {

    Context mContext;
    static Spinner mSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        List<String> myList = MyData.clientsName;
        Map<String,GoodsData> data = MyData.data;

        for(String name :myList){
            Log.i("CLIENTSNAMES", name);
        }

        mContext = this;
        mSpinner = (Spinner) findViewById(R.id.spinner2);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(onClickListener);

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(onClickListener);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Tab1.this, android.R.layout.simple_spinner_item, myList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(0);



    }


    //region SomeComents

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button:
                    Intent activity = new Intent(Tab1.this,PriceListActivity.class);
                    startActivity(activity);
                    break;

                case R.id.button2:
                    Intent activity_add_sell = new Intent(Tab1.this,AddSellActivity.class);
                    activity_add_sell.putExtra("client", (String)mSpinner.getSelectedItem());
                    startActivity(activity_add_sell);
                    break;
/*
                case R.id.button3:
                    //DO something
                    break;*/

            }
        }
    };

    //endregion

    @Override
    protected void onResume() {
        super.onResume();
    }



}
