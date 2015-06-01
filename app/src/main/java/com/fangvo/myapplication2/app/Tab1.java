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

import java.sql.SQLException;


public class Tab1 extends Activity {

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        mContext = this;

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(onClickListener);

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(onClickListener);

        Button btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(onClickListener);

        Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(onClickListener);





    }


    //region SomeComents

	// обработчик кнопок
	
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

                case R.id.button7:
                    Intent activity_add_client = new Intent(Tab1.this,AddClientActivity.class);
                    startActivity(activity_add_client);
                    break;

                case R.id.button3:
                    Intent activity_sell_list = new Intent(Tab1.this,SellListActivity.class);
                    startActivity(activity_sell_list);

                    break;

            }
        }
    };

    //endregion

    @Override
    protected void onResume() {

        super.onResume();

        PrefsRef pr = new PrefsRef(this);

		// загрузка даных если они еще не загружены
        if (!MyData.isLoaded){
            Log.i("LOAD","LOAD FROM TAB1");
            new GenerateConnectionString(this);
            try {
                new AsyncFirstLoad(Tab1.this).execute("Select name,chena,kolvo from Goods","Select [Клиент] from Clients where Type = 'Покупатель'","SELECT IDENT_CURRENT('Sells') + IDENT_INCR('Sells') as SUM",String.format("select name from imp where login = '%s'",Referense.MSSQL_LOGIN));
                Log.i("CODE","AsyncTask");
            }catch (Exception e){
                Log.e("Exception",e.getMessage());}


        }



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    void Resume(){

        PrefsRef pr = new PrefsRef(this);
        if(pr.MSSQL_URL.equals("")||pr.MSSQL_DB.equals("")||pr.MSSQL_Login.equals("")||pr.MSSQL_Password.equals("")){
            MainActivity.SwitchTab(1);
            return;
        }



    }



}
