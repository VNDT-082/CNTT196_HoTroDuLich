package com.example.cntt196_hotrodulichfirebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentThongTinCaNhan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentThongTinCaNhan extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button buttonDoiMatKhau_frg_trang_ca_nhan, buttonChonAnh_frg_trang_ca_nhan
    , buttonCapNhatAnh_frg_trang_ca_nhan, buttonCapNhat_frg_trang_ca_nhan;
    private ImageButton imageButtonEdit_thong_tin_ca_nhan_frg_user;
    private TextInputLayout edtHoVaTen_frg_trang_ca_nhan, edtEmail_frg_trang_ca_nhan, tvNgaySinh_frg_trang_ca_nhan;
    private RadioButton radioNam_frg_trang_ca_nhan, radioNu_frg_trang_ca_nhan;
    private ImageView img_frg_trang_ca_nhan;

    private View mView;
    private Context context;
    private HinhAnhBitMap hinhAnhBitMap;
    private FirebaseFirestore firebaseFirestore;
    private User_ currentUser;

    public FragmentThongTinCaNhan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentThongTinCaNhan.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentThongTinCaNhan newInstance(String param1, String param2) {
        FragmentThongTinCaNhan fragment = new FragmentThongTinCaNhan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_thong_tin_ca_nhan, container, false);
        context=requireContext();
        Init(mView);
        currentUser=MainActivity.USER_;
        firebaseFirestore=FirebaseFirestore.getInstance();
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == -1) {
                            if (result.getData() != null) {
                                Intent data=result.getData();
                                Uri uriData= data.getData();

                                try {
                                    Bitmap bitmapImage= MediaStore.Images.Media.getBitmap(context.getContentResolver(),uriData);
                                    img_frg_trang_ca_nhan.setImageBitmap(bitmapImage);
                                    hinhAnhBitMap=new HinhAnhBitMap();
                                    hinhAnhBitMap.setBitmap(bitmapImage);
                                    hinhAnhBitMap.setUri(uriData);
                                    hinhAnhBitMap.setTenHinhAnh("image_"+ DateTimeToString.GenarateID() +".jpg");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            Toast.makeText(context, "Chua chon hinh anh",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        SetValueToControl();
        buttonChonAnh_frg_trang_ca_nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);
            }
        });
        imageButtonEdit_thong_tin_ca_nhan_frg_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCapNhat_frg_trang_ca_nhan.setVisibility(View.VISIBLE);
                edtHoVaTen_frg_trang_ca_nhan.setEnabled(true);
            }
        });
        buttonCapNhatAnh_frg_trang_ca_nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hinhAnhBitMap!=null)
                {
                    String anhDaiDien =currentUser.getAvarta();
                    String maNguoiDang=currentUser.getIdentifier();
                    String tenNguoiDang=currentUser.getFullName();
                    firebaseFirestore.collection("UserInfo").document(MainActivity.USER_.getIdDocument())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    firebaseFirestore.collection("UserInfo").document(MainActivity.USER_.getIdDocument())
                                            .update("avarta", hinhAnhBitMap.getTenHinhAnh())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    DialogMessage.ThongBao("Đã cập nhật hinh ảnh",
                                                            context);
                                                    String folderPath="avarta/";
                                                    ArrayList<HinhAnhBitMap> dsAnh=new ArrayList<>();
                                                    dsAnh.add(hinhAnhBitMap);
                                                    StorageService.UploadImage(folderPath,dsAnh,context);
                                                    String tenAnhThayThe=hinhAnhBitMap.getTenHinhAnh();
                                                    GetUserAfterUpdate();
                                                    UpdateBaiViet(anhDaiDien,maNguoiDang,tenNguoiDang,currentUser.getAvarta(),
                                                            currentUser.getFullName());
                                                    SetValueToControl();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    DialogMessage.ThongBao("Cập nhật hinh ảnh thất bại", context);
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    DialogMessage.ThongBao("Lỗi không tìm thấy",context);
                                }
                            });
                }
            }
        });
        buttonCapNhat_frg_trang_ca_nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtHoVaTen_frg_trang_ca_nhan.getEditText().getText()!=null&&
                !edtHoVaTen_frg_trang_ca_nhan.getEditText().getText().toString().equals(currentUser.getFullName()))
                {
                    String anhDaiDien =currentUser.getAvarta();
                    String maNguoiDang=currentUser.getIdentifier();
                    String tenNguoiDang=currentUser.getFullName();
                    firebaseFirestore.collection("UserInfo").document(MainActivity.USER_.getIdDocument())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    firebaseFirestore.collection("UserInfo").document(MainActivity.USER_.getIdDocument())
                                    .update("fullName", edtHoVaTen_frg_trang_ca_nhan.getEditText().getText().toString().trim())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            DialogMessage.ThongBao("Đã cập nhật tên mới",
                                                    context);
                                            String tenNguoiDungThayThe=edtHoVaTen_frg_trang_ca_nhan.getEditText().getText().toString().trim();
                                            GetUserAfterUpdate();
                                            UpdateBaiViet(anhDaiDien,maNguoiDang,tenNguoiDang
                                                    ,currentUser.getAvarta(),tenNguoiDungThayThe);
                                            SetValueToControl();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            DialogMessage.ThongBao("Cập nhật tên thất bại thất bại", context);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    DialogMessage.ThongBao("Lỗi không tìm thấy",context);
                                }
                            });
                }
            }
        });

        return mView;
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
                            if(travel.getNguoiDang().getMaNguoiDang().equals(currentUser.getIdentifier()))
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
                                                        context);
                                            }
                                        });
                            }

                            if(travel.getDanhGias()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsNhapXet=new ArrayList<>();
                                for (DanhGia danhGia:travel.getDanhGias())
                                {
                                    if(danhGia.getMaNguoiDanhGia().equals(currentUser.getIdentifier()))
                                    {
                                        Map<String, Object> mapDanhGia=new HashMap<>();
                                        mapDanhGia.put("MaNguoiDanhGia",currentUser.getIdentifier());
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
                                        mapDanhGia.put("TenNguoiDanhGia",currentUser.getFullName());
                                        mapDanhGia.put("avartaNguoiDanhGia",currentUser.getAvarta());
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
                                                        context);
                                            }
                                        });
                            }
                            if(travel.getHoiDaps()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsHoiDap=new ArrayList<>();
                                for (HoiDap hoiDap:travel.getHoiDaps())
                                {
                                    if(hoiDap.getMaNguoiHoi().equals(currentUser.getIdentifier()))
                                    {
                                        Map<String, Object> mapHoiDap=new HashMap<>();
                                        mapHoiDap.put("MaHoiDap",hoiDap.getMaHoiDap());
                                        mapHoiDap.put("MaNguoiHoi",currentUser.getIdentifier());
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
                                        mapHoiDap.put("MaNguoiHoi",currentUser.getIdentifier());
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
                                                        context);
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
                            if(hotel.getNguoiDang().getMaNguoiDang().equals(currentUser.getIdentifier()))
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
                                                        context);
                                            }
                                        });
                            }

                            if(hotel.getDanhGias()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsNhapXet=new ArrayList<>();
                                for (DanhGia danhGia:hotel.getDanhGias())
                                {
                                    if(danhGia.getMaNguoiDanhGia().equals(currentUser.getIdentifier()))
                                    {
                                        Map<String, Object> mapDanhGia=new HashMap<>();
                                        Log.e("NguoiDanhGia","=>"+danhGia.getMaNguoiDanhGia());
                                        mapDanhGia.put("MaNguoiDanhGia",currentUser.getIdentifier());
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
                                                        context);
                                            }
                                        });
                            }
                            if(hotel.getHoiDaps()!=null)
                            {
                                ArrayList<Map<String, Object>> mapDsHoiDap=new ArrayList<>();
                                for (HoiDap hoiDap:hotel.getHoiDaps())
                                {
                                    if(hoiDap.getMaNguoiHoi().equals(currentUser.getIdentifier()))
                                    {
                                        Map<String, Object> mapHoiDap=new HashMap<>();
                                        mapHoiDap.put("MaHoiDap",hoiDap.getMaHoiDap());
                                        mapHoiDap.put("MaNguoiHoi",currentUser.getIdentifier());
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
                                        mapHoiDap.put("MaNguoiHoi",currentUser.getIdentifier());
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
                                                        context);
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
    private void SetValueToControl()
    {
        String filePath="avarta/" + currentUser.getAvarta();
        StorageService.LoadImageUri(filePath,img_frg_trang_ca_nhan,context,720,1280);

        edtEmail_frg_trang_ca_nhan.getEditText().setText(currentUser.getIdentifier());
        edtHoVaTen_frg_trang_ca_nhan.getEditText().setText(currentUser.getFullName());
        if(currentUser.isGen())
        {
            radioNam_frg_trang_ca_nhan.setChecked(true);
        }
        else {
            radioNu_frg_trang_ca_nhan.setChecked(true);
        }
        tvNgaySinh_frg_trang_ca_nhan.getEditText().setText(currentUser.getDateOfBirth().toString());
    }
    private void GetUserAfterUpdate()
    {
        firebaseFirestore.collection("UserInfo").document(currentUser.getIdDocument())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currentUser= UserService.ReadUserDocument(documentSnapshot);
                        MainActivity.USER_=currentUser;
                    }
                });
    }
    private void Init(View view)
    {
        buttonDoiMatKhau_frg_trang_ca_nhan=view.findViewById(R.id.buttonDoiMatKhau_frg_trang_ca_nhan);
        buttonChonAnh_frg_trang_ca_nhan=view.findViewById(R.id.buttonChonAnh_frg_trang_ca_nhan);
        buttonCapNhatAnh_frg_trang_ca_nhan=view.findViewById(R.id.buttonCapNhatAnh_frg_trang_ca_nhan);
        edtHoVaTen_frg_trang_ca_nhan=view.findViewById(R.id.edtHoVaTen_frg_trang_ca_nhan);
        edtEmail_frg_trang_ca_nhan=view.findViewById(R.id.edtEmail_frg_trang_ca_nhan);
        tvNgaySinh_frg_trang_ca_nhan=view.findViewById(R.id.tvNgaySinh_frg_trang_ca_nhan);
        radioNam_frg_trang_ca_nhan=view.findViewById(R.id.radioNam_frg_trang_ca_nhan);
        radioNu_frg_trang_ca_nhan=view.findViewById(R.id.radioNu_frg_trang_ca_nhan);
        img_frg_trang_ca_nhan=view.findViewById(R.id.img_frg_trang_ca_nhan);
        imageButtonEdit_thong_tin_ca_nhan_frg_user=view.findViewById(R.id.imageButtonEdit_thong_tin_ca_nhan_frg_user);
        buttonCapNhat_frg_trang_ca_nhan=view.findViewById(R.id.buttonCapNhat_frg_trang_ca_nhan);
        buttonCapNhat_frg_trang_ca_nhan.setVisibility(View.INVISIBLE);
        edtEmail_frg_trang_ca_nhan.setEnabled(false);
        tvNgaySinh_frg_trang_ca_nhan.setEnabled(false);//.setEnabled(false);
        radioNu_frg_trang_ca_nhan.setEnabled(false);
        radioNam_frg_trang_ca_nhan.setEnabled(false);
        edtHoVaTen_frg_trang_ca_nhan.setEnabled(false);
    }
}