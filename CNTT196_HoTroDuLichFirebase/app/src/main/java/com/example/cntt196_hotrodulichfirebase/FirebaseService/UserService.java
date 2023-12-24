package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.time.ZoneId;

public class UserService {
    public static User_ ReadUserDocument(DocumentSnapshot document)
    {
        User_ user_=new User_();
        user_.setIdDocument(document.getId());
        user_.setActive((boolean)document.get("active"));
        user_.setAuthor((boolean)document.get("author"));
        user_.setAvarta(document.getString("avarta"));

        Timestamp DanhGiatimestamp = (Timestamp) document.get("dateOfBirth");
        //convert sang localdatetime
        user_.setDateOfBirth(DanhGiatimestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime().toLocalDate());
        user_.setFullName(document.getString("fullName"));
        user_.setGen((boolean) document.get("gen"));

        Timestamp DanhGiatimestampLastConnect = (Timestamp) document.get("lastConnect");
        user_.setLastConnect(DanhGiatimestampLastConnect.toDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        user_.setUser_UID((String)document.get("userID"));
        user_.setIdentifier(document.getString("userName"));
        return user_;
    }
}
