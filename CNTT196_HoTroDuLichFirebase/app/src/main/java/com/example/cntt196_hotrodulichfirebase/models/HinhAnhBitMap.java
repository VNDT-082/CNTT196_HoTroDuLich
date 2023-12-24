package com.example.cntt196_hotrodulichfirebase.models;

import android.graphics.Bitmap;
import android.net.Uri;

public class HinhAnhBitMap {
    private Uri uri;
    private String tenHinhAnh;
    private Bitmap bitmap;

    public Uri getUri() {
        return uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getTenHinhAnh() {
        return tenHinhAnh;
    }

    public void setTenHinhAnh(String tenHinhAnh) {
        this.tenHinhAnh = tenHinhAnh;
    }

    public HinhAnhBitMap(){}
    public HinhAnhBitMap(Uri uri, String tenHinhAnh,Bitmap bitmap) {
        this.uri = uri;
        this.tenHinhAnh = tenHinhAnh;
        this.bitmap=bitmap;
    }
}
