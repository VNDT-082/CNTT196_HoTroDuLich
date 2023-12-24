package com.example.cntt196_hotrodulichfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;

public class ActivityTrangCaNhan extends AppCompatActivity {

    private FrameLayout frame_layout_Trang_Ca_Nhan;
    private ImageView imgNguoiDung_hotel_custom;
    private TextView tvTenNguoiDung_Trang_Ca_Nhan;
    private ImageButton btnBack_Trang_Ca_Nhan;
    private Button buttonThinTinCaNhan_Trang_Ca_Nhan, buttonTravel_Trang_Ca_Nhan, buttonHotel_Trang_Ca_Nhan;
    private void Init()
    {
        frame_layout_Trang_Ca_Nhan=findViewById(R.id.frame_layout_Trang_Ca_Nhan);
        imgNguoiDung_hotel_custom=findViewById(R.id.imgNguoiDung_hotel_custom);
        tvTenNguoiDung_Trang_Ca_Nhan=findViewById(R.id.tvTenNguoiDung_Trang_Ca_Nhan);
        buttonThinTinCaNhan_Trang_Ca_Nhan=findViewById(R.id.buttonThinTinCaNhan_Trang_Ca_Nhan);
        buttonTravel_Trang_Ca_Nhan=findViewById(R.id.buttonTravel_Trang_Ca_Nhan);
        buttonHotel_Trang_Ca_Nhan=findViewById(R.id.buttonHotel_Trang_Ca_Nhan);
        btnBack_Trang_Ca_Nhan=findViewById(R.id.btnBack_Trang_Ca_Nhan);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_ca_nhan);
        Init();
        String filePath="avarta/" + MainActivity.USER_.getAvarta();
        StorageService.LoadImageUri(filePath,imgNguoiDung_hotel_custom,ActivityTrangCaNhan.this,720,720);
        tvTenNguoiDung_Trang_Ca_Nhan.setText(MainActivity.USER_.getFullName());
        replaceFragment(new FragmentThongTinCaNhan());
        btnBack_Trang_Ca_Nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonHotel_Trang_Ca_Nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragmentHotelTrangCaNhan());
            }
        });
        buttonTravel_Trang_Ca_Nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragmentTravelTrangCaNhan());
            }
        });
        buttonThinTinCaNhan_Trang_Ca_Nhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FragmentThongTinCaNhan());
            }
        });
    }
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_Trang_Ca_Nhan, fragment);
        fragmentTransaction.commit();
    }
}