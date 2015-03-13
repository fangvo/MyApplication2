package com.fangvo.myapplication2.app;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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
        new AsyncRequest(this).execute("Select * from Goods");
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button:

                    List<ClientsTable> list  = new ArrayList<ClientsTable>();
                    list.add(new ClientsTable("name","adres",465546L,654L,"bank",789L,987L,321L,"director"));

                    new AsyncInsert(list).execute();
                    break;
                /*
                case R.id.button2:
                    //DO something
                    break;
                case R.id.button3:
                    //DO something
                    break;
                    */
            }
        }
    };


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
//25.82.46.162
    public final class AsyncRequest extends AsyncTask<String, Void, JSONArray> {

        private Context mContext;

        public AsyncRequest(Context context) {
            mContext = context;
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
                        Log.v("JSON RETURN", "key = " + rowObject.names().getString(j) + " value = " + rowObject.get(rowObject.names().getString(j)));
                    }
                    myList.add(s);

                }catch (JSONException e ){e.printStackTrace();}
            }


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1, myList);
            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(dataAdapter);
        }
    }

    public final class AsyncInsert extends AsyncTask<String, Void, Void> {

        private static final String REMOTE_TABLE = "dbo.Clients";
        private static final String SQL = "INSERT into " + REMOTE_TABLE + " values(?,?,?,?,?,?,?,?,?,?)";

        private final List<ClientsTable> mData;

        public AsyncInsert(List<ClientsTable> data) {
            this.mData = data;
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
                        prepared = con.prepareStatement(SQL);

                        for (ClientsTable item : mData) {
                            prepared.setString(1, item.cname);
                            prepared.setString(2, item.adres);
                            prepared.setLong(3, item.inn);
                            prepared.setLong(4, item.kpp);
                            prepared.setString(5, item.bank);
                            prepared.setLong(6, item.rs);
                            prepared.setLong(7, item.ks);
                            prepared.setLong(8, item.bik);
                            prepared.setString(9, item.ctype);
                            prepared.setString(10, item.dir);
                            prepared.addBatch();
                        }
                        prepared.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
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
            }
            return null;
        }
    }
}
