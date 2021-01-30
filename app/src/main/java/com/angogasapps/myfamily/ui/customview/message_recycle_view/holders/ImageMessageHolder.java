package com.angogasapps.myfamily.ui.customview.message_recycle_view.holders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.ChatFunks;
import com.angogasapps.myfamily.objects.ChatImageShower;


public class ImageMessageHolder extends AppBaseViewHolder {
    ImageView leftImage, rightImage, actualImage = null;
    ChatImageShower imageShower;

    public ImageMessageHolder(View itemView) {
        super(itemView);
        leftImage = view.findViewById(R.id.leftMessageImage);
        rightImage = view.findViewById(R.id.rightMessageImage);

    }

    @Override
    public void init(String from, Long time, String messageKey, String value, Activity activity) {
        super.init(from, time, messageKey, value, activity);
        imageShower = new ChatImageShower((AppCompatActivity) activity);

        OnClickListener onImageClickListener = v -> {
            imageShower.showImage(((BitmapDrawable)actualImage.getDrawable()).getBitmap());
        };

        leftImage.setOnClickListener(onImageClickListener);
        rightImage.setOnClickListener(onImageClickListener);
    }

    @Override
    protected void initLeftFields() {
        super.initLeftFields();

        actualImage = leftImage;

        ChatFunks.downloadImageMessageAndSetBitmap(
                value,
                messageKey,
                leftImage,
                activity
        );
    }

    @Override
    protected void initRightFields() {
        super.initRightFields();

        actualImage = rightImage;

        ChatFunks.downloadImageMessageAndSetBitmap(
                value,
                messageKey,
                rightImage,
                activity
        );
    }
}
