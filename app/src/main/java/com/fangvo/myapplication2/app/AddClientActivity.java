package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddClientActivity extends Activity {


	// иничиализация активности
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

		// поиск кнопок по ид и устоновка оброботчика
		
        Button btn8 = (Button)findViewById(R.id.button8);
        btn8.setOnClickListener(onClickListener);

        Button btn9 = (Button)findViewById(R.id.button9);
        btn9.setOnClickListener(onClickListener);
    }

	// оброботчик нажатий на кнопки
	
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button8:
                    SendToDB();
                    break;

                case R.id.button9:
                    finish();
                    break;

            }
        }
    };

	// сбор и отправка даных в дб
	
    private void SendToDB(){

	// переменые
	
        String cname;
        String adres;
        Long inn;
        Long kpp;
        String bank;
        Long rs;
        Long ks;
        Long bik;
        String dir;

		
		// поиск тесбоксов по id
		
        EditText editTextCName = (EditText)findViewById(R.id.editText2);
        EditText editTextAdres = (EditText)findViewById(R.id.editText3);
        EditText editTextInn = (EditText)findViewById(R.id.editText4);
        EditText editTextKpp = (EditText)findViewById(R.id.editText5);
        EditText editTextBank = (EditText)findViewById(R.id.editText6);
        EditText editTextRS = (EditText)findViewById(R.id.editText7);
        EditText editTextKS = (EditText)findViewById(R.id.editText8);
        EditText editTextBIK = (EditText)findViewById(R.id.editText9);
        EditText editTextdir = (EditText)findViewById(R.id.editText10);

		// проверка заполености всех полей
		
        if (editTextCName.getEditableText().toString().equals("")||editTextAdres.getEditableText().toString().equals("")||editTextInn.getEditableText().toString().equals("")||editTextKpp.getEditableText().toString().equals("")||editTextBank.getEditableText().toString().equals("")||editTextRS.getEditableText().toString().equals("")||editTextKS.getEditableText().toString().equals("")||editTextBIK.getEditableText().toString().equals("")||editTextdir.getEditableText().toString().equals("")){
            Toast.makeText(AddClientActivity.this,"Необходимо заполнить все поля",Toast.LENGTH_SHORT).show();
            return;
        }

		// присвоение значеий текстбоксов переменым
		
        cname = editTextCName.getEditableText().toString();
        adres = editTextAdres.getEditableText().toString();
        inn = Long.parseLong(editTextInn.getEditableText().toString());
        kpp = Long.parseLong(editTextKpp.getEditableText().toString());
        bank = editTextBank.getEditableText().toString();
        rs = Long.parseLong(editTextRS.getEditableText().toString());
        ks = Long.parseLong(editTextKS.getEditableText().toString());
        bik = Long.parseLong(editTextBIK.getEditableText().toString());
        dir = editTextdir.getEditableText().toString();

		// создание карты значений и добовление ее в лист

        List<Map<Integer,Object>> list  = new ArrayList<Map<Integer,Object>>();
        Map<Integer,Object> dict = new HashMap<Integer, Object>();
        dict.put(1, cname);
        dict.put(2, adres);
        dict.put(3, inn);
        dict.put(4, kpp);
        dict.put(5, bank);
        dict.put(6, rs);
        dict.put(7, ks);
        dict.put(8, bik);
        dict.put(9, "Покупатель");
        dict.put(10, dir);
        list.add(dict);

		// добовление имени клиента в список имен клиентов
        MyData.clientsName.add(cname);

		// запуск асинхроной отвпавки даных в дб
        new AsyncUpdate(list,getApplicationContext()).execute("INSERT into Clients values(?,?,?,?,?,?,?,?,?,?)");
		
		// закрытие активности
        finish();

    }

}
