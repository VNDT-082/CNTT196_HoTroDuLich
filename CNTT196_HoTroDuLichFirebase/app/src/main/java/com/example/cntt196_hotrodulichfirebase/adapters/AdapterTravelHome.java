package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailTravel;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.NguoiDang;
import com.example.cntt196_hotrodulichfirebase.models.Travel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Map;

public class AdapterTravelHome extends RecyclerView.Adapter<AdapterTravelHome.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Travel> Travels;

    private View mView;
    private Context context;
    private boolean IsTravel;

    public AdapterTravelHome(ArrayList<Travel> Travels, Context context) {
        this.context = context;
        this.inflater= LayoutInflater.from(context);
        this.Travels=Travels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_listview_new_travel_ver1, parent, false);
        AdapterTravelHome.MyViewHolder holder = new AdapterTravelHome.MyViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(Travels.get(position)!=null)
        {
            if(Travels.get(position).getNguoiDang()!=null)
            {
                String filePath= "avarta/"+Travels.get(position).getNguoiDang().getAnhDaiDien();
                StorageService.LoadImageUri_Avarta(filePath,holder.imgNguoiDung_travel_custom_fragHome,context);
                Log.e("LinkHinh","=>"+position+" ; "+Travels.get(position).getNguoiDang().getAnhDaiDien());

                //holder.tvTenNguoiDung_travel_custom_fragHome.setText(Travels.get(position).getNguoiDang().getTenNguoiDang());
                //holder.tvNgayDang_travel_custom_fragHome.setText(DateTimeToString.Format(Travels.get(position).getNgayDang()));
                holder.tvTieuDe_travel_custom_fragHome.setText(Travels.get(position).getTieuDe());
                String[] DiaChiSplit=Travels.get(position).getDiaChi().split(",");
                holder.tvDiaChi_travel_custom_fragHome.setText(DiaChiSplit[DiaChiSplit.length-1]);
                if(Travels.get(position).getGiaMax()==0&&Travels.get(position).getGiaMin()==0)
                { holder.tvPrice_travel_custom_fragHome.setText("Miễn phí vé tham quan");}
                else
                { holder.tvPrice_travel_custom_fragHome.setText("Giá tham khảo chỉ từ "
                        +DateTimeToString.FormatVND(Travels.get(position).getGiaMin()) +" đến "
                        +DateTimeToString.FormatVND(Travels.get(position).getGiaMax()));}

                if(Travels.get(position).getHinhAnhs()!=null)
                {
                    if(Travels.get(position).getHinhAnhs().size()>0)
                    {
                        String rootFile= "Travel/"+ Travels.get(position).getID_Document()+"/"
                                +Travels.get(position).getHinhAnhs().get(0);
                        StorageService.LoadImageUri(rootFile,holder.imgView_travel_custom_fragHome,context,1280,750);
                    }
                }
            }
        }
        Travel travelBundle=(Travel) Travels.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putSerializable("Travel", travelBundle);
                Intent intent=new Intent(context, ActivityDetailTravel.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Travels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imgNguoiDung_travel_custom_fragHome, imgView_travel_custom_fragHome;
        private TextView tvPrice_travel_custom_fragHome, tvDiaChi_travel_custom_fragHome,
                tvTieuDe_travel_custom_fragHome;// tvNgayDang_travel_custom_fragHome,
                //tvTenNguoiDung_travel_custom_fragHome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView_travel_custom_fragHome=itemView.findViewById(R.id.imgView_travel_custom_fragHome);
            imgNguoiDung_travel_custom_fragHome=itemView.findViewById(R.id.imgNguoiDung_travel_custom_fragHome);
            tvPrice_travel_custom_fragHome=itemView.findViewById(R.id.tvPrice_travel_custom_fragHome);
            tvDiaChi_travel_custom_fragHome=itemView.findViewById(R.id.tvDiaChi_travel_custom_fragHome);
            tvTieuDe_travel_custom_fragHome=itemView.findViewById(R.id.tvTieuDe_travel_custom_fragHome);
            //tvNgayDang_travel_custom_fragHome=itemView.findViewById(R.id.tvNgayDang_travel_custom_fragHome);
            //tvTenNguoiDung_travel_custom_fragHome=itemView.findViewById(R.id.tvTenNguoiDung_travel_custom_fragHome);
        }
    }
}
