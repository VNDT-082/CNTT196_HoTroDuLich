package com.example.cntt196_hotrodulichfirebase.models;

public class XaModel {
    private String maXa;
    private String tenXa;

    public String getMaXa() {
        return maXa;
    }

    public void setMaXa(String maXa) {
        this.maXa = maXa;
    }

    public String getTenXa() {
        return tenXa;
    }

    public void setTenXa(String tenXa) {
        this.tenXa = tenXa;
    }

    public XaModel(){}


    public XaModel(String maXa, String tenXa) {
        this.maXa = maXa;
        this.tenXa = tenXa;
    }
}
