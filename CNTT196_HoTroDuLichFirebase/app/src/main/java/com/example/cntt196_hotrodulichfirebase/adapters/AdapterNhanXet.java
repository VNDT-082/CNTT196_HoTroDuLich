package com.example.cntt196_hotrodulichfirebase.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.MainActivity;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HoiDap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterNhanXet extends RecyclerView.Adapter<AdapterNhanXet.MyViewHolder>{
    private ArrayList<DanhGia> arrayListDanhGia;
    private Context context;
    private LayoutInflater inflater;
    private View mView;
    private String Id_Document;
    private int versionLayout;
    private boolean IsTravel;

    public AdapterNhanXet(Context context, ArrayList<DanhGia> arrayListDanhGia, String Id_Document,int versionLayout
    ,boolean IsTravel)
    {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.arrayListDanhGia=arrayListDanhGia;
        this.Id_Document=Id_Document;
        this.versionLayout=versionLayout;
        this.IsTravel=IsTravel;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =(versionLayout==1)? inflater.inflate(R.layout.custom_item_danh_gia_ver1, parent, false)
                : inflater.inflate(R.layout.custom_item_danh_gia_ver2, parent, false);
        AdapterNhanXet.MyViewHolder holder = new AdapterNhanXet.MyViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DanhGia danhGia= arrayListDanhGia.get(position);
        if (danhGia.getImgNguoiDang()!= null) {
            if(IsCurrentUser(danhGia))
            {
                holder.imgBtnDelete_custom_item_danhgia.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.imgBtnDelete_custom_item_danhgia.setVisibility(View.GONE);
            }
            String rootFile= "avarta/"+danhGia.getImgNguoiDang();
            StorageService.LoadImageUri_Avarta(rootFile,holder.imgNguoiDung_custom_item_danhgia,context);

            holder.tvNgayDang_custom_item_danhgia.setText(DateTimeToString.Format(danhGia.getNgayDang()));
            holder.tvNoiDung_custom_item_danhgia.setText(danhGia.getNoiDung());
            holder.tvTenNguoiDung_custom_item_danhgia.setText(danhGia.getTenNguoiDanhGia());
            holder.ratingBar_custom_item_danhgia.setRating(danhGia.getRate());
            holder.ratingBar_custom_item_danhgia.setIsIndicator(true);

            int indexItem=position;
            holder.imgBtnDelete_custom_item_danhgia.setOnClickListener(new View.OnClickListener() {
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
                                            if(documentSnapshot.get("DanhGia")!=null)
                                            {
                                                arrayListDanhGia.remove(indexItem);
                                                notifyItemRemoved(indexItem);
                                                notifyItemRangeChanged(indexItem,getItemCount());
                                                ArrayList<Map<String,Object>> arrayDanhGia=new ArrayList<>();
                                                for(DanhGia item:arrayListDanhGia)
                                                {
                                                    Map<String,Object> NhanXet=new HashMap<>();
                                                    NhanXet.put("MaNguoiDanhGia",item.getMaNguoiDanhGia());
                                                    Timestamp timestamp=new Timestamp(item.getNgayDang()
                                                            .toEpochSecond(ZoneOffset.UTC), 0);
                                                    NhanXet.put("NgayDang",timestamp);
                                                    NhanXet.put("NoiDungDanhGia",item.getNoiDung());
                                                    long rate= (long) item.getRate();
                                                    NhanXet.put("Rate", rate);
                                                    NhanXet.put("TenNguoiDanhGia", item.getTenNguoiDanhGia());
                                                    NhanXet.put("avartaNguoiDanhGia", item.getImgNguoiDang());
                                                    arrayDanhGia.add(NhanXet);
                                                }
                                                firebaseFirestore.collection(IsTravel?"Travel":"Hotel").document(Id_Document)
                                                        .update("DanhGia",arrayDanhGia)
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
    }
    private boolean IsCurrentUser(DanhGia danhGia)
    {
        if(MainActivity.USER_==null)
            return false;
        else
        {
            return (danhGia.getMaNguoiDanhGia().equals(MainActivity.USER_.getIdentifier()))?true:false;
        }
    }

    @Override
    public int getItemCount() {
        return arrayListDanhGia.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvNoiDung_custom_item_danhgia,tvTenNguoiDung_custom_item_danhgia, tvNgayDang_custom_item_danhgia;
        ImageView imgNguoiDung_custom_item_danhgia;
        RatingBar ratingBar_custom_item_danhgia;
        ImageButton imgBtnDelete_custom_item_danhgia;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNgayDang_custom_item_danhgia = itemView.findViewById(R.id.tvNgayDang_custom_item_danhgia);
            tvNoiDung_custom_item_danhgia = itemView.findViewById(R.id.tvNoiDung_custom_item_danhgia);
            tvTenNguoiDung_custom_item_danhgia = itemView.findViewById(R.id.tvTenNguoiDung_custom_item_danhgia);
            imgNguoiDung_custom_item_danhgia = itemView.findViewById(R.id.imgNguoiDung_custom_item_danhgia);
            ratingBar_custom_item_danhgia = itemView.findViewById(R.id.ratingBar_custom_item_danhgia);
            imgBtnDelete_custom_item_danhgia = itemView.findViewById(R.id.imgBtnDelete_custom_item_danhgia);
        }
    }
}
