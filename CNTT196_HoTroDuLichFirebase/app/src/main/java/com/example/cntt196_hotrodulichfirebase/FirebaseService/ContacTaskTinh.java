package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;

import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTinh;
import com.example.cntt196_hotrodulichfirebase.models.TinhModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;

public class ContacTaskTinh extends AsyncTask<Void,Void, ArrayList<TinhModel>> {


    Spinner spinner;
    ArrayList<TinhModel> DANHSACHTINH;
    com.example.cntt196_hotrodulichfirebase.adapters.AdapterTinh AdapterTinh;
    Context context;
    public  ContacTaskTinh(Spinner spinner, ArrayList<TinhModel> DANHSACHTINH, Context context)
    {
        this.context=context;
        this.spinner=spinner;
        this.DANHSACHTINH=DANHSACHTINH;
    }

    @Override
    protected ArrayList<TinhModel> doInBackground(Void... voids) {
        ArrayList<TinhModel> dsTinh = new ArrayList<>();
        try {
            URL url = new URL("https://provinces.open-api.vn/api/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");//.setRequestMethod("GET");
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null)
            {
                stringBuilder.append(line);
                line=bufferedReader.readLine();

            }
            inputStreamReader.close();
            JSONArray jsonArray=new JSONArray(stringBuilder.toString());
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                TinhModel tinh=new TinhModel();
                tinh.setMaTinh(jsonObject.get("code").toString());
                tinh.setTenTinh(jsonObject.get("name").toString());
                dsTinh.add(tinh);
                DANHSACHTINH.add(tinh);
                //Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            //return dsTinh;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return  dsTinh;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(ArrayList<TinhModel> products) {
        super.onPostExecute(products);
        AdapterTinh = new AdapterTinh(context, DANHSACHTINH);
        spinner.setAdapter(AdapterTinh);

    }
}
