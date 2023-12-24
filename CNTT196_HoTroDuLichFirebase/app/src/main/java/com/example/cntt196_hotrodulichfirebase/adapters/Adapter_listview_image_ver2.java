package com.example.cntt196_hotrodulichfirebase.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;

import java.util.ArrayList;


public class Adapter_listview_image_ver2 extends RecyclerView.Adapter<Adapter_listview_image_ver2.MyViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<String> dsHinh;
    private View mView;
    private Context context;
    private String Id_Document;
    private boolean IsTravel;
    private ImageView imageView;

    public Adapter_listview_image_ver2(ArrayList<String> dsHinh, Context context, String Id_Document,boolean IsTravel,
                                       ImageView imageView) {
        this.context = context;
        this.inflater= LayoutInflater.from(context);
        this.Id_Document=Id_Document;
        this.dsHinh=dsHinh;
        this.IsTravel=IsTravel;
        this.imageView=imageView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_listview_images_ver2, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_listview_image_ver2.MyViewHolder holder, int position) {

        String rootFile=((IsTravel==true)?"Travel/": "Hotel/")+ Id_Document+"/"+dsHinh.get(position);
        StorageService.LoadImageUri(rootFile,holder.imgHinhAnh,context,1050,700);
        int index=position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath=((IsTravel==true)?"Travel/": "Hotel/")+ Id_Document+"/"+dsHinh.get(index);
                StorageService.LoadImageUri(filePath,imageView,context,1050,700);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dsHinh.size()>1)
                {
                    dsHinh.remove(index);
                    notifyItemRemoved(index);
                    notifyItemRangeChanged(index,getItemCount());
                    if(index<getItemCount()-1)
                    {
                        String filePath=((IsTravel==true)?"Travel/": "Hotel/")+ Id_Document+"/"+dsHinh.get(index+1);
                        StorageService.LoadImageUri(filePath,holder.imgHinhAnh,context,1050,700);
                    }
                    else
                    {
                        String filePath=((IsTravel==true)?"Travel/": "Hotel/")+ Id_Document+"/"+dsHinh.get(index-1);
                        StorageService.LoadImageUri(filePath,holder.imgHinhAnh,context,1050,700);
                    }

                }
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
