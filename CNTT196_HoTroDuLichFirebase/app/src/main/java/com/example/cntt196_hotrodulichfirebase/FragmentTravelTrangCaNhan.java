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
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravel;
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
 * Use the {@link FragmentTravelTrangCaNhan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTravelTrangCaNhan extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mView;
    private AdapterTravel adapterTravel;
    private Context context;
    private ListView recyclerView_travel_frg_travel_trang_ca_nhan;
    private ArrayList<Travel> arrayListTravel;

    public FragmentTravelTrangCaNhan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentTravelTrangCaNhan.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTravelTrangCaNhan newInstance(String param1, String param2) {
        FragmentTravelTrangCaNhan fragment = new FragmentTravelTrangCaNhan();
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

        mView=  inflater.inflate(R.layout.fragment_travel_trang_ca_nhan, container, false);
        Init(mView);
        context=requireContext();
        arrayListTravel=new ArrayList<>();
        adapterTravel=new AdapterTravel(arrayListTravel,context);
        recyclerView_travel_frg_travel_trang_ca_nhan.setAdapter(adapterTravel);
        LoadListTravel();
        return mView;
    }
    private void Init(View view)
    {
        recyclerView_travel_frg_travel_trang_ca_nhan=view.findViewById(R.id.recyclerView_travel_frg_travel_trang_ca_nhan);
    }
    private void LoadListTravel() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String,Object>NguoiDang= new HashMap<>();
        NguoiDang.put("AnhDaiDien",MainActivity.USER_.getAvarta());
        NguoiDang.put("MaNguoiDang",MainActivity.USER_.getIdentifier());
        NguoiDang.put("TenNguoiDang",MainActivity.USER_.getFullName());
        firebaseFirestore.collection("Travel").whereEqualTo("NguoiDang",NguoiDang)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size()>0)
                        {
                            for (DocumentSnapshot document:queryDocumentSnapshots)
                            {
                                Travel travel = TravelService.ReadTravelDocument(document);
                                arrayListTravel.add(travel);
                                Collections.sort(arrayListTravel, new Comparator<Travel>() {
                                    @Override
                                    public int compare(Travel o1, Travel o2) {
                                        return o2.getNgayDang().compareTo(o1.getNgayDang());
                                    }
                                });
                                adapterTravel.notifyDataSetChanged();
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