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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskHuyen;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskTinh;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskXa;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityNewTravel extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton btnBack_new_Travel;
    private EditText edtGiaMin_New_Travel,edtGiaMax_New_Travel, edtTenDiaDanh_New_Travel, edtMoTa_New_Travel,
            edtDiaChi_New_Travel;
    private Spinner spinnerTinh_New_Travel, spinnerHuyen_New_Travel, spinnerXa_New_Travel;
    private ImageView Image_New_Travel;
    private ImageButton expanSpiner_Xa_New_Travel, expanSpiner_Huyen_New_Travel, expanSpiner_Tinh_New_Travel;
    private RecyclerView dsHinh_New_Travel;
    private Button btnDang_New_Travel, btnChonAnh_New_Travel;

    private ArrayList<HinhAnhBitMap> dsHinh;
    private Travel Travel;
    private ArrayList<TinhModel> DANHSACHTINH;
    private ArrayList<HuyenModel> DANHSACHHUYEN;
    private  ArrayList<XaModel> DANHSACHXA;
    private boolean IsAdmin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);
        Init();

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
        dsHinh=new ArrayList<>();
        DANHSACHTINH=new ArrayList<>();
        ContacTaskTinh contacTaskTinh=new ContacTaskTinh(spinnerTinh_New_Travel,DANHSACHTINH,ActivityNewTravel.this);
        contacTaskTinh.execute();

        spinnerTinh_New_Travel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TinhModel tinhModel= (TinhModel) parent.getItemAtPosition(position);
                if(tinhModel!=null)
                {
                    spinnerTinh_New_Travel.setTag(tinhModel.getTenTinh());
                    DANHSACHHUYEN=new ArrayList<>();
                    ContacTaskHuyen contacTaskHuyen=new ContacTaskHuyen(spinnerHuyen_New_Travel,DANHSACHHUYEN
                            ,ActivityNewTravel.this, tinhModel.getMaTinh());
                    contacTaskHuyen.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerHuyen_New_Travel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HuyenModel huyenModel= (HuyenModel) parent.getItemAtPosition(position);
                if(huyenModel!=null)
                {
                    spinnerHuyen_New_Travel.setTag(huyenModel.getTenHuyen());
                    DANHSACHXA=new ArrayList<>();
                    ContacTaskXa contacTaskXa=new ContacTaskXa(spinnerXa_New_Travel,DANHSACHXA
                            ,ActivityNewTravel.this,huyenModel.getMaHuyen());
                    contacTaskXa.execute();

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerXa_New_Travel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                XaModel xaModel= (XaModel) parent.getItemAtPosition(position);
                if(xaModel!=null)
                {
                    spinnerXa_New_Travel.setTag(xaModel.getTenXa());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnBack_new_Travel.setOnClickListener(new View.OnClickListener() {
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
                            Image_New_Travel.setImageBitmap(bitmapImage);
                            HinhAnhBitMap hinhAnhBitMap=new HinhAnhBitMap();
                            hinhAnhBitMap.setBitmap(bitmapImage);
                            hinhAnhBitMap.setUri(uriData);
                            int IndexImage=dsHinh.size()+1;
                            hinhAnhBitMap.setTenHinhAnh("image_"+IndexImage+".jpg");
                            dsHinh.add(hinhAnhBitMap);
                            Adapter_listview_images_ver2_bitmap adapter_listview_images_ver2_bitmap=new
                                    Adapter_listview_images_ver2_bitmap(dsHinh,ActivityNewTravel.this,Image_New_Travel);
                            dsHinh_New_Travel.setAdapter(adapter_listview_images_ver2_bitmap);
                            dsHinh_New_Travel.setLayoutManager(new LinearLayoutManager(ActivityNewTravel.this
                                    , LinearLayoutManager.HORIZONTAL, false));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    Toast.makeText(ActivityNewTravel.this, "Chua chon hinh anh", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnChonAnh_New_Travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);


            }
        });

        btnDang_New_Travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangBai();
            }
        });



