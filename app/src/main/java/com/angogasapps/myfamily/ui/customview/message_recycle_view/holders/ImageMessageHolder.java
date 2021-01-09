package com.angogasapps.myfamily.ui.customview.message_recycle_view.holders;

import android.view.View;
import android.widget.ImageView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;


public class ImageMessageHolder extends AppBaseViewHolder {

    public ImageMessageHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initLeftFields() {
        super.initLeftFields();

        ChatFunks.downloadImageMessageAndSetBitmap(
                value,
                messageKey,
                ((ImageView)view.findViewById(R.id.leftMessageImage)),
                activity
        );
    }

    @Override
    protected void initRightFields() {
        super.initRightFields();

        ChatFunks.downloadImageMessageAndSetBitmap(
                value,
                messageKey,
                ((ImageView)view.findViewById(R.id.rightMessageImage)),
                activity
        );
    }
}
