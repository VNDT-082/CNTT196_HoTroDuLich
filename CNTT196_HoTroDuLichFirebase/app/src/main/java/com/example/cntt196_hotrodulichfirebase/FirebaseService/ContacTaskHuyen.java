package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;

import com.example.cntt196_hotrodulichfirebase.adapters.AdapterHuyen;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTinh;
import com.example.cntt196_hotrodulichfirebase.models.HuyenModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ContacTaskHuyen  extends AsyncTask<Void,Void, ArrayList<HuyenModel>> {

    Spinner spinner;
    ArrayList<HuyenModel> DANHSACHHUYEN;
    AdapterHuyen AdapterHuyen;
    String code="1";
    Context context;
    public  ContacTaskHuyen(Spinner spinner, ArrayList<HuyenModel> DANHSACHHUYEN, Context context,String code)
    {
        this.context=context;
        this.spinner=spinner;
        this.DANHSACHHUYEN=DANHSACHHUYEN;
        this.code=code;
    }

    @Override
    protected ArrayList<HuyenModel> doInBackground(Void... voids) {
        ArrayList<HuyenModel> dsTinh = new ArrayList<>();
        try {
            String StrUrl="https://provinces.open-api.vn/api/p/"+code+"?depth=2";
            URL url = new URL(StrUrl);
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
            //JSONArray jsonArray=new JSONArray(stringBuilder.toString());
            JSONObject jsonObject= new JSONObject(stringBuilder.toString()); //jsonArray.getJSONObject(0);
            JSONArray jsonSubArray= jsonObject.getJSONArray("districts");
            for(int i=0;i<jsonSubArray.length();i++)
            {
                JSONObject jsonObjectItem =jsonSubArray.getJSONObject(i);
                HuyenModel huyenModel=new HuyenModel();
                huyenModel.setMaHuyen(jsonObjectItem.get("code").toString());
                huyenModel.setTenHuyen(jsonObjectItem.get("name").toString());
                dsTinh.add(huyenModel);
                DANHSACHHUYEN.add(huyenModel);
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
    protected void onPostExecute(ArrayList<HuyenModel> products) {
        super.onPostExecute(products);
        AdapterHuyen = new AdapterHuyen(context, DANHSACHHUYEN);
        spinner.setAdapter(AdapterHuyen);

    }
}
