package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListAdapter;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListItem;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.pricelistadapter.PriceListItemInterface;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddSellActivity extends Activity {

    List<PriceListItemInterface> mItems;
    ListView mListView;
    PriceListAdapter adapter_listview;
    Spinner mSinner;
    Map<String,GoodsData> mData;
    EditText mEditText;
    String mClient_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sell);

        mItems = new ArrayList<PriceListItemInterface>();

        mEditText = (EditText) findViewById(R.id.editText);
        mSinner =(Spinner) findViewById(R.id.spinner);
        mListView =(ListView) findViewById(R.id.listView);

        ArrayList<String> myList = new ArrayList<String>();

        mData = MyData.data;

        for (String key:mData.keySet()){
            myList.add(key);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSellActivity.this, android.R.layout.simple_spinner_item, myList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSinner.setAdapter(adapter);
        mSinner.setSelection(0);

        Intent intent = getIntent();
        mClient_name = intent.getStringExtra("client");
        Button btn = (Button)findViewById(R.id.button4);
        btn.setOnClickListener(onClickListener);

        Button btn2 = (Button)findViewById(R.id.button5);
        btn2.setOnClickListener(onClickListener);

        Button btn6 = (Button)findViewById(R.id.button6);
        btn6.setOnClickListener(onClickListener);

        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        adapter_listview = new PriceListAdapter(this, mItems);
        mListView.setAdapter(adapter_listview);

        /*editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });*/

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //region OnClick
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button4:
                    UpdateDB();
                    break;
                case R.id.button5:
                    finish();
                    break;
                case R.id.button6:

                    Log.e("BUTTON", "button6");
                    String name =(String)mSinner.getSelectedItem();
                    Long kolvo =  Long.parseLong(mEditText.getEditableText().toString());

                    if(kolvo < mData.get(name).kolvo){
                        mData.get(name).kolvo -= kolvo;
                    }else if (mData.get(name).kolvo!=0L && mData.get(name).kolvo>kolvo){
                        kolvo = mData.get(name).kolvo;
                        mData.get(name).kolvo = 0L;
                    }else if (mData.get(name).kolvo == 0L){
                        Toast.makeText(AddSellActivity.this,"Нет Товара",Toast.LENGTH_SHORT).show();
                        return;
                    }


/*                    if(mTempData.get(name) !=null){
                        GoodsData tempGData = mTempData.get(name);
                        tempGData.kolvo +=kolvo;
                    }else{mTempData.put(name,mData.get(name));}*/

                    for (Object mItem : mItems) {
                        PriceListItem itItem = (PriceListItem) mItem;
                        if (itItem.name.equals(name)) {
                            itItem.kolvo += kolvo;
                            itItem.chena = mData.get(name).chena*itItem.kolvo;
                            adapter_listview.notifyDataSetChanged();
                            return;
                        }
                    }

                    mItems.add(new PriceListItem(name, mData.get(name).chena*kolvo, kolvo ) );

                    /*Button btn2 = (Button)findViewById(R.id.button5);
                    btn2.setOnClickListener(onClickListener);*/

                    adapter_listview.notifyDataSetChanged();



                    break;
            }
        }
    };
    //endregion

    private void UpdateDB(){

/*        AsyncResponse AS= new AsyncResponse() {
            @Override
            public void processFinish(JSONArray output) {

                Log.i("CODE", "AsyncResponse");


            }

        };*/

        List<Map<Integer,Object>> list  = new ArrayList<Map<Integer,Object>>();
        List<Map<Integer,Object>> list2  = new ArrayList<Map<Integer,Object>>();

        Integer num = 1;

        Double sum = 0D;

        for (Object item : mItems){
            Map<Integer,Object> dict = new HashMap<Integer, Object>();
            dict.put(1,num);
            dict.put(2,((PriceListItem) item).name);
            dict.put(3,((PriceListItem) item).kolvo);
            dict.put(4,MyData.ifOfNextSell);
            dict.put(5,((PriceListItem) item).chena);
            list.add(dict);
            sum += ((PriceListItem) item).chena;
            num++;
        }


        SimpleDateFormat df = new SimpleDateFormat("dd.M.yyyy");

        Date date = new Date();

        String dateStr = df.format(date.getTime());
        Log.i("TIME", dateStr);

        Map<Integer,Object> dict2 = new HashMap<Integer, Object>();
        dict2.put(1,mClient_name);
        dict2.put(2,"Продажа");
        dict2.put(3,dateStr);
        dict2.put(4,sum);
        dict2.put(5,MyData.name);
        dict2.put(6,false);
        dict2.put(7,null);
        list2.add(dict2);

        new AsyncUpdate(list2,getApplicationContext()).execute("INSERT into Sells values(?,?,?,?,?,?,?)");
        new AsyncUpdate(list,getApplicationContext()).execute("INSERT into SellInfo values(?,?,?,?,?)");
        finish();


    }

    /*private void setNameAsEditable (View rowView, boolean setToEditable) {

        EditText textView = (EditText) rowView
                .findViewById(R.id.editText);
        textView.setFocusableInTouchMode(setToEditable);
        textView.setFocusable(setToEditable);

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);

        if (setToEditable) {
            textView.requestFocus();
            imm.showSoftInput(textView, 0);
        }
    }*/
}
