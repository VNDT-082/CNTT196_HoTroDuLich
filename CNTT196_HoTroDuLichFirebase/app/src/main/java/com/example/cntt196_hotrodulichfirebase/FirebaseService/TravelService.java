package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.util.Log;

import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnh;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Map;

public class TravelService {
    public FirebaseFirestore firebaseFirestore;
    public TravelService()
    {
        firebaseFirestore=FirebaseFirestore.getInstance();
    }
    public void getListTravel(final OnDataLoadedListener listener)
    {

        firebaseFirestore.collection("Travel").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            ArrayList<Travel> dsTravel=new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Travel travel=new Travel();
                                travel.setID_Document(document.getId());
                                travel.setTieuDe(document.getString("TieuDe"));
                                travel.setMoTa(document.getString("MoTa"));
                                travel.setDanhGias(null);
                                travel.setDiaChi(document.getString("DiaChi"));

                                Number numMax = (Number) document.get("GiaMax");
                                Number numMin = (Number) document.get("GiaMin");
                                travel.setGiaMax((long) Float.parseFloat(numMax.toString()));
                                travel.setGiaMin((long)Float.parseFloat(numMin.toString()));

                                travel.setHinhAnhs((ArrayList<String>) document.get("HinhAnh"));

                                Timestamp timestamp=document.getTimestamp("NgayDang");

                                travel.setNgayDang(timestamp.toDate().toInstant()
                                        .atZone(ZoneId.systemDefault()).toLocalDateTime());
                                dsTravel.add(travel);
                                Log.d("TravelModel", document.getId() + " => " +  dsTravel.size());
                            }
                            listener.onDataLoaded(dsTravel);
                        }
                        else
                        {
                            listener.onDataLoadError(task.getException());
                        }
                    }
                });

        //return dsTravel;
    }

    public static Travel ReadTravelDocument(DocumentSnapshot document)
    {
        Travel travel=new Travel();
        travel.setID_Document(document.getId());
        Log.d("IdTravel"," => " +  travel.getID_Document());
        Map<String,Object> subDocument=(Map<String,Object>) document.get("NguoiDang");
        Log.d("TravelNguoiDang"," => " +  subDocument);
        if(subDocument!=null)
        {
            NguoiDang nguoiDang=new NguoiDang();
            nguoiDang.setMaNguoiDang((String)subDocument.get("MaNguoiDang"));
            nguoiDang.setTenNguoiDang((String)subDocument.get("TenNguoiDang"));
            nguoiDang.setAnhDaiDien((String)subDocument.get("AnhDaiDien"));
            travel.setNguoiDang(nguoiDang);
        }
        travel.setTieuDe(document.getString("TieuDe"));
        travel.setMoTa(document.getString("MoTa"));
        travel.setTrangThai(document.getBoolean("TrangThai"));

        ArrayList<Map<String,Object>> subArrayDocumentDanhGia= (ArrayList<Map<String, Object>>) document.get("DanhGia");
        if(subArrayDocumentDanhGia!=null) {
            if(subArrayDocumentDanhGia.size()>0)
            {
                Log.d("TravelNguoiDang"," => " +  subArrayDocumentDanhGia);
                ArrayList<DanhGia> dsDanhGia = new ArrayList<>();
                for (Map<String, Object> objectMap : subArrayDocumentDanhGia) {
                    DanhGia danhGia = new DanhGia();
                    danhGia.setMaNguoiDanhGia((String) objectMap.get("MaNguoiDanhGia"));
                    //lay bien thoi gian kieu timestamp
                    Timestamp DanhGiatimestamp = (Timestamp) objectMap.get("NgayDang");
                    //convert sang localdatetime
                    danhGia.setNgayDang(DanhGiatimestamp.toDate().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDateTime());

                    danhGia.setTenNguoiDanhGia((String) objectMap.get("TenNguoiDanhGia"));
                    danhGia.setRate((Long) objectMap.get("Rate"));
                    Log.e("Rate", "=>" + danhGia.getRate());
                    danhGia.setNoiDung((String) objectMap.get("NoiDungDanhGia"));
                    danhGia.setImgNguoiDang((String) objectMap.get("avartaNguoiDanhGia"));
                    dsDanhGia.add(danhGia);
                }
                travel.setDanhGias(dsDanhGia);
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
                travel.setHoiDaps(dsHoiDap);
            }

        }

        travel.setDiaChi(document.getString("DiaChi"));

        Number numMax = (Number) document.get("GiaMax");
        Number numMin = (Number) document.get("GiaMin");
        travel.setGiaMax((long) Float.parseFloat(numMax.toString()));
        travel.setGiaMin((long)Float.parseFloat(numMin.toString()));

        ArrayList<String> dsHinh=new ArrayList<>();
        dsHinh= (ArrayList<String>) document.get("HinhAnh");
        travel.setHinhAnhs(dsHinh);


        Timestamp timestamp=document.getTimestamp("NgayDang");

        travel.setNgayDang(timestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());

        return travel;
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(ArrayList<Travel> dataList);
        void onDataLoadError(Exception e);
    }
}
