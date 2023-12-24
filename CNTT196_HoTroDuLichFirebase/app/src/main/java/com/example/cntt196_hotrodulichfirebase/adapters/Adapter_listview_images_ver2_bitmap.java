package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnhBitMap;

import java.util.ArrayList;

public class Adapter_listview_images_ver2_bitmap extends RecyclerView.Adapter<Adapter_listview_images_ver2_bitmap.MyViewHolder>{

    private LayoutInflater inflater;
    private ArrayList<HinhAnhBitMap> dsHinh;
    private View mView;
    private Context context;
    private ImageView imageViewParent;

    public Adapter_listview_images_ver2_bitmap(ArrayList<HinhAnhBitMap> dsHinh, Context context,ImageView imageViewParent) {
        this.context = context;
        this.inflater= LayoutInflater.from(context);
        this.dsHinh=dsHinh;
        this.imageViewParent=imageViewParent;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_listview_images_ver2, parent, false);
        Adapter_listview_images_ver2_bitmap.MyViewHolder holder = new Adapter_listview_images_ver2_bitmap.MyViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imgHinhAnh.setImageBitmap(dsHinh.get(position).getBitmap());
        int index=position;
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dsHinh.remove(dsHinh.get(index));
                notifyItemRemoved(index);
                notifyItemRangeChanged(index,getItemCount());
                if(dsHinh.size()==0)
                {
                    imageViewParent.setImageResource(R.drawable.default_image_empty);
                }
                else if(index<getItemCount()-1)
                {
                    imageViewParent.setImageBitmap(dsHinh.get(index+1).getBitmap());
                }
                else
                {
                    imageViewParent.setImageBitmap(dsHinh.get(getItemCount()-1).getBitmap());
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewParent.setImageBitmap(dsHinh.get(index).getBitmap());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dsHinh.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgHinhAnh;
        ImageButton btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhAnh=itemView.findViewById(R.id.img_listview_image_ver2);
            btnDelete=itemView.findViewById(R.id.btn_listview_image_ver2);
        }
    }
}
