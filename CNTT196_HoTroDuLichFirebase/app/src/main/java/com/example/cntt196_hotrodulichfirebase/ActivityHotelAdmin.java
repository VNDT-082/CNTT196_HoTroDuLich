package com.example.cntt196_hotrodulichfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterHotelAdmin;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityHotelAdmin extends AppCompatActivity {

    private ImageButton btnBack_hotel_admin, btnSearch_hotel_admin
            ,btnRefesh_hotel_admin, btnAdd_hotel_admin;
    private EditText edtSearch_hotel_admin;
    private RecyclerView recyclerView_hotel_hotel_admin;

    private ArrayList<Hotel> Hotels;
    private AdapterHotelAdmin adapterHotelAdmin;
    public static User_ USER_;
    private void Init()
    {
        btnAdd_hotel_admin=findViewById(R.id.btnAdd_hotel_admin);
        btnBack_hotel_admin=findViewById(R.id.btnBack_hotel_admin);
        btnRefesh_hotel_admin=findViewById(R.id.btnRefesh_hotel_admin);
        btnSearch_hotel_admin=findViewById(R.id.btnSearch_hotel_admin);
        edtSearch_hotel_admin=findViewById(R.id.edtSearch_hotel_admin);
        recyclerView_hotel_hotel_admin=findViewById(R.id.recyclerView_hotel_hotel_admin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_admin);
        Init();
        this.USER_=MainActivityAdmin.USER_;
        btnBack_hotel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Hotels=new ArrayList<>();
        adapterHotelAdmin=new AdapterHotelAdmin(Hotels,ActivityHotelAdmin.this,true);
        recyclerView_hotel_hotel_admin.setAdapter(adapterHotelAdmin);
        recyclerView_hotel_hotel_admin.setLayoutManager(new LinearLayoutManager(ActivityHotelAdmin.this
                , LinearLayoutManager.VERTICAL, false));
        LoadListHotel();
        btnRefesh_hotel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hotels=new ArrayList<>();
                adapterHotelAdmin=new AdapterHotelAdmin(Hotels,ActivityHotelAdmin.this, true);
                recyclerView_hotel_hotel_admin.setAdapter(adapterHotelAdmin);
                recyclerView_hotel_hotel_admin.setLayoutManager(new LinearLayoutManager(ActivityHotelAdmin.this
                        , LinearLayoutManager.VERTICAL, false));
                LoadListHotel();
            }
        });
        btnAdd_hotel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityHotelAdmin.this,ActivityNewHotel.class);
                intent.putExtra("Author",1);
                ActivityHotelAdmin.this.startActivity(intent);
            }
        });
        btnSearch_hotel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtSearch_hotel_admin.getText().equals(""))
                {
                    Hotels=new ArrayList<>();
                    adapterHotelAdmin=new AdapterHotelAdmin(Hotels,ActivityHotelAdmin.this,true);
                    recyclerView_hotel_hotel_admin.setAdapter(adapterHotelAdmin);
                    recyclerView_hotel_hotel_admin.setLayoutManager(new LinearLayoutManager(ActivityHotelAdmin.this
                            , LinearLayoutManager.VERTICAL, false));

                    TimKiem();
                }

            }
        });
    }
    private void TimKiem()
    {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Hotel")
                .whereEqualTo("TenKhachSan",edtSearch_hotel_admin.getText().toString().trim()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            if(queryDocumentSnapshots.size()>0)
                            {
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                {

                                    Hotel hotel= HotelService.ReadHotelDocument(document);
                                    Hotels.add(hotel);
                                    Collections.sort(Hotels, new Comparator<Hotel>() {
                                        @Override
                                        public int compare(Hotel o1, Hotel o2) {
                                            return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                        }
                                    });
                                    adapterHotelAdmin.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                LoadListGreaterOrEqual(firebaseFirestore,edtSearch_hotel_admin.getText().toString().trim());
                            }
                        }
                        else
                        {
                            LoadListGreaterOrEqual(firebaseFirestore, edtSearch_hotel_admin.getText().toString().trim());
                        }

                    }
                });

    }
    private void LoadListGreaterOrEqual(FirebaseFirestore firebaseFirestore,String strCompare)
    {
        //.whereGreaterThanOrEqualTo("TenKhachSan",edtSearch_hotel_admin.getText().toString().trim())
        firebaseFirestore.collection("Hotel")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                                Hotel hotel= HotelService.ReadHotelDocument(document);

                                if(hotel.getTenKhachSan().contains(strCompare))
                                    Hotels.add(hotel);

                                Collections.sort(Hotels, new Comparator<Hotel>() {
                                    @Override
                                    public int compare(Hotel o1, Hotel o2) {
                                        return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                    }
                                });
                                adapterHotelAdmin.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
    private void LoadListHotel()
    {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Hotel").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                                Hotel hotel= HotelService.ReadHotelDocument(document);
                                Hotels.add(hotel);
                                Collections.sort(Hotels, new Comparator<Hotel>() {
                                    @Override
                                    public int compare(Hotel o1, Hotel o2) {
                                        return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                    }
                                });
                                adapterHotelAdmin.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
}