package com.example.cntt196_hotrodulichfirebase.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailHotel;
import com.example.cntt196_hotrodulichfirebase.ActivityLogin;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.MainActivity;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.example.cntt196_hotrodulichfirebase.models.Phong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter_listview_hoidap_ver1 extends RecyclerView.Adapter<Adapter_listview_hoidap_ver1.ViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<HoiDap> dsHoiDap;
    private View mView;
    private Context context;
    private String Id_Document;
    private boolean IsTravel;
    public Adapter_listview_hoidap_ver1(ArrayList<HoiDap> dsHoiDap,Context context,String Id_Document, boolean IsTravel)
    {
        this.dsHoiDap=dsHoiDap;
        this.context =context;
        this.inflater= LayoutInflater.from(context);
        this.IsTravel=IsTravel;
        this.Id_Document=Id_Document;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_hoi_dap_ver1, parent, false);
        Adapter_listview_hoidap_ver1.ViewHolder holder = new Adapter_listview_hoidap_ver1.ViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HoiDap hoiDap=(HoiDap) dsHoiDap.get(position);
        String filePath="avarta/" + hoiDap.getImgNguoiHoi();
        StorageService.LoadImageUri_Avarta(filePath,holder.imgNguoiDung_custom_item_hoiDap,context);

        holder.tvNgayDang_custom_item_hoiDap.setText(DateTimeToString.Format(hoiDap.getNgayHoi()));
        holder.tvTenNguoiDung_custom_item_hoiDap.setText(hoiDap.getTenNguoiHoi());
        holder.tvNoiDung_custom_item_hoiDap.setText(hoiDap.getNoiDungHoiDap());
        if(hoiDap.getTraLois()!=null)
        {
            Adapter_listview_hoidap_ver1 adapter_listview_hoidap_ver1=new Adapter_listview_hoidap_ver1(hoiDap.getTraLois(),
                    context,Id_Document,IsTravel);
            holder.list_item_traloi_custom_item_hoiDap.setAdapter(adapter_listview_hoidap_ver1);
            holder.list_item_traloi_custom_item_hoiDap.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL,false));
        }
        if(IsCurrentUser(hoiDap))
        {
            holder.imgBtnDelete_custom_item_hoiDap.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.imgBtnDelete_custom_item_hoiDap.setVisibility(View.GONE);
        }
        int indexItem=position;
        holder.imgBtnDelete_custom_item_hoiDap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc muốn xóa câu hỏi này không?");
                builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                        firebaseFirestore.collection(IsTravel?"Travel":"Hotel").document(Id_Document)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.get("HoiDap")!=null)
                                        {
                                            dsHoiDap.remove(indexItem);
                                            notifyItemRemoved(indexItem);
                                            notifyItemRangeChanged(indexItem,getItemCount());
                                            ArrayList<Map<String,Object>> arrayHoiDap=new ArrayList<>();
                                            for(HoiDap item:dsHoiDap)
                                            {
                                                Map<String,Object> mapHoiDap=new HashMap<>();
                                                mapHoiDap.put("MaHoiDap",item.getMaHoiDap());
                                                mapHoiDap.put("MaNguoiHoi",item.getMaNguoiHoi());
                                                Timestamp timestamp=new Timestamp(hoiDap.getNgayHoi()
                                                        .toEpochSecond(ZoneOffset.UTC), 0);
                                                mapHoiDap.put("NgayHoi",timestamp);
                                                mapHoiDap.put("NoiDungHoiDap", item.getNoiDungHoiDap());
                                                mapHoiDap.put("avartaNguoiHoi", item.getImgNguoiHoi());
                                                if(hoiDap.getTraLois()!=null)
                                                {
                                                    ArrayList<Map<String,Object>> arrayTraloi=new ArrayList<>();
                                                    for(HoiDap itemTraLoi : hoiDap.getTraLois())
                                                    {
                                                        Map<String,Object> mapTraLoi=new HashMap<>();
                                                        mapTraLoi.put("MaHoiDap",itemTraLoi.getMaHoiDap());
                                                        mapTraLoi.put("MaNguoiHoi",itemTraLoi.getMaNguoiHoi());
                                                        Timestamp timestampTraLoi =new Timestamp(itemTraLoi.getNgayHoi()
                                                                .toEpochSecond(ZoneOffset.UTC), 0);
                                                        mapTraLoi.put("NgayHoi",timestamp);
                                                        mapTraLoi.put("NoiDungHoiDap", itemTraLoi.getNoiDungHoiDap());
                                                        mapTraLoi.put("avartaNguoiHoi", itemTraLoi.getImgNguoiHoi());
                                                        arrayTraloi.add(mapTraLoi);
                                                    }
                                                    mapHoiDap.put("TraLoi",arrayTraloi);
                                                }
                                                else
                                                {
                                                    mapHoiDap.put("TraLoi",null);
                                                }
                                                arrayHoiDap.add(mapHoiDap);
                                            }
                                            firebaseFirestore.collection(IsTravel?"Travel":"Hotel").document(Id_Document)
                                                    .update("HoiDap",arrayHoiDap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            DialogMessage.ThongBao("Đã xóa thành công", context);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            DialogMessage.ThongBao("Xóa không thành công", context);
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private boolean IsCurrentUser(HoiDap hoiDap)
    {
        if(MainActivity.USER_==null)
            return false;
        else
        {
            return (hoiDap.getMaNguoiHoi().equals(MainActivity.USER_.getIdentifier()))?true:false;
        }
    }
    @Override
    public int getItemCount() {
        return dsHoiDap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView list_item_traloi_custom_item_hoiDap;
        public ImageButton imgBnt_show_more_custom_item_hoiDap, imgBtnDelete_custom_item_hoiDap;
        public TextView tvNoiDung_custom_item_hoiDap, tvNgayDang_custom_item_hoiDap, tvTenNguoiDung_custom_item_hoiDap;
        public ImageView imgNguoiDung_custom_item_hoiDap;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_item_traloi_custom_item_hoiDap=itemView.findViewById(R.id.list_item_traloi_custom_item_hoiDap);
            imgBnt_show_more_custom_item_hoiDap=itemView.findViewById(R.id.imgBnt_show_more_custom_item_hoiDap);
            tvNoiDung_custom_item_hoiDap=itemView.findViewById(R.id.tvNoiDung_custom_item_hoiDap);
            tvNgayDang_custom_item_hoiDap=itemView.findViewById(R.id.tvNgayDang_custom_item_hoiDap);
            tvTenNguoiDung_custom_item_hoiDap=itemView.findViewById(R.id.tvTenNguoiDung_custom_item_hoiDap);
            imgNguoiDung_custom_item_hoiDap=itemView.findViewById(R.id.imgNguoiDung_custom_item_hoiDap);
            imgBtnDelete_custom_item_hoiDap=itemView.findViewById(R.id.imgBtnDelete_custom_item_hoiDap);
        }
    }
}
