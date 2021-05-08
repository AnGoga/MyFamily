package com.angogasapps.myfamily.ui.screens.chat.holders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.utils.StringFormater;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.utils.WithUsers.*;

public class AppBaseViewHolder extends RecyclerView.ViewHolder {
//    protected ViewBinding binding;

    protected LinearLayout leftLayout, rightLayout;
    protected CircleImageView userAvatar;
    protected View view;

    protected String from, messageKey, value, time, name;
    protected Context context;

    public AppBaseViewHolder(@NonNull View itemView) {
        super(itemView);
        leftLayout = itemView.findViewById(R.id.leftChatLayout);
        rightLayout = itemView.findViewById(R.id.rightChatLayout);
        userAvatar = itemView.findViewById(R.id.messageUserAvatar);

        view = itemView;
    }

//    public AppBaseViewHolder(@NonNull ViewBinding binding){
//        super(binding.getRoot());
//        this.binding = binding;
//    }

    public void init(String from, Long time, String messageKey, String value, Activity context){
        this.from = from;
//        this.name = getMemberNameById(from);
        this.name = Family.getInstance().getMemberNameById(from);
        this.time = StringFormater.formatLongToTime(time);
        this.messageKey = messageKey;
        this.value = value;
        this.context = context;
    }

    public final void initLeftLayout() {
        leftLayout.setVisibility(View.VISIBLE);
        rightLayout.setVisibility(View.INVISIBLE);
        Bitmap bitmap = Family.getInstance().getMemberImageById(from, context);
        userAvatar.setImageBitmap(bitmap);
        initLeftFields();
    }

    protected void initLeftFields() {
        ((TextView)view.findViewById(R.id.leftMessageFromName)).setText(name);
        ((TextView)view.findViewById(R.id.leftMessageTime)).setText(time);

    }



    public final void initRightLayout() {
        rightLayout.setVisibility(View.VISIBLE);
        leftLayout.setVisibility(View.INVISIBLE);
        initRightFields();
    }

    protected void initRightFields() {
        ((TextView)view.findViewById(R.id.rightMessageFromName)).setText(name);
        ((TextView)view.findViewById(R.id.rightMessageTime)).setText(time);
    }

}

