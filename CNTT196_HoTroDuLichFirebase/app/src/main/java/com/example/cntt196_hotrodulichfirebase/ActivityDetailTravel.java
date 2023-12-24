package com.example.cntt196_hotrodulichfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterNhanXet;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_hoidap_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityDetailTravel extends AppCompatActivity {

    //controll
    private TextView tvTenNguoiDung_detail, tvNgayDang_detail, tvTieuDe_detail, tvMoTa_detail
            , tvDiaChi_detail, tvGia_detail, tvCountFavorite_detail, tvCountDanhGia_detail_travel;
    private ImageView imgNguoiDung_detail, imgHinhAnhBaiDang_detail;
    private RatingBar AvargarateRatingBar_detail_travel;
    private ImageButton btnFavorite_detail, btnBack, btnShowMap_detail_Travel, imgBtnMore_detailtravel;
    private RecyclerView recylistHinhAnh_detail,lvDanhGia_detail_travel, lvHoiDap_detail_travel;
    private Button btnXemTatCa_detail_travel, btnThemNhanXet_detail_travel, btnHoiDap_detail_travel;

    private EditText edtHoiDap_detail_travel;

    private Travel travel;

    private FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_travel);
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
//            Intent intentRedirect=new Intent(ActivityDetailTravel.this,MainActivity.class);
//            ActivityDetailTravel.this.startActivity(intent);
            finish();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFavorite_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_!=null)
                {
                    if((int)btnFavorite_detail.getTag()==R.drawable.baseline_volunteer_activism_24)
                    {
                        btnFavorite_detail.setTag(R.drawable.baseline_volunteer_activism_24_0);
                        btnFavorite_detail.setImageResource((int)btnFavorite_detail.getTag());
                    }
                    else {
                        btnFavorite_detail.setTag(R.drawable.baseline_volunteer_activism_24);
                        btnFavorite_detail.setImageResource((int) btnFavorite_detail.getTag());
                    }
                    Log.e("TagBtn","=>"+v.getTag());
                }
                else {
                    DialogMessage.ThongBaoYeuCauDangNhap("Cần đăng nhập để kích hoạt tính năng này"
                            ,ActivityDetailTravel.this);
                }
            }
        });


        btnThemNhanXet_detail_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_!=null)
                {
                    LoadDialogThemNhanXet();
                }
                else {
                    DialogMessage.ThongBaoYeuCauDangNhap("Cần đăng nhập để kích hoạt tính năng này"
                            ,ActivityDetailTravel.this);
                }
            }
        });

        btnXemTatCa_detail_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(travel.getDanhGias()!=null)
                {
                    LoadDialogListNhanXet();
                }
                else
                {
                    DialogMessage.ThongBao("Hiện chưa có đánh giá về bài viết này"
                            ,ActivityDetailTravel.this);
                }
            }
        });

        btnShowMap_detail_Travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleMap = new Bundle();
                bundleMap.putSerializable("DiaChi", travel.getDiaChi());
                Intent intentMap=new Intent(ActivityDetailTravel.this, MapsActivity.class);
                intentMap.putExtras(bundleMap);
                ActivityDetailTravel.this.startActivity(intentMap);
            }
        });
        btnHoiDap_detail_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_!=null)
                {
                    if(edtHoiDap_detail_travel.getText().equals(""))
                    {
                        DialogMessage.ThongBao("Hỏi đáp không được để trống",ActivityDetailTravel.this);
                    }
                    else
                    {
                        String IDTravel= travel.getID_Document();
                        firebaseFirestore.collection("Travel").document(IDTravel)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.get("HoiDap")==null)
                                        {

                                            String maHoiDap=MainActivity.USER_.getUser_UID() +
                                                    travel.getID_Document() + DateTimeToString.GenarateID();

                                            ArrayList<Map<String,Object>> arrayHoiDap=new ArrayList<>();
                                            Map<String,Object> HoiDap=new HashMap<>();
                                            HoiDap.put("MaHoiDap",maHoiDap);
                                            HoiDap.put("MaNguoiHoi",MainActivity.USER_.getIdentifier());
                                            HoiDap.put("NgayHoi", Timestamp.now());
                                            HoiDap.put("NoiDungHoiDap",edtHoiDap_detail_travel.getText().toString().trim());
                                            HoiDap.put("TenNguoiHoi", MainActivity.USER_.getFullName());
                                            HoiDap.put("avartaNguoiHoi", MainActivity.USER_.getAvarta());
                                            HoiDap.put("TraLoi",null);
                                            arrayHoiDap.add(HoiDap);

                                            firebaseFirestore.collection("Travel").document(IDTravel)
                                                    .update("HoiDap",arrayHoiDap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp thành công",
                                                                    ActivityDetailTravel.this);
                                                            AfterInsertNhanXet(IDTravel,firebaseFirestore);
                                                            edtHoiDap_detail_travel.setText("");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp không thành công",
                                                                    ActivityDetailTravel.this);
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            String maHoiDap=MainActivity.USER_.getUser_UID() +
                                                    travel.getID_Document() + DateTimeToString.GenarateID();

                                            Map<String,Object> HoiDap=new HashMap<>();
                                            HoiDap.put("MaHoiDap",maHoiDap);
                                            HoiDap.put("MaNguoiHoi",MainActivity.USER_.getIdentifier());
                                            HoiDap.put("NgayHoi", Timestamp.now());
                                            HoiDap.put("NoiDungHoiDap",edtHoiDap_detail_travel.getText().toString().trim());
                                            HoiDap.put("TenNguoiHoi", MainActivity.USER_.getFullName());
                                            HoiDap.put("avartaNguoiHoi", MainActivity.USER_.getAvarta());
                                            HoiDap.put("TraLoi",null);

                                            firebaseFirestore.collection("Travel").document(IDTravel)
                                                    .update("HoiDap", FieldValue.arrayUnion(HoiDap))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp thành công",
                                                                    ActivityDetailTravel.this);
                                                            AfterInsertNhanXet(IDTravel,firebaseFirestore);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp không thành công",
                                                                    ActivityDetailTravel.this);
                                                        }
                                                    });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("DIAOLOGNhanXet", "onFailure: Loi load document");
                                    }
                                });

                    }
                }
                else
                {
                    DialogMessage.ThongBao("Cần đăng nhập để kích hoạt tính năng này"
                            ,ActivityDetailTravel.this);
                }

            }
        });

        if(MainActivity.USER_!=null)
        {
            if (MainActivity.USER_.getIdentifier().equals(travel.getNguoiDang().getMaNguoiDang()))
            {
                imgBtnMore_detailtravel.setVisibility(View.VISIBLE);
            }
            else {
                imgBtnMore_detailtravel.setVisibility(View.INVISIBLE);
            }
        }
        else {
            imgBtnMore_detailtravel.setVisibility(View.INVISIBLE);
        }
        imgBtnMore_detailtravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
    }


    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_more_vert_ver1);

        LinearLayout linearlayout_edit = dialog.findViewById(R.id.linearlayout_edit);
        LinearLayout linearlayout_delete = dialog.findViewById(R.id.linearlayout_delete);
        LinearLayout linearlayout_refesh = dialog.findViewById(R.id.linearlayout_refesh);

        linearlayout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_==null)
                {
                    DialogMessage.ThongBao("Bạn cần đăng nhập,\nđể sử dụng tính năng đăng bài"
                            ,ActivityDetailTravel.this);
                }
                else
                {
                    if(MainActivity.USER_.getIdentifier().equals(travel.getNguoiDang().getMaNguoiDang()))
                    {
                        dialog.dismiss();
                        Intent intent =new Intent(ActivityDetailTravel.this,ActivityUpdateTravel.class);
                        intent.putExtra("Author",1);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("Travel", travel);
                        intent.putExtras(bundle);
                        ActivityDetailTravel.this.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(ActivityDetailTravel.this, "Đây không phải bài viết của bạn",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        linearlayout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_==null)
                {
                    DialogMessage.ThongBao("Bạn cần đăng nhập,\nđể sử dụng tính năng đăng bài"
                            ,ActivityDetailTravel.this);
                }
                else
                {
                    if(MainActivity.USER_.getIdentifier().equals(travel.getNguoiDang().getMaNguoiDang()))
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ActivityDetailTravel.this);
                        builder.setMessage("Bạn muốn xóa bài viết: "+travel.getTieuDe()+"?");
                        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DialogMessage.ThongBao("Đã xóa bài viết"
                                                        ,ActivityDetailTravel.this);
                                                StorageService.DeleteFolserImage("Travel/"+travel.getID_Document(),
                                                        ActivityDetailTravel.this);
                                                DialogMessage.ThongBao("Đã xóa",ActivityDetailTravel.this);
                                                Handler handler=new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        finish();
                                                    }
                                                },1500);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                DialogMessage.ThongBao("Xóa bài viết thất bại"
                                                        ,ActivityDetailTravel.this);
                                            }
                                        });
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else
                    {
                        Toast.makeText(ActivityDetailTravel.this, "Đây không phải bài viết của bạn",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(MainActivity.this,"Them bài dang khách san",Toast.LENGTH_SHORT).show();
            }
        });

        linearlayout_refesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Travel").document(travel.getID_Document())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Travel travelRefesh = TravelService.ReadTravelDocument(documentSnapshot);
                                travel = travelRefesh;
                                SetValueToControl();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogMessage.ThongBao("Lỗi tải lại", ActivityDetailTravel.this);
                            }
                        });
                dialog.dismiss();
            }
        });



        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void LoadDialogListNhanXet()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_danh_gia_ver1);
        dialog.setCancelable(false);

        ImageButton imgBtn_lose_dialog_layout_danh_gia;
        RecyclerView lvDanhGia_dialog_layout_danh_gia;

        imgBtn_lose_dialog_layout_danh_gia=dialog.findViewById(R.id.imgBtn_lose_dialog_layout_danh_gia);
        lvDanhGia_dialog_layout_danh_gia= dialog.findViewById(R.id.lvDanhGia_dialog_layout_danh_gia);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        imgBtn_lose_dialog_layout_danh_gia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.setCancelable(true);
                dialog.cancel();
            }
        });

        AdapterNhanXet adapterNhanXet=new AdapterNhanXet(ActivityDetailTravel.this,travel.getDanhGias()
                ,travel.getID_Document(),2, true);
        lvDanhGia_dialog_layout_danh_gia.setAdapter(adapterNhanXet);
        lvDanhGia_dialog_layout_danh_gia.setLayoutManager(new LinearLayoutManager(ActivityDetailTravel.this
                , LinearLayoutManager.VERTICAL, false));

    }
    private void LoadDialogThemNhanXet()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_add_danh_gia_ver1);
        dialog.setCancelable(false);

        ImageView imgNguoiDang_dialog_add_danhgia ;
        Button btnCancel_detail_hotel , btnNhanXet_detail_hotel;
        RatingBar ratingBar_dialog_add_danhgia;
        EditText edtDanhGia_dialog_add_danhgia;
        TextView tvTenNguoiDang_dialog_add_danhgia;
        imgNguoiDang_dialog_add_danhgia = dialog.findViewById(R.id.imgNguoiDang_dialog_add_danhgia);
        btnCancel_detail_hotel = dialog.findViewById(R.id.btnCancel_detail_hotel);
        btnNhanXet_detail_hotel = dialog.findViewById(R.id.btnNhanXet_detail_hotel);
        ratingBar_dialog_add_danhgia = dialog.findViewById(R.id.ratingBar_dialog_add_danhgia);
        edtDanhGia_dialog_add_danhgia = dialog.findViewById(R.id.edtDanhGia_dialog_add_danhgia);
        tvTenNguoiDang_dialog_add_danhgia=dialog.findViewById(R.id.tvTenNguoiDang_dialog_add_danhgia);

        String filePath="avarta/" + MainActivity.USER_.getAvarta();
        StorageService.LoadImageUri_Avarta(filePath,imgNguoiDang_dialog_add_danhgia,
                ActivityDetailTravel.this);

        tvTenNguoiDang_dialog_add_danhgia.setText(MainActivity.USER_.getFullName());
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        btnCancel_detail_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.setCancelable(true);
                dialog.cancel();
            }
        });
        btnNhanXet_detail_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtDanhGia_dialog_add_danhgia.getText().equals(""))
                {
                    DialogMessage.ThongBao("Nhận xét của bạn đang trống",dialog.getContext());
                }
                else
                {
                    String IDTravel=travel.getID_Document();
                    FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("Travel").document(IDTravel)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.get("DanhGia")==null)
                                    {
                                        ArrayList<Map<String,Object>> arrayNhanXet=new ArrayList<>();
                                        Map<String,Object> NhanXet=new HashMap<>();
                                        NhanXet.put("MaNguoiDanhGia",MainActivity.USER_.getIdentifier());
                                        NhanXet.put("NgayDang", Timestamp.now());
                                        NhanXet.put("NoiDungDanhGia",edtDanhGia_dialog_add_danhgia.getText().toString().trim());
                                        long rate= (long) ratingBar_dialog_add_danhgia.getRating();
                                        NhanXet.put("Rate", rate);
                                        NhanXet.put("TenNguoiDanhGia", MainActivity.USER_.getFullName());
                                        NhanXet.put("avartaNguoiDanhGia", MainActivity.USER_.getAvarta());
                                        arrayNhanXet.add(NhanXet);

                                        firebaseFirestore.collection("Travel").document(IDTravel)
                                                .update("DanhGia",arrayNhanXet)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        DialogMessage.ThongBao("Thêm nhận xét thành công",
                                                                dialog.getContext());
                                                        AfterInsertNhanXet(IDTravel,firebaseFirestore);
                                                        dialog.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        DialogMessage.ThongBao("Thêm nhận xét không thành công",
                                                                dialog.getContext());
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        {
                                            ArrayList<Map<String,Object>> arrayNhanXet=new ArrayList<>();
                                            Map<String,Object> NhanXet=new HashMap<>();
                                            NhanXet.put("MaNguoiDanhGia",MainActivity.USER_.getIdentifier());
                                            NhanXet.put("NgayDang", Timestamp.now());
                                            NhanXet.put("NoiDungDanhGia",edtDanhGia_dialog_add_danhgia.getText().toString().trim());
                                            long rate= (long) ratingBar_dialog_add_danhgia.getRating();
                                            NhanXet.put("Rate", rate);
                                            NhanXet.put("TenNguoiDanhGia", MainActivity.USER_.getFullName());
                                            NhanXet.put("avartaNguoiDanhGia", MainActivity.USER_.getAvarta());
                                            arrayNhanXet.add(NhanXet);

                                            firebaseFirestore.collection("Travel").document(IDTravel)
                                                    .update("DanhGia", FieldValue.arrayUnion(NhanXet))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            DialogMessage.ThongBao("Thêm nhận xét thành công",
                                                                    dialog.getContext());
                                                            AfterInsertNhanXet(IDTravel,firebaseFirestore);
                                                            dialog.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            DialogMessage.ThongBao("Thêm nhận xét không thành công",
                                                                    dialog.getContext());
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("DIAOLOGNhanXet", "onFailure: Loi load document");
                                }
                            });

                }
            }

        });


    }

    private void AfterInsertNhanXet( String IDTravel,FirebaseFirestore firebaseFirestore)
    {
        firebaseFirestore.collection("Travel").document(IDTravel)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getId()!=null)
                        {
                            travel.setID_Document(documentSnapshot.getId());
                            Log.d("IdTravel"," => " +  travel.getID_Document());
                            Map<String,Object> subDocument=(Map<String,Object>) documentSnapshot.get("NguoiDang");
                            Log.d("TravelNguoiDang"," => " +  subDocument);
                            if(subDocument!=null)
                            {
                                NguoiDang nguoiDang=new NguoiDang();
                                nguoiDang.setMaNguoiDang((String)subDocument.get("MaNguoiDang"));
                                nguoiDang.setTenNguoiDang((String)subDocument.get("TenNguoiDang"));
                                nguoiDang.setAnhDaiDien((String)subDocument.get("AnhDaiDien"));
                                travel.setNguoiDang(nguoiDang);
                            }
                            travel.setTieuDe(documentSnapshot.getString("TieuDe"));
                            travel.setMoTa(documentSnapshot.getString("MoTa"));

                            ArrayList<Map<String,Object>> subArrayDocumentDanhGia= (ArrayList<Map<String, Object>>) documentSnapshot.get("DanhGia");
                            if(subArrayDocumentDanhGia!=null) {
                                if(subArrayDocumentDanhGia.size()>0)
                                {
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
                                        danhGia.setNoiDung((String) objectMap.get("NoiDungDanhGia"));
                                        danhGia.setImgNguoiDang((String) objectMap.get("avartaNguoiDanhGia"));
                                        dsDanhGia.add(danhGia);
                                    }
                                    travel.setDanhGias(dsDanhGia);
                                }
                            }

                            ArrayList<Map<String,Object>> subArrayDocumentHoiDap=
                                    (ArrayList<Map<String, Object>>) documentSnapshot.get("HoiDap");
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

                            travel.setDiaChi(documentSnapshot.getString("DiaChi"));

                            Number numMax = (Number) documentSnapshot.get("GiaMax");
                            Number numMin = (Number) documentSnapshot.get("GiaMin");
                            travel.setGiaMax((long) Float.parseFloat(numMax.toString()));
                            travel.setGiaMin((long)Float.parseFloat(numMin.toString()));

                            ArrayList<String> dsHinh=new ArrayList<>();
                            dsHinh= (ArrayList<String>) documentSnapshot.get("HinhAnh");
                            travel.setHinhAnhs(dsHinh);


                            Timestamp timestamp=documentSnapshot.getTimestamp("NgayDang");

                            travel.setNgayDang(timestamp.toDate().toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
                        }
                        SetValueToControl();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DIAOLOGNhanXet", "onFailure: Loi load document");
                    }
                });
    }

    private void SetValueToControl()
    {
        if(travel.getNguoiDang()!=null)
        {
            String filePath="avarta/" + travel.getNguoiDang().getAnhDaiDien();
            StorageService.LoadImageUri_Avarta(filePath,imgNguoiDung_detail,this);

            tvTenNguoiDung_detail.setText(travel.getNguoiDang().getTenNguoiDang());
            tvNgayDang_detail.setText(DateTimeToString.Format(travel.getNgayDang()));
            tvTieuDe_detail.setText(travel.getTieuDe());
            tvMoTa_detail.setText(travel.getMoTa());
            tvDiaChi_detail.setText(travel.getDiaChi());
            if(travel.getGiaMax()==0&&travel.getGiaMin()==0)
            { tvGia_detail.setText("Miễn phí vé tham quan");}
            else
            { tvGia_detail.setText("Giá tham khảo chỉ từ "
                    +DateTimeToString.FormatVND(travel.getGiaMin()) +" đến "+DateTimeToString.FormatVND(travel.getGiaMax()));}

            if(travel.getLuotThichs()!=null) {
                if (travel.getLuotThichs().size() > 0) {
                    btnFavorite_detail.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                    btnFavorite_detail.setTag(R.drawable.baseline_volunteer_activism_24_0);
                    if (travel.getLuotThichs().size() > 1) {
                        tvCountFavorite_detail.setText(travel.getLuotThichs().get(0).getTenNguoiDang()
                                + " và " + travel.getLuotThichs().size() + "+");
                    } else {
                        tvCountFavorite_detail.setText(travel.getLuotThichs().get(0).getTenNguoiDang());
                    }

                }
            }
            else {
                btnFavorite_detail.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                btnFavorite_detail.setTag(R.drawable.baseline_volunteer_activism_24_0);
            }
            if(travel.getDanhGias()!=null)
            {
                float rate = 0;
                for (DanhGia danhGia : travel.getDanhGias()) {
                    rate += danhGia.getRate();
                }
                Log.e("LogDsDanhGia", "SetValueToControl: "+travel.getDanhGias().size() );
                rate= Math.round(rate / travel.getDanhGias().size() * 10) / 10f;
                AvargarateRatingBar_detail_travel.setRating(rate);
                tvCountDanhGia_detail_travel.setText(rate+"\n"+"Đánh giá "+"("+travel.getDanhGias().size()+")");
                AvargarateRatingBar_detail_travel.setIsIndicator(true);
                ArrayList<DanhGia> dsDanhGia=new ArrayList<>();
                if(travel.getDanhGias().size()<3)
                {
                    for (DanhGia danhGia:travel.getDanhGias())
                    {
                        dsDanhGia.add(danhGia);
                    }
                }
                else {
                    dsDanhGia.add(travel.getDanhGias().get(0));
                    dsDanhGia.add(travel.getDanhGias().get(1));
                }
                AdapterNhanXet adapterNhanXet=new AdapterNhanXet(ActivityDetailTravel.this
                        ,dsDanhGia,travel.getID_Document(),1,true);
                lvDanhGia_detail_travel.setAdapter(adapterNhanXet);
                lvDanhGia_detail_travel.setLayoutManager(new LinearLayoutManager(ActivityDetailTravel.this
                        , LinearLayoutManager.VERTICAL, false));

            }
            else
            {
                tvCountDanhGia_detail_travel.setText("0.0\n"+"Đánh giá(0)");
                AvargarateRatingBar_detail_travel.setIsIndicator(true);
            }

            if(travel.getHoiDaps()!=null)
            {
                Adapter_listview_hoidap_ver1 adapter_listview_hoidap_ver1=new Adapter_listview_hoidap_ver1(travel.getHoiDaps(),
                        ActivityDetailTravel.this,travel.getID_Document(),true);
                adapter_listview_hoidap_ver1.notifyDataSetChanged();
                lvHoiDap_detail_travel.setAdapter(adapter_listview_hoidap_ver1);
                lvHoiDap_detail_travel.setLayoutManager(new LinearLayoutManager(ActivityDetailTravel.this,
                        RecyclerView.VERTICAL,false));
            }

            if(travel.getHinhAnhs()!=null)
            {
                if(travel.getHinhAnhs().size()>0)
                {
                    String rootFile= "Travel/"+ travel.getID_Document()+"/"+travel.getHinhAnhs().get(0);
                    StorageService.LoadImageUri(rootFile,imgHinhAnhBaiDang_detail,this,1280,750);

                    Adapter_listview_images_ver1 adapter_listview_images_ver1=new
                            Adapter_listview_images_ver1(travel.getHinhAnhs(),ActivityDetailTravel.this
                            ,travel.getID_Document(),true);
                    recylistHinhAnh_detail.setAdapter(adapter_listview_images_ver1);
                    recylistHinhAnh_detail.setLayoutManager(new LinearLayoutManager(ActivityDetailTravel.this
                            , LinearLayoutManager.HORIZONTAL, false));
                }
            }

        }

    }
    private void Init()
    {
        btnShowMap_detail_Travel=findViewById(R.id.btnShowMap_detail_Travel);
        tvTieuDe_detail=findViewById(R.id.tvTieuDe_detail);

        tvTenNguoiDung_detail= findViewById(R.id.tvTenNguoiDung_detail);
        tvNgayDang_detail= findViewById(R.id.tvNgayDang_detail);
        tvTieuDe_detail= findViewById(R.id.tvTieuDe_detail);
        tvMoTa_detail= findViewById(R.id.tvMoTa_detail);
        tvDiaChi_detail= findViewById(R.id.tvDiaChi_detail);
        tvGia_detail= findViewById(R.id.tvGia_detail);
        tvCountFavorite_detail= findViewById(R.id.tvCountFavorite_detail);
        imgNguoiDung_detail= findViewById(R.id.imgNguoiDung_detail);
        imgHinhAnhBaiDang_detail= findViewById(R.id.imgHinhAnhBaiDang_detail);

        btnFavorite_detail= findViewById(R.id.btnFavorite_detail);
        btnBack=findViewById(R.id.btnBack);
        recylistHinhAnh_detail=findViewById(R.id.recylistHinhAnh_detail);
        lvDanhGia_detail_travel=findViewById(R.id.lvDanhGia_detail_travel);

        btnXemTatCa_detail_travel=findViewById(R.id.btnXemTatCa_detail_travel);
        btnThemNhanXet_detail_travel=findViewById(R.id.btnThemNhanXet_detail_travel);
        tvCountDanhGia_detail_travel=findViewById(R.id.tvCountDanhGia_detail_travel);
        AvargarateRatingBar_detail_travel=findViewById(R.id.AvargarateRatingBar_detail_travel);

        edtHoiDap_detail_travel=findViewById(R.id.edtHoiDap_detail_travel);
        lvHoiDap_detail_travel=findViewById(R.id.lvHoiDap_detail_travel);
        btnHoiDap_detail_travel=findViewById(R.id.btnHoiDap_detail_travel);
        imgBtnMore_detailtravel=findViewById(R.id.imgBtnMore_detailtravel);
    }
}