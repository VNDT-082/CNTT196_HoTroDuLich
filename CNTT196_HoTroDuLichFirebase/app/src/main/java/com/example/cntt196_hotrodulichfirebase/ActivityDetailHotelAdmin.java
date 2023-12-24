package com.example.cntt196_hotrodulichfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterNhanXet;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterPhong;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_hoidap_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDetailHotelAdmin extends AppCompatActivity {

    private TextView tvTenNguoiDung_detail_hotel_admin, tvNgayDang_detail_hotel_admin, tvTieuDe_detail_hotel_admin
            , tvMoTa_detail_hotel_admin, tvDiaChi_detail_hotel_admin, tvTrangThai_detail_hotel_admin;
    private ImageView imgNguoiDung_detail_hotel_admin, imgHinhAnhBaiDang_detail_hotel_admin;
    private ImageButton  btnBack_detail_hotel_admin;
    private RecyclerView recylistHinhAnh_detail_hotel_admin;
    private RecyclerView lvPhong_detail_hotel_admin,lvDanhGia_detail_hotel_admin;
    private Button btnXoa_detail_hotel_admin, btnDuyet_detail_hotel_admin;
    private Hotel hotel;

    private void Init()
    {
        tvTieuDe_detail_hotel_admin=findViewById(R.id.tvTieuDe_detail_hotel_admin);
        tvTenNguoiDung_detail_hotel_admin= findViewById(R.id.tvTenNguoiDung_detail_hotel_admin);
        tvNgayDang_detail_hotel_admin= findViewById(R.id.tvNgayDang_detail_hotel_admin);
        tvTieuDe_detail_hotel_admin= findViewById(R.id.tvTieuDe_detail_hotel_admin);
        tvMoTa_detail_hotel_admin= findViewById(R.id.tvMoTa_detail_hotel_admin);
        tvDiaChi_detail_hotel_admin= findViewById(R.id.tvDiaChi_detail_hotel_admin);
        imgNguoiDung_detail_hotel_admin= findViewById(R.id.imgNguoiDung_detail_hotel_admin);
        imgHinhAnhBaiDang_detail_hotel_admin= findViewById(R.id.imgHinhAnhBaiDang_detail_hotel_admin);
        btnBack_detail_hotel_admin=findViewById(R.id.btnBack_detail_hotel_admin);
        recylistHinhAnh_detail_hotel_admin=findViewById(R.id.recylistHinhAnh_detail_hotel_admin);
        lvPhong_detail_hotel_admin=findViewById(R.id.lvPhong_detail_hotel_admin);
        lvDanhGia_detail_hotel_admin=findViewById(R.id.lvDanhGia_detail_hotel_admin);
        btnXoa_detail_hotel_admin=findViewById(R.id.btnXoa_detail_hotel_admin);
        btnDuyet_detail_hotel_admin=findViewById(R.id.btnDuyet_detail_hotel_admin);
        tvTrangThai_detail_hotel_admin=findViewById(R.id.tvTrangThai_detail_hotel_admin);
    }
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel_admin);
        Init();
        firebaseFirestore= FirebaseFirestore.getInstance();
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            hotel=(Hotel) bundle.getSerializable("Hotel");
            SetValueToControl();
            if(hotel.isTrangThai())
            {
                btnDuyet_detail_hotel_admin.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            finish();
        }
        btnBack_detail_hotel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDuyet_detail_hotel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                                        .update("TrangThai",true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DialogMessage.ThongBao("Đã duyệt bài",ActivityDetailHotelAdmin.this);
                                                tvTrangThai_detail_hotel_admin.setText("Trạng thái: Đã duyệt");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Lỗi duyệt bài",ActivityDetailHotelAdmin.this);
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogMessage.ThongBao("Lỗi không tìm thấy",ActivityDetailHotelAdmin.this);
                            }
                        });
            }
        });
        btnXoa_detail_hotel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FoldePath","=> Hotel/"+hotel.getID_Document());

                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.e("FoldePath","=> Hotel/"+hotel.getID_Document());
                                StorageService.DeleteFolserImage("Hotel/"+hotel.getID_Document(),
                                        ActivityDetailHotelAdmin.this);
                                DialogMessage.ThongBao("Đã xóa",ActivityDetailHotelAdmin.this);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 500);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogMessage.ThongBao("Xóa bài viết thất bại",ActivityDetailHotelAdmin.this);
                            }
                        });
            }
        });
    }
    private void SetValueToControl()
    {
        if(hotel.getNguoiDang()!=null)
        {
            String filePath="avarta/" + hotel.getNguoiDang().getAnhDaiDien();
            StorageService.LoadImageUri_Avarta(filePath,imgNguoiDung_detail_hotel_admin,this);

            tvTenNguoiDung_detail_hotel_admin.setText(hotel.getNguoiDang().getTenNguoiDang());
            tvNgayDang_detail_hotel_admin.setText(DateTimeToString.Format(hotel.getNgayDang()));
            tvTieuDe_detail_hotel_admin.setText(hotel.getTenKhachSan());
            tvMoTa_detail_hotel_admin.setText(hotel.getMoTa());
            tvDiaChi_detail_hotel_admin.setText(hotel.getDiaChi());

            if(hotel.getDanhGias()!=null)
            {
                AdapterNhanXet adapterNhanXet=new AdapterNhanXet(ActivityDetailHotelAdmin.this
                        ,hotel.getDanhGias(),hotel.getID_Document(),1,false);
                lvDanhGia_detail_hotel_admin.setAdapter(adapterNhanXet);
                lvDanhGia_detail_hotel_admin.setLayoutManager(new LinearLayoutManager(ActivityDetailHotelAdmin.this
                        , LinearLayoutManager.VERTICAL, false));
            }
            if(hotel.getHinhAnhs()!=null)
            {
                if(hotel.getHinhAnhs().size()>0)
                {
                    String rootFile= "Hotel/"+ hotel.getID_Document()+"/"+hotel.getHinhAnhs().get(0);
                    StorageService.LoadImageUri(rootFile,imgHinhAnhBaiDang_detail_hotel_admin,this,1280,750);

                    Adapter_listview_images_ver1 adapter_listview_images_ver1=new
                            Adapter_listview_images_ver1(hotel.getHinhAnhs(),ActivityDetailHotelAdmin.this, hotel.getID_Document(),false);
                    recylistHinhAnh_detail_hotel_admin.setAdapter(adapter_listview_images_ver1);
                    recylistHinhAnh_detail_hotel_admin.setLayoutManager(new LinearLayoutManager(ActivityDetailHotelAdmin.this
                            , LinearLayoutManager.HORIZONTAL, false));
                }
            }
            if(hotel.getPhongs()!=null)
            {
                AdapterPhong adapterPhong=new AdapterPhong(hotel.getPhongs(),ActivityDetailHotelAdmin.this,
                        hotel.getID_Document(),false,false);
                lvPhong_detail_hotel_admin.setAdapter(adapterPhong);
                lvPhong_detail_hotel_admin.setLayoutManager(new LinearLayoutManager(ActivityDetailHotelAdmin.this
                        , LinearLayoutManager.HORIZONTAL, false));
            }
            tvTrangThai_detail_hotel_admin.setText(hotel.isTrangThai()?"Trạng thái: Đã duyệt":"Trạng thái: Chưa duyệt");

        }

    }
}