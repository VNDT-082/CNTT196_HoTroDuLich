package com.example.cntt196_hotrodulichfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.ActivityGetStared;
import com.example.cntt196_hotrodulichfirebase.ActivityRegister;
import com.example.cntt196_hotrodulichfirebase.MainActivity;
import com.example.cntt196_hotrodulichfirebase.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class Adapter_IntroView extends RecyclerView.Adapter<Adapter_IntroView.ViewHolder> {

    private LayoutInflater inflater;
    private int[] dsHinh;
    private  ArrayList<String> dsTitle,dsDetail;
    private View mView;
    private Context context;
    public Adapter_IntroView(ArrayList<String> dsTitle,ArrayList<String> dsDetail, int[] dsHinh
            ,Context context)
    {
        this.context=context;
        this.inflater= LayoutInflater.from(context);
        this.dsDetail=dsDetail;
        this.dsTitle=dsTitle;
        this.dsHinh=dsHinh;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_get_stared_item, parent, false);
        Adapter_IntroView.ViewHolder holder = new Adapter_IntroView.ViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position!=dsTitle.size()-1)
        {
            holder.cardview_goHome_custom_get_stared_item.setVisibility(View.INVISIBLE);
        }
        else {
            holder.cardview_goHome_custom_get_stared_item.setVisibility(View.VISIBLE);
        }
        holder.imgIvew_custom_get_stared_item.setImageResource(dsHinh[position]);
        holder.tvDetail_custom_get_stared_item.setText(dsDetail.get(position));
        holder.tvTitle_custom_get_stared_item.setText(dsTitle.get(position));
        holder.cardview_goHome_custom_get_stared_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardview_goHome_custom_get_stared_item;
        ImageView imgIvew_custom_get_stared_item;
        TextView tvTitle_custom_get_stared_item, tvDetail_custom_get_stared_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIvew_custom_get_stared_item=itemView.findViewById(R.id.imgIvew_custom_get_stared_item);
            tvTitle_custom_get_stared_item=itemView.findViewById(R.id.tvTitle_custom_get_stared_item);
            tvDetail_custom_get_stared_item=itemView.findViewById(R.id.tvDetail_custom_get_stared_item);
            cardview_goHome_custom_get_stared_item=itemView.findViewById(R.id.cardview_goHome_custom_get_stared_item);
        }
    }
}
