package com.fangvo.myapplication2.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class MainActivity extends TabActivity {

    //private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получаем TabHost
        TabHost tabHost = getTabHost();

        // инициализация была выполнена в getTabHost
        // метод setup вызывать не нужно

        TabHost.TabSpec tabSpec;


        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Tab1");
        tabSpec.setContent(new Intent(this, Tab1.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Tab2");
        tabSpec.setContent(new Intent(this, Tab2.class));
        tabHost.addTab(tabSpec);
/*
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(onClickListener);
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(onClickListener);
        */

    }


    //region SomeComents
/*
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button:

                    List<Map<Integer,Object>> list  = new ArrayList<Map<Integer,Object>>();
                    Map<Integer,Object> dict = new HashMap<Integer, Object>();
                    dict.put(1,"somename");
                    dict.put(2,"adres");
                    dict.put(3,465546L);
                    dict.put(4,654L);
                    dict.put(5,"bank");
                    dict.put(6,789L);
                    dict.put(7,987L);
                    dict.put(8,321L);
                    dict.put(9,"Покупатель");
                    dict.put(10,"director");
                    list.add(dict);

                    new AsyncInsert(list).execute("INSERT into Clients values(?,?,?,?,?,?,?,?,?,?)");
                    break;
                case R.id.button2:
                    List<Map<Integer,Object>> list_update  = new ArrayList<Map<Integer,Object>>();
                    Map<Integer,Object> dict_update = new HashMap<Integer, Object>();
                    dict_update.put(1, "somenameafterchange");
                    dict_update.put(2, "adres");
                    list_update.add(dict_update);

                    new AsyncInsert(list_update).execute("update Clients set [Клиент] = ? where [Адрес] = ?");
                    break;
                *//*
                case R.id.button3:
                    //DO something
                    break;
                    *//*
            }
        }
    };*/
    //endregion


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
