package com.fangvo.myapplication2.app;

import android.content.Context;
import android.util.Log;

// создание строки подключенй для дб из настроек

public class GenerateConnectionString {

    GenerateConnectionString(Context context){

        PrefsRef pr = new PrefsRef(context);

        Referense.MSSQL_DB = "jdbc:jtds:sqlserver://" + pr.MSSQL_URL+";";
        //"jdbc:jtds:sqlserver://192.168.56.1:1433;instance=SQLEXPRESS;DatabaseName=MyDB";
        if (!pr.MSSQL_DB.equals("")){
            Referense.MSSQL_DB += "DatabaseName =" +  pr.MSSQL_DB + ";";
        }
        if (!pr.MSSQL_Instance.equals("")){
            Referense.MSSQL_DB += "instance=" + pr.MSSQL_Instance + ";";
        }

        Referense.MSSQL_LOGIN = pr.MSSQL_Login;
        Referense.MSSQL_PASS = pr.MSSQL_Password;

        /* Log.i("CONNECTION STRING", Referense.MSSQL_DB);
        Log.i("CONNECTION STRING", Referense.MSSQL_LOGIN);
        Log.i("CONNECTION STRING", Referense.MSSQL_PASS); */
    }

}
