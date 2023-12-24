package com.example.cntt196_hotrodulichfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cntt196_hotrodulichfirebase.models.User_;

public class MainActivityAdmin extends AppCompatActivity {

    private ImageButton imgButton_Infor_main_admin, imgButton_Hotel_main_admin
            , imgButton_Travel_main_admin, imgButton_User_main_admin;
    private Button btnDangXuat_main_admin;
    public static User_ USER_;
    private void Init()
    {
        imgButton_Infor_main_admin=findViewById(R.id.imgButton_Infor_main_admin);
        imgButton_Hotel_main_admin=findViewById(R.id.imgButton_Hotel_main_admin);
        imgButton_Travel_main_admin=findViewById(R.id.imgButton_Travel_main_admin);
        imgButton_User_main_admin=findViewById(R.id.imgButton_User_main_admin);
        btnDangXuat_main_admin=findViewById(R.id.btnDangXuat_main_admin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Init();
        Intent intentLogin= getIntent();
        Bundle bundleLogin=intentLogin.getExtras();
        if(bundleLogin!=null)
        {
            USER_=(User_) bundleLogin.getSerializable("USER");
        }
        else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivityAdmin.this);
            builder.setMessage("Lỗi, không tìm thấy tài khoản\nVui long đăng nhập lại.");
            builder.setNegativeButton("Đăng nhập", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intentLogin=new Intent(MainActivityAdmin.this, ActivityLogin.class);
                    MainActivityAdmin.this.startActivity(intentLogin);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        imgButton_Hotel_main_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityAdmin.this,ActivityHotelAdmin.class);
                MainActivityAdmin.this.startActivity(intent);
            }
        });
        imgButton_User_main_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityAdmin.this,ActivityUserAdmin.class);
                MainActivityAdmin.this.startActivity(intent);
            }
        });
        imgButton_Travel_main_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityAdmin.this,ActivityTravelAdmin.class);
                MainActivityAdmin.this.startActivity(intent);
            }
        });
        btnDangXuat_main_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivityAdmin.this,ActivityLogin.class);
                MainActivityAdmin.this.startActivity(intent);
                USER_=null;
                finish();
            }
        });
    }
}