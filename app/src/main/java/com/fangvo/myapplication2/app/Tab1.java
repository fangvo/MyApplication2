package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Tab1 extends Activity {

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        if (!MyData.isLoaded){
            Log.i("LOAD","LOAD FROM TAB1");
            new GenerateConnectionString(this);
            try {
                new AsyncFirstLoad(Tab1.this).execute("Select name,chena,kolvo from Goods","Select [Клиент] from Clients where Type = 'Покупатель'","SELECT IDENT_CURRENT('Sells') + IDENT_INCR('Sells') as SUM",String.format("select name from imp where login = '%s'",Referense.MSSQL_LOGIN));
                Log.i("CODE","AsyncTask");
            }catch (NullPointerException e){e.printStackTrace();}
        }



        mContext = this;

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(onClickListener);

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(onClickListener);





    }


    //region SomeComents

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button:
                    if(!MyData.isLoaded){
                        Toast toast = Toast.makeText(mContext, "Подждите загрузка еще идет", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 25, 400);
                        toast.show();
                        return;
                    }
                    Intent activity = new Intent(Tab1.this,PriceListActivity.class);
                    startActivity(activity);
                    break;

                case R.id.button2:
                    if(!MyData.isLoaded){
                        Toast toast = Toast.makeText(mContext, "Подждите загрузка еще идет", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 25, 400);
                        toast.show();
                        return;
                    }
                    Intent activity_add_sell = new Intent(Tab1.this,AddSellActivity.class);
                    startActivity(activity_add_sell);
                    break;
/*
                case R.id.button3:
                    //DO something
                    break;*/

            }
        }
    };

    //endregion

    @Override
    protected void onResume() {
        super.onResume();
    }



}
