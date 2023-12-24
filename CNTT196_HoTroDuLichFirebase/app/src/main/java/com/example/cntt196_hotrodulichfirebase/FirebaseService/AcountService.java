package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import androidx.annotation.NonNull;

import com.example.cntt196_hotrodulichfirebase.MainActivity;
import com.example.cntt196_hotrodulichfirebase.adapters.DateTimeToString;
import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AcountService {
    public FirebaseFirestore firebaseFirestore;
    public AcountService()
    {
        firebaseFirestore=FirebaseFirestore.getInstance();
    }
    public boolean CreateNewUser(User_ user_)
    {
        final boolean[] kq = {false};
        Map<String,Object> NewUser=new HashMap<>();
        String DocumentId= DateTimeToString.GenarateID(MainActivity.USER_.getUser_UID());
        NewUser.put("active",true);
        NewUser.put("author",false);
        NewUser.put("avarta","default.png");

        Calendar calendar=Calendar.getInstance();
        calendar.set(user_.getDateOfBirth().getYear(),user_.getDateOfBirth().getMonthValue(),user_.getDateOfBirth().getDayOfMonth());
        Timestamp ngaySinh= new Timestamp(calendar.getTime());
        NewUser.put("dateOfBirth",ngaySinh);

        NewUser.put("fullName",user_.getFullName());
        NewUser.put("gen",user_.isGen());
        NewUser.put("lastConnect", Timestamp.now());
        NewUser.put("userID",user_.getUser_UID());
        NewUser.put("userName",user_.getIdentifier());
        firebaseFirestore.collection("UserInfo")
                .document(DocumentId)
                .set(NewUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        kq[0] =true;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        kq[0] =false;
                    }
                });
        return kq[0];
    }
}
