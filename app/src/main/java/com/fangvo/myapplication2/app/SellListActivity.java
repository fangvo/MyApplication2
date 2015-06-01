package com.fangvo.myapplication2.app;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.ListAdapter;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.ListItemInterface;
import com.fangvo.myapplication2.app.com.fangvo.myapplication.app.adapter.SellListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellListActivity extends ListActivity implements AdapterView.OnItemLongClickListener , AdapterView.OnItemClickListener {

    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sells_list);

        Button btn10 = (Button)findViewById(R.id.button10);
        btn10.setOnClickListener(onClickListener);

        //getListView().setOnItemLongClickListener(this);
        getListView().setOnItemClickListener(this);

		// запрос списка закозов
        String query = String.format("select  ID,ClientName as name, day, SUM as sum from Sells where [Кем] = '%s' ORDER BY day DESC ", MyData.name);
        new AsyncGetSellList(this).execute(query);
    }
	
	// обработчик нажатий
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.button10:
                    finish();
                    break;

            }
        }
    };

	/* // обработчик долгих нажатий по элементу списка
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        SellListItem selectedItem = (SellListItem)parent.getItemAtPosition(position);


        deleteDialog(SellListActivity.this,selectedItem);
        return true;
    } */

	// обработчик нажатий по элементу списка
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SellListItem selectedItem = (SellListItem)parent.getItemAtPosition(position);

        Intent sell_info = new Intent(SellListActivity.this,SellListInfoActivity.class);
        sell_info.putExtra("ID",selectedItem.ID);
        startActivity(sell_info);

        //Toast.makeText(getApplicationContext(),selectedItem.date,Toast.LENGTH_SHORT).show();
    }


	// асинхроное получение списка заказов
    public class AsyncGetSellList extends AsyncTask<String, Void, JSONArray> {

        private Context mContext;
        private ProgressDialog dialog;

        public AsyncGetSellList(Context context) {

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

		// обработка JSON и присаоение листа адаптеру
		
		
            List<ListItemInterface> items = new ArrayList<ListItemInterface>();


            for (int i = 0; i<result.length();i++){
                try {
                JSONObject obj = result.getJSONObject(i);
                items.add(new SellListItem(obj.getInt("ID"),obj.getString("name"),obj.getString("day"),obj.getDouble("sum")));
                }catch (JSONException e ){Log.e("JSONException", e.getMessage());}
            }

            mAdapter = new ListAdapter(SellListActivity.this, items);
            setListAdapter(mAdapter);


            dialog.dismiss();
        }
    }

	
	
    /* // удаление элемента списка
	public void deleteDialog(Context context, final SellListItem item) {
        AlertDialog.Builder confirmDelete = new AlertDialog.Builder(context);
        confirmDelete.setMessage("Are You Sure You Want to Delete")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mAdapter.remove(item);
                        mAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = confirmDelete.create();

        dialog.show();

    } */
}
