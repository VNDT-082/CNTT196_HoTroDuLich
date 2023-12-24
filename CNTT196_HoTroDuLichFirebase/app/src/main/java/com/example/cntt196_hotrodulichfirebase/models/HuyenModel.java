package com.example.cntt196_hotrodulichfirebase.models;

import java.util.ArrayList;

public class HuyenModel {
    private String maHuyen;
    private String tenHuyen;
    private ArrayList<XaModel> Xas;

    public String getMaHuyen() {
        return maHuyen;
    }

    public void setMaHuyen(String maHuyen) {
        this.maHuyen = maHuyen;
    }

    public String getTenHuyen() {
        return tenHuyen;
    }

    public void setTenHuyen(String tenHuyen) {
        this.tenHuyen = tenHuyen;
    }

    public ArrayList<XaModel> getXas() {
        return Xas;
    }

    public void setXas(ArrayList<XaModel> xas) {
        Xas = xas;
    }

    public HuyenModel(){}
    public HuyenModel(String maHuyen, String tenHuyen) {
        this.maHuyen = maHuyen;
        this.tenHuyen = tenHuyen;
    }
}
