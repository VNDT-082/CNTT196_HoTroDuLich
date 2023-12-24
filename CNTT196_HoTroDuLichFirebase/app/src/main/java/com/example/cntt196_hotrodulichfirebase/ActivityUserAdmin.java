package com.example.cntt196_hotrodulichfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.UserService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravelAdmin;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterUser;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityUserAdmin extends AppCompatActivity {
    private ImageButton btnBack_user_admin, btnSearch_user_admin
            ,btnRefesh_user_admin, btnAdd_user_admin;
    private EditText edtSearch_user_admin;
    private RecyclerView recyclerView_user_user_admin;

    private ArrayList<User_> Users;
    private AdapterUser adapterUserAdmin;
    private void Init()
    {
        btnAdd_user_admin=findViewById(R.id.btnAdd_user_admin);
        btnBack_user_admin=findViewById(R.id.btnBack_user_admin);
        btnRefesh_user_admin=findViewById(R.id.btnRefesh_user_admin);
        btnSearch_user_admin=findViewById(R.id.btnSearch_user_admin);
        edtSearch_user_admin=findViewById(R.id.edtSearch_user_admin);
        recyclerView_user_user_admin=findViewById(R.id.recyclerView_user_user_admin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        Init();
        Users=new ArrayList<>();
        adapterUserAdmin=new AdapterUser(Users,ActivityUserAdmin.this);
        recyclerView_user_user_admin.setAdapter(adapterUserAdmin);
        recyclerView_user_user_admin.setLayoutManager(new LinearLayoutManager(ActivityUserAdmin.this,
                LinearLayoutManager.VERTICAL,false));
        LoadListUser();
        btnBack_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRefesh_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users=new ArrayList<>();
                adapterUserAdmin=new AdapterUser(Users,ActivityUserAdmin.this);
                recyclerView_user_user_admin.setAdapter(adapterUserAdmin);
                recyclerView_user_user_admin.setLayoutManager(new LinearLayoutManager(ActivityUserAdmin.this,
                        LinearLayoutManager.VERTICAL,false));
                LoadListUser();
            }
        });
        btnAdd_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnSearch_user_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtSearch_user_admin.getText().equals(""))
                {
                    Users=new ArrayList<>();
                    adapterUserAdmin=new AdapterUser(Users,ActivityUserAdmin.this);
                    recyclerView_user_user_admin.setAdapter(adapterUserAdmin);
                    recyclerView_user_user_admin.setLayoutManager(new LinearLayoutManager(ActivityUserAdmin.this,
                            LinearLayoutManager.VERTICAL,false));

                    TimKiem();
                }
            }
        });
    }
    private void TimKiem()
    {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("UserInfo")
                .whereEqualTo("userName",edtSearch_user_admin.getText().toString().trim()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            if(queryDocumentSnapshots.size()>0)
                            {
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                {

                                    User_ user_=UserService.ReadUserDocument(document);
                                    Users.add(user_);
                                    Collections.sort(Users, new Comparator<User_>() {
                                        @Override
                                        public int compare(User_ o1, User_ o2) {
                                            return Boolean.compare(o1.isAuthor(),o2.isAuthor());
                                        }
                                    });
                                    adapterUserAdmin.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
        firebaseFirestore.collection("UserInfo")
                .whereEqualTo("fullName",edtSearch_user_admin.getText().toString().trim()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            if(queryDocumentSnapshots.size()>0)
                            {
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                {

                                    User_ user_=UserService.ReadUserDocument(document);
                                    Users.add(user_);
                                    Collections.sort(Users, new Comparator<User_>() {
                                        @Override
                                        public int compare(User_ o1, User_ o2) {
                                            return Boolean.compare(o1.isAuthor(),o2.isAuthor());
                                        }
                                    });
                                    adapterUserAdmin.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                LoadListGreaterOrEqual(firebaseFirestore,edtSearch_user_admin.getText().toString().trim());
                            }
                        }
                        else
                        {
                            LoadListGreaterOrEqual(firebaseFirestore, edtSearch_user_admin.getText().toString().trim());
                        }

                    }
                });

    }
    private void LoadListGreaterOrEqual(FirebaseFirestore firebaseFirestore,String strCompare)
    {
        //.whereGreaterThanOrEqualTo("TenKhachSan",edtSearch_travel_admin.getText().toString().trim())
        firebaseFirestore.collection("UserInfo")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                                User_ user_=UserService.ReadUserDocument(document);

                                if(user_.getFullName().contains(strCompare))
                                    Users.add(user_);

                                Collections.sort(Users, new Comparator<User_>() {
                                    @Override
                                    public int compare(User_ o1, User_ o2) {
                                        return Boolean.compare(o1.isAuthor(),o2.isAuthor());
                                    }
                                });
                                adapterUserAdmin.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
    private void LoadListUser()
    {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("UserInfo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                               User_ user_= UserService.ReadUserDocument(document);
                                Users.add(user_);
                                Collections.sort(Users, new Comparator<User_>() {
                                    @Override
                                    public int compare(User_ o1, User_ o2) {
                                        return Boolean.compare(o1.isAuthor(),o2.isAuthor());
                                    }
                                });
                                Collections.sort(Users, new Comparator<User_>() {
                                    @Override
                                    public int compare(User_ o1, User_ o2) {
                                        return Boolean.compare(o1.isActive(),o2.isActive());
                                    }
                                });

                                adapterUserAdmin.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
}