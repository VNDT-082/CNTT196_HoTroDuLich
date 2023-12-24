package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailHotel;
import com.example.cntt196_hotrodulichfirebase.ActivityDetailHotelAdmin;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.Travel;

import java.util.ArrayList;

public class AdapterHotelAdmin extends RecyclerView.Adapter<AdapterHotelAdmin.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<Hotel> Hotels;

    private View mView;
    private Context context;
    boolean IsAdmin;
    public AdapterHotelAdmin(ArrayList<Hotel> Hotels,Context context,  boolean IsAdmin)
    {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.Hotels=Hotels;
        this.IsAdmin=IsAdmin;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_hotel_admin_ver1, parent, false);
        AdapterHotelAdmin.MyViewHolder holder = new AdapterHotelAdmin.MyViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(Hotels.get(position)!=null)
        {
            if(IsAdmin==false)
            {
                holder.buttonBoQuan_main_admin.setVisibility(View.INVISIBLE);
                holder.edtTrangThai_custom_item_hotel_admin.setVisibility(View.INVISIBLE);
            }
            //holder.linearlayout_custom_item_hotel_admin.setBackgroundColor((int) R.color.WhiteSmoke);
            Hotel hotel=Hotels.get(position);
            if (hotel.getHinhAnhs() != null) {
                if (hotel.getHinhAnhs().size() > 0) {
                    String rootFile= "Hotel/"+ hotel.getID_Document()+"/"+hotel.getHinhAnhs().get(0);
                    StorageService.LoadImageUri(rootFile,holder.img_hotel_main_admin,context,1280,750);
                }
            }
            holder.edtNgayDang_custom_item_hotel_admin.setText(DateTimeToString.Format(hotel.getNgayDang()));
            holder.edtTenHodel_custom_item_hotel_admin.setText(hotel.getTenKhachSan().length()>60?
                    hotel.getTenKhachSan().substring(0,57)+"...":hotel.getTenKhachSan());

            if(hotel.getNguoiDang()!=null)
            {
                holder.edtNguoiDang_custom_item_hotel_admin.setText(hotel.getNguoiDang().getTenNguoiDang());
            }
            holder.edtTrangThai_custom_item_hotel_admin.setText(hotel.isTrangThai()?"Đã duyệt":"Chờ duyệt");

            holder.buttonXemChiTiet_main_admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Hotel", hotel);
                    if(IsAdmin)
                    {
                        Intent intent = new Intent(v.getContext(), ActivityDetailHotelAdmin.class);
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }
                    else {

                        Intent intent = new Intent(v.getContext(), ActivityDetailHotel.class);
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    }

                }
            });
            int index=position;
            holder.buttonBoQuan_main_admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Hotels.remove(Hotels.get(index));
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index,getItemCount());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Hotels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView img_hotel_main_admin;
        private Button buttonBoQuan_main_admin, buttonXemChiTiet_main_admin;
        private TextView edtTrangThai_custom_item_hotel_admin, edtNguoiDang_custom_item_hotel_admin,
                edtNgayDang_custom_item_hotel_admin, edtTenHodel_custom_item_hotel_admin;
        private LinearLayout linearlayout_custom_item_hotel_admin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_hotel_main_admin=itemView.findViewById(R.id.img_hotel_custom_item_hotel_admin);
            buttonBoQuan_main_admin=itemView.findViewById(R.id.buttonBoQuan_main_admin);
            buttonXemChiTiet_main_admin=itemView.findViewById(R.id.buttonXemChiTiet_main_admin);
            edtTrangThai_custom_item_hotel_admin=itemView.findViewById(R.id.edtTrangThai_custom_item_hotel_admin);
            edtNguoiDang_custom_item_hotel_admin=itemView.findViewById(R.id.edtNguoiDang_custom_item_hotel_admin);
            edtNgayDang_custom_item_hotel_admin=itemView.findViewById(R.id.edtNgayDang_custom_item_hotel_admin);
            edtTenHodel_custom_item_hotel_admin=itemView.findViewById(R.id.edtTenHodel_custom_item_hotel_admin);
            linearlayout_custom_item_hotel_admin=itemView.findViewById(R.id.linearlayout_custom_item_hotel_admin);
        }
    }
}
