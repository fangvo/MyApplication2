package com.fangvo.myapplication2.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsyncFirstLoad extends AsyncTask<String, Void, List<JSONArray>> {

    private Context mContext;
    private ProgressDialog dialog;

    public AsyncFirstLoad(Context context) {
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
    protected List<JSONArray> doInBackground(String... query) {

        //try {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        List<JSONArray> lJArray = new ArrayList<JSONArray>();
        try {
            con = DriverManager.getConnection(Referense.MSSQL_DB, Referense.MSSQL_LOGIN, Referense.MSSQL_PASS);
            Log.i("Connection", " open");
            if (con != null) {
                for (String sqlquery:query) {
                    JSONArray resultSet = new JSONArray();
                    st = con.createStatement();
                    rs = st.executeQuery(sqlquery);
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

                    lJArray.add(resultSet);
                    if (rs != null) rs.close();
                    st.close();

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
        /*} catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        //Log.i("JSON ADD",resultSet.toString());
        return lJArray;
    }

    protected void onCancelled() {
        dialog.dismiss();
        Toast toast = Toast.makeText(mContext, "Error connecting to Server", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    @Override
    protected void onPostExecute(List<JSONArray> result) {

        SetPriceList(result.get(0));
        SetClientNameList(result.get(1));

        try {
            String SUM = result.get(2).getJSONObject(0).getString("SUM");

            Log.i("SUM", SUM);

            MyData.ifOfNextSell = Integer.valueOf(SUM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            String name = result.get(3).getJSONObject(0).getString("name");

            Log.i("NAME", name);

            MyData.name = name;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyData.isLoaded = true;

        dialog.dismiss();
    }

    private void SetPriceList(JSONArray output){
        JSONObject result = new JSONObject();

        Map<String,GoodsData> map = new HashMap<String, GoodsData>();

        for (int i = 0;i<output.length();i++   ){
            try {
                JSONObject rowObject = output.getJSONObject(i);
                JSONArray tempArray;
                JSONObject tempObject = new JSONObject();

                String temp = rowObject.getString("name");
                String[] separated = temp.split(" ");
                String proiz = separated[separated.length-1];
                String name ="";
                for (int j=0;j<separated.length-1;j++){
                    name += separated[j]+" ";
                }
                Double chena = rowObject.getDouble("chena");
                Long kolvo = rowObject.getLong("kolvo");

                try {
                    tempArray = result.getJSONArray(proiz);

                }catch (JSONException e){

                    Log.v("JSONException", e.getMessage());
                    tempArray = new JSONArray();
                }

                map.put(temp,new GoodsData(chena,kolvo));
                tempObject.put("chena",chena);
                tempObject.put("kolvo",kolvo);
                tempObject.put("name",name.trim());
                tempArray.put(tempObject);
                result.put(proiz,tempArray);

            }catch (JSONException e ){e.printStackTrace();}
        }

        MyData.priceList = result;
        MyData.data = map;
    }

    private void SetClientNameList(JSONArray output){
        List<String> myList = new ArrayList<String>();

        for (int i = 0;i<output.length();i++   ){
            try {
                JSONObject rowObject = output.getJSONObject(i);
                String name = rowObject.getString("Клиент");
                myList.add(name);

            }catch (JSONException e ){e.printStackTrace();}
        }

        MyData.clientsName = myList;
    }

}

