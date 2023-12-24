package com.example.cntt196_hotrodulichfirebase.models;

public class BaiVietAdmin {
    private String img;
    private String tenNguoiDang;
    private String ngayGioDang;
    private String noiDungDang;

    public BaiVietAdmin()
    {

    }
    public BaiVietAdmin(String img, String tenNguoiDang, String ngayGioDang, String noiDungDang) {
        this.img = img;
        this.tenNguoiDang = tenNguoiDang;
        this.ngayGioDang = ngayGioDang;
        this.noiDungDang = noiDungDang;
    }

    public String getImg() {
        return img;
    }

    public String getTenNguoiDang() {
        return tenNguoiDang;
    }

    public String getNgayGioDang() {
        return ngayGioDang;
    }

    public String getNoiDungDang() {
        return noiDungDang;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTenNguoiDang(String tenNguoiDang) {
        this.tenNguoiDang = tenNguoiDang;
    }

    public void setNgayGioDang(String ngayGioDang) {
        this.ngayGioDang = ngayGioDang;
    }

    public void setNoiDungDang(String noiDungDang) {
        this.noiDungDang = noiDungDang;
    }
}
