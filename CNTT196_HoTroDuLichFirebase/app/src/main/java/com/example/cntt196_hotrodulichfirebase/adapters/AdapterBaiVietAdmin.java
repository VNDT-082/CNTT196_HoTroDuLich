package com.example.cntt196_hotrodulichfirebase.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.BaiVietAdmin;

import java.util.ArrayList;

public class AdapterBaiVietAdmin extends ArrayAdapter<BaiVietAdmin> {
    Activity context;
    int layout;
    ArrayList<BaiVietAdmin> lstBaiVietAdmin;

    public AdapterBaiVietAdmin(Activity context, int layout, ArrayList<BaiVietAdmin> lstBaiVietAdmin) {
        super(context, layout, lstBaiVietAdmin);
        this.context = context;
        this.layout = layout;
        this.lstBaiVietAdmin = lstBaiVietAdmin;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myFlat = context.getLayoutInflater();
        convertView = myFlat.inflate(layout, null);
        BaiVietAdmin baiViet = lstBaiVietAdmin.get(position);
        ImageView imgViewAvatar = convertView.findViewById(R.id.imgViewAvatar);
//        imgViewAvatar.setImageResource();
        TextView tvTenUser = convertView.findViewById(R.id.tvTenUser);
        tvTenUser.setText(baiViet.getTenNguoiDang());
        TextView tvDateTime = convertView.findViewById(R.id.tvDateTime);
        tvDateTime.setText(baiViet.getNgayGioDang());
        TextView tvTieuDe = convertView.findViewById(R.id.tvTieuDe);
        tvTieuDe.setText(baiViet.getNoiDungDang());
        return  convertView;
    }
}
