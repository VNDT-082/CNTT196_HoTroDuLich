package com.example.cntt196_hotrodulichfirebase;

import android.animation.Animator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TinhService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterHotel;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravel;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_tinh_ver1;
import com.example.cntt196_hotrodulichfirebase.adapters.Adapter_listview_tinh_ver2;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.LuotThich;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.Phong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHotel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHotel extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mView;
    //Adapter
    private AdapterHotel adapterHotel;
    private Adapter_listview_tinh_ver1 adapter_listview_tinh_ver1;
    private Adapter_listview_tinh_ver2 adapter_listview_tinh_ver2;
    //DuLieu
    private Context context;
    private LinearLayout linearLayout_fragmentHotel, linearLayout_listTinh_fragmentHotel;
    private ListView listView;


    private RecyclerView lvTinh_fragmentHotel,lvTinh_ver2_fragmentHotel;

    private boolean Flag;

    private ArrayList<Hotel> arrayListHotel;
    private ArrayList<String> arrayListTinh;
    private ArrayList<Map<String,Object>> mapTinhs;
    public FragmentHotel() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHotel.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHotel newInstance(String param1, String param2) {
        FragmentHotel fragment = new FragmentHotel();
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
        arrayListHotel=new ArrayList<>();
        arrayListTinh=new ArrayList<>();
        mapTinhs=new ArrayList<>();
        arrayListTinh.add("Tất cả");

        mView = inflater.inflate(R.layout.fragment_hotel, container, false);
        context=requireContext();
        addControls(mView);
//        String rootFileImgIntro= "SlideIntro/Hotel/intro-hotel1.png";
//        StorageService.LoadImageUri(rootFileImgIntro, imageIntro_fragmentHotel,context,1580,720);

        adapterHotel=new AdapterHotel(arrayListHotel,getContext());
        listView.setAdapter(adapterHotel);

        SetAdapter();

        LoadListHotel();

        Animation animationIn= AnimationUtils.loadAnimation(context,R.anim.list_view_in);
        Animation animationOut= AnimationUtils.loadAnimation(context,R.anim.list_view_out);
        //lvTinh_fragmentHotel.startAnimation(animationIn);
        SetStateSrollListView(animationIn, animationOut);

        return mView;
    }
    private void SetAdapter()
    {
        adapter_listview_tinh_ver1=new Adapter_listview_tinh_ver1(arrayListTinh,getContext(),listView,false);
        adapter_listview_tinh_ver2=new Adapter_listview_tinh_ver2(arrayListTinh, getContext(),listView,false);

        lvTinh_ver2_fragmentHotel.setAdapter(adapter_listview_tinh_ver2);
        lvTinh_ver2_fragmentHotel.setLayoutManager(new LinearLayoutManager(context
                , LinearLayoutManager.HORIZONTAL, false));

        lvTinh_fragmentHotel.setAdapter(adapter_listview_tinh_ver1);
        lvTinh_fragmentHotel.setLayoutManager(new LinearLayoutManager(context
                , LinearLayoutManager.HORIZONTAL, false));
    }


    private void SetStateSrollListView(Animation animationIn, Animation animationOut)
    {
        final int[] previousVisibleItem = {0};
        final int[] previousVisibleItemLast = {0};
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(arrayListHotel.size()>2) {
                    if (firstVisibleItem > previousVisibleItem[0]) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (previousVisibleItem[0] < previousVisibleItemLast[0]) {
                                    linearLayout_listTinh_fragmentHotel.startAnimation(animationIn);

                                }
                                lvTinh_ver2_fragmentHotel.setVisibility(View.VISIBLE);
                                linearLayout_listTinh_fragmentHotel.setVisibility(View.GONE);
                            }
                        }, 1000);

                    } else if (firstVisibleItem == previousVisibleItem[0]) {
                    } else if (firstVisibleItem < previousVisibleItem[0]) {
                        // Người dùng đang cuộn lên
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (previousVisibleItem[0] > previousVisibleItemLast[0]) {
                                    linearLayout_listTinh_fragmentHotel.startAnimation(animationOut);

                                }
                                lvTinh_ver2_fragmentHotel.setVisibility(View.GONE);
                                linearLayout_listTinh_fragmentHotel.setVisibility(View.VISIBLE);
                            }
                        }, 1000);


                    }
                }
                previousVisibleItemLast[0]=previousVisibleItem[0];
                previousVisibleItem[0] = firstVisibleItem;
            }
        });
    }
    private void addControls(View view) {
        listView= view.findViewById(R.id.listViewHotel);
        linearLayout_fragmentHotel=view.findViewById(R.id.linearLayout_fragmentHotel);

        lvTinh_fragmentHotel = view.findViewById(R.id.lvTinh_fragmentHotel);
        lvTinh_ver2_fragmentHotel = view.findViewById(R.id.lvTinh_ver2_fragmentHotel);
        linearLayout_listTinh_fragmentHotel=view.findViewById(R.id.linearLayout_listTinh_fragmentHotel);
        lvTinh_ver2_fragmentHotel.setVisibility(View.GONE);
    }
    private void LoadListHotel()
    {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Hotel").whereEqualTo("TrangThai",true).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots!=null)
                        {

                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {
                                Hotel hotel= HotelService.ReadHotelDocument(document);

                                arrayListHotel.add(hotel);
                                Collections.sort(arrayListHotel, new Comparator<Hotel>() {
                                    @Override
                                    public int compare(Hotel o1, Hotel o2) {
                                        return o2.getNgayDang().compareTo(o1.getNgayDang());
                                    }
                                });
                                String[] DiaChiSplit=hotel.getDiaChi().split(",");
                                if(!TinhIsInstance(DiaChiSplit[DiaChiSplit.length-1].trim()))
                                {
                                    Map<String,Object> mapTinh=new HashMap<>();
                                    mapTinh.put("TenTinh", DiaChiSplit[DiaChiSplit.length-1].trim());
                                    mapTinh.put("Loai", TinhService.IsMucDoUuTienTinh(DiaChiSplit[DiaChiSplit.length-1].trim()));
                                    mapTinhs.add(mapTinh);
                                    arrayListTinh=TinhService.SapXepDanhSach(mapTinhs);
                                    Log.e("DSTinh", "onSuccess: "+arrayListTinh.size() );
                                    SetAdapter();

                                }


                                adapterHotel.notifyDataSetChanged();
                                adapter_listview_tinh_ver1.notifyDataSetChanged();
                                adapter_listview_tinh_ver2.notifyDataSetChanged();
                            }
                        }

                    }
                });

    }
    private boolean TinhIsInstance(String tenTinh)
    {
        for(String tinh: arrayListTinh)
        {
            if(tinh.equals(tenTinh))
            {
                return true;
            }
        }
        return false;
    }

}