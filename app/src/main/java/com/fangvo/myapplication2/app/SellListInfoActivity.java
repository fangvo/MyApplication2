package com.fangvo.myapplication2.app;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.ListAdapter;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.ListItemInterface;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.PriceListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellListInfoActivity extends ListActivity {

    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_list_info);

        Intent intent = getIntent();
        Integer ID =intent.getIntExtra("ID", 0);

        TextView text1 = (TextView) findViewById(R.id.textView2);
        text1.setText("Заказ №" + ID);


        //Toast.makeText(getApplicationContext(),"ID = "+ID,Toast.LENGTH_SHORT).show();
        String query = String.format("select name ,ed , chena from SellInfo as si left join Sells as s on si.idofsell = s.ID where s.ID = %s",ID);

        // Log.i("QUERY",query);
		
		// аснихроний запрос и присвоение значений листу
        new AsyncGetSellListInfo(this).execute(query);
    }

    public void onClick(View view) {
        finish();
    }

    public class AsyncGetSellListInfo extends AsyncTask<String, Void, JSONArray> {

        private Context mContext;
        private ProgressDialog dialog;

        public AsyncGetSellListInfo(Context context) {

            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading from DB wait... Please wait...");
            dialog.show();
        }

        @SuppressWarnings("ThrowFromFinallyBlock")
        @Override
        protected JSONArray doInBackground(String... query) {
            JSONArray resultSet = new JSONArray();
            //try {
            Connection con = null;
            Statement st = null;
            ResultSet rs = null;
            try {
                con = DriverManager.getConnection(Referense.MSSQL_DB, Referense.MSSQL_LOGIN, Referense.MSSQL_PASS);
                Log.i("Connection", " open");
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
            return resultSet;
        }

        protected void onCancelled() {
            dialog.dismiss();
            Toast toast = Toast.makeText(mContext, "Error connecting to Server", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }

        @Override
        protected void onPostExecute(JSONArray result) {

            List<ListItemInterface> items = new ArrayList<ListItemInterface>();


            for (int i = 0; i<result.length();i++){
                try {
                    JSONObject obj = result.getJSONObject(i);
                    items.add(new PriceListItem(obj.getString("name"),obj.getDouble("chena"),obj.getLong("ed")));
                }catch (JSONException e ){Log.e("JSONException", e.getMessage());}
            }

            mAdapter = new ListAdapter(SellListInfoActivity.this, items);
            setListAdapter(mAdapter);


            dialog.dismiss();
        }
    }
}
