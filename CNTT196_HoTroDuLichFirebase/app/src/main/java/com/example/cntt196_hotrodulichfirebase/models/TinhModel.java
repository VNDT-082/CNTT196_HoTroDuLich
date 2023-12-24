package com.example.cntt196_hotrodulichfirebase.models;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TinhModel {
    private String maTinh;
    private String tenTinh;
    private ArrayList<HuyenModel> Huyens;

    public String getMaTinh() {
        return maTinh;
    }

    public void setMaTinh(String maTinh) {
        this.maTinh = maTinh;
    }

    public String getTenTinh() {
        return tenTinh;
    }

    public void setTenTinh(String tenTinh) {
        this.tenTinh = tenTinh;
    }

    public ArrayList<HuyenModel> getHuyens() {
        return Huyens;
    }

    public void setHuyens(ArrayList<HuyenModel> huyens) {
        Huyens = huyens;
    }

    public TinhModel(){}
    public TinhModel(String maTinh, String tenTinh) {
        this.maTinh = maTinh;
        this.tenTinh = tenTinh;
    }
}
