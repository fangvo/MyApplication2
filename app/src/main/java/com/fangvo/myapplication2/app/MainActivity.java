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

    static TabHost tabHost;

    //private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PrefsRef pr = new PrefsRef(this);




		// провека дрвйвера дб
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        }catch (ClassNotFoundException e){e.printStackTrace();}
        tabHost = getTabHost();

		// создание таб хоста и дабовление в него табов
        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Меню");
        tabSpec.setContent(new Intent(MainActivity.this, Tab1.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Настройки");
        tabSpec.setContent(new Intent(MainActivity.this, Tab2.class));
        tabHost.addTab(tabSpec);

		
		// переключение на настройки если в настройках нету логина и пароля и сылки на дб
        if(pr.MSSQL_URL.equals("")||pr.MSSQL_Login.equals("")||pr.MSSQL_Password.equals("")){
            tabHost.setCurrentTab(1);
            Toast toast = Toast.makeText(this, "Please specify SQL URl,Login and password", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }

    }

	
	//переключение таба доступно отовсюду
    public static void SwitchTab(Integer id){

        tabHost.setCurrentTab(id);

    }

}
