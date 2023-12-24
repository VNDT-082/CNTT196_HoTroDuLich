package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.HotelService;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.TravelService;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



public class Adapter_listview_tinh_ver2 extends RecyclerView.Adapter<Adapter_listview_tinh_ver2.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<String> dsTinh;
    private View mView;
    private Context context;
    private boolean IsTravel;
    private ListView recyclerView;

    public Adapter_listview_tinh_ver2(ArrayList<String> dsTinh, Context context,ListView recyclerView,boolean IsTravel) {
        this.context = context;
        this.inflater= LayoutInflater.from(context);
        this.dsTinh=dsTinh;
        this.recyclerView=recyclerView;
        this.IsTravel=IsTravel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_tinh_thanh_ver2, parent, false);
        Adapter_listview_tinh_ver2.MyViewHolder holder = new Adapter_listview_tinh_ver2.MyViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.text_custom_item_tinh_thanh_ver1.setText(dsTinh.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenTinh=holder.text_custom_item_tinh_thanh_ver1.getText().toString().trim();
                FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                if(IsTravel)
                {
                    if(tenTinh.equals("Tất cả"))
                    {
                        ArrayList<Travel> dsTravel=new ArrayList<>();
                        firebaseFirestore.collection("Travel").whereEqualTo("TrangThai",true)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots != null)
                                        {
                                            Log.e("SizeList",tenTinh+"=>"+queryDocumentSnapshots.size());
                                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                            {
                                                Travel travel=new Travel();
                                                travel= TravelService.ReadTravelDocument(document);
                                                Log.e("TravelTitle",travel.getTieuDe());

                                                dsTravel.add(travel);
                                                notifyDataSetChanged();
                                            }
                                            AdapterTravel adapterTravel=new AdapterTravel(dsTravel,context);
                                            recyclerView.setAdapter(adapterTravel);
                                        }
                                    }
                                });
                    }
                    else
                    {
                        ArrayList<Travel> dsTravel=new ArrayList<>();
                        firebaseFirestore.collection("Travel").whereEqualTo("TrangThai",true)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots != null)
                                        {
                                            Log.e("SizeList",tenTinh+"=>"+queryDocumentSnapshots.size());
                                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                            {
                                                String DiaChi=document.getString("DiaChi");
                                                if(DiaChi!=null)
                                                {
                                                    String[] DiaChiSplit=DiaChi.split(",");
                                                    Log.e("TravelTitle",DiaChiSplit[DiaChiSplit.length-1]
                                                            +" : "+tenTinh);
                                                    if(tenTinh.trim().equals(DiaChiSplit[DiaChiSplit.length-1].trim()))
                                                    {
                                                        Travel travel=new Travel();
                                                        travel= TravelService.ReadTravelDocument(document);
                                                        Log.e("TravelTitle",travel.getTieuDe());
                                                        dsTravel.add(travel);
                                                        notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                            AdapterTravel adapterTravel=new AdapterTravel(dsTravel,context);
                                            recyclerView.setAdapter(adapterTravel);
                                        }
                                    }
                                });
                    }
                }
                else
                {
                    ArrayList<Hotel> dsHotel=new ArrayList<>();
                    if(tenTinh.equals("Tất cả"))
                    {
                        firebaseFirestore.collection("Hotel").whereEqualTo("TrangThai",true)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots != null)
                                        {
                                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                            {
                                                Hotel hotel=new Hotel();
                                                hotel= HotelService.ReadHotelDocument(document);
                                                dsHotel.add(hotel);
                                                notifyDataSetChanged();
                                            }
                                            AdapterHotel adapterHotel=new AdapterHotel(dsHotel,context);
                                            recyclerView.setAdapter(adapterHotel);
                                        }
                                    }
                                });
                    }
                    else
                    {
                        ArrayList<Travel> dsTravel=new ArrayList<>();
                        firebaseFirestore.collection("Hotel").whereEqualTo("TrangThai",true)
                                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        if(queryDocumentSnapshots != null)
                                        {
                                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                                            {
                                                String DiaChi=document.getString("DiaChi");
                                                if(DiaChi!=null)
                                                {
                                                    String[] DiaChiSplit=DiaChi.split(",");
                                                    if(tenTinh.trim().equals(DiaChiSplit[DiaChiSplit.length-1].trim()))
                                                    {
                                                        Hotel hotel=new Hotel();
                                                        hotel= HotelService.ReadHotelDocument(document);
                                                        dsHotel.add(hotel);
                                                        notifyDataSetChanged();
                                                    }
                                                }
                                            }
                                            AdapterHotel adapterHotel=new AdapterHotel(dsHotel,context);
                                            recyclerView.setAdapter(adapterHotel);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsTinh.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_custom_item_tinh_thanh_ver1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_custom_item_tinh_thanh_ver1=itemView.findViewById(R.id.text_custom_item_tinh_thanh_ver1);
        }
    }
}
