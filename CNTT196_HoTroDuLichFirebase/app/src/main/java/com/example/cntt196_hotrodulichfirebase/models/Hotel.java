package com.example.cntt196_hotrodulichfirebase.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Hotel implements Serializable {
    private String ID_Document;
    private String tenKhachSan;
    private long hangSao;
    private String moTa;
    private LocalDateTime ngayDang;
    private String diaChi;
    private boolean trangThai;
    private NguoiDang nguoiDang;
    private ArrayList<Phong> phongs;

    private ArrayList<String> hinhAnhs;
    private ArrayList<HoiDap> hoiDaps;
    private ArrayList<DanhGia> danhGias;
    private ArrayList<NguoiDang> luotThichs;

    public String getID_Document() {
        return ID_Document;
    }

    public void setID_Document(String ID_Document) {
        this.ID_Document = ID_Document;
    }

    public String getTenKhachSan() {
        return tenKhachSan;
    }

    public void setTenKhachSan(String tenKhachSan) {
        this.tenKhachSan = tenKhachSan;
    }

    public long getHangSao() {
        return hangSao;
    }

    public void setHangSao(long hangSao) {
        this.hangSao = hangSao;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public LocalDateTime getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(LocalDateTime ngayDang) {
        this.ngayDang = ngayDang;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public NguoiDang getNguoiDang() {
        return nguoiDang;
    }

    public void setNguoiDang(NguoiDang nguoiDang) {
        this.nguoiDang = nguoiDang;
    }

    public ArrayList<Phong> getPhongs() {
        return phongs;
    }

    public void setPhongs(ArrayList<Phong> phongs) {
        this.phongs = phongs;
    }

    public ArrayList<String> getHinhAnhs() {
        return hinhAnhs;
    }

    public void setHinhAnhs(ArrayList<String> hinhAnhs) {
        this.hinhAnhs = hinhAnhs;
    }

    public ArrayList<HoiDap> getHoiDaps() {
        return hoiDaps;
    }

    public void setHoiDaps(ArrayList<HoiDap> hoiDaps) {
        this.hoiDaps = hoiDaps;
    }

    public ArrayList<DanhGia> getDanhGias() {
        return danhGias;
    }

    public void setDanhGias(ArrayList<DanhGia> danhGias) {
        this.danhGias = danhGias;
    }

    public ArrayList<NguoiDang> getLuotThichs() {
        return luotThichs;
    }

    public void setLuotThichs(ArrayList<NguoiDang> luotThichs) {
        this.luotThichs = luotThichs;
    }

    public Hotel(){}
    public Hotel(String ID_Document, String tenKhachSan, int hangSao, String moTa, LocalDateTime ngayDang,
                 String diaChi, boolean trangThai, NguoiDang nguoiDang) {
        this.ID_Document = ID_Document;
        this.tenKhachSan = tenKhachSan;
        this.hangSao = hangSao;
        this.moTa = moTa;
        this.ngayDang = ngayDang;
        this.diaChi = diaChi;
        this.trangThai = trangThai;
        this.nguoiDang = nguoiDang;
    }
}
