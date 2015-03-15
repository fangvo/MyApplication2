package com.fangvo.myapplication2.app;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListHeader;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListItemInterface;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListItem;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PriceListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.price_list);


        GenerateList(this);

        /*
        items.add(new Header("Header 1"));
        items.add(new ListItem("Text 1", "Rabble rabble","text3"));
        items.add(new ListItem("Text 2", "Rabble rabble","text3"));
        items.add(new ListItem("Text 3", "Rabble rabble","text3"));
        items.add(new ListItem("Text 4", "Rabble rabble","text3"));
        items.add(new Header("Header 2"));
        items.add(new ListItem("Text 5", "Rabble rabble","text3"));
        items.add(new ListItem("Text 6", "Rabble rabble","text3"));
        items.add(new ListItem("Text 7", "Rabble rabble","text3"));
        items.add(new ListItem("Text 8", "Rabble rabble","text3"));*/

        //PriceListAdapter adapter = new PriceListAdapter(this, items);
        //setListAdapter(adapter);
    }


    private void GenerateList(final Context context){

        AsyncResponse AS= new AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {

                Log.i("CODE","AsyncResponse");

                List<PriceListItemInterface> items = new ArrayList<PriceListItemInterface>();
                JSONObject result = new JSONObject();

                //List<String> myList = new ArrayList<String>();

                for (int i = 0;i<output.length();i++   ){
                    try {
                        JSONObject rowObject = output.getJSONObject(i);
                        JSONArray tempArray;// = new JSONArray();
                        JSONObject tempObject = new JSONObject();
                        //JSONObject namedObject = new JSONObject();


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
                        tempObject.put("chena",chena);
                        tempObject.put("kolvo",kolvo);
                        tempObject.put("name",name.trim());
                        tempArray.put(tempObject);
                        result.put(proiz,tempArray);

                    }catch (JSONException e ){e.printStackTrace();}
                }

                Log.i("RSEULT JSON",result.toString());

                Iterator JSONObjectIterator = result.keys();
                while(JSONObjectIterator.hasNext()){
                    String key = (String)JSONObjectIterator.next();
                    items.add(new PriceListHeader(key));
                    try {
                        JSONArray JArray = result.getJSONArray(key);
                        for (int i = 0; i<JArray.length();i++){
                            JSONObject obj = JArray.getJSONObject(i);
                            items.add(new PriceListItem(obj.getString("name"),obj.getDouble("chena"),obj.getLong("kolvo")) {
                            });
                        }
                    }catch (JSONException e ){Log.e("JSONException", e.getMessage());}
                }


                PriceListAdapter adapter = new PriceListAdapter(context, items);
                setListAdapter(adapter);
            }
        };

        try {
            new AsyncRequest(this, AS).execute("Select name,chena,kolvo from Goods");
            Log.i("CODE","AsyncTask");
        }catch (NullPointerException e){e.printStackTrace();}

        Log.i("CODE","Return");

    }
}
