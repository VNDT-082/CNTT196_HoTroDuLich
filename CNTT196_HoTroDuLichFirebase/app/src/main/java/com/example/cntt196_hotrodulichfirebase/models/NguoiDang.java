package com.example.cntt196_hotrodulichfirebase.models;

import java.io.Serializable;

public class NguoiDang implements Serializable {
    private String anhDaiDien;
    private String maNguoiDang;
    private String tenNguoiDang;

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public String getMaNguoiDang() {
        return maNguoiDang;
    }

    public String getTenNguoiDang() {
        return tenNguoiDang;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public void setMaNguoiDang(String maNguoiDang) {
        this.maNguoiDang = maNguoiDang;
    }

    public void setTenNguoiDang(String tenNguoiDang) {
        this.tenNguoiDang = tenNguoiDang;
    }

    public NguoiDang(){}
    public NguoiDang(String anhDaiDien, String maNguoiDang, String tenNguoiDang) {
        this.anhDaiDien = anhDaiDien;
        this.maNguoiDang = maNguoiDang;
        this.tenNguoiDang = tenNguoiDang;
    }
}
