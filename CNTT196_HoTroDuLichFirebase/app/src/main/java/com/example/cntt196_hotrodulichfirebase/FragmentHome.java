package com.example.cntt196_hotrodulichfirebase;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TinhService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterHotelHome;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterSlideFragmentHome;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravel;
import com.example.cntt196_hotrodulichfirebase.adapters.AdapterTravelHome;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.TinhModel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

import me.relex.circleindicator.CircleIndicator3;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mView;
    private ImageButton btn_Previous_frgHome, btn_Next_frgHome;

    private ImageView imgTablelayout_fraHome;
    private ViewPager2 viewPager2_frgHome, viewPager_Travel_frgHome, viewPagerHotel_frgHome;
    private CircleIndicator3 circleIndicator3_frgHome;
    private TableLayout tablelayout_frgHome;




    private Context context;
    private AdapterTravelHome adapterTravelHome;
    private AdapterHotelHome adapterHotelHome;
    private AdapterSlideFragmentHome adapterSlideFragmentHome;
    private ArrayList<Travel> travelArrayList;
    private ArrayList<Hotel> hotelArrayList;
    Handler handler_viewPager=new Handler();
    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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
        context=requireContext();
        mView=inflater.inflate(R.layout.fragment_home, container, false);
        Init(mView);
        LoadListSlider();
        travelArrayList=new ArrayList<>();
        hotelArrayList=new ArrayList<>();
        adapterTravelHome=new AdapterTravelHome(travelArrayList,context);
        adapterHotelHome=new AdapterHotelHome(hotelArrayList,context);

        viewPager_Travel_frgHome.setAdapter(adapterTravelHome);
        CompositePageTransformer compositePageTransformerTravel=new CompositePageTransformer();
        compositePageTransformerTravel.addTransformer(new MarginPageTransformer(10));
        compositePageTransformerTravel.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1 - Math.abs(position);
                page.setScaleY(r);
            }
        });
        viewPager_Travel_frgHome.setPageTransformer(compositePageTransformerTravel);


        viewPagerHotel_frgHome.setAdapter(adapterHotelHome);
        CompositePageTransformer compositePageTransformerHotel=new CompositePageTransformer();
        compositePageTransformerHotel.addTransformer(new MarginPageTransformer(10));
        compositePageTransformerHotel.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1 - Math.abs(position);
                page.setScaleY(r);
            }
        });
        viewPagerHotel_frgHome.setPageTransformer(compositePageTransformerHotel);


        LoadListTravel();
        LoadListHotel();
        LoadGalery();
        return mView;
    }

    private void LoadListSlider() {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
        String[] slideIntros= {"dulich1.jpg","dulich2.jpg","dulich3.jpg","dulich4.jpg","dulich5.jpg"};
        adapterSlideFragmentHome=new AdapterSlideFragmentHome(slideIntros,context);
        viewPager2_frgHome.setAdapter(adapterSlideFragmentHome);
        circleIndicator3_frgHome.setViewPager(viewPager2_frgHome);


        handler_viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                int indexCurrentSlide=viewPager2_frgHome.getCurrentItem();
                viewPager2_frgHome.setCurrentItem(indexCurrentSlide+1);
            }
        },3000);

        btn_Previous_frgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int indexViewPagerCurrent=viewPager2_frgHome.getCurrentItem();
               if(indexViewPagerCurrent-1<0)
               {
                   viewPager2_frgHome.setCurrentItem(slideIntros.length);
               }
               else
               {
                   viewPager2_frgHome.setCurrentItem(indexViewPagerCurrent-1);
               }
            }
        });
        btn_Next_frgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexViewPagerCurrent=viewPager2_frgHome.getCurrentItem()+1;
                if(indexViewPagerCurrent> slideIntros.length-1)
                {
                    viewPager2_frgHome.setCurrentItem(0);
                }
                else
                {
                    viewPager2_frgHome.setCurrentItem(indexViewPagerCurrent);
                }
            }
        });
    }
    private void LoadListTravel()
    {
        //.orderBy("NgayDang", Query.Direction.ASCENDING)
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Travel").whereEqualTo("TrangThai",true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            Log.e("QuerySize", "onSuccess: "+queryDocumentSnapshots.size() );

                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                                Travel travel= TravelService.ReadTravelDocument(document);
                                travelArrayList.add(travel);
                                Collections.sort(travelArrayList, new Comparator<Travel>() {
                                    @Override
                                    public int compare(Travel o1, Travel o2) {
                                        return o2.getNgayDang().compareTo(o1.getNgayDang());
                                    }
                                });
                                adapterTravelHome.notifyDataSetChanged();
                            }
                            if(travelArrayList.size()>11)
                            {
                                for (int i=10;i<travelArrayList.size();i++)
                                {
                                    travelArrayList.remove(i);
                                }
                                adapterTravelHome.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
    private void LoadListHotel()
    {
        //.orderBy("NgayDang", Query.Direction.ASCENDING)
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Hotel").whereEqualTo("TrangThai",true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots != null)
                        {
                            Log.e("QuerySize", "onSuccess: "+queryDocumentSnapshots.size() );

                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                            {

                                Hotel hotel= HotelService.ReadHotelDocument(document);
                                hotelArrayList.add(hotel);
                                Collections.sort(hotelArrayList, new Comparator<Hotel>() {
                                    @Override
                                    public int compare(Hotel o1, Hotel o2) {
                                        return o2.getNgayDang().compareTo(o1.getNgayDang());
                                    }
                                });
                                adapterHotelHome.notifyDataSetChanged();
                            }
                            if(hotelArrayList.size()>11)
                            {
                                for (int i=10;i<hotelArrayList.size();i++)
                                {
                                    hotelArrayList.remove(i);
                                }
                                adapterHotelHome.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }
    private void LoadGalery()
    {
        ArrayList<String> dsTinh= TinhService.getAllTinh();
        for (int i=0;i<tablelayout_frgHome.getChildCount();i++)
        {
            View row=tablelayout_frgHome.getChildAt(i);
            if(row instanceof TableRow)
            {
                TableRow tableRow = (TableRow) row;
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f);
                layoutParams.setMargins(4, 4, 4,4);
                ImageView imageView1=new ImageView(context);
                ImageView imageView2=new ImageView(context);
                ImageView imageView3=new ImageView(context);
                imageView1.setLayoutParams(layoutParams);
                imageView2.setLayoutParams(layoutParams);
                imageView3.setLayoutParams(layoutParams);
                tableRow.addView(imageView1);
                tableRow.addView(imageView2);
                tableRow.addView(imageView3);
                Random random=new Random();
                int indexImage1=random.nextInt(dsTinh.size());
                int indexImage2=random.nextInt(dsTinh.size());
                int indexImage3=random.nextInt(dsTinh.size());
                String rootFile1= "SlideIntro/Tinh/"+ dsTinh.get(indexImage1)+".jpg";
                Log.e("Table",rootFile1);
                String rootFile2= "SlideIntro/Tinh/"+ dsTinh.get(indexImage2)+".jpg";
                Log.e("Table",rootFile2);
                String rootFile3= "SlideIntro/Tinh/"+ dsTinh.get(indexImage3)+".jpg";
                Log.e("Table",rootFile3);
                StorageService.LoadImageUri(rootFile1,imageView1,context,480,360);
                StorageService.LoadImageUri(rootFile2,imageView2,context,480,360);
                StorageService.LoadImageUri(rootFile3,imageView3,context,480,360);
            }
        }
    }
    private void Init(View view)
    {
        btn_Previous_frgHome= view.findViewById(R.id.btn_Previous_frgHome);
        btn_Next_frgHome= view.findViewById(R.id.btn_Next_frgHome);

        viewPager2_frgHome= view.findViewById(R.id.viewPager2_frgHome);
        circleIndicator3_frgHome=view.findViewById(R.id.circleIndicator3_frgHome);
        tablelayout_frgHome=view.findViewById(R.id.tablelayout_frgHome);
        imgTablelayout_fraHome=view.findViewById(R.id.imgTablelayout_fraHome);
        viewPager_Travel_frgHome =view.findViewById(R.id.viewPager_Travel_frgHome);
        viewPagerHotel_frgHome=view.findViewById(R.id.viewPagerHotel_frgHome);
    }
}