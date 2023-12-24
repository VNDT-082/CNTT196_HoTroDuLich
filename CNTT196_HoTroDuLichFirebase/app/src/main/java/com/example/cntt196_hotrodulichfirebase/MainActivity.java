package com.example.cntt196_hotrodulichfirebase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.UserService;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fBtn;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    public static User_ USER_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();

        Intent intentLogin= getIntent();
        Bundle bundleLogin=intentLogin.getExtras();
        if(bundleLogin!=null)
        {
            USER_=(User_) bundleLogin.getSerializable("USER");
        }
        else
        {
            USER_=null;
        }

//        this.USER_=new User_();
//        this.USER_.setActive(true);
//        this.USER_.setAuthor(false);
//        this.USER_.setDateOfBirth(LocalDate.from(LocalDateTime.now()));
//        this.USER_.setFullName("Võ Nguyễn Duy Tân");
//        this.USER_.setAvarta("lisa.jpg");
//        this.USER_.setIdentifier("duytantt9@gmail.com");
//        this.USER_.setUser_UID("UbH5oNtTWwWQWTD6ueqcBAih1yC3");



        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(USER_!=null)
        {
            editor.putString("Email",USER_.getIdentifier());
            editor.apply();
        }
//        else
//        {
//            editor.putString("Email","");
//            editor.apply();
//        }

        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            Intent intent=new Intent(MainActivity.this,ActivityGetStared.class);
            MainActivity.this.startActivity(intent);

            editor.putBoolean("isFirstTime", false);
            editor.apply();
        }
        String EmailShared=sharedPreferences.getString("Email","");
        if(!EmailShared.equals(""))
        {
            FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection("UserInfo")
                    .whereEqualTo("userName",EmailShared)
                    .limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots!=null)
                            {
                                DocumentSnapshot documentSnapshot=queryDocumentSnapshots
                                        .getDocuments().get(0);
                                USER_= UserService.ReadUserDocument(documentSnapshot);
                            }
                            else
                            {
                                USER_=null;
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {
                            USER_=null;
                        }
                    });
        }

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new FragmentHome()).commit();

        }
        replaceFragment(new FragmentHome());

        navigationView.setCheckedItem(R.id.nav_personal);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.openDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.nav_personal:
                    {
                        if(USER_==null)
                        {
                            DialogMessage.ThongBaoYeuCauDangNhap("Bạn chưa đăng nhập vào hệ thống.\n Bạn có muốn đăng nhập ngay bây giờ?"
                                    ,MainActivity.this);
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, ActivityTrangCaNhan.class);
                            MainActivity.this.startActivity(intent);
                        }
                        break;
                    }
                    case R.id.top_hotel:
                        break;
                    case  R.id.top_tour:
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        setSubMenuSelected();//set click sub menu

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new FragmentHome());
                    break;
                case R.id.travel:
                    replaceFragment(new FragmentrTravel());
                    break;
                case R.id.hotel:
                    replaceFragment(new FragmentHotel());
                    break;
                case R.id.search:
                {
                    Intent intent=new Intent(MainActivity.this,ActivitySearch.class);
                    MainActivity.this.startActivity(intent);
                    break;
                }
            }
            return true;
        });

        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });


    }


    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private  void setSubMenuSelected()
    {
        SubMenu subMenu=navigationView.getMenu().findItem(R.id.parentmenu).getSubMenu();
        MenuItem nav_settings=subMenu.findItem(R.id.nav_settings);
        MenuItem nav_share=subMenu.findItem(R.id.nav_share);
        MenuItem nav_about=subMenu.findItem(R.id.nav_about);
        MenuItem nav_login=subMenu.findItem(R.id.nav_login);
        MenuItem nav_logout=subMenu.findItem(R.id.nav_logout);

        nav_settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Chức năng cài đặt hiện chưa hoàn thành", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        nav_share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Chức năng chia sẻ hiện chưa hoàn thành", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        nav_about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "Chức năng thông tin hiện chưa hoàn thành", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        nav_login.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(MainActivity.USER_!=null)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Bạn đang sử dụng tài khoản: \n" + MainActivity.USER_.getFullName()+
                            "\n("+MainActivity.USER_.getIdentifier()+")"+"\nBạn có muốn đăng nhập một tài khoản khác không?");
                    builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            USER_=null;
                            Log.d("Checked item", "ID: "+nav_login.getItemId());
                            Intent intent=new Intent(MainActivity.this,ActivityLogin.class);
                            MainActivity.this.startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    Intent intent=new Intent(MainActivity.this,ActivityLogin.class);
                    MainActivity.this.startActivity(intent);
                }

                return true;
            }
        });
        nav_logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(USER_!=null)
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Bạn có muốn đăng xuất?");
                    builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            USER_=null;
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("Email","");
                            editor.apply();
                            Toast.makeText(MainActivity.this,"Đã đăng xuất", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
               else
                {
                    DialogMessage.ThongBao("Bạn chưa đăng nhập.",MainActivity.this);
                }
                return true;
            }
        });

    }
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_bottom_sheet);

        LinearLayout linearlayout_DiemDuLich = dialog.findViewById(R.id.linearlayout_DiemDuLich);
        LinearLayout linearlayout_KhachSan = dialog.findViewById(R.id.linearlayout_KhachSan);

        linearlayout_DiemDuLich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(USER_==null)
                {
                    DialogMessage.ThongBao("Bạn cần đăng nhập,\nđể sử dụng tính năng đăng bài"
                    ,MainActivity.this);
                }
                else
                {
                    dialog.dismiss();
                    Intent intent =new Intent(MainActivity.this,ActivityNewTravel.class);
                    intent.putExtra("Author",2);
                    MainActivity.this.startActivity(intent);
                }

                //Toast.makeText(MainActivity.this, "Them bai dang du lich", Toast.LENGTH_SHORT).show();

            }
        });

        linearlayout_KhachSan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(USER_==null)
                {
                    DialogMessage.ThongBao("Bạn cần đăng nhập,\nđể sử dụng tính năng đăng bài"
                            ,MainActivity.this);
                }
                else
                {
                    dialog.dismiss();
                    Intent intent =new Intent(MainActivity.this,ActivityNewHotel.class);
                    intent.putExtra("Author",2);
                    MainActivity.this.startActivity(intent);
                }

                //Toast.makeText(MainActivity.this,"Them bài dang khách san",Toast.LENGTH_SHORT).show();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private void Init()
    {
        fBtn=findViewById(R.id.fbtn);
        drawerLayout=findViewById(R.id.drawer_layout);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        //toolbar=findViewById(R.id.toolbar);
        navigationView=findViewById(R.id.navigation_view);

    }


}