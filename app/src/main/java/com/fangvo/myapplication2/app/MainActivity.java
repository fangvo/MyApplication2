package com.fangvo.myapplication2.app;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;
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

        PrefsRef pr = new PrefsRef(this);




        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        }catch (ClassNotFoundException e){e.printStackTrace();}
        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;


        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Tab1");
        tabSpec.setContent(new Intent(MainActivity.this, Tab1.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Настройки");
        tabSpec.setContent(new Intent(MainActivity.this, Tab2.class));
        tabHost.addTab(tabSpec);

        if(pr.MSSQL_URL.equals("")||pr.MSSQL_DB.equals("")||pr.MSSQL_Login.equals("")||pr.MSSQL_Password.equals("")){
            tabHost.setCurrentTab(1);
        }

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
