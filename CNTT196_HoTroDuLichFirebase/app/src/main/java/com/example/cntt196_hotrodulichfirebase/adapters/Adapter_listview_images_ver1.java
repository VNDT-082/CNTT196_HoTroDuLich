package com.example.cntt196_hotrodulichfirebase.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cntt196_hotrodulichfirebase.ActivityDetailHotel;
import com.example.cntt196_hotrodulichfirebase.FirebaseService.StorageService;
import com.example.cntt196_hotrodulichfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_listview_images_ver1 extends RecyclerView.Adapter<Adapter_listview_images_ver1.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<String> dsHinh;
    private View mView;
    private Context context;
    private String Id_Document;
    private boolean IsTravel;

    public Adapter_listview_images_ver1(ArrayList<String> dsHinh, Context context, String Id_Document,boolean IsTravel) {
        this.context = context;
        this.inflater= LayoutInflater.from(context);
        this.Id_Document=Id_Document;
        this.dsHinh=dsHinh;
        this.IsTravel=IsTravel;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_listview_images_ver1, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        mView=view;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_listview_images_ver1.MyViewHolder holder, int position) {

        String rootFile=((IsTravel==true)?"Travel/": "Hotel/")+ Id_Document+"/"+dsHinh.get(position);
        StorageService.LoadImageUri(rootFile,holder.imgHinhAnh,context,1050,700);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout_list_gallery_ver1);

                ImageView img_dialog_layout_list_gallery ;
                ImageButton imgBtnLose_dialog_layout_list_gallery;
                ViewFlipper viewFlipper_dialog_layout_list_gallery;
                ImageButton btn_Next_dialog_layout_list_gallery,btn_Previous_dialog_layout_list_gallery;
                btn_Next_dialog_layout_list_gallery=dialog.findViewById(R.id.btn_Next_dialog_layout_list_gallery);
                btn_Previous_dialog_layout_list_gallery=dialog.findViewById(R.id.btn_Previous_dialog_layout_list_gallery);
                img_dialog_layout_list_gallery=dialog.findViewById(R.id.img_dialog_layout_list_gallery);
                imgBtnLose_dialog_layout_list_gallery=dialog.findViewById(R.id.imgBtnLose_dialog_layout_list_gallery);
                viewFlipper_dialog_layout_list_gallery=dialog.findViewById(R.id.viewFlipper_dialog_layout_list_gallery);
                StorageService.LoadImageUri(rootFile,img_dialog_layout_list_gallery,context,1050,700);
                for(String img:dsHinh)
                {
                    String filePath=((IsTravel==true)?"Travel/": "Hotel/")+ Id_Document+"/"+img;
                    ImageView imageView=new ImageView(context.getApplicationContext());
                    imageView.setId(View.generateViewId());
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageView.setBackground(null);

                    StorageService.LoadImageUri(filePath,imageView,context,1050,700);
                    viewFlipper_dialog_layout_list_gallery.addView(imageView);
                }

                imgBtnLose_dialog_layout_list_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                btn_Previous_dialog_layout_list_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipper_dialog_layout_list_gallery.setInAnimation(context,
                                R.anim.slide_left_to_right);
                        viewFlipper_dialog_layout_list_gallery.setOutAnimation(context,
                                R.anim.slide_right_to_left);

                        // It shows previous item.
                        viewFlipper_dialog_layout_list_gallery.showPrevious();
                    }
                });
                btn_Next_dialog_layout_list_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFlipper_dialog_layout_list_gallery.setInAnimation(context,
                                R.anim.slide_right_to_left);
                        viewFlipper_dialog_layout_list_gallery.setOutAnimation(context,
                                R.anim.slide_left_to_right);
                        viewFlipper_dialog_layout_list_gallery.showNext();
                    }
                });

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhAnh=itemView.findViewById(R.id.img_listview_image);
        }
    }
}
