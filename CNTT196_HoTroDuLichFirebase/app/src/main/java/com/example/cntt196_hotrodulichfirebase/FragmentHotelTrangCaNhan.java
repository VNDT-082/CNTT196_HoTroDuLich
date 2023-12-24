package com.example.cntt196_hotrodulichfirebase;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterHotel;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravel;
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
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHotelTrangCaNhan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHotelTrangCaNhan extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private AdapterHotel adapterHotel;
    private Context context;
    private ListView recyclerView_hotel_frg_travel_trang_ca_nhan;
    private ArrayList<Hotel> arrayListHotels;

    public FragmentHotelTrangCaNhan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHotelTrangCaNhan.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHotelTrangCaNhan newInstance(String param1, String param2) {
        FragmentHotelTrangCaNhan fragment = new FragmentHotelTrangCaNhan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_hotel_trang_ca_nhan, container, false);
        Init(mView);
        context=requireContext();
        arrayListHotels=new ArrayList<>();
        adapterHotel=new AdapterHotel(arrayListHotels,context);
        recyclerView_hotel_frg_travel_trang_ca_nhan.setAdapter(adapterHotel);
        LoadListTravel();
        return mView;
    }
    private void Init(View view)
    {
        recyclerView_hotel_frg_travel_trang_ca_nhan=view.findViewById(R.id.recyclerView_hotel_frg_hotel_trang_ca_nhan);
    }
    private void LoadListTravel() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String,Object> NguoiDang= new HashMap<>();
        NguoiDang.put("AnhDaiDien",MainActivity.USER_.getAvarta());
        NguoiDang.put("MaNguoiDang",MainActivity.USER_.getIdentifier());
        NguoiDang.put("TenNguoiDang",MainActivity.USER_.getFullName());
        firebaseFirestore.collection("Hotel").whereEqualTo("NguoiDang",NguoiDang)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0)
                        {
                            for (DocumentSnapshot document:queryDocumentSnapshots)
                            {
                                Hotel hotel= HotelService.ReadHotelDocument(document);
                                arrayListHotels.add(hotel);
                                Collections.sort(arrayListHotels, new Comparator<Hotel>() {
                                    @Override
                                    public int compare(Hotel o1, Hotel o2) {
                                        return o2.getNgayDang().compareTo(o1.getNgayDang());
                                    }
                                });
                                adapterHotel.notifyDataSetChanged();
                            }
                        }
                        else
                        {
                            DialogMessage.ThongBao("Chưa có bài viết nào",context);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}