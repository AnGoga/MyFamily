package com.angogasapps.myfamily.ui.customview.family_members_rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.models.User;
import com.angogasapps.myfamily.ui.customview.family_members_rv.holders.FamilyMembersHolder;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.*;

import java.util.ArrayList;


public class FamilyMembersAdapter extends RecyclerView.Adapter<FamilyMembersHolder> {

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

        for (User user : familyMembersMap.values())
            mFamilyMembersList.add(user);

        notifyDataSetChanged();
    }
}
