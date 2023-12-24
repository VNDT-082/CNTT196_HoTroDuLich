package com.example.cntt196_hotrodulichfirebase;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskHuyen;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskTinh;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.ContacTaskXa;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterPhong;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_images_ver2_bitmap;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnhBitMap;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.HuyenModel;
import com.example.cntt196_hotrodulichfirebase.models.Phong;
import com.example.cntt196_hotrodulichfirebase.models.TinhModel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.example.cntt196_hotrodulichfirebase.models.XaModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityNewHotel extends AppCompatActivity {

    /////////////--------------dialog_variable-----------------------///////////////////
    Button btnDong_New_Hotel, btnThemPhong_New_Phong, btnChonAnhPhong_New_Hotel;
    EditText edtGiaMin_New_Phong,edtGiaMax_New_Phong;
    ImageView ImagePhong_New_Hotel;
    RadioButton radioButton1Giuong_dialog_them_phong , radioButtonKhongXacDinh_dialog_them_phong
            ,radioButton2Giuong_dialog_them_phong;
    Dialog dialog;// = new Dialog(ActivityNewHotel.this);
    private void InitDialog(Dialog dialog)
    {
        //final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_phong_hotel_ver1);
        dialog.setCancelable(false);

        btnDong_New_Hotel = dialog.findViewById(R.id.btnDong_New_Hotel);
        btnThemPhong_New_Phong = dialog.findViewById(R.id.btnThemPhong_New_Hotel);
        btnChonAnhPhong_New_Hotel=dialog.findViewById(R.id.btnChonAnhPhong_New_Hotel);

        edtGiaMin_New_Phong = dialog.findViewById(R.id.edtGiaMin_New_Phong);
        edtGiaMax_New_Phong = dialog.findViewById(R.id.edtGiaMax_New_Phong);
        ImagePhong_New_Hotel = dialog.findViewById(R.id.ImagePhong_New_Hotel);

        radioButton1Giuong_dialog_them_phong = dialog.findViewById(R.id.radioButton1Giuong_dialog_them_phong);
        radioButton2Giuong_dialog_them_phong = dialog.findViewById(R.id.radioButton2Giuong_dialog_them_phong);
        radioButtonKhongXacDinh_dialog_them_phong = dialog.findViewById(R.id.radioButtonKhongXacDinh_dialog_them_phong);
        radioButtonKhongXacDinh_dialog_them_phong.setChecked(true);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////

    class MyLifecycleObserver implements DefaultLifecycleObserver {
        private final ActivityResultRegistry mRegistry;
        private ActivityResultLauncher<String> mGetContent;
        private ImageView imageView;
        MyLifecycleObserver(@NonNull ActivityResultRegistry registry,ImageView imageView) {
            mRegistry = registry;
            this.imageView=imageView;
        }

        public void onCreate(@NonNull LifecycleOwner owner) {
            mGetContent = mRegistry.register("key", owner, new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            // Handle the returned Uri
                            try {
                                Bitmap bitmapImage= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                                imageView.setImageBitmap(bitmapImage);

                                HINHANHPHONG=new HinhAnhBitMap();
                                HINHANHPHONG.setUri(uri);
                                HINHANHPHONG.setBitmap(bitmapImage);
                                int IndexImage=dsPhong.size()+1;
                                HINHANHPHONG.setTenHinhAnh("image_phong_"+IndexImage+".jpg");
                                Toast.makeText(ActivityNewHotel.this,HINHANHPHONG.getTenHinhAnh(),Toast.LENGTH_LONG).show();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
        }
        public void selectImage() {
            // Open the activity to select an image
            mGetContent.launch("image/*");
        }
    }


    ////////////////////////////////___________________//////////////////////////

    private ImageButton  btnBack_new_hotel;
    private EditText  edtTenHotel_New_Hotel, edtMoTa_New_Hotel, edtDiaChi_New_Hotel;
    private Spinner spinnerTinh_New_Hotel, spinnerHuyen_New_Hotel, spinnerXa_New_Hotel;
    private ImageView Image_New_Hotel;
    private ImageButton expanSpiner_Xa_New_Hotel, expanSpiner_Huyen_New_Hotel, expanSpiner_Tinh_New_Hotel;
    private RecyclerView dsHinh_New_Hotel, dsPhong_New_Hotel;
    private Button btnDang_New_Hotel, btnThemPhong_New_Hotel, btnChonAnh_New_Hotel;

    private RadioButton radioButton1sao_new_hotel, radioButton2sao_new_hotel, radioButton3sao_new_hotel,
            radioButton4sao_new_hotel, radioButton5sao_new_hotel;

    private ArrayList<HinhAnhBitMap> dsHinh;
    private ArrayList<HinhAnhBitMap> dsHinhPhong;
    private ArrayList<Phong> dsPhong;
    private ArrayList<TinhModel> DANHSACHTINH;
    private ArrayList<HuyenModel> DANHSACHHUYEN;
    private  ArrayList<XaModel> DANHSACHXA;
    private HinhAnhBitMap HINHANHPHONG; //luu anh phong dialog
    private MyLifecycleObserver mObserverNewPhong;//open ActivityResultLauncher dialog
    private boolean IsAdmin;
    public ActivityNewHotel() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hotel);
        Init();
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
        dsHinh=new ArrayList<>();
        dsHinhPhong=new ArrayList<>();
        dsPhong=new ArrayList<>();
       // HINHANHPHONG=new ArrayList<>();


        DANHSACHTINH=new ArrayList<>();
        ContacTaskTinh contacTaskTinh=new ContacTaskTinh(spinnerTinh_New_Hotel,DANHSACHTINH,ActivityNewHotel.this);
        contacTaskTinh.execute();

        dialog = new Dialog(ActivityNewHotel.this);
        InitDialog(dialog);
        mObserverNewPhong = new MyLifecycleObserver(ActivityNewHotel.this.getActivityResultRegistry(),
                ImagePhong_New_Hotel);
        getLifecycle().addObserver(mObserverNewPhong);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                Intent data=result.getData();
                                Uri uriData=data.getData();
                                try {
                                    Bitmap bitmapImage= MediaStore.Images.Media.getBitmap(getContentResolver(),uriData);
                                    Image_New_Hotel.setImageBitmap(bitmapImage);
                                    HinhAnhBitMap hinhAnhBitMap=new HinhAnhBitMap();
                                    hinhAnhBitMap.setBitmap(bitmapImage);
                                    hinhAnhBitMap.setUri(uriData);
                                    int IndexImage=dsHinh.size()+1;
                                    hinhAnhBitMap.setTenHinhAnh("image_"+IndexImage+".jpg");
                                    dsHinh.add(hinhAnhBitMap);
                                    Adapter_listview_images_ver2_bitmap adapter_listview_images_ver2_bitmap=new
                                            Adapter_listview_images_ver2_bitmap(dsHinh,ActivityNewHotel.this, Image_New_Hotel);
                                    dsHinh_New_Hotel.setAdapter(adapter_listview_images_ver2_bitmap);
                                    dsHinh_New_Hotel.setLayoutManager(new LinearLayoutManager(ActivityNewHotel.this
                                            , LinearLayoutManager.HORIZONTAL, false));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            Toast.makeText(ActivityNewHotel.this, "Chua chon hinh anh", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        spinnerTinh_New_Hotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TinhModel tinhModel= (TinhModel) parent.getItemAtPosition(position);
                if(tinhModel!=null)
                {
                    spinnerTinh_New_Hotel.setTag(tinhModel.getTenTinh());
                    DANHSACHHUYEN=new ArrayList<>();
                    ContacTaskHuyen contacTaskHuyen=new ContacTaskHuyen(spinnerHuyen_New_Hotel,DANHSACHHUYEN
                            ,ActivityNewHotel.this, tinhModel.getMaTinh());
                    contacTaskHuyen.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerHuyen_New_Hotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HuyenModel huyenModel= (HuyenModel) parent.getItemAtPosition(position);
                if(huyenModel!=null)
                {
                    spinnerHuyen_New_Hotel.setTag(huyenModel.getTenHuyen());
                    DANHSACHXA=new ArrayList<>();
                    ContacTaskXa contacTaskXa=new ContacTaskXa(spinnerXa_New_Hotel,DANHSACHXA
                            ,ActivityNewHotel.this,huyenModel.getMaHuyen());
                    contacTaskXa.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerXa_New_Hotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                XaModel xaModel= (XaModel) parent.getItemAtPosition(position);
                if(xaModel!=null)
                {
                    spinnerXa_New_Hotel.setTag(xaModel.getTenXa());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btnChonAnh_New_Hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);
            }
        });


        btnBack_new_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnThemPhong_New_Hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogAddPhong();
            }
        });


        btnDang_New_Hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangBai();
            }
        });

    }

    private void ShowDialogAddPhong() {

        btnDong_New_Hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btnChonAnhPhong_New_Hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserverNewPhong.selectImage();
            }
        });

        btnThemPhong_New_Phong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(edtGiaMin_New_Phong.getText().toString().equals(""))
                {
                    DialogMessage.ThongBao("Bạn chưa nhập chi phí tham khảo.\n" +
                            "Chi phí sẽ mặc định là 0 VNĐ",ActivityNewHotel.this);
                    edtGiaMin_New_Phong.setText("0");
                }
                else if(edtGiaMax_New_Phong.getText().toString().equals(""))
                {
                    DialogMessage.ThongBao("Bạn chưa nhập chi phí tham khảo.\n" +
                            "Chi phí sẽ mặc định là 0 VNĐ",ActivityNewHotel.this);
                    edtGiaMax_New_Phong.setText("0");
                    edtGiaMin_New_Phong.setText("0");
                }
                else if(HINHANHPHONG.getBitmap()==null)
                {
                    DialogMessage.ThongBao("Phải chọn ít nhất một ảnh",ActivityNewHotel.this);
                }
                else
                 {
                     Phong NewPhong = new Phong();
                     if(radioButton1Giuong_dialog_them_phong.isChecked()==true)
                     {
                         NewPhong.setSoGiuong(Long.parseLong("1"));
                     }
                     else if(radioButton2Giuong_dialog_them_phong.isChecked()==true)
                     {
                         NewPhong.setSoGiuong(Long.parseLong("2"));
                     }
                     else
                     {
                         NewPhong.setSoGiuong(Long.parseLong("3"));
                     }
                    NewPhong.setGiaMin(Long.parseLong(edtGiaMin_New_Phong.getText().toString().trim()));
                    NewPhong.setGiaMax(Long.parseLong(edtGiaMax_New_Phong.getText().toString().trim()));
                    NewPhong.setBitmapHinhAnh(HINHANHPHONG.getBitmap());
                    NewPhong.setUri(HINHANHPHONG.getUri());
                    NewPhong.setHinhAnh(HINHANHPHONG.getTenHinhAnh());
                    dsPhong.add(NewPhong);
                    //dsHinh.add(HINHANHPHONG);
                     dsHinhPhong.add(HINHANHPHONG);
                     AdapterPhong adapterPhong=new AdapterPhong(dsPhong,ActivityNewHotel.this,
                             "",true,true);
                     dsPhong_New_Hotel.setAdapter(adapterPhong);
                     dsPhong_New_Hotel.setLayoutManager(new LinearLayoutManager(ActivityNewHotel.this
                             , LinearLayoutManager.HORIZONTAL, false));
                     dialog.dismiss();
                     HINHANHPHONG=null;
                     radioButtonKhongXacDinh_dialog_them_phong.setChecked(true);
                     edtGiaMax_New_Phong.setText("");
                     edtGiaMin_New_Phong.setText("");
                     ImagePhong_New_Hotel.setImageResource(R.drawable.default_image_empty);
                 }
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

    }



    private  void Init()
    {
        btnBack_new_hotel=findViewById(R.id.btnBack_new_hotel);
        edtTenHotel_New_Hotel=findViewById(R.id.edtTenHotel_New_Hotel);
        edtDiaChi_New_Hotel=findViewById(R.id.edtDiaChi_New_Hotel);
        edtMoTa_New_Hotel=findViewById(R.id.edtMoTa_New_Hotel);
        spinnerTinh_New_Hotel=findViewById(R.id.spinnerTinh_New_Hotel);
        spinnerHuyen_New_Hotel=findViewById(R.id.spinnerHuyen_New_Hotel);
        spinnerXa_New_Hotel=findViewById(R.id.spinnerXa_New_Hotel);
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

        expanSpiner_Tinh_New_Hotel=findViewById(R.id.expanSpiner_Tinh_New_Hotel);
        expanSpiner_Huyen_New_Hotel=findViewById(R.id.expanSpiner_Huyen_New_Hotel);
        expanSpiner_Xa_New_Hotel=findViewById(R.id.expanSpiner_Xa_New_Hotel);
    }
    private void DangBai()
    {
        if(edtTenHotel_New_Hotel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Tên khách sạn không để trống",ActivityNewHotel.this);
        }
        else if(edtMoTa_New_Hotel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Mô tả khách sạn không để trống",ActivityNewHotel.this);
        }
        else if(edtDiaChi_New_Hotel.getText().toString().equals(""))
        {
            DialogMessage.ThongBao("Địa chỉ khách sạn không để trống",ActivityNewHotel.this);
        }
        else if(dsHinh.size()<1)
        {
            DialogMessage.ThongBao("Phải chọn ít nhất một ảnh",ActivityNewHotel.this);
        }
        else
        {
            Map<String,Object> NewHotel=new HashMap<>();
            String DocumentId= DateTimeToString.GenarateID(IsAdmin?MainActivityAdmin.USER_.getUser_UID()
                    :MainActivity.USER_.getUser_UID());
            NewHotel.put("TenKhachSan",edtTenHotel_New_Hotel.getText().toString().trim());
            NewHotel.put("MoTa",edtMoTa_New_Hotel.getText().toString().trim());
            String DiaChi=edtDiaChi_New_Hotel.getText()+", "+spinnerXa_New_Hotel.getTag()
                    +", "+spinnerHuyen_New_Hotel.getTag()+", " +spinnerTinh_New_Hotel.getTag();
            NewHotel.put("DiaChi",DiaChi);

            if(radioButton1sao_new_hotel.isChecked())
            {
                NewHotel.put("HangSao", 1);
            }
            else if(radioButton2sao_new_hotel.isChecked())
            {
                NewHotel.put("HangSao", 2);
            }
            else if(radioButton3sao_new_hotel.isChecked())
            {
                NewHotel.put("HangSao", 3);
            }
            else if(radioButton4sao_new_hotel.isChecked())
            {
                NewHotel.put("HangSao", 4);
            }
            else
            {
                NewHotel.put("HangSao", 5);
            }

            NewHotel.put("TrangThai",IsAdmin?true:false);
            NewHotel.put("NgayDang", Timestamp.now());

            Map<String,Object> NguoiDungMap=new HashMap<>();
            if(IsAdmin)
            {
                NguoiDungMap.put("AnhDaiDien",ActivityHotelAdmin.USER_.getAvarta());
                NguoiDungMap.put("MaNguoiDang",ActivityHotelAdmin.USER_.getIdentifier());
                NguoiDungMap.put("TenNguoiDang",ActivityHotelAdmin.USER_.getFullName());
            }
            else
            {
                NguoiDungMap.put("AnhDaiDien",MainActivity.USER_.getAvarta());
                NguoiDungMap.put("MaNguoiDang",MainActivity.USER_.getIdentifier());
                NguoiDungMap.put("TenNguoiDang",MainActivity.USER_.getFullName());
            }
            NewHotel.put("NguoiDang",NguoiDungMap);

            ArrayList<Map<String,Object>> arrayMapPhong=new ArrayList<>();
            for(Phong phong:dsPhong)
            {
                Map<String,Object> mapPhong=new HashMap<>();
                mapPhong.put("GiaMax",phong.getGiaMax());
                mapPhong.put("GiaMin",phong.getGiaMin());
                mapPhong.put("HinhAnh",phong.getHinhAnh());
                mapPhong.put("SoGiuong", phong.getSoGiuong());
                arrayMapPhong.add(mapPhong);
                HinhAnhBitMap hinhAnhBitMap=new HinhAnhBitMap();
                hinhAnhBitMap.setBitmap(phong.getBitmapHinhAnh());
                hinhAnhBitMap.setTenHinhAnh(phong.getHinhAnh());
                hinhAnhBitMap.setUri(phong.getUri());
                dsHinh.add(hinhAnhBitMap);
            }
            NewHotel.put("Phong",arrayMapPhong);

            NewHotel.put("DanhGia",null);
            NewHotel.put("HoiDap",null);
            NewHotel.put("LuotThich",null);

            ArrayList<String> arrayHinhAnh=new ArrayList<>();
            for (HinhAnhBitMap hinhAnhBitMap : dsHinh)
            {
                arrayHinhAnh.add(hinhAnhBitMap.getTenHinhAnh());
            }
//            for (HinhAnhBitMap hinhAnhBitMap : dsHinhPhong)
//            {
//                arrayHinhAnh.add(hinhAnhBitMap.getTenHinhAnh());
//            }
            NewHotel.put("HinhAnh",arrayHinhAnh);

            Log.e("NewHotel","=>"+NewHotel);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Hotel")
                    .document(DocumentId)
                    .set(NewHotel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            StorageService.UploadImage("Hotel/"+DocumentId+"/", dsHinh, ActivityNewHotel.this);
                            DialogMessage.ThongBao("Đăng bài thành công",ActivityNewHotel.this);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogMessage.ThongBao("Lỗi đăng bài",ActivityNewHotel.this);
                        }
                    });
        }
            

    }
}