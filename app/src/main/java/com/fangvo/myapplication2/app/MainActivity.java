package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.*;


public class MainActivity extends Activity {

    final static String MSSQL_DB = "jdbc:jtds:sqlserver://192.168.56.1:1433;instance=SQLEXPRESS;DatabaseName=MyDB";
    final static String MSSQL_LOGIN = "fangvo";
    final static String MSSQL_PASS= "84695237";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(onClickListener);
        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(onClickListener);
        new AsyncRequest(this).execute("Select * from Goods");
    }

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
                /*
                case R.id.button3:
                    //DO something
                    break;
                    */
            }
        }
    };


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

//25.82.46.162
    public class AsyncRequest extends AsyncTask<String, Void, JSONArray> {

        private Context mContext;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        public AsyncRequest(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading from DB wait... Please wait...");
            dialog.show();
        }

        @SuppressWarnings("ThrowFromFinallyBlock")
        @Override
        protected JSONArray doInBackground(String... query) {
            JSONArray resultSet = new JSONArray();
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                Connection con = null;
                Statement st = null;
                ResultSet rs = null;
                try {
                    con = DriverManager.getConnection(MSSQL_DB, MSSQL_LOGIN, MSSQL_PASS);
                    Log.i("Connection"," open");
                    if (con != null) {
                        st = con.createStatement();
                        rs = st.executeQuery(query[0]);
                        if (rs != null) {
                            int columnCount = rs.getMetaData().getColumnCount();
                            // Сохранение данных в JSONArray
                            while (rs.next()) {
                                JSONObject rowObject = new JSONObject();
                                for (int i = 1; i <= columnCount; i++) {
                                    rowObject.put(rs.getMetaData().getColumnName(i), (rs.getString(i) != null) ? rs.getString(i) : "");
                                }
                                resultSet.put(rowObject);
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (st != null) st.close();
                        if (con != null) con.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            //Log.i("JSON ADD",resultSet.toString());
            return resultSet;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast toast = Toast.makeText(MainActivity.this, "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            // TODO: вернуть результат

            List<String> myList = new ArrayList<String>();

            for (int i = 0;i<result.length();i++   ){
                try {
                    JSONObject rowObject = result.getJSONObject(i);
                    String s ="";
                    for(int j = 0; j<rowObject.names().length(); j++){
                        s += rowObject.names().getString(j)+"\n"+rowObject.get(rowObject.names().getString(j))+"\n";
                        //Log.v("JSON RETURN", "key = " + rowObject.names().getString(j) + " value = " + rowObject.get(rowObject.names().getString(j)));
                    }
                    myList.add(s);

                }catch (JSONException e ){e.printStackTrace();dialog.dismiss();}
            }


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1, myList);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(dataAdapter);
            dialog.dismiss();
        }
    }

    public class AsyncInsert extends AsyncTask<String, Void, Void> {

        private List<Map<Integer,Object>> mData;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        public AsyncInsert(List<Map<Integer,Object>> data) {
            this.mData = data;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Updating DB wait... Please wait...");
            dialog.show();
        }

        @SuppressWarnings("ThrowFromFinallyBlock")
        @Override
        protected Void doInBackground(String... proc_params) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                Connection con = null;
                PreparedStatement prepared = null;
                try {
                    con = DriverManager.getConnection(MSSQL_DB, MSSQL_LOGIN, MSSQL_PASS);
                    if (con != null) {
                        prepared = con.prepareStatement(proc_params[0]);

                        for (Map<Integer,Object> item : mData) {

                            for (Map.Entry<Integer, Object> entry : item.entrySet())
                            {
                                prepared.setObject(entry.getKey(), entry.getValue());
                                System.out.println(entry.getKey() + "/" + entry.getValue());
                            }
                            prepared.addBatch();
                        }
                        prepared.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    cancel(true);
                } finally {
                    try {
                        if (prepared != null) prepared.close();
                        if (con != null) con.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                cancel(true);
            }
            return null;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast toast = Toast.makeText(MainActivity.this, "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }
}
