package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cntt196_hotrodulichfirebase.MainActivity;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.Phong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HotelService {
    FirebaseFirestore firebaseFirestore;

    public HotelService() {
        firebaseFirestore= FirebaseFirestore.getInstance();
    }

    public Hotel GetOneByID(String IDHotel) {
        Hotel hotel=new Hotel();
        firebaseFirestore.collection("Hotel").document(IDHotel)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getId()!=null)
                        {
                            hotel.setID_Document(documentSnapshot.getId());
                            Map<String,Object> subDocument=(Map<String,Object>) documentSnapshot.get("NguoiDang");
                            Log.d("HotelNguoiDang"," => " +  subDocument);
                            if(subDocument!=null)
                            {
                                NguoiDang nguoiDang=new NguoiDang();
                                nguoiDang.setMaNguoiDang((String)subDocument.get("MaNguoiDang"));
                                nguoiDang.setTenNguoiDang((String)subDocument.get("TenNguoiDang"));
                                nguoiDang.setAnhDaiDien((String)subDocument.get("AnhDaiDien"));
                                hotel.setNguoiDang(nguoiDang);
                            }
                            hotel.setTenKhachSan(documentSnapshot.getString("TenKhachSan"));
                            hotel.setMoTa(documentSnapshot.getString("MoTa"));
                            hotel.setDanhGias(null);
                            hotel.setDiaChi(documentSnapshot.getString("DiaChi"));
                            hotel.setTrangThai(documentSnapshot.getBoolean("TrangThai"));
                            hotel.setHangSao((long)documentSnapshot.get("HangSao"));

                            hotel.setHinhAnhs((ArrayList<String>) documentSnapshot.get("HinhAnh"));

                            ArrayList<Phong> dsPhong=new ArrayList<>();
                            ArrayList<Map<String,Object>> subArrayDocument= (ArrayList<Map<String, Object>>) documentSnapshot.get("Phong");
                            if(subArrayDocument!=null)
                            {
                                Log.e("subArrayDocument","=>"+subArrayDocument);
                                for (Map<String,Object> objectMap:subArrayDocument)
                                {
                                    Phong phong=new Phong();
                                    Number numMax = (Number) objectMap.get("GiaMax");
                                    Number numMin = (Number) objectMap.get("GiaMin");
                                    Number numSoGiuong= (Number)objectMap.get("SoGiuong");
                                    phong.setGiaMax((long) Float.parseFloat(numMax.toString()));
                                    phong.setGiaMin((long) Float.parseFloat(numMin.toString()));
                                    phong.setSoGiuong((long) Float.parseFloat(numSoGiuong.toString()));
                                    phong.setHinhAnh((String) objectMap.get("HinhAnh"));
                                    dsPhong.add(phong);
                                }
                            }
                            hotel.setPhongs(dsPhong);


                            ArrayList<Map<String,Object>> subArrayDocumentDanhGia=
                                    (ArrayList<Map<String, Object>>) documentSnapshot.get("DanhGia");
                            if(subArrayDocumentDanhGia!=null)
                            {
                                ArrayList<DanhGia> dsDanhGia=new ArrayList<>();
                                for (Map<String,Object> objectMap:subArrayDocumentDanhGia)
                                {
                                    DanhGia danhGia=new DanhGia();
                                    danhGia.setMaNguoiDanhGia((String) objectMap.get("MaNguoiDanhGia"));
                                    //lay bien thoi gian kieu timestamp
                                    Timestamp DanhGiatimestamp= (Timestamp) objectMap.get("NgayDang");
                                    //convert sang localdatetime
                                    danhGia.setNgayDang(DanhGiatimestamp.toDate().toInstant()
                                            .atZone(ZoneId.systemDefault()).toLocalDateTime());

                                    danhGia.setTenNguoiDanhGia((String) objectMap.get("TenNguoiDanhGia"));
                                    danhGia.setRate((Long) objectMap.get("Rate"));
                                    Log.e("Rate","=>"+danhGia.getRate());
                                    danhGia.setNoiDung((String) objectMap.get("NoiDungDanhGia"));
                                    danhGia.setImgNguoiDang((String) objectMap.get("avartaNguoiDanhGia"));
                                    dsDanhGia.add(danhGia);
                                }
                                hotel.setDanhGias(dsDanhGia);
                            }


                            ArrayList<Map<String,Object>> subArrayDocumentLuotThich=
                                    (ArrayList<Map<String, Object>>) documentSnapshot.get("LuotThich");
                            if(subArrayDocumentLuotThich!=null)
                            {
                                ArrayList<NguoiDang> dsLuotThich=new ArrayList<>();
                                for (Map<String,Object> objectMap:subArrayDocumentLuotThich)
                                {
                                    dsLuotThich.add(new NguoiDang(null,(String) objectMap.get("MaNguoiThich")
                                            , (String) objectMap.get("TenNguoiThich")));
                                }
                                hotel.setLuotThichs(dsLuotThich);
                            }

                            Timestamp timestamp=documentSnapshot.getTimestamp("NgayDang");

                            hotel.setNgayDang(timestamp.toDate().toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DIAOLOGNhanXet", "onFailure: Loi load document");
                        hotel.setID_Document(null);
                    }
                });
        return hotel;

    }

    public static Hotel ReadHotelDocument(DocumentSnapshot document)
    {
        Hotel hotel=new Hotel();
        hotel.setID_Document(document.getId());
        Map<String,Object> subDocument=(Map<String,Object>) document.get("NguoiDang");
        Log.d("HotelNguoiDang"," => " +  subDocument);
        if(subDocument!=null)
        {
            NguoiDang nguoiDang=new NguoiDang();
            nguoiDang.setMaNguoiDang((String)subDocument.get("MaNguoiDang"));
            nguoiDang.setTenNguoiDang((String)subDocument.get("TenNguoiDang"));
            nguoiDang.setAnhDaiDien((String)subDocument.get("AnhDaiDien"));
            hotel.setNguoiDang(nguoiDang);
        }
        hotel.setTenKhachSan(document.getString("TenKhachSan"));
        hotel.setMoTa(document.getString("MoTa"));
        hotel.setDanhGias(null);
        hotel.setDiaChi(document.getString("DiaChi"));
        hotel.setTrangThai(document.getBoolean("TrangThai"));
        hotel.setHangSao((long)document.get("HangSao"));

        hotel.setHinhAnhs((ArrayList<String>) document.get("HinhAnh"));

        ArrayList<Phong> dsPhong=new ArrayList<>();
        ArrayList<Map<String,Object>> subArrayDocument= (ArrayList<Map<String, Object>>) document.get("Phong");
        if(subArrayDocument!=null)
        {
            if(subArrayDocument.size()>0)
            {
                Log.e("subArrayDocument","=>"+subArrayDocument);
                for (Map<String,Object> objectMap:subArrayDocument)
                {
                    Phong phong=new Phong();
                    Number numMax = (Number) objectMap.get("GiaMax");
                    Number numMin = (Number) objectMap.get("GiaMin");
                    Number numSoGiuong= (Number)objectMap.get("SoGiuong");
                    phong.setGiaMax((long) Float.parseFloat(numMax.toString()));
                    phong.setGiaMin((long) Float.parseFloat(numMin.toString()));
                    phong.setSoGiuong((long) Float.parseFloat(numSoGiuong.toString()));
                    phong.setHinhAnh((String) objectMap.get("HinhAnh"));
                    dsPhong.add(phong);
                }
            }
        }
        hotel.setPhongs(dsPhong);


        ArrayList<Map<String,Object>> subArrayDocumentDanhGia= (ArrayList<Map<String, Object>>) document.get("DanhGia");
        if(subArrayDocumentDanhGia!=null)
        {
            if(subArrayDocumentDanhGia.size()>0)
            {
                ArrayList<DanhGia> dsDanhGia=new ArrayList<>();
                for (Map<String,Object> objectMap:subArrayDocumentDanhGia)
                {
                    DanhGia danhGia=new DanhGia();
                    danhGia.setMaNguoiDanhGia((String) objectMap.get("MaNguoiDanhGia"));
                    //lay bien thoi gian kieu timestamp
                    Timestamp DanhGiatimestamp= (Timestamp) objectMap.get("NgayDang");
                    //convert sang localdatetime
                    danhGia.setNgayDang(DanhGiatimestamp.toDate().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime());

                    danhGia.setTenNguoiDanhGia((String) objectMap.get("TenNguoiDanhGia"));
                    danhGia.setRate((Long) objectMap.get("Rate"));
                    Log.e("Rate","=>"+danhGia.getRate());
                    danhGia.setNoiDung((String) objectMap.get("NoiDungDanhGia"));
                    danhGia.setImgNguoiDang((String) objectMap.get("avartaNguoiDanhGia"));
                    dsDanhGia.add(danhGia);
                }
                hotel.setDanhGias(dsDanhGia);
            }
        }

        ArrayList<Map<String,Object>> subArrayDocumentHoiDap=
                (ArrayList<Map<String, Object>>) document.get("HoiDap");
        if(subArrayDocumentHoiDap!=null)
        {
            if(subArrayDocumentHoiDap.size()>0)
            {
                ArrayList<HoiDap> dsHoiDap=new ArrayList<>();
                for (Map<String,Object> objectMap:subArrayDocumentHoiDap)
                {
                    HoiDap hoiDap=new HoiDap();
                    hoiDap.setMaHoiDap((String) objectMap.get("MaHoiDap"));
                    hoiDap.setMaNguoiHoi((String)objectMap.get("MaNguoiHoi"));
                    //lay bien thoi gian kieu timestamp
                    Timestamp DanhGiatimestamp= (Timestamp) objectMap.get("NgayHoi");
                    //convert sang localdatetime
                    hoiDap.setNgayHoi(DanhGiatimestamp.toDate().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime());

                    hoiDap.setTenNguoiHoi((String) objectMap.get("TenNguoiHoi"));
                    hoiDap.setNoiDungHoiDap((String) objectMap.get("NoiDungHoiDap"));
                    hoiDap.setImgNguoiHoi((String)objectMap.get("avartaNguoiHoi"));

                    ArrayList<Map<String,Object>> subArrayDocumentTraLoiHoiDap=
                            (ArrayList<Map<String, Object>>) objectMap.get("TraLoi");
                    if(subArrayDocumentTraLoiHoiDap!=null)
                    {
                        ArrayList<HoiDap> dsTraLoi=new ArrayList<>();
                        for (Map<String,Object> objectMapTraLoi : subArrayDocumentTraLoiHoiDap)
                        {
                            HoiDap traloi=new HoiDap();
                            traloi.setMaHoiDap((String) objectMapTraLoi.get("MaCauTraLoi"));
                            traloi.setMaNguoiHoi((String)objectMapTraLoi.get("MaNguoiTraLoi"));
                            //lay bien thoi gian kieu timestamp
                            Timestamp DanhGiatimestampTraLoi= (Timestamp) objectMapTraLoi.get("NgayTraLoi");
                            //convert sang localdatetime
                            traloi.setNgayHoi(DanhGiatimestampTraLoi.toDate().toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime());

                            traloi.setTenNguoiHoi((String) objectMapTraLoi.get("TenNguoiTraLoi"));
                            traloi.setImgNguoiHoi((String)objectMapTraLoi.get("avartaNguoiHoi"));
                            traloi.setNoiDungHoiDap((String) objectMapTraLoi.get("NoiDungTraLoi"));
                            dsTraLoi.add(traloi);
                        }
                        hoiDap.setTraLois(dsTraLoi);
                    }

                    dsHoiDap.add(hoiDap);
                }
                hotel.setHoiDaps(dsHoiDap);
            }
        }


        ArrayList<Map<String,Object>> subArrayDocumentLuotThich= (ArrayList<Map<String, Object>>) document.get("LuotThich");
        if(subArrayDocumentLuotThich!=null)
        {
            ArrayList<NguoiDang> dsLuotThich=new ArrayList<>();
            for (Map<String,Object> objectMap:subArrayDocumentLuotThich)
            {
                dsLuotThich.add(new NguoiDang(null,(String) objectMap.get("MaNguoiThich")
                        , (String) objectMap.get("TenNguoiThich")));
            }
            hotel.setLuotThichs(dsLuotThich);
        }

        Timestamp timestamp=document.getTimestamp("NgayDang");

        hotel.setNgayDang(timestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());

        return hotel;
    }
}
