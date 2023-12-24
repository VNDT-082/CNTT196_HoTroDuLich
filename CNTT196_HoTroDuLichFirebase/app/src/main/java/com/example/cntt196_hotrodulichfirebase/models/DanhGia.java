package com.example.cntt196_hotrodulichfirebase.models;

import android.view.LayoutInflater;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DanhGia implements Serializable {
    private String maNguoiDanhGia;
    private LocalDateTime ngayDang;
    private String noiDung;
    private long rate;
    private String tenNguoiDanhGia;
    private String imgNguoiDang;

    public String getImgNguoiDang() {
        return imgNguoiDang;
    }

    public void setImgNguoiDang(String imgNguoiDang) {
        this.imgNguoiDang = imgNguoiDang;
    }

    public String getMaNguoiDanhGia() {
        return maNguoiDanhGia;
    }

    public void setMaNguoiDanhGia(String maNguoiDanhGia) {
        this.maNguoiDanhGia = maNguoiDanhGia;
    }

    public LocalDateTime getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(LocalDateTime ngayDang) {
        this.ngayDang = ngayDang;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public String getTenNguoiDanhGia() {
        return tenNguoiDanhGia;
    }

    public void setTenNguoiDanhGia(String tenNguoiDanhGia) {
        this.tenNguoiDanhGia = tenNguoiDanhGia;
    }

    public DanhGia(){}
    public DanhGia(String maNguoiDanhGia, LocalDateTime ngayDang, String noiDung, long rate, String tenNguoiDanhGia,
    String imgNguoiDang) {
        this.maNguoiDanhGia = maNguoiDanhGia;
        this.ngayDang = ngayDang;
        this.noiDung = noiDung;
        this.rate = rate;
        this.tenNguoiDanhGia = tenNguoiDanhGia;
        this.imgNguoiDang=imgNguoiDang;
    }
}
