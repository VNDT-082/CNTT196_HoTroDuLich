package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Spinner;

import com.example.cntt196_hotrodulichfirebase.adapters.AdapterXa;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTinh;
import com.example.cntt196_hotrodulichfirebase.models.XaModel;

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

public class ContacTaskXa  extends AsyncTask<Void,Void, ArrayList<XaModel>> {

    Spinner spinner;
    ArrayList<XaModel> DANHSACHXA;
    AdapterXa AdapterXa;
    String code="1";
    Context context;
    public  ContacTaskXa(Spinner spinner, ArrayList<XaModel> DANHSACHXA, Context context,String code)
    {
        this.context=context;
        this.spinner=spinner;
        this.DANHSACHXA=DANHSACHXA;
        this.code=code;
    }

    @Override
    protected ArrayList<XaModel> doInBackground(Void... voids) {
        ArrayList<XaModel> dsTinh = new ArrayList<>();
        try {
            String StrUrl="https://provinces.open-api.vn/api/d/"+code+"?depth=2";
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
            JSONObject jsonObject= new JSONObject(stringBuilder.toString());
            JSONArray jsonSubArray= jsonObject.getJSONArray("wards");
            for(int i=0;i<jsonSubArray.length();i++)
            {
                JSONObject jsonObjectItem =jsonSubArray.getJSONObject(i);
                XaModel XaModel=new XaModel();
                XaModel.setMaXa(jsonObjectItem.get("code").toString());
                XaModel.setTenXa(jsonObjectItem.get("name").toString());
                dsTinh.add(XaModel);
                DANHSACHXA.add(XaModel);
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
    protected void onPostExecute(ArrayList<XaModel> products) {
        super.onPostExecute(products);
        AdapterXa = new AdapterXa(context, DANHSACHXA);
        spinner.setAdapter(AdapterXa);

    }
}
