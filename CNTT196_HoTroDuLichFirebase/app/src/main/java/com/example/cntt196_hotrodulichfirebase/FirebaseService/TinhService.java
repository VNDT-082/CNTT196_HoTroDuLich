package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import com.example.cntt196_hotrodulichfirebase.models.TinhModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class TinhService {
    public static int IsMucDoUuTienTinh(String tenTinh)
    {
        //https://dulich.laodong.vn/tin-tuc/top-10-tinh-thanh-don-khach-du-lich-sau-9-thang-dau-nam-1104052.html
        //link tham khao
        ArrayList<String> dsTinhLoai1 = new ArrayList<>();
        dsTinhLoai1.add("Thành phố Hà Nội");
        dsTinhLoai1.add("Thành phố Hồ Chí Minh");
        dsTinhLoai1.add("Thành phố Cần Thơ");

        ArrayList<String> dsTinhLoai2 = new ArrayList<>();
        dsTinhLoai2.add("Tỉnh Thanh Hóa");
        dsTinhLoai2.add("Tỉnh Quảng Ninh");
        dsTinhLoai2.add("Tỉnh An Giang");
        dsTinhLoai2.add("Tỉnh Kiên Giang");
        dsTinhLoai2.add("Thành phố Hải Phòng");
        dsTinhLoai2.add("Tỉnh Nghệ An");
        dsTinhLoai2.add("Tỉnh Bình Thuận");
        dsTinhLoai2.add("Tỉnh Lào Cai");
        for (String strTinh :dsTinhLoai1)
        {
            if(strTinh.contains(tenTinh))
            {
                return 1;
            }
        }
        for (String strTinh :dsTinhLoai2)
        {
            if(strTinh.contains(tenTinh))
            {
                return 2;
            }
        }
        return 3;
    }
    public static ArrayList<String> SapXepDanhSach(ArrayList<Map<String,Object>> dsMapTinh)
    {
        Collections.sort(dsMapTinh, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Integer x1= (Integer) o1.get("Loai");
                Integer x2= (Integer) o2.get("Loai");
                return x1.compareTo(x2);
            }
        });
        ArrayList<String> ds=new ArrayList<>();
        ds.add("Tất cả");
        for(Map<String,Object> map:dsMapTinh)
        {
            ds.add(map.get("TenTinh").toString());
        }
        return ds;
    }
    public static ArrayList<String> getAllTinh()
    {
        ArrayList<String> dsTinh = new ArrayList<>();
        dsTinh.add("Thành phố Hà Nội");
        dsTinh.add("Thành phố Hồ Chí Minh");
        dsTinh.add("Tỉnh Hà Giang");
        dsTinh.add("Tỉnh Cao Bằng");
        dsTinh.add("Tỉnh Bắc Kạn");
        dsTinh.add("Tỉnh Tuyên Quang");
        dsTinh.add("Tỉnh Lào Cai");
        dsTinh.add("Tỉnh Điện Biên");
        dsTinh.add("Tỉnh Lai Châu");
        dsTinh.add("Tỉnh Sơn La");
        dsTinh.add("Tỉnh Yên Bái");
        dsTinh.add("Tỉnh Hòa Bình");
        dsTinh.add("Tỉnh Thái Nguyên");
        dsTinh.add("Tỉnh Lạng Sơn");
        dsTinh.add("Tỉnh Quảng Ninh");
        dsTinh.add("Tỉnh Bắc Giang");
        dsTinh.add("Tỉnh Phú Thọ");
        dsTinh.add("Tỉnh Vĩnh Phúc");
        dsTinh.add("Tỉnh Bắc Ninh");
        dsTinh.add("Tỉnh Hải Dương");
        dsTinh.add("Thành phố Hải Phòng");
        dsTinh.add("Tỉnh Hưng Yên");
        dsTinh.add("Tỉnh Thái Bình");
        dsTinh.add("Tỉnh Hà Nam");
        dsTinh.add("Tỉnh Nam Định");
        dsTinh.add("Tỉnh Ninh Bình");
        dsTinh.add("Tỉnh Thanh Hóa");
        dsTinh.add("Tỉnh Nghệ An");
        dsTinh.add("Tỉnh Hà Tĩnh");
        dsTinh.add("Tỉnh Quảng Bình");
        dsTinh.add("Tỉnh Quảng Trị");
        dsTinh.add("Tỉnh Thừa Thiên Huế");
        dsTinh.add("Thành phố Đà Nẵng");
        dsTinh.add("Tỉnh Quảng Nam");
        dsTinh.add("Tỉnh Quảng Ngãi");
        dsTinh.add("Tỉnh Bình Định");
        dsTinh.add("Tỉnh Phú Yên");
        dsTinh.add("Tỉnh Khánh Hòa");
        dsTinh.add("Tỉnh Ninh Thuận");
        dsTinh.add("Tỉnh Bình Thuận");
        dsTinh.add("Tỉnh Kon Tum");
        dsTinh.add("Tỉnh Gia Lai");
        dsTinh.add("Tỉnh Đắk Lắk");
        dsTinh.add("Tỉnh Đắk Nông");
        dsTinh.add("Tỉnh Lâm Đồng");
        dsTinh.add("Tỉnh Bình Phước");
        dsTinh.add("Tỉnh Tây Ninh");
        dsTinh.add("Tỉnh Bình Dương");
        dsTinh.add("Tỉnh Đồng Nai");
        dsTinh.add("Tỉnh Bà Rịa - Vũng Tàu");
        dsTinh.add("Tỉnh Long An");
        dsTinh.add("Tỉnh Tiền Giang");
        dsTinh.add("Tỉnh Bến Tre");
        dsTinh.add("Tỉnh Trà Vinh");
        dsTinh.add("Tỉnh Vĩnh Long");
        dsTinh.add("Tỉnh Đồng Tháp");
        dsTinh.add("Tỉnh An Giang");
        dsTinh.add("Tỉnh Kiên Giang");
        dsTinh.add("Thành phố Cần Thơ");
        dsTinh.add("Tỉnh Hậu Giang");
        dsTinh.add("Tỉnh Sóc Trăng");
        dsTinh.add("Tỉnh Bạc Liêu");
        dsTinh.add("Tỉnh Cà Mau");

        return  dsTinh;
    }
}
