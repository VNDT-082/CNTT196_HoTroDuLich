package com.example.cntt196_hotrodulichfirebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravelAdmin;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivityTravelAdmin extends AppCompatActivity {

  
    private ImageButton btnBack_travel_admin, btnSearch_travel_admin
            ,btnRefesh_travel_admin, btnAdd_travel_admin;
    private EditText edtSearch_travel_admin;
    private RecyclerView recyclerView_travel_travel_admin;

    private ArrayList<Travel> travels;
    private AdapterTravelAdmin adaptertravelAdmin;
    public static User_ USER_;
    private void Init()
    {
        btnAdd_travel_admin=findViewById(R.id.btnAdd_travel_admin);
        btnBack_travel_admin=findViewById(R.id.btnBack_travel_admin);
        btnRefesh_travel_admin=findViewById(R.id.btnRefesh_travel_admin);
        btnSearch_travel_admin=findViewById(R.id.btnSearch_travel_admin);
        edtSearch_travel_admin=findViewById(R.id.edtSearch_travel_admin);
        recyclerView_travel_travel_admin=findViewById(R.id.recyclerView_travel_admin);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_admin);
        Init();
        this.USER_=MainActivityAdmin.USER_;
        btnBack_travel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        travels=new ArrayList<>();
        adaptertravelAdmin=new AdapterTravelAdmin(travels,ActivityTravelAdmin.this,true);
        recyclerView_travel_travel_admin.setAdapter(adaptertravelAdmin);
        recyclerView_travel_travel_admin.setLayoutManager(new LinearLayoutManager(ActivityTravelAdmin.this
                , LinearLayoutManager.VERTICAL, false));
        LoadListtravel();
        btnRefesh_travel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                travels=new ArrayList<>();
                adaptertravelAdmin=new AdapterTravelAdmin(travels,ActivityTravelAdmin.this, true);
                recyclerView_travel_travel_admin.setAdapter(adaptertravelAdmin);
                recyclerView_travel_travel_admin.setLayoutManager(new LinearLayoutManager(ActivityTravelAdmin.this
                        , LinearLayoutManager.VERTICAL, false));
                LoadListtravel();
            }
        });
        btnAdd_travel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityTravelAdmin.this,ActivityNewTravel.class);
                intent.putExtra("Author",1);
                ActivityTravelAdmin.this.startActivity(intent);
            }
        });
        btnSearch_travel_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtSearch_travel_admin.getText().equals(""))
                {
                    travels=new ArrayList<>();
                    adaptertravelAdmin=new AdapterTravelAdmin(travels,ActivityTravelAdmin.this,true);
                    recyclerView_travel_travel_admin.setAdapter(adaptertravelAdmin);
                    recyclerView_travel_travel_admin.setLayoutManager(new LinearLayoutManager(ActivityTravelAdmin.this
                            , LinearLayoutManager.VERTICAL, false));

                    TimKiem();
                }

            }
        });
    }
    private void TimKiem()
    {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Travel")
                .whereEqualTo("TenKhachSan",edtSearch_travel_admin.getText().toString().trim()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            if(queryDocumentSnapshots.size()>0)
                            {
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                {

                                    Travel travel= TravelService.ReadTravelDocument(document);
                                    travels.add(travel);
                                    Collections.sort(travels, new Comparator<Travel>() {
                                        @Override
                                        public int compare(Travel o1, Travel o2) {
                                            return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                        }
                                    });
                                    adaptertravelAdmin.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                LoadListGreaterOrEqual(firebaseFirestore,edtSearch_travel_admin.getText().toString().trim());
                            }
                        }
                        else
                        {
                            LoadListGreaterOrEqual(firebaseFirestore, edtSearch_travel_admin.getText().toString().trim());
                        }

                    }
                });

    }
    private void LoadListGreaterOrEqual(FirebaseFirestore firebaseFirestore,String strCompare)
    {
        //.whereGreaterThanOrEqualTo("TenKhachSan",edtSearch_travel_admin.getText().toString().trim())
        firebaseFirestore.collection("Travel")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                                Travel travel= TravelService.ReadTravelDocument(document);

                                if(travel.getTieuDe().contains(strCompare))
                                    travels.add(travel);

                                Collections.sort(travels, new Comparator<Travel>() {
                                    @Override
                                    public int compare(Travel o1, Travel o2) {
                                        return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                    }
                                });
                                adaptertravelAdmin.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
    private void LoadListtravel()
    {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Travel").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                                Travel travel= TravelService.ReadTravelDocument(document);
                                travels.add(travel);
                                Collections.sort(travels, new Comparator<Travel>() {
                                    @Override
                                    public int compare(Travel o1, Travel o2) {
                                        return o2.getNgayDang().compareTo(o1.getNgayDang());
                                    }
                                });
                                Collections.sort(travels, new Comparator<Travel>() {
                                    @Override
                                    public int compare(Travel o1, Travel o2) {
                                        return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                    }
                                });
                                adaptertravelAdmin.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
}