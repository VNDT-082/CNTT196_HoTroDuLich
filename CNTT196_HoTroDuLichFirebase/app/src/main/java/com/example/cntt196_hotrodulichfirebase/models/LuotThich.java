package com.example.cntt196_hotrodulichfirebase.models;

import java.io.Serializable;

public class LuotThich implements Serializable {
    private String maNguoiThich;
    private String tenNguoiThich;

    public String getMaNguoiThich() {
        return maNguoiThich;
    }

    public void setMaNguoiThich(String maNguoiThich) {
        this.maNguoiThich = maNguoiThich;
    }

    public String getTenNguoiThich() {
        return tenNguoiThich;
    }

    public void setTenNguoiThich(String tenNguoiThich) {
        this.tenNguoiThich = tenNguoiThich;
    }

    public LuotThich(String maNguoiThich, String tenNguoiThich) {
        this.maNguoiThich = maNguoiThich;
        this.tenNguoiThich = tenNguoiThich;
    }
}
