package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailHotelAdmin;
import com.example.cntt196_hotrodulichfirebase.ActivityThongTinCaNhanUserAdmin;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<User_> Users;

    private View mView;
    private Context context;
    public AdapterUser(ArrayList<User_> Users,Context context)
    {
        this.context=context;
        this.inflater=LayoutInflater.from(context);
        this.Users=Users;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_listview_user_ver1, parent, false);
        AdapterUser.ViewHolder holder = new AdapterUser.ViewHolder (view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(Users.get(position)!=null)
        {
            User_ user_=Users.get(position);
            holder.edtEmail_custom_item_user_admin.setText(user_.getIdentifier());
            holder.edtTenNguoiDung_custom_item_user_admin.setText(user_.isAuthor()?user_.getFullName()+" (Admin)": user_.getFullName());
            holder.edtTrangThai_custom_item_user_admin.setText(user_.isActive()?"Còn hoạt động":"Đã khóa");
            String filePath="avarta/" + user_.getAvarta();
            StorageService.LoadImageUri(filePath,holder.img_hotel_custom_item_user_admin,context,520,720);

            holder.buttonXemChiTiet_item_user_admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("User", user_);
                    Intent intent = new Intent(v.getContext(), ActivityThongTinCaNhanUserAdmin.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            holder.buttonKhoa_item_user_admin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    firebaseFirestore.collection("UserInfo").document(user_.getIdDocument())
                                            .update("active",false)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    DialogMessage.ThongBao("Đã khóa",
                                                            context);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    DialogMessage.ThongBao("Cập nhật thất bại",
                                                            context);
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    DialogMessage.ThongBao("Lỗi không tìm thấy",
                                            context);
                                }
                            });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private Button buttonKhoa_item_user_admin, buttonXemChiTiet_item_user_admin;
        private ImageView img_hotel_custom_item_user_admin;
        private TextView edtTenNguoiDung_custom_item_user_admin,edtEmail_custom_item_user_admin, edtTrangThai_custom_item_user_admin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonKhoa_item_user_admin=itemView.findViewById(R.id.buttonKhoa_item_user_admin);
            buttonXemChiTiet_item_user_admin=itemView.findViewById(R.id.buttonXemChiTiet_item_user_admin);
            img_hotel_custom_item_user_admin=itemView.findViewById(R.id.img_hotel_custom_item_user_admin);
            edtTenNguoiDung_custom_item_user_admin=itemView.findViewById(R.id.edtTenNguoiDung_custom_item_user_admin);
            edtEmail_custom_item_user_admin=itemView.findViewById(R.id.edtEmail_custom_item_user_admin);
            edtTrangThai_custom_item_user_admin=itemView.findViewById(R.id.edtTrangThai_custom_item_user_admin);
        }
    }
}
