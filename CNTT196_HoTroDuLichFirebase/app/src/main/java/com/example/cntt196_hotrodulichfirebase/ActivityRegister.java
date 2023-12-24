package com.example.cntt196_hotrodulichfirebase;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.AcountService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.AuthenticationService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityRegister extends AppCompatActivity {

    private TextInputLayout edtHoVaTen,edtEmail,edtPassword, tvNgaySinh, edtRePassword;
    private RadioGroup radioGroup;
    private RadioButton radioNam, radioNu;
    private Button btnDangKy;
    private TextView tvDagnNhap;//, tvNgaySinh;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateOf;

    private AuthenticationService mAuth;
    private AcountService mAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=new AuthenticationService(ActivityRegister.this);
        mAcc=new AcountService();
        Init();
        KhoiTaoEdtNgaySinh();
        tvNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ActivityRegister.this, dateOf,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        tvNgaySinh.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ActivityRegister.this, dateOf,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        tvDagnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ActivityRegister.this, ActivityLogin.class);
                ActivityRegister.this.startActivity(intent);
            }
        });

        edtHoVaTen.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String hoTen=edtHoVaTen.getEditText().getText().toString().trim();
                if( DateTimeToString.isNoneNumber(hoTen))
                {
                    edtHoVaTen.setError("Họ tên không được phép tồn tại ký số");
                }
                else
                {
                    edtHoVaTen.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strEmail= String.valueOf(edtEmail.getEditText().getText());
                if(!DateTimeToString.isEmailValid(strEmail))
                {
                    edtEmail.setError("Email không đúng định dạng");
                }
                else
                {
                    edtEmail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int passwordSize=edtPassword.getEditText().getText().length();
                if(passwordSize<6)
                {
                    edtPassword.setError("Mật khẩu có ít nhất 6 ký tự");
                }
                else
                {
                    edtPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtRePassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String matKhau=edtPassword.getEditText().getText().toString();
                String nhapLaiMatKhau=edtRePassword.getEditText().getText().toString();
                if(matKhau.equals(nhapLaiMatKhau))
                {
                    edtRePassword.setError(null);
                }
                else
                {
                    edtRePassword.setError("Mật khẩu không khớp");
                }
        }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvNgaySinh.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Date date=calendar.getTime();
                if(calendar.getTime().compareTo(new Date())>0)
                {
                    tvNgaySinh.setError("Ngày sinh không đúng");
                }
                else
                {
                    tvNgaySinh.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangKy();
            }
        });
    }

    private void DangKy()
    {
            if(edtHoVaTen.getError()!=null)
            {
                DialogMessage.ThongBao("Vui lòng nhập hợp lệ các thông tin",ActivityRegister.this);
            }
            else if(edtEmail.getError()!=null)
            {
                DialogMessage.ThongBao("Vui lòng nhập hợp lệ các thông tin",ActivityRegister.this);
            }
            else if(edtEmail.getError()!=null)
            {
                DialogMessage.ThongBao("Vui lòng nhập hợp lệ các thông tin",ActivityRegister.this);
            }
            else if( edtRePassword.getError()!=null)
            {
                DialogMessage.ThongBao("Vui lòng nhập hợp lệ các thông tin",ActivityRegister.this);
            }
            else if(tvNgaySinh.getError()!=null)
            {
                DialogMessage.ThongBao("Vui lòng nhập hợp lệ các thông tin",ActivityRegister.this);
            }
            else {
                String hoTen = edtHoVaTen.getEditText().getText().toString().trim();
                String email = edtEmail.getEditText().getText().toString().trim();
                String matKhau = edtPassword.getEditText().getText().toString();
                Timestamp ngaySinh = new Timestamp(calendar.getTime());
                boolean gioiTinh = (radioNam.isChecked()) ? true : false;

                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                mAuth.createUserWithEmailAndPassword(email, matKhau)
                        .addOnCompleteListener(ActivityRegister.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    FirebaseFirestore firebaseFirestore;
                                    firebaseFirestore = FirebaseFirestore.getInstance();
                                    Map<String, Object> NewUser = new HashMap<>();
                                    String DocumentId = DateTimeToString.GenarateID();
                                    NewUser.put("active", true);
                                    NewUser.put("author", false);
                                    NewUser.put("avarta", "default.png");

                                    NewUser.put("dateOfBirth", ngaySinh);

                                    NewUser.put("fullName", hoTen);
                                    NewUser.put("gen", gioiTinh);
                                    NewUser.put("lastConnect", Timestamp.now());
                                    NewUser.put("userID", user.getUid());
                                    NewUser.put("userName", user.getEmail());
                                    firebaseFirestore.collection("UserInfo")
                                            .document(DocumentId)
                                            .set(NewUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    DialogMessage.ThongBao("Đăng ký thành công", ActivityRegister.this);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    DialogMessage.ThongBao("Đăng ký không thành công", ActivityRegister.this);
                                                }
                                            });
                                }

                            }
                        });
            }


    }

    private void Init()
    {
        edtEmail=findViewById(R.id.edtEmail);
        edtHoVaTen=findViewById(R.id.edtHoVaTen);
        edtPassword=findViewById(R.id.edtPassword);
        edtRePassword=findViewById(R.id.edtRePassword);
        tvNgaySinh=findViewById(R.id.tvNgaySinh);
        btnDangKy=findViewById(R.id.btnDangKy_register);
        tvDagnNhap=findViewById(R.id.tvDangNhap);
        radioGroup = findViewById(R.id.radioGroup);
        radioNam=findViewById(R.id.radioNam);
        radioNu=findViewById(R.id.radioNu);

        edtHoVaTen.setError("Bắt buộc");
        edtEmail.setError("Bắt buộc");
        edtPassword.setError("Bắt buộc");
        edtRePassword.setError("Bắt buộc");
        tvNgaySinh.setError("Bắt buộc");
    }
    private void KhoiTaoEdtNgaySinh()
    {
        DateFormat fmtDate=DateFormat.getDateInstance();
        calendar=Calendar.getInstance();

        dateOf=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateDateLabel(calendar);

            }
        };

    }
    public void updateDateLabel(Calendar calendar)
    {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        tvNgaySinh.getEditText().setText(simpleDateFormat.format(calendar.getTime()));
    }
}