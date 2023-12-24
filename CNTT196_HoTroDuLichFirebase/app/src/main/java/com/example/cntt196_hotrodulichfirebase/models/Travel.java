package com.example.cntt196_hotrodulichfirebase.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Travel implements Serializable {
    private String ID_Document;
    private String tieuDe;
    private String moTa;
    private String diaChi;
    private long giaMax;
    private long giaMin;
    private boolean trangThai;
    private NguoiDang nguoiDang;
    private LocalDateTime ngayDang;

    private ArrayList<String> hinhAnhs;
    private ArrayList<HoiDap> hoiDaps;
    private ArrayList<DanhGia> danhGias;
    private ArrayList<NguoiDang> luotThichs;

    public ArrayList<NguoiDang> getLuotThichs() {
        return luotThichs;
    }

    public void setLuotThichs(ArrayList<NguoiDang> luotThichs) {
        this.luotThichs = luotThichs;
    }

    public String getID_Document() {
        return ID_Document;
    }

    public void setID_Document(String ID_Document) {
        this.ID_Document = ID_Document;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public long getGiaMax() {
        return giaMax;
    }

    public void setGiaMax(long giaMax) {
        this.giaMax = giaMax;
    }

    public long getGiaMin() {
        return giaMin;
    }

    public void setGiaMin(long giaMin) {
        this.giaMin = giaMin;
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

    public LocalDateTime getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(LocalDateTime ngayDang) {
        this.ngayDang = ngayDang;
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

    public Travel()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        this.ID_Document = LocalDateTime.now().format(formatter).toString();
        this.ngayDang=LocalDateTime.now();

    }
    public Travel(String ID_Document, String tieuDe, String moTa, String diaChi, long giaMax, long giaMin,
                  boolean trangThai, NguoiDang nguoiDang, LocalDateTime ngayDang) {
        this.ID_Document = ID_Document;
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.diaChi = diaChi;
        this.giaMax = giaMax;
        this.giaMin = giaMin;
        this.trangThai = trangThai;
        this.nguoiDang = nguoiDang;
        this.ngayDang = ngayDang;
    }
}
