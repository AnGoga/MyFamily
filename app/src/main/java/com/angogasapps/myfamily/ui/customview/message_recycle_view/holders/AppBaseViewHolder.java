package com.angogasapps.myfamily.ui.customview.message_recycle_view.holders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.utils.StringFormater;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.utils.WithUsers.*;

public class AppBaseViewHolder extends RecyclerView.ViewHolder implements Initializable {
    public LinearLayout leftLayout, rightLayout;
    public CircleImageView userAvatar;
    public View view;

    protected String from, messageKey, value, time, name;
    protected Activity activity;

    public AppBaseViewHolder(@NonNull View itemView) {
        super(itemView);
        leftLayout = itemView.findViewById(R.id.leftChatLayout);
        rightLayout = itemView.findViewById(R.id.rightChatLayout);
        userAvatar = itemView.findViewById(R.id.messageUserAvatar);

        view = itemView;
    }

    public final void init(String from, Long time, String messageKey, String value, Activity activity){
        this.from = from;
        this.name = getMemberNameById(from);
        this.time = StringFormater.formatLongToTime(time);
        this.messageKey = messageKey;
        this.value = value;
        this.activity = activity;
    }

    @Override
    public final void initLeftLayout() {
        leftLayout.setVisibility(View.VISIBLE);
        rightLayout.setVisibility(View.INVISIBLE);
        Bitmap bitmap = getMemberImageById(from, activity);
        userAvatar.setImageBitmap(bitmap);
        initLeftFields();
    }

    protected void initLeftFields() {
        ((TextView)view.findViewById(R.id.leftMessageFromName)).setText(name);
        ((TextView)view.findViewById(R.id.leftMessageTime)).setText(time);

    }



    @Override
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
interface Initializable{
    void initLeftLayout();
    void initRightLayout();
}