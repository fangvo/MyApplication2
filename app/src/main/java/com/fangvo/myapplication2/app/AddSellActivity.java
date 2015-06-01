package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.ListAdapter;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.PriceListItem;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.ListItemInterface;

import java.text.SimpleDateFormat;
import java.util.*;



public class AddSellActivity extends Activity {


	// инициализация переменых
    List<ListItemInterface> mItems;
    ListView mListView;
    ListAdapter adapter_listview;
    Spinner mSinner;
    Spinner mCSinner;
    Map<String,GoodsData> mData = new HashMap<String, GoodsData>();
    EditText mEditText;

	
	// саздание активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sell);

        mItems = new ArrayList<ListItemInterface>();

        List<String> clientList = MyData.clientsName;
		
        for(String name :clientList){
            Log.i("CLIENTSNAMES", name);
        }

        mEditText = (EditText) findViewById(R.id.editText);
        mSinner =(Spinner) findViewById(R.id.spinner);
        mCSinner =(Spinner) findViewById(R.id.spinner3);
        mListView =(ListView) findViewById(R.id.listView);

        ArrayList<String> myList = new ArrayList<String>();
		
		
		
        mData.putAll(MyData.data);

		// копирование ключей из карты в лист
		
        for (String key:mData.keySet()){
            myList.add(key);
        }

		// создание адептера для спинера с значениями из листа 
		
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddSellActivity.this, android.R.layout.simple_spinner_item, myList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSinner.setAdapter(adapter);
		// устрановка на 1 значение спинера
        mSinner.setSelection(0);

		// поиск кнопок по ид и устоновка оброботчика

        Button btn = (Button)findViewById(R.id.button4);
        btn.setOnClickListener(onClickListener);

        Button btn2 = (Button)findViewById(R.id.button5);
        btn2.setOnClickListener(onClickListener);

        Button btn6 = (Button)findViewById(R.id.button6);
        btn6.setOnClickListener(onClickListener);

        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);


		// кастомный адаптер для листвиева
        adapter_listview = new ListAdapter(this, mItems);
        mListView.setAdapter(adapter_listview);

		// адаптер для списка клиентов
        ArrayAdapter<String> adapter_clients = new ArrayAdapter<String>(AddSellActivity.this, android.R.layout.simple_spinner_item, clientList);
        adapter_clients.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCSinner.setAdapter(adapter_clients);
        mCSinner.setSelection(0);


    }

	// переключение активности спинера
    public void changeSpinnerState(Spinner spinner,Boolean state) {
            spinner.setEnabled(state);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //region OnClick
	// обработчик нажатий
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
			// свитчь для определения кнопки по ее ид 
            switch(v.getId()){
                case R.id.button4:
                    UpdateDB();
                    break;
                case R.id.button5:
                    finish();
                    break;
                case R.id.button6:

                    // Log.i("BUTTON", "button6");
					
                    String name =(String)mSinner.getSelectedItem();
                    Long kolvo =  Long.parseLong(mEditText.getEditableText().toString());

					// логика для сранения количиства товара в заказе и на складе
					
                    if(kolvo < mData.get(name).kolvo){
                        // Log.i("IFCOND", "kolvo<mdata kolvo");
                        mData.get(name).kolvo -= kolvo;
                    }else if (mData.get(name).kolvo!=0L && mData.get(name).kolvo<=kolvo){
                        // Log.i("IFCOND", "mDatakolvo ");
                        kolvo = mData.get(name).kolvo;
                        mData.get(name).kolvo = 0L;
                    }else if (mData.get(name).kolvo == 0L){
                        Toast.makeText(AddSellActivity.this,"Нет Товара",Toast.LENGTH_SHORT).show();
                        return;
                    }

					// измениения в локальных даных
                    for (Object mItem : mItems) {
                        PriceListItem itItem = (PriceListItem) mItem;
                        if (itItem.name.equals(name)) {
                            itItem.kolvo += kolvo;
                            itItem.chena = mData.get(name).chena*itItem.kolvo;
                            adapter_listview.notifyDataSetChanged();
                            return;
                        }
                    }
					
					// добовлеие в список покупок
                    mItems.add(new PriceListItem(name, mData.get(name).chena*kolvo, kolvo ) );
                    adapter_listview.notifyDataSetChanged();
                    //changeSpinnerState(mSinner,false);



                    break;
            }
        }
    };
    //endregion

	
	// обновление дб
    private void UpdateDB(){

        List<Map<Integer,Object>> list  = new ArrayList<Map<Integer,Object>>();
        List<Map<Integer,Object>> list2  = new ArrayList<Map<Integer,Object>>();
        List<Map<Integer,Object>> list3  = new ArrayList<Map<Integer,Object>>();

        SimpleDateFormat df = new SimpleDateFormat("dd.M.yyyy HH:mm");
        Date date = new Date();
        String dateStr = df.format(date.getTime());
        // Log.i("TIME", dateStr);

        Integer num = 1;
        Double sum = 0D;
		// создание листов словарей из списка покупок
		//list1 даные о купленом товаре
		//лист3 даные для изменения
        for (Object item : mItems){
            Map<Integer,Object> dict = new HashMap<Integer, Object>();
            Map<Integer,Object> dict3 = new HashMap<Integer, Object>();
            dict.put(1,num);
            dict.put(2,((PriceListItem) item).name);
            dict.put(3,((PriceListItem) item).kolvo);
            dict.put(4,MyData.ifOfNextSell);
            dict.put(5,((PriceListItem) item).chena);
            list.add(dict);
            dict3.put(1,mData.get(((PriceListItem) item).name).kolvo);
            dict3.put(2,dateStr);
            dict3.put(3,((PriceListItem) item).name);
            list3.add(dict3);
            sum += ((PriceListItem) item).chena;
            num++;
        }



		//лист2 общяи инф о заказе
        Map<Integer,Object> dict2 = new HashMap<Integer, Object>();
        dict2.put(1,(String)mCSinner.getSelectedItem());
        dict2.put(2,"Продажа");
        dict2.put(3,dateStr);
        dict2.put(4,sum);
        dict2.put(5,MyData.name);
        dict2.put(6,false);
        dict2.put(7,null);
        list2.add(dict2);
		
		// запуск асинхроной отвпавки даных в дб
        new AsyncUpdate(list2,getApplicationContext()).execute("INSERT into Sells values(?,?,?,?,?,?,?)");
        new AsyncUpdate(list,getApplicationContext()).execute("INSERT into SellInfo values(?,?,?,?,?)");
        new AsyncUpdate(list3,getApplicationContext()).execute("update Goods set kolvo = ? , Last = ? where name = ? ");

        MyData.data = mData;

        finish();

    }

}
