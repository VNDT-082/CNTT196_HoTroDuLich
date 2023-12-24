package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.TinhModel;

import java.util.ArrayList;

public class AdapterTinh extends BaseAdapter {
    public Context context;
    public ArrayList<TinhModel> DANHSACHTINH;
    public AdapterTinh(Context context, ArrayList<TinhModel> DANHSACHTINH)
    {
        this.context=context;
        this.DANHSACHTINH=DANHSACHTINH;
    }

    @Override
    public int getCount() {
        return DANHSACHTINH.size();
    }

    @Override
    public Object getItem(int position) {
        return DANHSACHTINH.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView= LayoutInflater.from(context).inflate(R.layout.custom_item_spiner_ver1, parent,false);
        TextView name=mView.findViewById(R.id.DisplayItemName_Spiner);
        name.setText(DANHSACHTINH.get(position).getTenTinh());
        return mView;
    }
}
