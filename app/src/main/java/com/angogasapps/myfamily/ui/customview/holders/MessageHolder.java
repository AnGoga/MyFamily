package com.angogasapps.myfamily.ui.customview.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageHolder extends RecyclerView.ViewHolder{
    public LinearLayout leftLayout, rightLayout;
    public TextView fromName, text, timestampText;
    public CircleImageView userAvatar;
    public View view;
    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        leftLayout = itemView.findViewById(R.id.leftChatLayout);
        rightLayout = itemView.findViewById(R.id.rightChatLayout);
        view = itemView;
    }
}
