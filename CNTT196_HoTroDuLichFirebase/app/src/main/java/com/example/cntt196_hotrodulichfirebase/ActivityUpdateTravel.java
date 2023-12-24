package com.example.cntt196_hotrodulichfirebase;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskHuyen;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskTinh;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskXa;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterHuyen;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTinh;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterXa;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_image_ver2;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver2_bitmap;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnhBitMap;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.HuyenModel;
import com.example.cntt196_hotrodulichfirebase.models.Phong;
import com.example.cntt196_hotrodulichfirebase.models.TinhModel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.example.cntt196_hotrodulichfirebase.models.XaModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityUpdateTravel extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton btnBack_new_Travel;
    private EditText edtGiaMin_New_Travel,edtGiaMax_New_Travel, edtTenDiaDanh_New_Travel, edtMoTa_New_Travel,
            edtDiaChi_New_Travel;
    private ImageView Image_New_Travel;
    private TextView edtTenXa_New_Travel, edtTenHuyen_New_Travel, edtTenTinh_New_Travel;
    private RecyclerView dsHinh_New_Travel;
    private Adapter_listview_image_ver2 adapter_listview_images_ver2;
    private Button btnDang_New_Travel;

    private ArrayList<TinhModel> DANHSACHTINH;
    private ArrayList<HuyenModel> DANHSACHHUYEN;
    private  ArrayList<XaModel> DANHSACHXA;
    private boolean IsAdmin;
    private Travel travel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_travel);
        Init();
        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            travel=(Travel) bundle.getSerializable("Travel");
            if(travel.getTieuDe()==null){finish();}
            Log.e("SpinerValue", "=>"+ travel.getTieuDe());

        }
        else
        {
            finish();
        }

        int authorNum=getIntent().getIntExtra("Author",0);
        if(authorNum==0)
        {
            finish();
            return;
        }
        else
        {
            if(authorNum==2)
            {
                IsAdmin=false;
            }
            else if(authorNum==1)
            {
                IsAdmin=true;
            }
            else
            {
                finish();
                return;
            }
        }

        Toast.makeText(this, IsAdmin?"Quản trị viên":"Người dùng", Toast.LENGTH_SHORT).show();

        SetValueToControl();

        btnBack_new_Travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDang_New_Travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangBai();
            }
        });




    }

    private void DangBai()
    {
        if(edtTenDiaDanh_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Tên địa danh không để trống",ActivityUpdateTravel.this);
        }
        else if(edtMoTa_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Mô tả địa danh không để trống",ActivityUpdateTravel.this);
        }
        else if(edtGiaMin_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Bạn chưa nhập chi phí tham khảo.\n" +
                    "Chi phí sẽ mặc định là 0 VNĐ",ActivityUpdateTravel.this);
            edtGiaMin_New_Travel.setText("0");
        }
        else if(edtGiaMax_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Bạn chưa nhập chi phí tham khảo.\n" +
                    "Chi phí sẽ mặc định là 0 VNĐ",ActivityUpdateTravel.this);
            edtGiaMin_New_Travel.setText("0");
            edtGiaMax_New_Travel.setText("0");
        }
        else if(edtDiaChi_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Địa chỉ địa danh không để trống",ActivityUpdateTravel.this);
        }
        else
        {
            String DiaChi=edtDiaChi_New_Travel.getText()+", "+edtTenXa_New_Travel.getText().toString().trim()
                    +", "+edtTenHuyen_New_Travel.getText().toString().trim()
                    +", " +edtTenTinh_New_Travel.getText().toString().trim();

            String IDTravel=travel.getID_Document();
            final boolean[] kq = {true};
            FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Travel").document(IDTravel)
            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    firebaseFirestore.collection("Travel").document(IDTravel)
                            .update("TieuDe", edtTenDiaDanh_New_Travel.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kq[0] =false;
                                    DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateTravel.this);
                                }
                            });
                    firebaseFirestore.collection("Travel").document(IDTravel)
                            .update("MoTa",edtMoTa_New_Travel.getText().toString().trim())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kq[0] =false;
                                    DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateTravel.this);
                                }
                            });
                    firebaseFirestore.collection("Travel").document(IDTravel)
                            .update("DiaChi",DiaChi)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kq[0] =false;
                                    DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateTravel.this);
                                }
                            });
                    firebaseFirestore.collection("Travel").document(IDTravel)
                            .update("DiaChi",DiaChi)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kq[0] =false;
                                    DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateTravel.this);
                                }
                            });
                    firebaseFirestore.collection("Travel").document(IDTravel)
                            .update("TrangThai",false)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kq[0] =false;
                                    DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateTravel.this);
                                }
                            });
                    firebaseFirestore.collection("Travel").document(IDTravel)
                            .update("HinhAnh",travel.getHinhAnhs())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    kq[0] =false;
                                    DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateTravel.this);
                                }
                            });
                    if(kq[0] ==true)
                    {
                        DialogMessage.ThongBao("Đã cập nhật bài đăng",ActivityUpdateTravel.this);
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
                    DialogMessage.ThongBao("Cập nhật thất bại",ActivityUpdateTravel.this);
                }
            });
        }
    }

    private  void Init()
    {
        btnBack_new_Travel=findViewById(R.id.btnBack_New_Travel);

        edtGiaMin_New_Travel=findViewById(R.id.edtGiaMin_New_Travel);
        edtGiaMax_New_Travel=findViewById(R.id.edtGiaMax_New_Travel);
        edtTenDiaDanh_New_Travel=findViewById(R.id.edtTenDiaDanh_New_Travel);
        edtMoTa_New_Travel=findViewById(R.id.edtMoTa_New_Travel);
        edtDiaChi_New_Travel=findViewById(R.id.edtDiaChi_New_Travel);

        edtTenXa_New_Travel=findViewById(R.id.edtTenXa_New_Travel);
        edtTenHuyen_New_Travel=findViewById(R.id.edtTenHuyen_New_Travel);
        edtTenTinh_New_Travel=findViewById(R.id.edtTenTinh_New_Travel);

        Image_New_Travel=findViewById(R.id.Image_New_Travel);
        dsHinh_New_Travel=findViewById(R.id.dsHinh_New_Travel);

        btnDang_New_Travel=findViewById(R.id.btnDang_New_Travel);

    }
    private void SetValueToControl()
    {
        edtTenDiaDanh_New_Travel.setText(travel.getTieuDe());
        edtMoTa_New_Travel.setText(travel.getMoTa());
        edtGiaMax_New_Travel.setText(String.valueOf(travel.getGiaMax()));
        edtGiaMin_New_Travel.setText(String.valueOf(travel.getGiaMax()));

        String[] DiaChiSplit=travel.getDiaChi().split(",");

        edtTenTinh_New_Travel.setText(DiaChiSplit[DiaChiSplit.length-1].trim());
        edtTenHuyen_New_Travel.setText(DiaChiSplit[DiaChiSplit.length-2].trim());
        edtTenXa_New_Travel.setText(DiaChiSplit[DiaChiSplit.length-3].trim());

        edtDiaChi_New_Travel.setText(DiaChiSplit[DiaChiSplit.length-4].trim());
        if(travel.getHinhAnhs()!=null)
        {
            if(travel.getHinhAnhs().size()>0)
            {
                String rootFile= "Travel/"+ travel.getID_Document()+"/"+travel.getHinhAnhs().get(0);
                StorageService.LoadImageUri(rootFile,Image_New_Travel,this,1280,750);

                adapter_listview_images_ver2=new Adapter_listview_image_ver2(travel.getHinhAnhs(),ActivityUpdateTravel.this
                        ,travel.getID_Document(),true,Image_New_Travel);
                dsHinh_New_Travel.setAdapter(adapter_listview_images_ver2);
                dsHinh_New_Travel.setLayoutManager(new LinearLayoutManager(ActivityUpdateTravel.this
                        , LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }

}