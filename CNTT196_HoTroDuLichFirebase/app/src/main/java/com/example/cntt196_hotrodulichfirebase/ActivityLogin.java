package com.example.cntt196_hotrodulichfirebase;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.UserService;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {

    private TextInputLayout editTextTextEmailAddress,editTextTextPassword;
    private Button btnLogin, btnRegister;
    private TextView tvQuenMatKhau;
    private ImageButton btnShowHiden_Register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
        mAuth = FirebaseAuth.getInstance();


        editTextTextEmailAddress.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strEmail= String.valueOf(editTextTextEmailAddress.getEditText().getText());
                if(!DateTimeToString.isEmailValid(strEmail))
                {
                    editTextTextEmailAddress.setError("Email không đúng định dạng");
                }
                else
                {
                    editTextTextEmailAddress.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextTextPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int passwordSize=editTextTextPassword.getEditText().getText().length();
                if(passwordSize<6)
                {
                    editTextTextPassword.setError("Mật khẩu có ít nhất 6 ký tự");
                }
                else
                {
                    editTextTextPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnShowHiden_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextTextPassword.getEditText().getInputType()== InputType.TYPE_TEXT_VARIATION_PASSWORD)
                {
                    editTextTextPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
                    editTextTextPassword.getEditText().setTransformationMethod(SingleLineTransformationMethod.getInstance());
                    btnShowHiden_Register.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editTextTextPassword.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            editTextTextPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                            btnShowHiden_Register.setImageResource(R.drawable.baseline_visibility_off_24);
                        }
                    },5000);
                }
                else
                {
                    editTextTextPassword.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextTextPassword.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnShowHiden_Register.setImageResource(R.drawable.baseline_visibility_off_24);
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityLogin.this, ActivityGetStared.class);
                ActivityLogin.this.startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextTextEmailAddress.getEditText().getText().toString().equals(""))
                {
                    editTextTextEmailAddress.setError("Email không được để trống");

                }
                else if( editTextTextPassword.getEditText().getText().toString().equals(""))
                {
                    editTextTextPassword.setError("Mật khẩu không được để trống");
                }
                else
                {
                    User_ user_=new User_();
                    user_.setIdentifier(editTextTextEmailAddress.getEditText().getText().toString());
                    user_.setPassword(editTextTextPassword.getEditText().getText().toString());

                    mAuth.signInWithEmailAndPassword(user_.getIdentifier(),user_.getPassword())
                            .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                                        firebaseFirestore.collection("UserInfo")
                                                .whereEqualTo("userName", user_.getIdentifier())
                                                .limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        if(queryDocumentSnapshots!=null)
                                                        {
                                                            DocumentSnapshot documentSnapshot=queryDocumentSnapshots
                                                                    .getDocuments().get(0);
                                                            User_ user= UserService.ReadUserDocument(documentSnapshot);
                                                            if(user.isActive())
                                                            {
                                                                Toast.makeText(ActivityLogin.this,
                                                                        "Đăng nhập thành công",
                                                                        Toast.LENGTH_SHORT).show();
                                                                Bundle bundle = new Bundle();
                                                                bundle.putSerializable("USER", user);
                                                                if(!user.isAuthor())
                                                                {
                                                                    Intent intent=new Intent(ActivityLogin.this, MainActivity.class);
                                                                    intent.putExtras(bundle);
                                                                    startActivity(intent);
                                                                }
                                                                else
                                                                {
                                                                    Intent intent=new Intent(ActivityLogin.this, MainActivityAdmin.class);
                                                                    intent.putExtras(bundle);
                                                                    startActivity(intent);
                                                                }
                                                                finish();
                                                            }
                                                            else {
                                                                DialogMessage.ThongBao("Tài khoản đã bị khóa",
                                                                        ActivityLogin.this);
                                                            }
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                                                        Toast.makeText(ActivityLogin.this,
                                                                "Lỗi không tìm thất tài khoản",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        editTextTextPassword.setError("Mật khẩu không đúng");

//                                        editTextTextPassword.getEditText().setText("");
                                    }
                                }
                            });
                }
            }
        });
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadDialogResetPass();
            }
        });
    }
    private void Init()
    {
        editTextTextEmailAddress=findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword=findViewById(R.id.editTextTextPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnDangKy);
        tvQuenMatKhau=findViewById(R.id.tvQuenMatKhau);
        btnShowHiden_Register=findViewById(R.id.btnShowHiden_Register);
    }
    private void LoadDialogResetPass()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_reset_password_ver1);
        dialog.setCancelable(false);

        TextInputLayout editTextTextEmailAddress_dialog_layout_reset_pass;
        ImageView imgNguoiDung_dialog_layout_reset_pass ;
        Button btnTimTaiKhoan_dialog_layout_reset_pass , btnReset_dialog_layout_reset_pass;
        TextView tvTenNguoiDung_dialog_layout_reset_pass, tvEmailNguoiDung_dialog_layout_reset_pass;
        ImageButton imageButtonCancel;
        CardView Cardview_imgNguoiDung_dialog_layout_reset_pass;
        editTextTextEmailAddress_dialog_layout_reset_pass = dialog.findViewById(R.id.editTextTextEmailAddress_dialog_layout_reset_pass);
        imgNguoiDung_dialog_layout_reset_pass = dialog.findViewById(R.id.imgNguoiDung_dialog_layout_reset_pass);
        btnTimTaiKhoan_dialog_layout_reset_pass = dialog.findViewById(R.id.btnTimTaiKhoan_dialog_layout_reset_pass);
        btnReset_dialog_layout_reset_pass = dialog.findViewById(R.id.btnReset_dialog_layout_reset_pass);
        tvTenNguoiDung_dialog_layout_reset_pass = dialog.findViewById(R.id.tvTenNguoiDung_dialog_layout_reset_pass);
        tvEmailNguoiDung_dialog_layout_reset_pass=dialog.findViewById(R.id.tvEmailNguoiDung_dialog_layout_reset_pass);
        imageButtonCancel=dialog.findViewById(R.id.imageButtonCancel);
        Cardview_imgNguoiDung_dialog_layout_reset_pass=dialog.findViewById(R.id.Cardview_imgNguoiDung_dialog_layout_reset_pass);

        //imgNguoiDung_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
        tvEmailNguoiDung_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
        tvTenNguoiDung_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
        btnReset_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
        Cardview_imgNguoiDung_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        imageButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.setCancelable(true);
                dialog.cancel();
            }
        });

        btnTimTaiKhoan_dialog_layout_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextTextEmailAddress_dialog_layout_reset_pass.getEditText().getText().toString().equals(""))
                {
                    FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("UserInfo").whereEqualTo("userName"
                                    ,editTextTextEmailAddress_dialog_layout_reset_pass.getEditText().getText().toString().trim())
                            .limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    tvEmailNguoiDung_dialog_layout_reset_pass.setVisibility(View.VISIBLE);
                                    tvTenNguoiDung_dialog_layout_reset_pass.setVisibility(View.VISIBLE);
                                    if(queryDocumentSnapshots!=null)
                                    {
                                        if(queryDocumentSnapshots.size()>0)
                                        {
                                            //imgNguoiDung_dialog_layout_reset_pass.setVisibility(View.VISIBLE);
                                            Cardview_imgNguoiDung_dialog_layout_reset_pass.setVisibility(View.VISIBLE);
                                            btnReset_dialog_layout_reset_pass.setVisibility(View.VISIBLE);
                                            for (DocumentSnapshot document:queryDocumentSnapshots )
                                            {
                                                User_ user_=UserService.ReadUserDocument(document);
                                                String filePath="avarta/" + user_.getAvarta();
                                                StorageService.LoadImageUri(filePath,imgNguoiDung_dialog_layout_reset_pass,
                                                        dialog.getContext(),180,240);

                                                tvTenNguoiDung_dialog_layout_reset_pass.setText(user_.getFullName());
                                                tvEmailNguoiDung_dialog_layout_reset_pass.setText(user_.getIdentifier());
                                            }
                                        }
                                        else {
                                            btnReset_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
                                            Cardview_imgNguoiDung_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
                                            tvTenNguoiDung_dialog_layout_reset_pass.setText("Không tìm thấy tài khoản.");
                                            tvEmailNguoiDung_dialog_layout_reset_pass.setText("");
                                        }
                                    }
                                    else {
                                        btnReset_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
                                        Cardview_imgNguoiDung_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
                                        tvTenNguoiDung_dialog_layout_reset_pass.setText("Không tìm thấy tài khoản.");
                                        tvEmailNguoiDung_dialog_layout_reset_pass.setText("");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    btnReset_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
                                    Cardview_imgNguoiDung_dialog_layout_reset_pass.setVisibility(View.INVISIBLE);
                                    tvEmailNguoiDung_dialog_layout_reset_pass.setVisibility(View.VISIBLE);
                                    tvTenNguoiDung_dialog_layout_reset_pass.setVisibility(View.VISIBLE);
                                    tvTenNguoiDung_dialog_layout_reset_pass.setText("Không tìm thấy tài khoản.");
                                    tvEmailNguoiDung_dialog_layout_reset_pass.setText("");
                                }
                            });
                }
                else {
                    DialogMessage.ThongBao("Bạn chưa nhập email để tìm kiếm",dialog.getContext());
                }

            }
        });
        btnReset_dialog_layout_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.sendPasswordResetEmail(tvEmailNguoiDung_dialog_layout_reset_pass.getText().toString().trim() )
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    DialogMessage.ThongBao("Mật khẩu mới đã gửi tới email của bạn.",dialog.getContext());
                                    dialog.dismiss();
                                }
                                else{
                                    DialogMessage.ThongBao("Lỗi cấp phát mật khẩu mới.",dialog.getContext());
                                }
                            }
                        });
            }
        });

    }
}