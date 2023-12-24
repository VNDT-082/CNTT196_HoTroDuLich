package com.example.cntt196_hotrodulichfirebase.FirebaseService;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.cntt196_hotrodulichfirebase.models.User_;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationService {
    public FirebaseAuth mAuth;
    private Context context;
    public AuthenticationService(Context context)
    {
        this.context=context;
        mAuth = FirebaseAuth.getInstance();
    }
    public User_ CreateNewUser(String email, String password)
    {
        User_ user_=new User_();
        this.mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user=mAuth.getCurrentUser();
                            user_.setUser_UID(user.getUid());
                            user_.setIdentifier(user.getEmail());
                            user_.setFullName(user.getDisplayName());
                        }

                    }
                });

        return user_.getIdentifier().equals(email)?user_:null;
    }
}
