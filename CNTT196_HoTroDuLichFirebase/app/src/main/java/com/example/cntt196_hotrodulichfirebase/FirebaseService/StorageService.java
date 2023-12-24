package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.cntt196_hotrodulichfirebase.R;
import com.example.cntt196_hotrodulichfirebase.models.HinhAnhBitMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StorageService {
    public static void LoadImageUri(String filePath, ImageView imageView, Context context,int width,int height)
    {
        String uriImage1;
        Uri uri_final;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(filePath).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso picasso = Picasso.with(context);
                        picasso.load(uri).resize(width, height)
                                .placeholder(R.drawable.default_image_empty)
                                .into(imageView);
                        picasso.invalidate(uri);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LoiLoadImage","=>"+filePath);
                    }
                });
    }
    public static void LoadImageUri_Avarta(String filePath, ImageView imageView, Context context)
    {
        String uriImage1;
        Uri uri_final;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(filePath).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso picasso = Picasso.with(context);
                        picasso.load(uri).resize(90,90)
                                .placeholder(R.drawable.icon2)
                                .into(imageView);
                        picasso.invalidate(uri);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("LoiLoadImage","=>"+filePath);
                    }
                });
    }

    public static void UploadImage(String folderPath, ArrayList<HinhAnhBitMap> dsHinh, Context context) {
        if(dsHinh.size()>0)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            for (HinhAnhBitMap hinhAnhBitMap:dsHinh)
            {
                Uri imageUri = hinhAnhBitMap.getUri();
                String fileName = folderPath + hinhAnhBitMap.getTenHinhAnh();
                StorageReference imageRef = storageRef.child(fileName);
                UploadTask uploadTask = imageRef.putFile(imageUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DialogMessage.ThongBao("Upload hinh anh"+hinhAnhBitMap.getTenHinhAnh()
                                +" không thành công",context);
                    }
                });
            }
        }
    }
    public static void DeleteFolserImage(String folderPath, Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(folderPath).listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                if (task.isSuccessful()) {
                    for (StorageReference item : task.getResult().getItems()) {
                        item.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Xử lý khi xóa thành công
                                Log.d("DeleteImage", "Deleted image: " + item.getName());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Xử lý khi xóa không thành công
                                Log.d("DeleteImage", "Failed to delete image: " + item.getName());
                            }
                        });
                    }
                }
            }
        });
//        storageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(!task.isSuccessful())
//                {
//                    DialogMessage.ThongBao("Chưa xóa hoàn toàn ảnh",context);
//                }
//            }
//        });
        }

}
