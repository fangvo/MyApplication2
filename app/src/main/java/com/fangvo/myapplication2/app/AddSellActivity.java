package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListItemInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddSellActivity extends Activity {


    public static JSONArray MYJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sell);
        Button btn = (Button)findViewById(R.id.button4);
        btn.setOnClickListener(onClickListener);

        Button btn2 = (Button)findViewById(R.id.button5);
        btn2.setOnClickListener(onClickListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

            }else {            }
        }
    }

    private void UpdateFromDB(){
        AsyncResponse AS= new AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {

                Log.i("CODE", "AsyncResponse");

                MYJSON = output;
            }
        };

        try {
            new AsyncRequest(this, AS).execute("Select name,chena,kolvo from Goods");
            Log.i("CODE","AsyncTask");
        }catch (NullPointerException e){e.printStackTrace();}

        Log.i("CODE","Return");
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button4:
                    Intent activity = new Intent(AddSellActivity.this,AddSellItemActivity.class);
                    activity.putExtra("json",MYJSON.toString());
                    startActivity(activity);
                    break;

                case R.id.button5:
                    //Intent activity_add_sell = new Intent(Tab1.this,AddSellActivity.class);
                    //startActivity(activity_add_sell);
                    break;
            }
        }
    };
}
