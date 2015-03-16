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

public class AsyncRequest extends AsyncTask<String, Void, JSONArray> {

    public AsyncResponse response;
    private Context mContext;
    private ProgressDialog dialog;

    public AsyncRequest(Context context,AsyncResponse response) {

        mContext = context;
        this.response = response;
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
        /*} catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        //Log.i("JSON ADD",resultSet.toString());
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

        response.processFinish(result);
        dialog.dismiss();
    }
}
