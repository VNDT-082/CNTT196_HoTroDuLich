package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailHotel;
import com.example.cntt196_hotrodulichfirebase.ActivityLogin;

public class DialogMessage {
    public static void ThongBao(String thongBao, Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(thongBao);
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void ThongBaoYeuCauDangNhap(String thongBao,Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(thongBao);
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Đăng nhập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intentLogin=new Intent(context, ActivityLogin.class);
                context.startActivity(intentLogin);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
