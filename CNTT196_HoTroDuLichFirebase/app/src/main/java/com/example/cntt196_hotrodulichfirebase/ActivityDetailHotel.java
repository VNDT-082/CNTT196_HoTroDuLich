package com.example.cntt196_hotrodulichfirebase;

import androidx.annotation.NonNull;
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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterNhanXet;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterPhong;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_hoidap_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.Phong;
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

public class ActivityDetailHotel extends AppCompatActivity {

    private TextView tvTenNguoiDung_detail_hotel, tvNgayDang_detail_hotel, tvTieuDe_detail_hotel, tvMoTa_detail_hotel
            , tvDiaChi_detail_hotel, tvCountFavorite_detail_hotel, tvCountDanhGia_detail_hotel;
    private ImageView imgNguoiDung_detail_hotel, imgHinhAnhBaiDang_detail_hotel;
    private RatingBar ratingBar_detail_hotel,AvargarateRatingBar_detail_hotel;
    private ImageButton btnFavorite_detail_hotel, btnBack_hotel, btnShowMap_detail_hotel, imgBtnMore_detailhotel;
    private RecyclerView recylistHinhAnh_detail_hotel;
    private RecyclerView lvPhong_detail_hotel, lvHoiDap_detail_hotel,lvDanhGia_detail_hotel;
    private EditText edtHoiDap_detail_hotel;
    private Button btnThemNhanXet_detail_hotel, btnXemTatCa_detail_hotel, btnHoiDap_detail_hotel;
    private Hotel hotel;


