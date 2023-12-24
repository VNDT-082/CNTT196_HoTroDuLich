package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.HuyenModel;
import com.example.cntt196_hotrodulichfirebase.models.XaModel;

import java.util.ArrayList;

public class AdapterXa extends BaseAdapter {
    public Context context;
    public ArrayList<XaModel> DANHSACHXA;
    public AdapterXa(Context context, ArrayList<XaModel> DANHSACHXA)
    {
        this.context=context;
        this.DANHSACHXA=DANHSACHXA;
    }

    @Override
    public int getCount() {
        return DANHSACHXA.size();
    }

    @Override
    public Object getItem(int position) {
        return DANHSACHXA.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView= LayoutInflater.from(context).inflate(R.layout.custom_item_spiner_ver1, parent,false);
        TextView name=mView.findViewById(R.id.DisplayItemName_Spiner);
        name.setText(DANHSACHXA.get(position).getTenXa());
        return mView;
    }
}
