package com.fangvo.myapplication2.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//хранение логина пророля... взятых из настроек 

public class PrefsRef {

    public String MSSQL_URL ;
    public String MSSQL_Instance ;
    public String MSSQL_DB ;
    public String MSSQL_Login ;
    public String MSSQL_Password ;


    PrefsRef(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        MSSQL_URL = sp.getString("MSSQL_URL", "");
        MSSQL_Instance = sp.getString("MSSQL_Instance", "");
        MSSQL_DB = sp.getString("MSSQL_DB", "");
        MSSQL_Login = sp.getString("MSSQL_Login", "");
        MSSQL_Password = sp.getString("MSSQL_Password", "");
    }


}
