package com.example.cntt196_hotrodulichfirebase.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TraLoi implements Serializable {
    private String maCauTraLoi;
    private String maNguoiTraLoi;
    private String tenNguoiTraLoi;
    private String imgNguoiTraLoi;
    private String noiDungTraLoi;
    private LocalDateTime ngayTraLoi;

    public String getMaCauTraLoi() {
        return maCauTraLoi;
    }

    public String getMaNguoiTraLoi() {
        return maNguoiTraLoi;
    }

    public String getTenNguoiTraLoi() {
        return tenNguoiTraLoi;
    }

    public String getNoiDungTraLoi() {
        return noiDungTraLoi;
    }

    public LocalDateTime getNgayTraLoi() {
        return ngayTraLoi;
    }

    public String getImgNguoiTraLoi() {
        return imgNguoiTraLoi;
    }

    public void setImgNguoiTraLoi(String imgNguoiTraLoi) {
        this.imgNguoiTraLoi = imgNguoiTraLoi;
    }

    public void setMaCauTraLoi(String maCauTraLoi) {
        this.maCauTraLoi = maCauTraLoi;
    }

    public void setMaNguoiTraLoi(String maNguoiTraLoi) {
        this.maNguoiTraLoi = maNguoiTraLoi;
    }

    public void setTenNguoiTraLoi(String tenNguoiTraLoi) {
        this.tenNguoiTraLoi = tenNguoiTraLoi;
    }

    public void setNoiDungTraLoi(String noiDungTraLoi) {
        this.noiDungTraLoi = noiDungTraLoi;
    }

    public void setNgayTraLoi(LocalDateTime ngayTraLoi) {
        this.ngayTraLoi = ngayTraLoi;
    }
    public  TraLoi()
    {
        this.ngayTraLoi = LocalDateTime.now();
    }

    public TraLoi(String maCauTraLoi, String maNguoiTraLoi, String tenNguoiTraLoi,
                  String noiDungTraLoi, LocalDateTime ngayTraLoi, String imgNguoiTraLoi) {
        this.maCauTraLoi = maCauTraLoi;
        this.maNguoiTraLoi = maNguoiTraLoi;
        this.tenNguoiTraLoi = tenNguoiTraLoi;
        this.noiDungTraLoi = noiDungTraLoi;
        this.ngayTraLoi = ngayTraLoi;
        this.imgNguoiTraLoi=imgNguoiTraLoi;
    }
}
