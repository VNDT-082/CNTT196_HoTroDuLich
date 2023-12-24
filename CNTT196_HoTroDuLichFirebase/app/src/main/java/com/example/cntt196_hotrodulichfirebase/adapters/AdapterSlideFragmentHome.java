package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class AdapterSlideFragmentHome extends RecyclerView.Adapter<AdapterSlideFragmentHome.ViewHolder> {
    private LayoutInflater inflater;
    private String[] dsHinh;
    private View mView;
    private Context context;
    public AdapterSlideFragmentHome(String[] dsHinh,Context context)
    {
        this.context=context;
        this.inflater= LayoutInflater.from(context);
        this.dsHinh=dsHinh;
    }

    @NonNull
    @Override
    public AdapterSlideFragmentHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_slide_image_frghome_ver1, parent, false);
        AdapterSlideFragmentHome.ViewHolder holder = new AdapterSlideFragmentHome.ViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSlideFragmentHome.ViewHolder holder, int position) {
        String rootFile2= "SlideIntro/"+ dsHinh[position];
        StorageService.LoadImageUri(rootFile2, holder.img_custom_slide_frfhome,context,1280,570);
    }

    @Override
    public int getItemCount() {
        return dsHinh.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img_custom_slide_frfhome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_custom_slide_frfhome=itemView.findViewById(R.id.img_custom_slide_frfhome);
        }
    }
}
