package com.angogasapps.myfamily.async.notification;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.objects.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import java.util.concurrent.Executor;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_TOKEN;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_USERS;

public class TokensManager {
    private static TokensManager manager;

    public static TokensManager getInstance(){
        if (manager == null)
            manager = new TokensManager();
        return manager;
    }

    public void updateToken(String token, User user){
        DATABASE_ROOT.child(NODE_USERS).child(user.getId()).child(CHILD_TOKEN).setValue(token)
          .addOnCompleteListener(task -> {
            // . . . . .
        });
    }

    public void updateToken(User user){
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        updateToken(idToken, user);
                    } else {
                        task.getException().printStackTrace();
                    }
                });
    }
}