//        expanSpiner_Xa_New_Travel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
    }
    private void DangBai()
    {
        if(edtTenDiaDanh_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Tên địa danh không để trống",ActivityNewTravel.this);
        }
        else if(edtMoTa_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Mô tả địa danh không để trống",ActivityNewTravel.this);
        }
        else if(edtGiaMin_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Bạn chưa nhập chi phí tham khảo.\n" +
                    "Chi phí sẽ mặc định là 0 VNĐ",ActivityNewTravel.this);
            edtGiaMin_New_Travel.setText("0");
        }
        else if(edtGiaMax_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Bạn chưa nhập chi phí tham khảo.\n" +
                    "Chi phí sẽ mặc định là 0 VNĐ",ActivityNewTravel.this);
            edtGiaMin_New_Travel.setText("0");
            edtGiaMax_New_Travel.setText("0");
        }
        else if(edtDiaChi_New_Travel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Địa chỉ địa danh không để trống",ActivityNewTravel.this);
        }
        else if(dsHinh.size()<=0)
        {
            DialogMessage.ThongBao("Phải chọn ít nhất một ảnh",ActivityNewTravel.this);
        }
        else
        {
            Map<String,Object> NewTravel=new HashMap<>();
            String DocumentId= DateTimeToString.GenarateID(MainActivity.USER_.getUser_UID());
            NewTravel.put("TieuDe",edtTenDiaDanh_New_Travel.getText().toString().trim());
            NewTravel.put("MoTa",edtMoTa_New_Travel.getText().toString().trim());
            String DiaChi=edtDiaChi_New_Travel.getText()+", "+spinnerXa_New_Travel.getTag()
                    +", "+spinnerHuyen_New_Travel.getTag()+", " +spinnerTinh_New_Travel.getTag();
            NewTravel.put("DiaChi",DiaChi);
            NewTravel.put("TrangThai",false);
            NewTravel.put("NgayDang", Timestamp.now());

            NewTravel.put("GiaMin",Long.parseLong(edtGiaMin_New_Travel.getText().toString().trim()));
            NewTravel.put("GiaMax",Long.parseLong(edtGiaMax_New_Travel.getText().toString().trim()));

            Map<String,Object> NguoiDungMap=new HashMap<>();
            NguoiDungMap.put("AnhDaiDien",MainActivity.USER_.getAvarta());
            NguoiDungMap.put("MaNguoiDang",MainActivity.USER_.getIdentifier());
            NguoiDungMap.put("TenNguoiDang",MainActivity.USER_.getFullName());
            NewTravel.put("NguoiDang",NguoiDungMap);

            NewTravel.put("DanhGia",null);
            NewTravel.put("HoiDap",null);
            NewTravel.put("LuotThich",null);

            ArrayList<String> arrayHinhAnh=new ArrayList<>();
            for (HinhAnhBitMap hinhAnhBitMap : dsHinh)
            {
                arrayHinhAnh.add(hinhAnhBitMap.getTenHinhAnh());
            }
            NewTravel.put("HinhAnh",arrayHinhAnh);

            Log.e("NewTravel","=>"+NewTravel);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Travel")
                    .document(DocumentId)
                    .set(NewTravel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            StorageService.UploadImage("Travel/"+DocumentId+"/", dsHinh, ActivityNewTravel.this);
                            DialogMessage.ThongBao("Đăng bài thành công",ActivityNewTravel.this);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogMessage.ThongBao("Lỗi đăng bài",ActivityNewTravel.this);
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

        spinnerTinh_New_Travel=findViewById(R.id.spinnerTinh_New_Travel);
        spinnerHuyen_New_Travel=findViewById(R.id.spinnerHuyen_New_Travel);
        spinnerXa_New_Travel=findViewById(R.id.spinnerXa_New_Travel);

        Image_New_Travel=findViewById(R.id.Image_New_Travel);
        dsHinh_New_Travel=findViewById(R.id.dsHinh_New_Travel);

        btnDang_New_Travel=findViewById(R.id.btnDang_New_Travel);
        btnChonAnh_New_Travel=findViewById(R.id.btnChonAnh_New_Travel);

        expanSpiner_Tinh_New_Travel=findViewById(R.id.expanSpiner_Tinh_New_Travel);
        expanSpiner_Huyen_New_Travel=findViewById(R.id.expanSpiner_Huyen_New_Travel);
        expanSpiner_Xa_New_Travel=findViewById(R.id.expanSpiner_Xa_New_Travel);
    }

}