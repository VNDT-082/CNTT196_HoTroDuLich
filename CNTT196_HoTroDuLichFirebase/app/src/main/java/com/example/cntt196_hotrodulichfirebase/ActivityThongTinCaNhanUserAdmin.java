package com.example.cntt196_hotrodulichfirebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.UserService;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver2_bitmap;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnhBitMap;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityThongTinCaNhanUserAdmin extends AppCompatActivity {

    private TextInputLayout edtHoVaTen_thong_tin_ca_nhan_user_admin
            ,edtEmail_thong_tin_ca_nhan_user_admin, tvNgaySinh_thong_tin_ca_nhan_user_admin;
    private Button btnDuyet_thong_tin_ca_nhan_user_admin,btnXoa_thong_tin_ca_nhan_user_admin
            ,buttonChonAnh_thong_tin_ca_nhan_user_admin, buttonCapNhatAnh_thong_tin_ca_nhan_user_admin;
    private ImageButton btnBack_thong_tin_ca_nhan_user_admin, imageButtonEdit_thong_tin_ca_nhan_user_admin;
    private ImageView img_thong_tin_ca_nhan_user_admin;
    private RadioButton radioNam, radioNu, radioTrangthaiTrue, radioQuyenFalse
            ,radioQuyenTrue , radioTrangThaiFalse;

    private void Init()
    {
        tvNgaySinh_thong_tin_ca_nhan_user_admin=findViewById(R.id.tvNgaySinh_thong_tin_ca_nhan_user_admin);
        radioTrangthaiTrue=findViewById(R.id.radioTrangthaiTrue);
        radioQuyenFalse=findViewById(R.id.radioQuyenFalse);
        radioQuyenTrue=findViewById(R.id.radioQuyenTrue);
        radioTrangThaiFalse=findViewById(R.id.radioTrangThaiFalse);
        edtHoVaTen_thong_tin_ca_nhan_user_admin=findViewById(R.id.edtHoVaTen_thong_tin_ca_nhan_user_admin);
        edtEmail_thong_tin_ca_nhan_user_admin=findViewById(R.id.edtEmail_thong_tin_ca_nhan_user_admin);

        btnDuyet_thong_tin_ca_nhan_user_admin=findViewById(R.id.btnDuyet_thong_tin_ca_nhan_user_admin);
        btnXoa_thong_tin_ca_nhan_user_admin=findViewById(R.id.btnXoa_thong_tin_ca_nhan_user_admin);
        buttonChonAnh_thong_tin_ca_nhan_user_admin=findViewById(R.id.buttonChonAnh_thong_tin_ca_nhan_user_admin);
        buttonCapNhatAnh_thong_tin_ca_nhan_user_admin=findViewById(R.id.buttonCapNhatAnh_thong_tin_ca_nhan_user_admin);
        btnBack_thong_tin_ca_nhan_user_admin=findViewById(R.id.btnBack_thong_tin_ca_nhan_user_admin);
        img_thong_tin_ca_nhan_user_admin=findViewById(R.id.img_thong_tin_ca_nhan_user_admin);
        imageButtonEdit_thong_tin_ca_nhan_user_admin=findViewById(R.id.imageButtonEdit_thong_tin_ca_nhan_user_admin);
        radioNam=findViewById(R.id.radioNam);
        radioNu=findViewById(R.id.radioNu);
        tvNgaySinh_thong_tin_ca_nhan_user_admin.setEnabled(false);
        edtHoVaTen_thong_tin_ca_nhan_user_admin.setEnabled(false);
        edtEmail_thong_tin_ca_nhan_user_admin.setEnabled(false);
        edtHoVaTen_thong_tin_ca_nhan_user_admin.setEnabled(false);
        radioNu.setEnabled(false);
        radioNam.setEnabled(false);


    }


    private User_ user_;
    private HinhAnhBitMap hinhAnhBitMap;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan_user_admin);
        Init();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            user_=(User_) bundle.getSerializable("User");
            SetValueToControl();
            if(!user_.isAuthor())
            {
                buttonChonAnh_thong_tin_ca_nhan_user_admin.setVisibility(View.INVISIBLE);
                buttonCapNhatAnh_thong_tin_ca_nhan_user_admin.setVisibility(View.INVISIBLE);
                imageButtonEdit_thong_tin_ca_nhan_user_admin.setEnabled(false);
            }
        }
        else
        {
            finish();
        }
        btnBack_thong_tin_ca_nhan_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                Intent data=result.getData();
                                Uri uriData= data.getData();

                                try {
                                    Bitmap bitmapImage= MediaStore.Images.Media.getBitmap(getContentResolver(),uriData);
                                    img_thong_tin_ca_nhan_user_admin.setImageBitmap(bitmapImage);
                                    hinhAnhBitMap=new HinhAnhBitMap();
                                    hinhAnhBitMap.setBitmap(bitmapImage);
                                    hinhAnhBitMap.setUri(uriData);
                                    hinhAnhBitMap.setTenHinhAnh("image_"+ DateTimeToString.GenarateID() +".jpg");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            Toast.makeText(ActivityThongTinCaNhanUserAdmin.this, "Chua chon hinh anh",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        buttonChonAnh_thong_tin_ca_nhan_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);


            }
        });
        imageButtonEdit_thong_tin_ca_nhan_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtHoVaTen_thong_tin_ca_nhan_user_admin.setEnabled(true);
            }
        });
        buttonCapNhatAnh_thong_tin_ca_nhan_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                                        .update("avarta", hinhAnhBitMap.getTenHinhAnh())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                String anhDaiDien =user_.getAvarta();
                                                String maNguoiDang=user_.getIdentifier();
                                                String tenNguoiDang=user_.getFullName();
                                                DialogMessage.ThongBao("Đã cập nhật hinh ảnh",
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                                String folderPath="avarta/";
                                                ArrayList<HinhAnhBitMap> dsAnh=new ArrayList<>();
                                                dsAnh.add(hinhAnhBitMap);
                                                StorageService.UploadImage(folderPath,dsAnh,ActivityThongTinCaNhanUserAdmin.this);
                                                SetValueToControl();
                                                firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                User_ userUpdate= UserService.ReadUserDocument(documentSnapshot);
                                                                UpdateBaiViet(anhDaiDien,maNguoiDang,tenNguoiDang, userUpdate.getAvarta(),
                                                                        userUpdate.getFullName());
                                                                //MainActivity.USER_=currentUser;
                                                            }
                                                        });
                                                //SetValueToControl();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Cập nhật hinh ảnh thất bại",
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogMessage.ThongBao("Lỗi không tìm thấy",
                                        ActivityThongTinCaNhanUserAdmin.this);
                            }
                        });
            }
        });
        btnDuyet_thong_tin_ca_nhan_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtHoVaTen_thong_tin_ca_nhan_user_admin.getEditText().getText().equals("")&&
                        !edtHoVaTen_thong_tin_ca_nhan_user_admin.getEditText().getText().equals(user_.getFullName()))
                {
                    firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                                            .update("fullName", edtHoVaTen_thong_tin_ca_nhan_user_admin.
                                                    getEditText().getText().toString().trim())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    String anhDaiDien =user_.getAvarta();
                                                    String maNguoiDang=user_.getIdentifier();
                                                    String tenNguoiDang=user_.getFullName();
                                                    DialogMessage.ThongBao("Đã cập thông tin",
                                                            ActivityThongTinCaNhanUserAdmin.this);
                                                    SetValueToControl();

                                                    firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    User_ userUpdate= UserService.ReadUserDocument(documentSnapshot);
                                                                    UpdateBaiViet(anhDaiDien,maNguoiDang,tenNguoiDang, userUpdate.getAvarta(),
                                                                            userUpdate.getFullName());
                                                                    //MainActivity.USER_=currentUser;
                                                                }
                                                            });



                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    DialogMessage.ThongBao("Cập nhật hinh ảnh thất bại",
                                                            ActivityThongTinCaNhanUserAdmin.this);
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    DialogMessage.ThongBao("Lỗi không tìm thấy",
                                            ActivityThongTinCaNhanUserAdmin.this);
                                }
                            });
                }
                if(user_.isActive())
                {
                    if(!radioTrangthaiTrue.isChecked())
                    {
                        UpdateTrangThai("active",false);
                    }
                }
                else
                {
                    if(radioTrangthaiTrue.isChecked())
                    {
                        UpdateTrangThai("active",true);
                    }
                }
                if(user_.isAuthor())
                {
                    if(!radioQuyenTrue.isChecked())
                    {
                        UpdateTrangThai("author",false);
                    }
                }
                else
                {
                    if(radioQuyenTrue.isChecked())
                    {
                        UpdateTrangThai("author",true);
                    }
                }

            }
        });
    }
    private void UpdateBaiViet(String anhDaiDien,String maNguoiDang,String tenNguoiDang,String tenAnhThayThe, String tenNguoiDungNew) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String,Object> NguoiDang= new HashMap<>();
        NguoiDang.put("AnhDaiDien",anhDaiDien);
        NguoiDang.put("MaNguoiDang",maNguoiDang);
        NguoiDang.put("TenNguoiDang",tenNguoiDang);
        firebaseFirestore.collection("Travel")//.whereEqualTo("NguoiDang",NguoiDang)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document:queryDocumentSnapshots)
                        {
                            Travel travel = TravelService.ReadTravelDocument(document);
                            if(travel.getNguoiDang().getMaNguoiDang().equals(user_.getIdentifier()))
                            {
                                Map<String,Object> NguoiDangNew= new HashMap<>();
                                NguoiDangNew.put("AnhDaiDien",tenAnhThayThe);
                                NguoiDangNew.put("MaNguoiDang",maNguoiDang);
                                NguoiDangNew.put("TenNguoiDang",tenNguoiDungNew);
                                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                                        .update("NguoiDang",NguoiDangNew)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Loi cập nhật bài viết"+travel.getTieuDe(),
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                            }
                                        });
                            }

                            if(travel.getDanhGias()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsNhapXet=new ArrayList<>();
                                for (DanhGia danhGia:travel.getDanhGias())
                                {
                                    if(danhGia.getMaNguoiDanhGia().equals(user_.getIdentifier()))
                                    {
                                        Map<String, Object> mapDanhGia=new HashMap<>();
                                        mapDanhGia.put("MaNguoiDanhGia",user_.getIdentifier());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, danhGia.getNgayDang().getYear());
                                        calendar.set(Calendar.MONTH,danhGia.getNgayDang().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,danhGia.getNgayDang().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, danhGia.getNgayDang().getHour());
                                        calendar.set(Calendar.MINUTE,danhGia.getNgayDang().getMinute());
                                        calendar.set(Calendar.SECOND,danhGia.getNgayDang().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapDanhGia.put("NgayDang",ngayDang);
                                        mapDanhGia.put("Rate",danhGia.getRate());
                                        mapDanhGia.put("TenNguoiDanhGia",user_.getFullName());
                                        mapDanhGia.put("avartaNguoiDanhGia",user_.getAvarta());
                                        mapDsNhapXet.add(mapDanhGia);
                                    }
                                    else {
                                        Map<String, Object> mapDanhGia=new HashMap<>();
                                        mapDanhGia.put("MaNguoiDanhGia",danhGia.getMaNguoiDanhGia());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, danhGia.getNgayDang().getYear());
                                        calendar.set(Calendar.MONTH,danhGia.getNgayDang().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,danhGia.getNgayDang().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, danhGia.getNgayDang().getHour());
                                        calendar.set(Calendar.MINUTE,danhGia.getNgayDang().getMinute());
                                        calendar.set(Calendar.SECOND,danhGia.getNgayDang().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapDanhGia.put("NgayDang",ngayDang);
                                        mapDanhGia.put("Rate",danhGia.getRate());
                                        mapDanhGia.put("TenNguoiDanhGia",danhGia.getTenNguoiDanhGia());
                                        mapDanhGia.put("avartaNguoiDanhGia",danhGia.getImgNguoiDang());
                                        mapDsNhapXet.add(mapDanhGia);
                                    }
                                }
                                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                                        .update("DanhGia",mapDsNhapXet)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Loi cập nhật bài viết"+travel.getTieuDe(),
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                            }
                                        });
                            }
                            if(travel.getHoiDaps()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsHoiDap=new ArrayList<>();
                                for (HoiDap hoiDap:travel.getHoiDaps())
                                {
                                    if(hoiDap.getMaNguoiHoi().equals(user_.getIdentifier()))
                                    {
                                        Map<String, Object> mapHoiDap=new HashMap<>();
                                        mapHoiDap.put("MaHoiDap",hoiDap.getMaHoiDap());
                                        mapHoiDap.put("MaNguoiHoi",user_.getIdentifier());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, hoiDap.getNgayHoi().getYear());
                                        calendar.set(Calendar.MONTH,hoiDap.getNgayHoi().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,hoiDap.getNgayHoi().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, hoiDap.getNgayHoi().getHour());
                                        calendar.set(Calendar.MINUTE,hoiDap.getNgayHoi().getMinute());
                                        calendar.set(Calendar.SECOND,hoiDap.getNgayHoi().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapHoiDap.put("NgayHoi",ngayDang);
                                        mapHoiDap.put("NoiDungHoiDap",hoiDap.getNoiDungHoiDap());
                                        mapHoiDap.put("TenNguoiHoi",tenNguoiDungNew);
                                        mapHoiDap.put("TraLoi",null);
                                        mapHoiDap.put("avartaNguoiHoi",tenAnhThayThe);
                                        mapDsHoiDap.add(mapHoiDap);
                                    }
                                    else {
                                        Map<String, Object> mapHoiDap=new HashMap<>();
                                        mapHoiDap.put("MaHoiDap",hoiDap.getMaHoiDap());
                                        mapHoiDap.put("MaNguoiHoi",hoiDap.getMaNguoiHoi());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, hoiDap.getNgayHoi().getYear());
                                        calendar.set(Calendar.MONTH,hoiDap.getNgayHoi().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,hoiDap.getNgayHoi().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, hoiDap.getNgayHoi().getHour());
                                        calendar.set(Calendar.MINUTE,hoiDap.getNgayHoi().getMinute());
                                        calendar.set(Calendar.SECOND,hoiDap.getNgayHoi().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapHoiDap.put("NgayHoi",ngayDang);
                                        mapHoiDap.put("NoiDungHoiDap",hoiDap.getNoiDungHoiDap());
                                        mapHoiDap.put("TenNguoiHoi",hoiDap.getTenNguoiHoi());
                                        mapHoiDap.put("TraLoi",null);
                                        mapHoiDap.put("avartaNguoiHoi",hoiDap.getImgNguoiHoi());
                                        mapDsHoiDap.add(mapHoiDap);
                                    }
                                }
                                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                                        .update("HoiDap",mapDsHoiDap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Loi cập nhật bài viết"+travel.getTieuDe(),
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                            }
                                        });
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        firebaseFirestore.collection("Hotel")//.whereEqualTo("NguoiDang",NguoiDang)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document:queryDocumentSnapshots)
                        {
                            Hotel hotel= HotelService.ReadHotelDocument(document);
                            if(hotel.getNguoiDang().getMaNguoiDang().equals(user_.getIdentifier()))
                            {
                                Map<String,Object> NguoiDangNew= new HashMap<>();
                                NguoiDangNew.put("AnhDaiDien",tenAnhThayThe);
                                NguoiDangNew.put("MaNguoiDang",maNguoiDang);
                                NguoiDangNew.put("TenNguoiDang",tenNguoiDungNew);
                                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                                        .update("NguoiDang",NguoiDangNew)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Loi cập nhật bài viết"+hotel.getTenKhachSan(),
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                            }
                                        });
                            }

                            if(hotel.getDanhGias()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsNhapXet=new ArrayList<>();
                                for (DanhGia danhGia:hotel.getDanhGias())
                                {
                                    if(danhGia.getMaNguoiDanhGia().equals(user_.getIdentifier()))
                                    {
                                        Map<String, Object> mapDanhGia=new HashMap<>();
                                        Log.e("NguoiDanhGia","=>"+danhGia.getMaNguoiDanhGia());
                                        mapDanhGia.put("MaNguoiDanhGia",user_.getIdentifier());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, danhGia.getNgayDang().getYear());
                                        calendar.set(Calendar.MONTH,danhGia.getNgayDang().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,danhGia.getNgayDang().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, danhGia.getNgayDang().getHour());
                                        calendar.set(Calendar.MINUTE,danhGia.getNgayDang().getMinute());
                                        calendar.set(Calendar.SECOND,danhGia.getNgayDang().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapDanhGia.put("NgayDang",ngayDang);
                                        mapDanhGia.put("Rate",danhGia.getRate());
                                        mapDanhGia.put("TenNguoiDanhGia",tenNguoiDungNew);
                                        mapDanhGia.put("avartaNguoiDanhGia",tenAnhThayThe);
                                        mapDsNhapXet.add(mapDanhGia);
                                    }
                                    else {
                                        Map<String, Object> mapDanhGia=new HashMap<>();
                                        mapDanhGia.put("MaNguoiDanhGia",danhGia.getMaNguoiDanhGia());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, danhGia.getNgayDang().getYear());
                                        calendar.set(Calendar.MONTH,danhGia.getNgayDang().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,danhGia.getNgayDang().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, danhGia.getNgayDang().getHour());
                                        calendar.set(Calendar.MINUTE,danhGia.getNgayDang().getMinute());
                                        calendar.set(Calendar.SECOND,danhGia.getNgayDang().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapDanhGia.put("NgayDang",ngayDang);
                                        mapDanhGia.put("Rate",danhGia.getRate());
                                        mapDanhGia.put("TenNguoiDanhGia",danhGia.getTenNguoiDanhGia());
                                        mapDanhGia.put("avartaNguoiDanhGia",danhGia.getImgNguoiDang());
                                        mapDsNhapXet.add(mapDanhGia);
                                    }
                                }
                                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                                        .update("DanhGia",mapDsNhapXet)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Loi cập nhật bài viết"+hotel.getTenKhachSan(),
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                            }
                                        });
                            }
                            if(hotel.getHoiDaps()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsHoiDap=new ArrayList<>();
                                for (HoiDap hoiDap:hotel.getHoiDaps())
                                {
                                    if(hoiDap.getMaNguoiHoi().equals(user_.getIdentifier()))
                                    {
                                        Map<String, Object> mapHoiDap=new HashMap<>();
                                        mapHoiDap.put("MaHoiDap",hoiDap.getMaHoiDap());
                                        mapHoiDap.put("MaNguoiHoi",user_.getIdentifier());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, hoiDap.getNgayHoi().getYear());
                                        calendar.set(Calendar.MONTH,hoiDap.getNgayHoi().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,hoiDap.getNgayHoi().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, hoiDap.getNgayHoi().getHour());
                                        calendar.set(Calendar.MINUTE,hoiDap.getNgayHoi().getMinute());
                                        calendar.set(Calendar.SECOND,hoiDap.getNgayHoi().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapHoiDap.put("NgayHoi",ngayDang);
                                        mapHoiDap.put("NoiDungHoiDap",hoiDap.getNoiDungHoiDap());
                                        mapHoiDap.put("TenNguoiHoi",tenNguoiDungNew);
                                        mapHoiDap.put("TraLoi",null);
                                        mapHoiDap.put("avartaNguoiHoi",tenAnhThayThe);
                                        mapDsHoiDap.add(mapHoiDap);
                                    }
                                    else {
                                        Map<String, Object> mapHoiDap=new HashMap<>();
                                        mapHoiDap.put("MaHoiDap",hoiDap.getMaHoiDap());
                                        mapHoiDap.put("MaNguoiHoi",hoiDap.getMaNguoiHoi());
                                        Calendar calendar=Calendar.getInstance();
                                        calendar.set(Calendar.YEAR, hoiDap.getNgayHoi().getYear());
                                        calendar.set(Calendar.MONTH,hoiDap.getNgayHoi().getMonthValue());
                                        calendar.set(Calendar.DAY_OF_MONTH,hoiDap.getNgayHoi().getDayOfMonth());
                                        calendar.set(Calendar.HOUR, hoiDap.getNgayHoi().getHour());
                                        calendar.set(Calendar.MINUTE,hoiDap.getNgayHoi().getMinute());
                                        calendar.set(Calendar.SECOND,hoiDap.getNgayHoi().getSecond());
                                        Timestamp ngayDang = new Timestamp(calendar.getTime());
                                        mapHoiDap.put("NgayHoi",ngayDang);
                                        mapHoiDap.put("NoiDungHoiDap",hoiDap.getNoiDungHoiDap());
                                        mapHoiDap.put("TenNguoiHoi",hoiDap.getTenNguoiHoi());
                                        mapHoiDap.put("TraLoi",null);
                                        mapHoiDap.put("avartaNguoiHoi",hoiDap.getImgNguoiHoi());
                                        mapDsHoiDap.add(mapHoiDap);
                                    }
                                }
                                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                                        .update("HoiDap",mapDsHoiDap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Loi cập nhật bài viết"+hotel.getTenKhachSan(),
                                                        ActivityThongTinCaNhanUserAdmin.this);
                                            }
                                        });
                            }

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void UpdateTrangThai(String field,Boolean trangthai)
    {
        firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                                .update(field,trangthai)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        DialogMessage.ThongBao("Đã cập nhật thành công",
                                                ActivityThongTinCaNhanUserAdmin.this);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        DialogMessage.ThongBao("Cập nhật thất bại",
                                                ActivityThongTinCaNhanUserAdmin.this);
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DialogMessage.ThongBao("Lỗi không tìm thấy",
                                ActivityThongTinCaNhanUserAdmin.this);
                    }
                });
    }
    private void SetValueToControl()
    {
        firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user_= UserService.ReadUserDocument(documentSnapshot);
                        //MainActivity.USER_=currentUser;
                        String filePath="avarta/" + user_.getAvarta();
                        StorageService.LoadImageUri(filePath,img_thong_tin_ca_nhan_user_admin,
                                ActivityThongTinCaNhanUserAdmin.this,720,1280);
                        edtEmail_thong_tin_ca_nhan_user_admin.getEditText().setText(user_.getIdentifier());
                        edtHoVaTen_thong_tin_ca_nhan_user_admin.getEditText().setText(user_.getFullName());
                        edtHoVaTen_thong_tin_ca_nhan_user_admin.setEnabled(false);
                        if(user_.isActive())
                        {
                            radioTrangthaiTrue.setChecked(true);
                        }
                        else
                        {
                            radioTrangThaiFalse.setChecked(true);
                        }

                        if(user_.isAuthor())
                        {
                            radioQuyenTrue.setChecked(true);
                        }
                        else
                        {
                            radioQuyenFalse.setChecked(true);
                        }

                        if(user_.isGen())
                        {
                            radioNam.setChecked(true);
                        }
                        else {
                            radioNu.setChecked(true);
                        }
                        tvNgaySinh_thong_tin_ca_nhan_user_admin.getEditText().setText(user_.getDateOfBirth().toString());
                    }
                });

    }

}