    private FirebaseFirestore firebaseFirestore;
    private void Init()
    {
        btnShowMap_detail_hotel =findViewById(R.id.btnShowMap_detail_hotel);
        btnXemTatCa_detail_hotel=findViewById(R.id.btnXemTatCa_detail_hotel);
        btnThemNhanXet_detail_hotel=findViewById(R.id.btnThemNhanXet_detail_hotel);
        tvTieuDe_detail_hotel=findViewById(R.id.tvTieuDe_detail_hotel);
        tvCountDanhGia_detail_hotel=findViewById(R.id.tvCountDanhGia_detail_hotel);

        tvTenNguoiDung_detail_hotel= findViewById(R.id.tvTenNguoiDung_detail_hotel);
        tvNgayDang_detail_hotel= findViewById(R.id.tvNgayDang_detail_hotel);
        tvTieuDe_detail_hotel= findViewById(R.id.tvTieuDe_detail_hotel);
        tvMoTa_detail_hotel= findViewById(R.id.tvMoTa_detail_hotel);
        tvDiaChi_detail_hotel= findViewById(R.id.tvDiaChi_detail_hotel);
        tvCountFavorite_detail_hotel= findViewById(R.id.tvCountFavorite_detail_hotel);
        imgNguoiDung_detail_hotel= findViewById(R.id.imgNguoiDung_detail_hotel);
        imgHinhAnhBaiDang_detail_hotel= findViewById(R.id.imgHinhAnhBaiDang_detail_hotel);
        AvargarateRatingBar_detail_hotel=findViewById(R.id.AvargarateRatingBar_detail_hotel);
        btnFavorite_detail_hotel= findViewById(R.id.btnFavorite_detail_hotel);
        btnBack_hotel=findViewById(R.id.btnBack_detail_hotel);
        recylistHinhAnh_detail_hotel=findViewById(R.id.recylistHinhAnh_detail_hotel);
        lvPhong_detail_hotel=findViewById(R.id.lvPhong_detail_hotel);
        lvDanhGia_detail_hotel=findViewById(R.id.lvDanhGia_detail_hotel);
        lvHoiDap_detail_hotel=findViewById(R.id.lvHoiDap_detail_hotel);
        edtHoiDap_detail_hotel = findViewById(R.id.edtHoiDap_detail_hotel);
        btnHoiDap_detail_hotel=findViewById(R.id.btnHoiDap_detail_hotel);
        imgBtnMore_detailhotel = findViewById(R.id.imgBtnMore_detailhotel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel);

        Init();
        firebaseFirestore= FirebaseFirestore.getInstance();
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            hotel=(Hotel) bundle.getSerializable("Hotel");
            SetValueToControl();
        }
        else
        {
            finish();
        }
        btnBack_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnThemNhanXet_detail_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_!=null)
                {
                    LoadDialogThemNhanXet();
                }
                else {
                    DialogMessage.ThongBaoYeuCauDangNhap("Cần đăng nhập để kích hoạt tính năng này"
                    ,ActivityDetailHotel.this);
                }
            }
        });
        btnXemTatCa_detail_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hotel.getDanhGias()!=null)
                {
                    LoadDialogListNhanXet();
                }
                else
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(ActivityDetailHotel.this);
                    builder.setMessage("Hiện chưa có đánh giá về bài viết này");
                    builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        btnShowMap_detail_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundleMap = new Bundle();
                bundleMap.putSerializable("DiaChi", hotel.getDiaChi());
                Intent intentMap=new Intent(ActivityDetailHotel.this, MapsActivity.class);
                intentMap.putExtras(bundleMap);
                ActivityDetailHotel.this.startActivity(intentMap);
            }
        });
        btnHoiDap_detail_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_!=null)
                {
                    if(edtHoiDap_detail_hotel.getText().equals(""))
                    {
                        DialogMessage.ThongBao("Hỏi đáp không được để trống",ActivityDetailHotel.this);
                    }
                    else
                    {
                        String IDHotel=hotel.getID_Document();
                        firebaseFirestore.collection("Hotel").document(IDHotel)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.get("HoiDap")==null)
                                        {

                                            String maHoiDap=MainActivity.USER_.getUser_UID() +
                                                    hotel.getID_Document() + DateTimeToString.GenarateID();

                                            ArrayList<Map<String,Object>> arrayHoiDap=new ArrayList<>();
                                            Map<String,Object> HoiDap=new HashMap<>();
                                            HoiDap.put("MaHoiDap",maHoiDap);
                                            HoiDap.put("MaNguoiHoi",MainActivity.USER_.getIdentifier());
                                            HoiDap.put("NgayHoi", Timestamp.now());
                                            HoiDap.put("NoiDungHoiDap",edtHoiDap_detail_hotel.getText().toString().trim());
                                            HoiDap.put("TenNguoiHoi", MainActivity.USER_.getFullName());
                                            HoiDap.put("avartaNguoiHoi", MainActivity.USER_.getAvarta());
                                            HoiDap.put("TraLoi",null);
                                            arrayHoiDap.add(HoiDap);

                                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                                    .update("HoiDap",arrayHoiDap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp thành công",
                                                                    ActivityDetailHotel.this);
                                                            AfterInsertNhanXet(IDHotel,firebaseFirestore);
                                                            edtHoiDap_detail_hotel.setText("");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp không thành công",
                                                                    ActivityDetailHotel.this);
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            String maHoiDap=MainActivity.USER_.getUser_UID() +
                                                    hotel.getID_Document() + DateTimeToString.GenarateID();

                                            Map<String,Object> HoiDap=new HashMap<>();
                                            HoiDap.put("MaHoiDap",maHoiDap);
                                            HoiDap.put("MaNguoiHoi",MainActivity.USER_.getIdentifier());
                                            HoiDap.put("NgayHoi", Timestamp.now());
                                            HoiDap.put("NoiDungHoiDap",edtHoiDap_detail_hotel.getText().toString().trim());
                                            HoiDap.put("TenNguoiHoi", MainActivity.USER_.getFullName());
                                            HoiDap.put("avartaNguoiHoi", MainActivity.USER_.getAvarta());
                                            HoiDap.put("TraLoi",null);

                                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                                    .update("HoiDap", FieldValue.arrayUnion(HoiDap))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp thành công",
                                                                    ActivityDetailHotel.this);
                                                            AfterInsertNhanXet(IDHotel,firebaseFirestore);

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            DialogMessage.ThongBao("Thêm hỏi đáp không thành công",
                                                                    ActivityDetailHotel.this);
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
                else {
                    DialogMessage.ThongBao("Cần đăng nhập để kích hoạt tính năng này",ActivityDetailHotel.this);
                }

            }
        });

        imgBtnMore_detailhotel.setOnClickListener(new View.OnClickListener() {
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
                            ,ActivityDetailHotel.this);
                }
                else
                {
                    if( MainActivity.USER_.getIdentifier().equals(hotel.getNguoiDang().getMaNguoiDang()))
                    {
                        dialog.dismiss();
                        Intent intent =new Intent(ActivityDetailHotel.this,ActivityUpdateHotel.class);
                        intent.putExtra("Author",1);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("Hotel", hotel);
                        intent.putExtras(bundle);
                        ActivityDetailHotel.this.startActivity(intent);
                    }
                    else {
                        Toast.makeText(ActivityDetailHotel.this, "Dây không phải bài viết của bạn",
                                Toast.LENGTH_SHORT).show();
                    }

                }

                //Toast.makeText(MainActivity.this, "Them bai dang du lich", Toast.LENGTH_SHORT).show();

            }
        });

        linearlayout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.USER_==null)
                {
                    DialogMessage.ThongBao("Bạn cần đăng nhập,\nđể sử dụng tính năng đăng bài"
                            ,ActivityDetailHotel.this);
                }
                else
                {
                    if( MainActivity.USER_.getIdentifier().equals(hotel.getNguoiDang().getMaNguoiDang()))
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ActivityDetailHotel.this);
                        builder.setMessage("Bạn muốn xóa bài viết: "+hotel.getTenKhachSan()+"?");
                        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DialogMessage.ThongBao("Đã xóa bài viết"
                                                        ,ActivityDetailHotel.this);
                                                StorageService.DeleteFolserImage("Hotel/"+hotel.getID_Document(),
                                                        ActivityDetailHotel.this);
                                                DialogMessage.ThongBao("Đã xóa",ActivityDetailHotel.this);;
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
                                                        ,ActivityDetailHotel.this);
                                            }
                                        });
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();


                    }
                    else {
                        Toast.makeText(ActivityDetailHotel.this, "Đây không phải bài viết của bạn",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                //Toast.makeText(MainActivity.this,"Them bài dang khách san",Toast.LENGTH_SHORT).show();
            }
        });
        linearlayout_refesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Hotel").document(hotel.getID_Document())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Hotel hotelRefesh=HotelService.ReadHotelDocument(documentSnapshot);
                                hotel=hotelRefesh;
                                SetValueToControl();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogMessage.ThongBao("Lỗi tải lại",ActivityDetailHotel.this);
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

        AdapterNhanXet adapterNhanXet=new AdapterNhanXet(ActivityDetailHotel.this,hotel.getDanhGias()
                ,hotel.getID_Document(),2,false);
        lvDanhGia_dialog_layout_danh_gia.setAdapter(adapterNhanXet);
        lvDanhGia_dialog_layout_danh_gia.setLayoutManager(new LinearLayoutManager(ActivityDetailHotel.this
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
                ActivityDetailHotel.this);

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
                    String IDHotel=hotel.getID_Document();
                    firebaseFirestore.collection("Hotel").document(IDHotel)
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

                                        firebaseFirestore.collection("Hotel").document(IDHotel)
                                                .update("DanhGia",arrayNhanXet)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        DialogMessage.ThongBao("Thêm nhận xét thành công",
                                                                dialog.getContext());
                                                        AfterInsertNhanXet(IDHotel,firebaseFirestore);
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

                                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                            .update("DanhGia", FieldValue.arrayUnion(NhanXet))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    DialogMessage.ThongBao("Thêm nhận xét thành công",
                                                            dialog.getContext());
                                                    AfterInsertNhanXet(IDHotel,firebaseFirestore);
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

    private void AfterInsertNhanXet( String IDHotel,FirebaseFirestore firebaseFirestore)
    {
        firebaseFirestore.collection("Hotel").document(IDHotel)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getId()!=null)
                        {
                            hotel=HotelService.ReadHotelDocument(documentSnapshot);
                        }
                        SetValueToControl();
                        //dialog.dismiss();
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
        if(hotel.getNguoiDang()!=null)
        {
            String filePath="avarta/" + hotel.getNguoiDang().getAnhDaiDien();
            StorageService.LoadImageUri_Avarta(filePath,imgNguoiDung_detail_hotel,this);

            tvTenNguoiDung_detail_hotel.setText(hotel.getNguoiDang().getTenNguoiDang());
            tvNgayDang_detail_hotel.setText(DateTimeToString.Format(hotel.getNgayDang()));
            tvTieuDe_detail_hotel.setText(hotel.getTenKhachSan());
            tvMoTa_detail_hotel.setText(hotel.getMoTa());
            tvDiaChi_detail_hotel.setText(hotel.getDiaChi());

            if(hotel.getLuotThichs()!=null) {
                btnFavorite_detail_hotel.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                btnFavorite_detail_hotel.setTag(R.drawable.baseline_volunteer_activism_24_0);
                if (hotel.getLuotThichs().size() > 0)
                {
                    for (NguoiDang nguoiDang : hotel.getLuotThichs()) {
                        if(MainActivity.USER_!=null)
                        {
                            if (nguoiDang.getMaNguoiDang().equals(MainActivity.USER_.getIdentifier())) {
                                Log.e("UserState", "=>" + nguoiDang.getMaNguoiDang());
                                btnFavorite_detail_hotel.setTag(R.drawable.baseline_volunteer_activism_24);
                                btnFavorite_detail_hotel.setImageResource((int) btnFavorite_detail_hotel.getTag());
                            }
                        }
                    }
                    if (hotel.getLuotThichs().size() > 1) {
                        int countLuotThich = hotel.getLuotThichs().size() - 1;
                        tvCountFavorite_detail_hotel.setText(hotel.getLuotThichs().get(0).getTenNguoiDang() +
                                " và +" + countLuotThich);

                    } else {
                        tvCountFavorite_detail_hotel.setText(hotel.getLuotThichs().get(0).getTenNguoiDang());
                    }
                }
            }
            else {
                btnFavorite_detail_hotel.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                btnFavorite_detail_hotel.setTag(R.drawable.baseline_volunteer_activism_24_0);
            }
            if(hotel.getDanhGias()!=null)
            {
                float rate = 0;
                for (DanhGia danhGia : hotel.getDanhGias()) {
                    rate += danhGia.getRate();
                }
                rate = Math.round(rate / hotel.getDanhGias().size() * 10) / 10f;
                AvargarateRatingBar_detail_hotel.setRating(rate);
                AvargarateRatingBar_detail_hotel.setIsIndicator(true);
                tvCountDanhGia_detail_hotel.setText(rate+"\n"+"Đánh giá "+"("+hotel.getDanhGias().size()+")");

                ArrayList<DanhGia> dsDanhGia=new ArrayList<>();
                if(hotel.getDanhGias().size()<3)
                {
                    for (DanhGia danhGia:hotel.getDanhGias())
                    {
                        dsDanhGia.add(danhGia);
                    }
                }
                else {
                    dsDanhGia.add(hotel.getDanhGias().get(0));
                    dsDanhGia.add(hotel.getDanhGias().get(1));
                }
                AdapterNhanXet adapterNhanXet=new AdapterNhanXet(ActivityDetailHotel.this
                        ,dsDanhGia,hotel.getID_Document(),1,false);
                lvDanhGia_detail_hotel.setAdapter(adapterNhanXet);
                lvDanhGia_detail_hotel.setLayoutManager(new LinearLayoutManager(ActivityDetailHotel.this
                        , LinearLayoutManager.VERTICAL, false));
            }
            else
            {
                tvCountDanhGia_detail_hotel.setText("0.0\n"+"Đánh giá(0)");
                AvargarateRatingBar_detail_hotel.setIsIndicator(true);
            }
            if(hotel.getHoiDaps()!=null)
            {
                Adapter_listview_hoidap_ver1 adapter_listview_hoidap_ver1=new Adapter_listview_hoidap_ver1(hotel.getHoiDaps(),
                        ActivityDetailHotel.this,hotel.getID_Document(),false);
                adapter_listview_hoidap_ver1.notifyDataSetChanged();
                lvHoiDap_detail_hotel.setAdapter(adapter_listview_hoidap_ver1);
                lvHoiDap_detail_hotel.setLayoutManager(new LinearLayoutManager(ActivityDetailHotel.this,
                        RecyclerView.VERTICAL,false));
            }

            if(hotel.getHinhAnhs()!=null)
            {
                if(hotel.getHinhAnhs().size()>0)
                {
                    String rootFile= "Hotel/"+ hotel.getID_Document()+"/"+hotel.getHinhAnhs().get(0);
                    StorageService.LoadImageUri(rootFile,imgHinhAnhBaiDang_detail_hotel,this,1280,750);

                    Adapter_listview_images_ver1 adapter_listview_images_ver1=new
                            Adapter_listview_images_ver1(hotel.getHinhAnhs(),ActivityDetailHotel.this, hotel.getID_Document(),false);
                    recylistHinhAnh_detail_hotel.setAdapter(adapter_listview_images_ver1);
                    recylistHinhAnh_detail_hotel.setLayoutManager(new LinearLayoutManager(ActivityDetailHotel.this
                            , LinearLayoutManager.HORIZONTAL, false));
                }
            }
            if(hotel.getPhongs()!=null)
            {
                AdapterPhong adapterPhong=new AdapterPhong(hotel.getPhongs(),ActivityDetailHotel.this,
                        hotel.getID_Document(),false,false);
                lvPhong_detail_hotel.setAdapter(adapterPhong);
                lvPhong_detail_hotel.setLayoutManager(new LinearLayoutManager(ActivityDetailHotel.this
                        , LinearLayoutManager.HORIZONTAL, false));
            }

        }

    }
}