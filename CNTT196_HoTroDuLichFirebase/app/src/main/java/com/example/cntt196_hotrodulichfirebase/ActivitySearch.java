package com.example.cntt196_hotrodulichfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterHotelAdmin;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravelAdmin;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ActivitySearch extends AppCompatActivity {

    ImageButton btnSearch, btnBack;
    EditText edtSearch;
    CheckBox checkBoxTimKiem;
    RecyclerView listViewTimKiem;
    TextView NoTFound_TimKiem;
    private ArrayList<Hotel> Hotels;
    private ArrayList<Travel> Travels;
    private AdapterHotelAdmin adapterHotelSearch;
    private AdapterTravelAdmin adapterTravelSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Init();
        Hotels=new ArrayList<>();
        Travels=new ArrayList<>();
        adapterHotelSearch=new AdapterHotelAdmin(Hotels,ActivitySearch.this,false);
        adapterTravelSearch=new AdapterTravelAdmin(Travels,ActivitySearch.this,false);
        if(checkBoxTimKiem.getTag().equals(0))
        {
            listViewTimKiem.setAdapter(adapterHotelSearch);
            listViewTimKiem.setLayoutManager(new LinearLayoutManager(ActivitySearch.this
                    , LinearLayoutManager.VERTICAL, false));
        }
        else {
            listViewTimKiem.setAdapter(adapterTravelSearch);
            listViewTimKiem.setLayoutManager(new LinearLayoutManager(ActivitySearch.this
                    , LinearLayoutManager.VERTICAL, false));

        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(ActivitySearch.this, MainActivity.class);
//                ActivitySearch.this.startActivity(intent);
                finish();
            }
        });
        checkBoxTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxTimKiem.getTag().equals(0))
                {
                    checkBoxTimKiem.setChecked(true);
                    checkBoxTimKiem.setTag(1);
                    checkBoxTimKiem.setText("Tìm theo điểm đến du lịch.");
                }
                else
                {
                    checkBoxTimKiem.setChecked(true);
                    checkBoxTimKiem.setTag(0);
                    checkBoxTimKiem.setText("Tìm theo khách sạn.");
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBoxTimKiem.getTag().equals(0))
                {
                    Hotels=new ArrayList<>();
                    adapterHotelSearch=new AdapterHotelAdmin(Hotels,ActivitySearch.this,false);
                    listViewTimKiem.setAdapter(adapterHotelSearch);
                    listViewTimKiem.setLayoutManager(new LinearLayoutManager(ActivitySearch.this
                            , LinearLayoutManager.VERTICAL, false));
                    TimKiem("Hotel","TenKhachSan",false);
                }
                else
                {
                    Travels=new ArrayList<>();
                    adapterTravelSearch=new AdapterTravelAdmin(Travels,ActivitySearch.this,false);
                    listViewTimKiem.setAdapter(adapterTravelSearch);
                    listViewTimKiem.setLayoutManager(new LinearLayoutManager(ActivitySearch.this
                            , LinearLayoutManager.VERTICAL, false));
                    TimKiem("Travel","TieuDe",true);
                }

            }
        });
    }
    private void TimKiem(String collectionName,String fieldName,boolean IsTravel)
    {
        if(!edtSearch.getText().toString().equals(""))
        {
            FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
            firebaseFirestore.collection(collectionName)
                    .whereEqualTo(fieldName,edtSearch.getText().toString().trim()).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots != null)
                            {
                                if(queryDocumentSnapshots.size()>0)
                                {
                                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                    {

                                        if(IsTravel)
                                        {
                                            Travel travel= TravelService.ReadTravelDocument(document);
                                            Travels.add(travel);
                                            Collections.sort(Travels, new Comparator<Travel>() {
                                                @Override
                                                public int compare(Travel o1, Travel o2) {
                                                    return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                                }
                                            });
                                            adapterTravelSearch.notifyDataSetChanged();
                                        }
                                        else
                                        {
                                            Hotel hotel= HotelService.ReadHotelDocument(document);
                                            Hotels.add(hotel);
                                            Collections.sort(Hotels, new Comparator<Hotel>() {
                                                @Override
                                                public int compare(Hotel o1, Hotel o2) {
                                                    return Boolean.compare(o1.isTrangThai(),o2.isTrangThai());
                                                }
                                            });
                                            adapterHotelSearch.notifyDataSetChanged();
                                        }

                                    }
                                }
                                else
                                {
                                    LoadListGreaterOrEqual(firebaseFirestore,edtSearch.getText().toString().trim()
                                            ,collectionName,IsTravel);
                                }
                            }
                            else
                            {
                                LoadListGreaterOrEqual(firebaseFirestore, edtSearch.getText().toString().trim()
                                        ,collectionName,IsTravel);
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActivitySearch.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            DialogMessage.ThongBao("Bạn chưa nhập thông tin ở mục tìm kiếm",ActivitySearch.this);
        }

    }
    private void LoadListGreaterOrEqual(FirebaseFirestore firebaseFirestore,String strCompare
    ,String collectionName,boolean IsTravel)
    {
        //.whereGreaterThanOrEqualTo("TenKhachSan",edtSearch_hotel_admin.getText().toString().trim())
        firebaseFirestore.collection(collectionName)
                .whereEqualTo("TrangThai",true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            if(queryDocumentSnapshots.size()>0)
                            {
                                NoTFound_TimKiem.setVisibility(View.INVISIBLE);
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                {

                                    if(IsTravel)
                                    {
                                        Travel travel= TravelService.ReadTravelDocument(document);

                                        if(travel.getTieuDe().contains(strCompare))
                                            Travels.add(travel);

                                        Collections.sort(Travels, new Comparator<Travel>() {
                                            @Override
                                            public int compare(Travel o1, Travel o2) {
                                                return o2.getNgayDang().compareTo(o1.getNgayDang());
                                            }
                                        });
                                        adapterTravelSearch.notifyDataSetChanged();
                                    }
                                    else {
                                        Hotel hotel= HotelService.ReadHotelDocument(document);

                                        if(hotel.getTenKhachSan().contains(strCompare))
                                            Hotels.add(hotel);

                                        Collections.sort(Hotels, new Comparator<Hotel>() {
                                            @Override
                                            public int compare(Hotel o1, Hotel o2) {
                                                return o2.getNgayDang().compareTo(o1.getNgayDang());
                                            }
                                        });
                                        adapterHotelSearch.notifyDataSetChanged();
                                    }

                                }
                            }
                            else
                            {
                                DialogMessage.ThongBao("Không tìm thấy bài viết với: '"
                                        +strCompare+"'", ActivitySearch.this);
                            }
                        }
                        if(IsTravel)
                        {
                            if(Travels.size()<1)
                            {
                                NoTFound_TimKiem.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            if(Hotels.size()<1)
                            {
                                NoTFound_TimKiem.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        NoTFound_TimKiem.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void Init()
    {
        btnBack=findViewById(R.id.btnBack);
        btnSearch=findViewById(R.id.btnSearch);
        edtSearch=findViewById(R.id.edtSearch);
        NoTFound_TimKiem=findViewById(R.id.NoTFound_TimKiem);
        NoTFound_TimKiem.setVisibility(View.INVISIBLE);
        listViewTimKiem=findViewById(R.id.listViewTimKiem);
        checkBoxTimKiem=findViewById(R.id.checkBoxTimKiem);
        checkBoxTimKiem.setTag(1);

    }
}