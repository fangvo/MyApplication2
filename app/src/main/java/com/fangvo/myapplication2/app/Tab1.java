package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListAdapter;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListHeader;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListItem;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListItemInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Tab1 extends Activity {

    Context mContext;
    static Spinner mSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        mContext = this;
        mSpinner = (Spinner) findViewById(R.id.spinner2);

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(onClickListener);

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(onClickListener);

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
                    activity_add_sell.putExtra("client", "name");
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
        GenerateList(Tab1.this,mSpinner);
    }

    private void GenerateList(final Context context,final Spinner spinner){

        AsyncResponse AS= new AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {

                Log.i("CODE","AsyncResponse");

                List<String> myList = new ArrayList<String>();

                for (int i = 0;i<output.length();i++   ){
                    try {
                        JSONObject rowObject = output.getJSONObject(i);
                        String name = rowObject.getString("Клиент");
                        myList.add(name);

                    }catch (JSONException e ){e.printStackTrace();}
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, myList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);
                spinner.setSelection(0);

            }
        };

        try {
            new AsyncRequest(this, AS).execute("Select [Клиент] from Clients");
            Log.i("CODE","AsyncTask");
        }catch (NullPointerException e){e.printStackTrace();}

        Log.i("CODE","Return");

    }

}
