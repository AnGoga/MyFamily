package com.angogasapps.myfamily.ui.screens.family_settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.models.User;
import com.angogasapps.myfamily.utils.StringFormater;


import java.util.ArrayList;


public class FamilyMembersAdapter extends RecyclerView.Adapter<FamilyMembersAdapter.FamilyMembersHolder> {

    Context context;
    LayoutInflater inflater;

    ArrayList<User> mFamilyMembersList = new ArrayList<>();

    public FamilyMembersAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public FamilyMembersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FamilyMembersHolder(inflater.inflate(R.layout.family_member_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyMembersHolder holder, int position) {
        holder.initHolder(mFamilyMembersList.get(position), context);
    }

    @Override
    public int getItemCount() {
        return mFamilyMembersList.size();
    }

    public void reset(){
        this.mFamilyMembersList = new ArrayList<>();

        mFamilyMembersList.addAll(Family.getInstance().getUsersList());

        notifyDataSetChanged();
    }

    public static class FamilyMembersHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, userRoleTextView;
        ImageView userImage;

        public FamilyMembersHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.member_holder_user_photo);
            userNameTextView = itemView.findViewById(R.id.member_holder_user_name);
            userRoleTextView = itemView.findViewById(R.id.member_holder_user_role);
        }

        public void initHolder(User user, Context context){
            userNameTextView.setText(Family.getInstance().getMemberNameById(user.getId()));
            userRoleTextView.setText(StringFormater.formatToRole(Family.getInstance().getMemberNameById(user.getId())));

            Bitmap bitmap = Family.getInstance().getUserById(user.getId()).getUserPhoto();
            if (bitmap != null)
                userImage.setImageBitmap(bitmap);


        }
    }

}
