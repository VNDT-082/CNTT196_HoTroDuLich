package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailTravel;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.DanhGia;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnh;
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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Map;

public class AdapterTravel extends BaseAdapter {
    private ArrayList<Travel> arrayListTravel;
    private Context context;
    private AdapterView.OnItemClickListener mListener;



    public AdapterTravel(ArrayList<Travel> arrayListTravel, Context context) {
        this.arrayListTravel = arrayListTravel;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayListTravel.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListTravel.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public  class  ViewHolder
    {
        TextView tvTenNguoiDung, tvNgayDang, tvTieuDe, tvMoTa, tvDiaChi, tvGia, tvCountFavorite;
        ImageView imgNguoiDung_custom, imgHinhAnhBaiDang_custom;
        RatingBar ratingBar_custom;
        ImageButton btnFavorite_custom;
        Button btnXemChiTiet_custom;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null)
        {
            viewHolder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.custom_listview_item_ver1,null);

            viewHolder.tvTenNguoiDung=view.findViewById(R.id.tvTenNguoiDung);
            viewHolder.tvNgayDang=view.findViewById(R.id.tvNgayDang);
            viewHolder.tvTieuDe=view.findViewById(R.id.tvTieuDe);
            viewHolder.tvMoTa=view.findViewById(R.id.tvMoTa);
            viewHolder.tvDiaChi=view.findViewById(R.id.tvDiaChi);
            viewHolder.tvGia=view.findViewById(R.id.tvGia);
            viewHolder.tvCountFavorite=view.findViewById(R.id.tvCountFavorite);
            viewHolder.imgNguoiDung_custom=view.findViewById(R.id.imgNguoiDung_custom);
            viewHolder.imgHinhAnhBaiDang_custom=view.findViewById(R.id.imgHinhAnhBaiDang_custom);
            viewHolder.ratingBar_custom=view.findViewById(R.id.ratingBar_custom_travel);
            viewHolder.btnFavorite_custom=view.findViewById(R.id.btnFavorite_custom);
            viewHolder.btnXemChiTiet_custom=view.findViewById(R.id.btnXemChiTiet_custom);


            view.setTag(viewHolder);

            viewHolder=(ViewHolder) view.getTag();
            Travel travel= (Travel) getItem(i);
            if(travel.getNguoiDang()!=null)
            {
                String filePath="avarta/" + travel.getNguoiDang().getAnhDaiDien();
                StorageService.LoadImageUri_Avarta(filePath,viewHolder.imgNguoiDung_custom,context);

                viewHolder.tvTenNguoiDung.setText(travel.getNguoiDang().getTenNguoiDang());
                Log.e("Travel_TenNguoiDung","=>"+travel.getNguoiDang().getTenNguoiDang());
                viewHolder.tvNgayDang.setText(DateTimeToString.Format(travel.getNgayDang()));
                viewHolder.tvTieuDe.setText(travel.getTieuDe());
                viewHolder.tvMoTa.setText((travel.getMoTa().length()>200)? ("Mô tả: "+travel.getMoTa().substring(0,200)
                        +"..."):("Mô tả: "+travel.getMoTa()));
                viewHolder.tvDiaChi.setText("Địa chỉ: "+travel.getDiaChi());


                if(travel.getDanhGias()!=null)
                {
                    float rate = 0;
                    for (DanhGia danhGia : travel.getDanhGias()) {
                        rate += danhGia.getRate();
                    }
                    rate =  Math.round(rate / travel.getDanhGias().size() * 10) / 10f; ;
                    viewHolder.ratingBar_custom.setRating(rate);
                }

                if(travel.getGiaMax()==0&&travel.getGiaMin()==0)
                { viewHolder.tvGia.setText("Miễn phí vé tham quan");}
                else
                { viewHolder.tvGia.setText("Giá tham khảo chỉ từ "
                        + DateTimeToString.FormatVND(travel.getGiaMin()) +" đến "+DateTimeToString.FormatVND(travel.getGiaMax()));}
                if(travel.getLuotThichs()!=null)
                {
                    if(travel.getLuotThichs().size()>0)
                    {
                        viewHolder.btnFavorite_custom.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                        //viewHolder.ratingBar_custom.setRating(5);
                        viewHolder.btnFavorite_custom.setTag(R.drawable.baseline_volunteer_activism_24_0);
                        viewHolder.tvCountFavorite.setText(travel.getLuotThichs().get(0).getTenNguoiDang()+" và "+travel.getLuotThichs().size()+"+");
                    }
                }
                else
                {
                    viewHolder.btnFavorite_custom.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                    //viewHolder.ratingBar_custom.setRating(0);
                    viewHolder.btnFavorite_custom.setTag(R.drawable.baseline_volunteer_activism_24_0);
                    viewHolder.tvCountFavorite.setText("");}

                if(travel.getHinhAnhs()!=null)
                {
                    if(travel.getHinhAnhs().size()>0)
                    {
                        String rootFile= "Travel/"+ travel.getID_Document()+"/"+travel.getHinhAnhs().get(0);
                        StorageService.LoadImageUri(rootFile,viewHolder.imgHinhAnhBaiDang_custom,context,1280,750);
                    }
                }
            }



            viewHolder.btnXemChiTiet_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Travel travelBundle=(Travel) getItem(i);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Travel", travelBundle);
                    Intent intent=new Intent(v.getContext(), ActivityDetailTravel.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            Log.e("TagBtnCurrent","=>"+viewHolder.btnFavorite_custom.getTag());
            ViewHolder finalViewHolder = viewHolder;
            viewHolder.btnFavorite_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((int)v.getTag()==R.drawable.baseline_volunteer_activism_24)
                    {
                        v.setTag(R.drawable.baseline_volunteer_activism_24_0);
                        finalViewHolder.btnFavorite_custom.setImageResource((int) finalViewHolder.btnFavorite_custom.getTag());
                    }
                    else {
                        v.setTag(R.drawable.baseline_volunteer_activism_24);
                        finalViewHolder.btnFavorite_custom.setImageResource((int) finalViewHolder.btnFavorite_custom.getTag());
                    }
                    Log.e("TagBtn","=>"+v.getTag());
                }
            });
            //viewHolder.btnFavorite_custom.setImageResource((int)viewHolder.btnFavorite_custom.getTag());



        }
        else
        {
            viewHolder=(ViewHolder) view.getTag();
            Travel travel= (Travel) getItem(i);
            if(travel.getNguoiDang()!=null)
            {
                Log.e("Travel_Avarta","=>"+travel.getNguoiDang().getAnhDaiDien());
                String filePath="avarta/" + travel.getNguoiDang().getAnhDaiDien();
                StorageService.LoadImageUri_Avarta(filePath,viewHolder.imgNguoiDung_custom,context);

                viewHolder.tvTenNguoiDung.setText(travel.getNguoiDang().getTenNguoiDang());
                viewHolder.tvNgayDang.setText(DateTimeToString.Format(travel.getNgayDang()));
                viewHolder.tvTieuDe.setText(travel.getTieuDe());
                viewHolder.tvMoTa.setText((travel.getMoTa().length()>200)? ("Mô tả: "+travel.getMoTa().substring(0,200)
                        +"..."):("Mô tả: "+travel.getMoTa()));
                viewHolder.tvDiaChi.setText("Địa chỉ: "+travel.getDiaChi());

                if(travel.getDanhGias()!=null)
                {
                    float rate = 0;
                    for (DanhGia danhGia : travel.getDanhGias()) {
                        rate += danhGia.getRate();
                    }
                    rate =  Math.round(rate / travel.getDanhGias().size() * 10) / 10f; ;
                    viewHolder.ratingBar_custom.setRating(rate);
                }

                if(travel.getGiaMax()==0&&travel.getGiaMin()==0)
                { viewHolder.tvGia.setText("Miễn phí vé tham quan");}
                else
                { viewHolder.tvGia.setText("Giá tham khảo chỉ từ "
                        + DateTimeToString.FormatVND(travel.getGiaMin()) +" đến "+DateTimeToString.FormatVND(travel.getGiaMax()));}
                if(travel.getLuotThichs()!=null)
                {
                    if(travel.getLuotThichs().size()>0)
                    {
                        viewHolder.btnFavorite_custom.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                        viewHolder.btnFavorite_custom.setTag(R.drawable.baseline_volunteer_activism_24_0);
                        viewHolder.tvCountFavorite.setText(travel.getLuotThichs().get(0).getTenNguoiDang()+" và "+travel.getLuotThichs().size()+"+");
                    }
                }
                else
                {
                    viewHolder.btnFavorite_custom.setImageResource(R.drawable.baseline_volunteer_activism_24_0);
                    //viewHolder.ratingBar_custom.setRating(0);
                    viewHolder.btnFavorite_custom.setTag(R.drawable.baseline_volunteer_activism_24_0);
                    viewHolder.tvCountFavorite.setText("");}

                if(travel.getHinhAnhs()!=null)
                {
                    Log.e("Travel_AnhBia","=>"+travel.getHinhAnhs().size());
                    if(travel.getHinhAnhs().size()>0)
                    {
                        //Log.e("Travel_AnhBia","=>"+travel.getHinhAnhs().get(0));
                        String rootFile= "Travel/"+ travel.getID_Document()+"/"+travel.getHinhAnhs().get(0);
                        StorageService.LoadImageUri(rootFile,viewHolder.imgHinhAnhBaiDang_custom,context,1280,750);
                    }
                }
                else
                {
                    String rootFile= "default.png";
                    StorageService.LoadImageUri(rootFile,viewHolder.imgHinhAnhBaiDang_custom,context,1280,750);
                }
            }



            viewHolder.btnXemChiTiet_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Travel travelBundle=(Travel) getItem(i);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("Travel", travelBundle);
                    Intent intent=new Intent(v.getContext(), ActivityDetailTravel.class);
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);
                }
            });

            Log.e("TagBtnCurrent","=>"+viewHolder.btnFavorite_custom.getTag());
            ViewHolder finalViewHolder = viewHolder;
            viewHolder.btnFavorite_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if((int)v.getTag()==R.drawable.baseline_volunteer_activism_24_0)
                    {
                        v.setTag(R.drawable.baseline_volunteer_activism_24);
                        finalViewHolder.btnFavorite_custom.setImageResource((int) finalViewHolder.btnFavorite_custom.getTag());
                    }
                    else {
                        v.setTag(R.drawable.baseline_volunteer_activism_24_0);
                        finalViewHolder.btnFavorite_custom.setImageResource((int) finalViewHolder.btnFavorite_custom.getTag());
                    }
                    Log.e("TagBtn","=>"+v.getTag());
                }
            });
            //viewHolder.btnFavorite_custom.setImageResource((int)viewHolder.btnFavorite_custom.getTag());

        }
        return view;
    }

}
