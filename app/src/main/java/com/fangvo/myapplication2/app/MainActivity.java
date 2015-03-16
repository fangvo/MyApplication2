package com.fangvo.myapplication2.app;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends TabActivity {



    //private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        }catch (ClassNotFoundException e){e.printStackTrace();}
        TabHost tabHost = getTabHost();
                GeneratePriceList(this);
        GetNumOfSells(this);
        GetNameOfTraider(this);
        GenerateList(this,tabHost);



    }


    private void GeneratePriceList(final Context context){

        AsyncResponse AS= new AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {

                Log.i("CODE", "AsyncResponse");

                JSONObject result = new JSONObject();

                Map<String,GoodsData> map = new HashMap<String, GoodsData>();

                for (int i = 0;i<output.length();i++   ){
                    try {
                        JSONObject rowObject = output.getJSONObject(i);
                        JSONArray tempArray;
                        JSONObject tempObject = new JSONObject();

                        String temp = rowObject.getString("name");
                        String[] separated = temp.split(" ");
                        String proiz = separated[separated.length-1];
                        String name ="";
                        for (int j=0;j<separated.length-1;j++){
                            name += separated[j]+" ";
                        }
                        Double chena = rowObject.getDouble("chena");
                        Long kolvo = rowObject.getLong("kolvo");

                        try {
                            tempArray = result.getJSONArray(proiz);

                        }catch (JSONException e){

                            Log.v("JSONException", e.getMessage());
                            tempArray = new JSONArray();
                        }

                        map.put(temp,new GoodsData(chena,kolvo));
                        tempObject.put("chena",chena);
                        tempObject.put("kolvo",kolvo);
                        tempObject.put("name",name.trim());
                        tempArray.put(tempObject);
                        result.put(proiz,tempArray);

                    }catch (JSONException e ){e.printStackTrace();}
                }

                MyData.priceList = result;
                MyData.data = map;

            }
        };

        try {
            new AsyncRequest(context, AS).execute("Select name,chena,kolvo from Goods");
            Log.i("CODE","AsyncTask");
        }catch (NullPointerException e){e.printStackTrace();}

        Log.i("CODE","Return");

    }

    private void GenerateList(final Context context,final TabHost tabHost){

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


                MyData.clientsName = myList;

                TabHost.TabSpec tabSpec;


                tabSpec = tabHost.newTabSpec("tag1");
                tabSpec.setIndicator("Tab1");
                tabSpec.setContent(new Intent(context, Tab1.class));
                tabHost.addTab(tabSpec);

                tabSpec = tabHost.newTabSpec("tag2");
                tabSpec.setIndicator("Tab2");
                tabSpec.setContent(new Intent(context, Tab2.class));
                tabHost.addTab(tabSpec);

            }
        };

        try {
            new AsyncRequest(context, AS).execute("Select [Клиент] from Clients where Type = 'Покупатель'");
            Log.i("CODE","AsyncTask");
        }catch (NullPointerException e){e.printStackTrace();}

        Log.i("CODE","Return");

    }

    private void GetNumOfSells(final Context context){

        AsyncResponse AS= new AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {

                Log.i("CODE", "AsyncResponse");

                try {

                    String SUM = output.getJSONObject(0).getString("SUM");

                    Log.i("SUM", SUM);

                    MyData.ifOfNextSell = Integer.valueOf(SUM);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        try {
            new AsyncRequest(context, AS).execute("SELECT IDENT_CURRENT('Sells') + IDENT_INCR('Sells') as SUM");
            Log.i("CODE", "AsyncTask");
        }catch (NullPointerException e){e.printStackTrace();}

        Log.i("CODE","Return");

    }

    private void GetNameOfTraider(final Context context){

        AsyncResponse AS= new AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {

                Log.i("CODE", "AsyncResponse");

                try {

                    String name = output.getJSONObject(0).getString("name");

                    Log.i("NAME", name);

                    MyData.name = name;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        try {
            new AsyncRequest(context, AS).execute(String.format("select name from imp where login = '%s'",Referense.MSSQL_LOGIN));
            Log.i("CODE", "AsyncTask");
        }catch (NullPointerException e){e.printStackTrace();}

        Log.i("CODE","Return");

    }


    //region OptionMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //endregion


}
