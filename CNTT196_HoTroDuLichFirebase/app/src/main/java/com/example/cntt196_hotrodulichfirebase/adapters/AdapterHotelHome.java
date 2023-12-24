package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailHotel;
import com.example.cntt196_hotrodulichfirebase.ActivityDetailTravel;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;

import java.util.ArrayList;

public class AdapterHotelHome extends RecyclerView.Adapter<AdapterHotelHome.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Hotel> Hotels;

    private View mView;
    private Context context;
    private boolean IsTravel;

    public AdapterHotelHome(ArrayList<Hotel> Hotels, Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.Hotels = Hotels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_listview_new_travel_ver1, parent, false);
        AdapterHotelHome.MyViewHolder holder = new AdapterHotelHome.MyViewHolder(view);
        mView = view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (Hotels.get(position) != null) {
            if (Hotels.get(position).getNguoiDang() != null) {
                String filePath = "avarta/" + Hotels.get(position).getNguoiDang().getAnhDaiDien();
                StorageService.LoadImageUri_Avarta(filePath, holder.imgNguoiDung_travel_custom_fragHome, context);
                Log.e("LinkHinh", "=>" + position + " ; " + Hotels.get(position).getNguoiDang().getAnhDaiDien());

                //holder.tvTenNguoiDung_travel_custom_fragHome.setText(Travels.get(position).getNguoiDang().getTenNguoiDang());
                //holder.tvNgayDang_travel_custom_fragHome.setText(DateTimeToString.Format(Travels.get(position).getNgayDang()));
                holder.tvTieuDe_travel_custom_fragHome.setText(Hotels.get(position).getTenKhachSan());
                String[] DiaChiSplit = Hotels.get(position).getDiaChi().split(",");
                holder.tvDiaChi_travel_custom_fragHome.setText(DiaChiSplit[DiaChiSplit.length - 1]);
                holder.tvPrice_travel_custom_fragHome.setText("Xem tại trang chi tiết");

                if (Hotels.get(position).getHinhAnhs() != null) {
                    if (Hotels.get(position).getHinhAnhs().size() > 0) {
                        String rootFile = "Hotel/" + Hotels.get(position).getID_Document() + "/"
                                + Hotels.get(position).getHinhAnhs().get(0);
                        StorageService.LoadImageUri(rootFile, holder.imgView_travel_custom_fragHome, context, 1280, 750);
                    }
                }
            }
        }
        Hotel hotelBundle = (Hotel) Hotels.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("Hotel", hotelBundle);
                Intent intent = new Intent(context, ActivityDetailHotel.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Hotels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgNguoiDung_travel_custom_fragHome, imgView_travel_custom_fragHome;
        private TextView tvPrice_travel_custom_fragHome, tvDiaChi_travel_custom_fragHome,
                tvTieuDe_travel_custom_fragHome;// tvNgayDang_travel_custom_fragHome,
        //tvTenNguoiDung_travel_custom_fragHome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_travel_custom_fragHome = itemView.findViewById(R.id.imgView_travel_custom_fragHome);
            imgNguoiDung_travel_custom_fragHome = itemView.findViewById(R.id.imgNguoiDung_travel_custom_fragHome);
            tvPrice_travel_custom_fragHome = itemView.findViewById(R.id.tvPrice_travel_custom_fragHome);
            tvDiaChi_travel_custom_fragHome = itemView.findViewById(R.id.tvDiaChi_travel_custom_fragHome);
            tvTieuDe_travel_custom_fragHome = itemView.findViewById(R.id.tvTieuDe_travel_custom_fragHome);
            //tvNgayDang_travel_custom_fragHome=itemView.findViewById(R.id.tvNgayDang_travel_custom_fragHome);
            //tvTenNguoiDung_travel_custom_fragHome=itemView.findViewById(R.id.tvTenNguoiDung_travel_custom_fragHome);
        }
    }
}