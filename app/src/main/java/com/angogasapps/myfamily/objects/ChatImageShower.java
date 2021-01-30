package com.angogasapps.myfamily.objects;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.angogasapps.myfamily.R;
import com.github.chrisbanes.photoview.PhotoView;

public class ChatImageShower {
    private AppCompatActivity context;
    private ImageShowerDialog dialog;

    public ChatImageShower(AppCompatActivity context){
        this.context = context;
        this.dialog = new ImageShowerDialog();
    }

    public void showImage(Bitmap bitmap){
        dialog.setImage(bitmap);
        dialog.show(this.context.getSupportFragmentManager().beginTransaction(), ImageShowerDialog.TAG);
    }

    public void dismiss(){
        dialog.dismiss();
    }

    public static class ImageShowerDialog extends DialogFragment {
        public static final String TAG = "ImageShowerDialog";

        private PhotoView imageView;

        private Bitmap bitmap;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.dialog_image_shower, container, false);

            imageView = rootView.findViewById(R.id.imageView);

            imageView.setImageBitmap(bitmap);



            return rootView;
        }

        public void setImage(Bitmap bitmap){
            if (bitmap != null)
                this.bitmap = bitmap;
        }

//        @Override
//        public void show(@NonNull FragmentManager manager, @Nullable String tag) {
//            manager.beginTransaction().addSharedElement(imageView, "").commit();
//            super.show(manager, tag);
//        }

        @Override
        public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
            transaction.addSharedElement(imageView, imageView.getTransitionName());
            return super.show(transaction, tag);
        }
    }
}
