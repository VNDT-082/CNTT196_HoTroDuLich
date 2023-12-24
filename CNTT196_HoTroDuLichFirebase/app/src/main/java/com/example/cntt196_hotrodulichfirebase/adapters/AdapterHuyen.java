package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.HuyenModel;
import com.example.cntt196_hotrodulichfirebase.models.TinhModel;

import java.util.ArrayList;

public class AdapterHuyen extends BaseAdapter {
    public Context context;
    public ArrayList<HuyenModel> DANHSACHHUYEN;
    public AdapterHuyen(Context context, ArrayList<HuyenModel> DANHSACHHUYEN)
    {
        this.context=context;
        this.DANHSACHHUYEN=DANHSACHHUYEN;
    }

    @Override
    public int getCount() {
        return DANHSACHHUYEN.size();
    }

    @Override
    public Object getItem(int position) {
        return DANHSACHHUYEN.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView= LayoutInflater.from(context).inflate(R.layout.custom_item_spiner_ver1, parent,false);
        TextView name=mView.findViewById(R.id.DisplayItemName_Spiner);
        name.setText(DANHSACHHUYEN.get(position).getTenHuyen());

//        mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return mView;
    }
}
