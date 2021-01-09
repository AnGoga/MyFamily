package com.angogasapps.myfamily.ui.customview.family_members_rv.holders;

import android.content.Context;
import android.graphics.Bitmap;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.objects.User;
import com.angogasapps.myfamily.utils.StringFormater;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*;

public class FamilyMembersHolder extends RecyclerView.ViewHolder {
    TextView userNameTextView, userRoleTextView;
    ImageView userImage;

    public FamilyMembersHolder(@NonNull View itemView) {
        super(itemView);

        userImage = itemView.findViewById(R.id.member_holder_user_photo);
        userNameTextView = itemView.findViewById(R.id.member_holder_user_name);
        userRoleTextView = itemView.findViewById(R.id.member_holder_user_role);
    }

    public void initHolder(User user, Context context){
        userNameTextView.setText(familyMembersMap.get(user.getId()).getName());
        userRoleTextView.setText(StringFormater.formatToRole(familyMembersRolesMap.get(user.getId())));

        Bitmap bitmap = familyMembersImagesMap.get(user.getId());
        if (bitmap != null)
            userImage.setImageBitmap(bitmap);


    }
}
