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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterNhanXet;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_hoidap_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ActivityDetailTravelAdmin extends AppCompatActivity {


    private TextView tvTenNguoiDung_detail_travel_admin, tvNgayDang_detail_travel_admin, tvTieuDe_detail_travel_admin,
            tvMoTa_detail_travel_admin, tvDiaChi_detail_travel_admin, tvGia_detail_travel_admin, tvTrangThai_detail_travel_admin;
    private ImageView imgNguoiDung_detail_travel_admin, imgHinhAnhBaiDang_detail_travel_admin;
    private ImageButton btnBack;
    private RecyclerView recylistHinhAnh_detail_travel_admin;
    private Button btnXoa_detail_travel_admin, btnDuyet_detail_travel_admin;

    private Travel travel;

    private void Init()
    {
        tvTieuDe_detail_travel_admin=findViewById(R.id.tvTieuDe_detail_travel_admin);
        tvTrangThai_detail_travel_admin=findViewById(R.id.tvTrangThai_detail_travel_admin);
        tvTenNguoiDung_detail_travel_admin= findViewById(R.id.tvTenNguoiDung_detail_travel_admin);
        tvNgayDang_detail_travel_admin= findViewById(R.id.tvNgayDang_detail_travel_admin);
        tvTieuDe_detail_travel_admin= findViewById(R.id.tvTieuDe_detail_travel_admin);
        tvMoTa_detail_travel_admin= findViewById(R.id.tvMoTa_detail_travel_admin);
        tvDiaChi_detail_travel_admin= findViewById(R.id.tvDiaChi_detail_travel_admin);
        tvGia_detail_travel_admin= findViewById(R.id.tvGia_detail_travel_admin);
        imgNguoiDung_detail_travel_admin= findViewById(R.id.imgNguoiDung_detail_travel_admin);
        imgHinhAnhBaiDang_detail_travel_admin= findViewById(R.id.imgHinhAnhBaiDang_detail_travel_admin);
        btnBack=findViewById(R.id.btnBack_detail_travel_admin);
        recylistHinhAnh_detail_travel_admin=findViewById(R.id.recylistHinhAnh_detail_travel_admin);
        btnXoa_detail_travel_admin=findViewById(R.id.btnXoa_detail_travel_admin);
        btnDuyet_detail_travel_admin=findViewById(R.id.btnDuyet_detail_travel_admin);
    }
    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_travel_admin);
        firebaseFirestore= FirebaseFirestore.getInstance();
        Init();
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            travel=(Travel) bundle.getSerializable("Travel");
            SetValueToControl();
        }
        else
        {
            finish();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDuyet_detail_travel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                                        .update("TrangThai",true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DialogMessage.ThongBao("Đã duyệt bài",ActivityDetailTravelAdmin.this);
                                                tvTrangThai_detail_travel_admin.setText("Trạng thái: Đã duyệt");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Lỗi duyệt bài",ActivityDetailTravelAdmin.this);
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogMessage.ThongBao("Lỗi không tìm thấy",ActivityDetailTravelAdmin.this);
                            }
                        });
            }
        });
        btnXoa_detail_travel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                StorageService.DeleteFolserImage("Travel/"+travel.getID_Document(),
                                        ActivityDetailTravelAdmin.this);
                                DialogMessage.ThongBao("Đã xóa",ActivityDetailTravelAdmin.this);
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
                                DialogMessage.ThongBao("Xóa bài viết thất bại",ActivityDetailTravelAdmin.this);
                            }
                        });
            }
        });
    }
    private void SetValueToControl()
    {
        if(travel.getNguoiDang()!=null)
        {
            String filePath="avarta/" + travel.getNguoiDang().getAnhDaiDien();
            StorageService.LoadImageUri_Avarta(filePath,imgNguoiDung_detail_travel_admin,this);

            tvTenNguoiDung_detail_travel_admin.setText(travel.getNguoiDang().getTenNguoiDang());
            tvNgayDang_detail_travel_admin.setText(DateTimeToString.Format(travel.getNgayDang()));
            tvTieuDe_detail_travel_admin.setText(travel.getTieuDe());
            tvMoTa_detail_travel_admin.setText(travel.getMoTa());
            tvDiaChi_detail_travel_admin.setText(travel.getDiaChi());
            if(travel.getGiaMax()==0&&travel.getGiaMin()==0)
            { tvGia_detail_travel_admin.setText("Miễn phí vé tham quan");}
            else
            { tvGia_detail_travel_admin.setText("Giá tham khảo chỉ từ "
                    +DateTimeToString.FormatVND(travel.getGiaMin()) +" đến "+DateTimeToString.FormatVND(travel.getGiaMax()));}


            if(travel.getHinhAnhs()!=null)
            {
                if(travel.getHinhAnhs().size()>0)
                {
                    String rootFile= "Travel/"+ travel.getID_Document()+"/"+travel.getHinhAnhs().get(0);
                    StorageService.LoadImageUri(rootFile,imgHinhAnhBaiDang_detail_travel_admin,this,1280,750);

                    Adapter_listview_images_ver1 adapter_listview_images_ver1=new
                            Adapter_listview_images_ver1(travel.getHinhAnhs(),ActivityDetailTravelAdmin.this
                            ,travel.getID_Document(),true);
                    recylistHinhAnh_detail_travel_admin.setAdapter(adapter_listview_images_ver1);
                    recylistHinhAnh_detail_travel_admin.setLayoutManager(new LinearLayoutManager(ActivityDetailTravelAdmin.this
                            , LinearLayoutManager.HORIZONTAL, false));
                }
            }

        }

    }
}