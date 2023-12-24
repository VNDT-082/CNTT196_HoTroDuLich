package com.example.cntt196_hotrodulichfirebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskHuyen;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskTinh;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskXa;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterNhanXet;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterPhong;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_hoidap_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_image_ver2;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver2_bitmap;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnhBitMap;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.HuyenModel;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.Phong;
import com.example.cntt196_hotrodulichfirebase.models.TinhModel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.XaModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityUpdateHotel extends AppCompatActivity {
    
    private ImageButton btnBack_new_hotel;
    private EditText edtTenHotel_New_Hotel, edtMoTa_New_Hotel, edtDiaChi_New_Hotel;
    
    private TextView edtTenXa_New_Hotel, edtTenHuyen_New_Hotel, edtTenTinh_New_Hotel;
    private ImageView Image_New_Hotel;
    private RecyclerView dsHinh_New_Hotel, dsPhong_New_Hotel;
    private Button btnDang_New_Hotel, btnThemPhong_New_Hotel, btnChonAnh_New_Hotel;

    private RadioButton radioButton1sao_new_hotel, radioButton2sao_new_hotel, radioButton3sao_new_hotel,
            radioButton4sao_new_hotel, radioButton5sao_new_hotel;
    private Adapter_listview_image_ver2 adapter_listview_images_ver2;

    private Hotel hotel;
    private boolean IsAdmin;
    public ActivityUpdateHotel() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hotel);
        Init();
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            hotel=(Hotel) bundle.getSerializable("Hotel");
            if(hotel.getTenKhachSan()==null){finish();}

        }
        else
        {
            finish();
        }
        int authorNum=getIntent().getIntExtra("Author",0);
        Log.e("AuthoNum","=>"+authorNum);
        if(authorNum==0)
        {
            finish();
            Log.e("AuthoNum0","=>"+authorNum);
            return;
        }
        else
        {
            if(authorNum==2)
            {
                Log.e("AuthoNum2","=>"+authorNum);
                IsAdmin=false;
            }
            else if(authorNum==1)
            {
                Log.e("AuthoNum1","=>"+authorNum);
                IsAdmin=true;
            }
            else
            {
                Log.e("AuthoNum-1","=>"+authorNum);
                finish();
                return;
            }
        }
        Toast.makeText(this, IsAdmin?"Quản trị viên":"Người dùng", Toast.LENGTH_SHORT).show();
        // HINHANHPHONG=new ArrayList<>();


        SetValueToControl();

        btnBack_new_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        


        btnDang_New_Hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangBai();
            }
        });

    }


    


    private  void Init()
    {
        btnBack_new_hotel=findViewById(R.id.btnBack_new_hotel);
        edtTenHotel_New_Hotel=findViewById(R.id.edtTenHotel_New_Hotel);
        edtDiaChi_New_Hotel=findViewById(R.id.edtDiaChi_New_Hotel);
        edtMoTa_New_Hotel=findViewById(R.id.edtMoTa_New_Hotel);
        Image_New_Hotel=findViewById(R.id.Image_New_Hotel);
        dsHinh_New_Hotel=findViewById(R.id.dsHinh_New_Hotel);
        dsPhong_New_Hotel=findViewById(R.id.dsPhong_New_Hotel);
        btnDang_New_Hotel=findViewById(R.id.btnDang_New_Hotel);
        btnThemPhong_New_Hotel=findViewById(R.id.btnThemPhong_New_Hotel);
        btnChonAnh_New_Hotel=findViewById(R.id.btnChonAnh_New_Hotel);

        radioButton1sao_new_hotel=findViewById(R.id.radioButton1sao_new_hotel);
        radioButton2sao_new_hotel=findViewById(R.id.radioButton2sao_new_hotel);
        radioButton3sao_new_hotel=findViewById(R.id.radioButton3sao_new_hotel);
        radioButton4sao_new_hotel=findViewById(R.id.radioButton4sao_new_hotel);
        radioButton5sao_new_hotel=findViewById(R.id.radioButton5sao_new_hotel);

        edtTenXa_New_Hotel=findViewById(R.id.edtTenXa_New_Hotel);
        edtTenHuyen_New_Hotel=findViewById(R.id.edtTenHuyen_New_Hotel);
        edtTenTinh_New_Hotel=findViewById(R.id.edtTenTinh_New_Hotel);
    }
    private void DangBai()
    {
        if(edtTenHotel_New_Hotel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Tên khách sạn không để trống",ActivityUpdateHotel.this);
        }
        else if(edtMoTa_New_Hotel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Mô tả khách sạn không để trống",ActivityUpdateHotel.this);
        }
        else if(edtDiaChi_New_Hotel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Địa chỉ khách sạn không để trống",ActivityUpdateHotel.this);
        }
        else
        {
            String DiaChi=edtDiaChi_New_Hotel.getText()+", "+edtTenXa_New_Hotel.getText()
                    +", "+edtTenHuyen_New_Hotel.getText()+", " +edtTenTinh_New_Hotel.getText();
            int hangSaoUpate=1;
            if(radioButton1sao_new_hotel.isChecked())
            {
                hangSaoUpate=1;
            }
            else if(radioButton2sao_new_hotel.isChecked())
            {
                hangSaoUpate=2;
            }
            else if(radioButton3sao_new_hotel.isChecked())
            {
                hangSaoUpate=3;
            }
            else if(radioButton4sao_new_hotel.isChecked())
            {
                hangSaoUpate=4;
            }
            else
            {
                hangSaoUpate=5;
            }
            String IDHotel=hotel.getID_Document();
            final boolean[] kq = {true};
            FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
            int finalHangSaoUpate = hangSaoUpate;
            firebaseFirestore.collection("Hotel").document(IDHotel)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("TenKhachSan",edtTenHotel_New_Hotel.getText().toString().trim())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });

                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("MoTa",edtMoTa_New_Hotel.getText().toString().trim())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });

                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("DiaChi",DiaChi)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });

                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("DiaChi",DiaChi)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });

                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("HangSao", finalHangSaoUpate)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });

                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("TrangThai", false)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });


                            ArrayList<Map<String,Object>> arrayMapPhong=new ArrayList<>();
                            for(Phong phong:hotel.getPhongs())
                            {
                                Map<String,Object> mapPhong=new HashMap<>();
                                mapPhong.put("GiaMax",phong.getGiaMax());
                                mapPhong.put("GiaMin",phong.getGiaMin());
                                mapPhong.put("HinhAnh",phong.getHinhAnh());
                                mapPhong.put("SoGiuong", phong.getSoGiuong());
                                arrayMapPhong.add(mapPhong);
                            }
                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("Phong",arrayMapPhong)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });
                            firebaseFirestore.collection("Hotel").document(IDHotel)
                                    .update("HinhAnh",hotel.getHinhAnhs())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            kq[0] =false;
                                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                                        }
                                    });
                            if(kq[0] ==true)
                            {
                                DialogMessage.ThongBao("Đã cập nhật bài đăng",ActivityUpdateHotel.this);
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },1500);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateHotel.this);
                        }
                    });

        }


    }
    private void SetValueToControl()
    {
        if(hotel.getNguoiDang()!=null)
        {
            edtTenHotel_New_Hotel.setText(hotel.getTenKhachSan());
            edtMoTa_New_Hotel.setText(hotel.getMoTa());
            if(hotel.getHangSao()==1)
            {
                radioButton1sao_new_hotel.setChecked(true);
            }
            else if(hotel.getHangSao()==2)
            {
                radioButton2sao_new_hotel.setChecked(true);
            }
            else if(hotel.getHangSao()==3)
            {
                radioButton3sao_new_hotel.setChecked(true);
            }
            else if(hotel.getHangSao()==4)
            {
                radioButton4sao_new_hotel.setChecked(true);
            }
            else
            {
                radioButton5sao_new_hotel.setChecked(true);
            }
            String[] DiaChiSplit=hotel.getDiaChi().split(",");
            Log.e("HotelUpdate","=>"+hotel.getDiaChi());
            edtTenTinh_New_Hotel.setText(DiaChiSplit[DiaChiSplit.length-1].trim());
            edtTenHuyen_New_Hotel.setText(DiaChiSplit[DiaChiSplit.length-2].trim());
            edtTenXa_New_Hotel.setText(DiaChiSplit[DiaChiSplit.length-3].trim());

            edtDiaChi_New_Hotel.setText(DiaChiSplit[DiaChiSplit.length-4].trim());

            if(hotel.getHinhAnhs()!=null)
            {
                if(hotel.getHinhAnhs().size()>0)
                {
                    String rootFile= "Hotel/"+ hotel.getID_Document()+"/"+hotel.getHinhAnhs().get(0);
                    StorageService.LoadImageUri(rootFile,Image_New_Hotel,this,1280,750);

                    adapter_listview_images_ver2=new
                            Adapter_listview_image_ver2(hotel.getHinhAnhs(),ActivityUpdateHotel.this,
                            hotel.getID_Document(),false,Image_New_Hotel);
                    dsHinh_New_Hotel.setAdapter(adapter_listview_images_ver2);
                    dsHinh_New_Hotel.setLayoutManager(new LinearLayoutManager(ActivityUpdateHotel.this
                            , LinearLayoutManager.HORIZONTAL, false));
                }
            }
            if(hotel.getPhongs()!=null)
            {
                AdapterPhong adapterPhong=new AdapterPhong(hotel.getPhongs(),ActivityUpdateHotel.this,
                        hotel.getID_Document(),false,true);
                dsPhong_New_Hotel.setAdapter(adapterPhong);
                dsPhong_New_Hotel.setLayoutManager(new LinearLayoutManager(ActivityUpdateHotel.this
                        , LinearLayoutManager.HORIZONTAL, false));
            }

        }

    }
}