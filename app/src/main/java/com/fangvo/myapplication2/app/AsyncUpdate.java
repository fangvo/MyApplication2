package com.fangvo.myapplication2.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

//асинхроное обнавление даных в дб

public class AsyncUpdate extends AsyncTask<String, Void, Void> {

    private List<Map<Integer,Object>> mData;
    private Context mContext;
    private ProgressDialog dialog ;
    public AsyncResponse response;


    public AsyncUpdate(List<Map<Integer,Object>> data,Context cont,AsyncResponse response) {
        this.mData = data;
        mContext = cont;
        this.response = response;
        System.out.println("INICIALIZATION");
    }

    public AsyncUpdate(List<Map<Integer,Object>> data,Context cont) {
        this.mData = data;
        mContext = cont;
    }

    @Override
    protected void onPreExecute() {
        /*dialog = new ProgressDialog(mContext);
        dialog.setMessage("Updating DB wait... Please wait...");
        dialog.show();*/
    }

    @SuppressWarnings("ThrowFromFinallyBlock")
    @Override
    protected Void doInBackground(String... proc_params) {
        System.out.println("doInBackground");
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = null;
            PreparedStatement prepared = null;
            try {
                con = DriverManager.getConnection(Referense.MSSQL_DB, Referense.MSSQL_LOGIN, Referense.MSSQL_PASS);
                if (con != null) {
                    prepared = con.prepareStatement(proc_params[0]);

                    for (Map<Integer,Object> item : mData) {

                        for (Map.Entry<Integer, Object> entry : item.entrySet())
                        {
                            prepared.setObject(entry.getKey(), entry.getValue());
                            System.out.println(entry.getKey() + "/" + entry.getValue());
                        }
                        prepared.addBatch();
                        prepared.executeUpdate();

                        System.out.println("addBatch");
                    }

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
        }

        System.out.println("doInBackground  END");
        return null;
    }

    protected void onCancelled() {
        //dialog.dismiss();
        Toast toast = Toast.makeText(mContext, "Error connecting to Server", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 25, 400);
        toast.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (response!=null) {
            response.processFinish(null);
        }
        //dialog.dismiss();
        super.onPostExecute(aVoid);
    }
}
