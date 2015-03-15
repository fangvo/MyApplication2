package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddSellItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_sell_item);

        Intent intent = getIntent();
        JSONArray JArray;
        try {
            JArray = new JSONArray(intent.getStringExtra("json"));
            for(int i = 0;i<JArray.length();i++){
                JSONObject rowObject = JArray.getJSONObject(i);
            }
        }catch (JSONException e){Log.e("JSONException", e.getMessage());}


    }
}
